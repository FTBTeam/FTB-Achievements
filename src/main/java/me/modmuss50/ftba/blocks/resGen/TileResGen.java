package me.modmuss50.ftba.blocks.resGen;

import me.modmuss50.ftba.api.IResGenHandler;
import me.modmuss50.ftba.api.IResGenUpgrade;
import me.modmuss50.ftba.config.ConfigResourceGen;
import me.modmuss50.ftba.config.DuplicationResource;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import reborncore.api.power.IPowerConfig;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.ItemUtils;

import java.util.*;
import java.util.stream.*;

/**
 * Created by Mark on 18/06/2017.
 */
public class TileResGen extends TilePowerAcceptor implements IResGenHandler {

	public static PowerConfig powerConfig = new PowerConfig();
	public float progress;
	public float speedModifier = 1F;
	public float powerModifier = 1F;
	public int luckModifier = 0;
	public boolean running = false;
	public boolean lastRunning = false;
	public List<DuplicationResource> duplicationResources = new ArrayList<>();
	public DuplicationResource currentResource;
	public int rr_int;

	public TileResGen() {
		super();
	}

	//TODO optimise the shit of of this at some point
	@Override
	public void update() {
		super.update();
		if (world.isRemote) {
			return;
		}
		//TODO cache this
		final List<EntityItemFrame> frameList = world.getEntitiesWithinAABB(EntityItemFrame.class, new AxisAlignedBB(getPos().getX() - 0.1D, getPos().getY(), getPos().getZ() - 0.1D, getPos().getX() + 1.1D, getPos().getY() + 1.0D, getPos().getZ() + 1.1D));
		// extract items from frames
		final List<ItemStack> frameItems = frameList.stream().map(EntityItemFrame::getDisplayedItem).collect(Collectors.toList());

		// if they're all empty we skip a lot of logic
		final boolean allEmpty = frameItems.stream().allMatch(ItemStack::isEmpty);

		if (!allEmpty) {
			// compute our current upgrade state and return anything that isn't an upgrade for further processing
			final List<ItemStack> nonUpgrades = computeUpgrades(frameItems);
			// compute our dupe resource
			final List<DuplicationResource> resources = nonUpgrades.stream().map(this::getResource).filter(Objects::nonNull).collect(Collectors.toList());
			if (resources.isEmpty()) {
				this.running = false;
			} else {
				this.running = true;
				// try and make a resource
				tryCreateResource(resources);
			}
		} else {
			this.running = false;
		}

		if (lastRunning != running) {
			updateActive();
		}
		lastRunning = running;

	}

	private void tryCreateResource(final List<DuplicationResource> resources) {
		if (!resources.contains(currentResource)) {
			rr_int++;
			rr_int%=resources.size();
			currentResource = resources.get(rr_int);
		}

		double powerUsage = Math.max((currentResource.powerUsage * powerModifier), 1);
		if (canUseEnergy(powerUsage) || currentResource.powerUsage == 0) {
			if (progress >= currentResource.processTime) {
				BlockPos up = getPos().up();
				TileEntity tileEntity = world.getTileEntity(up);
				IItemHandler itemHandler = null;
				if (tileEntity != null && tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)) {
					itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
				}
				int amount = 1;
				for (int bonusLuck = luckModifier-world.rand.nextInt(100); bonusLuck > 0; bonusLuck-=world.rand.nextInt(100)+1) {
				    amount++;
                }
				boolean madeItem;
				if (itemHandler != null) {
					ItemStack stack = currentResource.stack.copy();
					stack.setCount(amount);
					madeItem = ItemHandlerHelper.insertItem(itemHandler, stack, false).isEmpty();
				} else {
					for (int x = 0; x < amount; x++) {
						EntityItem item = new EntityItem(getWorld(), getPos().getX() + 0.5, getPos().getY() + 1, getPos().getZ() + 0.5, currentResource.stack.copy());
						item.motionX += world.rand.nextDouble() / 4 - 0.1;
						item.motionY += world.rand.nextDouble() / 2;
						item.motionZ += world.rand.nextDouble() / 4 - 0.1;
						item.setAgeToCreativeDespawnTime();
						world.spawnEntity(item);
					}
					madeItem = true;
				}
				progress = 0;
				currentResource = null;
				if (madeItem) {
					useEnergy(powerUsage);
				}
			} else {
				progress+=speedModifier;
			}
		}
	}

	private List<ItemStack> computeUpgrades(final List<ItemStack> frameItems) {
		final List<ItemStack> upgradeItems = frameItems.stream()
				.filter(is->IResGenUpgrade.class.isInstance(is.getItem()))
				.collect(Collectors.toList());

		reset();
		upgradeItems.forEach(is->((IResGenUpgrade)is.getItem()).processUpgrade(is, this));

		final List<ItemStack> nonUpgrades = new ArrayList<>(frameItems);
		nonUpgrades.removeAll(upgradeItems);
		return nonUpgrades;
	}

	public void updateActive() {
		IBlockState state = world.getBlockState(pos).withProperty(BlockMachineBase.ACTIVE, running);
		world.setBlockState(pos, state, 3);
	}

	public DuplicationResource getResource(ItemStack stack) {
		if (stack.isEmpty()) {
			return null;
		}
		if (ConfigResourceGen.duplicationResources == null || ConfigResourceGen.duplicationResources.isEmpty()) {
			return null;
		}
		for (DuplicationResource resource : ConfigResourceGen.duplicationResources) {
			if (ItemUtils.isItemEqual(stack, resource.stack, true, true, false)) {
				return resource;
			}
		}
		return null;
	}

	public float getProcessTime(DuplicationResource resource) {
		if (resource != null) {
			return resource.processTime * (1 - speedModifier);
		} else {
			return -1;
		}
	}

	@Override
	public double getBaseMaxPower() {
		return 100000 / 4;
	}

	@Override
	public double getBaseMaxOutput() {
		return 0;
	}

	@Override
	public double getBaseMaxInput() {
		return 512;
	}

	@Override
	public boolean canAcceptEnergy(EnumFacing enumFacing) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(EnumFacing enumFacing) {
		return false;
	}

	@Override
	public IPowerConfig getPowerConfig() {
		return powerConfig;
	}

	@Override
	public void reset() {
		speedModifier = 1F;
		powerModifier = 1F;
		luckModifier = 0;
	}

	@Override
	public void addSpeedModifier(float speed) {
		speedModifier = speedModifier * (1f + speed);
	}

	@Override
	public void addPowerModifier(float power) {
		powerModifier += power;
	}

	@Override
	public void addLuckModifier(int luck) {
		luckModifier += luck;
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	private static class PowerConfig implements IPowerConfig {

		@Override
		public boolean eu() {
			return true;
		}

		@Override
		public boolean tesla() {
			return true;
		}

		@Override
		public boolean internal() {
			return true;
		}

		@Override
		public boolean forge() {
			return true;
		}
	}
}
