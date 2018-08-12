package me.modmuss50.ftba.client.gui.poweredCrafting;

import me.modmuss50.ftba.FTBAchievements;
import me.modmuss50.ftba.api.IPoweredRecipe;
import me.modmuss50.ftba.blocks.crafting.TilePoweredCraftingTable;
import me.modmuss50.ftba.util.recipes.PoweredCraftingManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class ContainerCraftingTable extends ContainerWorkbench {

	World world;
	BlockPos pos;
	EntityPlayer player;

	TilePoweredCraftingTable tile;

	public int power = 0;
	public int maxPower = 0;

	private final ArrayList<MutableTriple<IntSupplier, IntConsumer, Short>> shortValues;
	private final ArrayList<MutableTriple<IntSupplier, IntConsumer, Integer>> integerValues;
	private Integer[] integerParts;

	public ContainerCraftingTable(EntityPlayer player, World worldIn, BlockPos posIn) {
		super(player.inventory, worldIn, posIn);
		world = worldIn;
		pos = posIn;
		player.openContainer = this;
		this.player = player;
		this.tile = (TilePoweredCraftingTable) worldIn.getTileEntity(posIn);
		this.shortValues = new ArrayList<>();
		this.integerValues = new ArrayList<>();

		addIntegerSync(Pair.of(() -> (int) tile.getEnergy(), value -> power = value));
		addIntegerSync(Pair.of(() -> (int) tile.getMaxPower(), value -> maxPower = value));
	}

	public void addIntegerSync(final Pair<IntSupplier, IntConsumer> syncables) {
		addIntegerSync(Collections.singletonList(syncables));
	}

	public void addIntegerSync(final List<Pair<IntSupplier, IntConsumer>> syncables) {

		for (final Pair<IntSupplier, IntConsumer> syncable : syncables)
			this.integerValues.add(MutableTriple.of(syncable.getLeft(), syncable.getRight(), 0));
		this.integerValues.trimToSize();
		this.integerParts = new Integer[this.integerValues.size()];
	}

	@Override
	protected void slotChangedCraftingGrid(World worldIn, EntityPlayer player, InventoryCrafting inventoryCrafting, InventoryCraftResult craftResult) {
		if (!worldIn.isRemote) {
			EntityPlayerMP entityplayermp = (EntityPlayerMP) player;
			ItemStack itemstack = ItemStack.EMPTY;
			List<IPoweredRecipe> recipes = PoweredCraftingManager.findAllMatchingRecipe(inventoryCrafting, worldIn);
			for (IPoweredRecipe irecipe : recipes) {
				if (irecipe != null && (irecipe.isDynamic() || !worldIn.getGameRules().getBoolean("doLimitedCrafting")) && (irecipe.enabled(player) || ((EntityPlayerMP) player).world.isRemote) && tile.canUseEnergy(irecipe.powerUsage())) {
					craftResult.setRecipeUsed(irecipe);
					itemstack = irecipe.getCraftingResult(inventoryCrafting);
					break;
				}
			}
			craftResult.setInventorySlotContents(0, itemstack);
			entityplayermp.connection.sendPacket(new SPacketSetSlot(this.windowId, 0, itemstack));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		if (this.world.getBlockState(this.pos).getBlock() != FTBAchievements.poweredCraftingTable) {
			return false;
		} else {
			return playerIn.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}

	@Override
	public Slot addSlotToContainer(Slot slotIn) {
		if (slotIn instanceof SlotCrafting && !(slotIn instanceof SlotPoweredCrafting)) {
			SlotPoweredCrafting newSlot = null;
			try {
				newSlot = new SlotPoweredCrafting(getPlayer((SlotCrafting) slotIn), this.craftMatrix, this.craftResult, slotIn.getSlotIndex(), slotIn.xPos, slotIn.yPos, this);
			} catch (IllegalAccessException | NoSuchFieldException e) {
				throw new RuntimeException("Failed to get player from slot", e);
			}
			return super.addSlotToContainer(newSlot);
		}
		return super.addSlotToContainer(slotIn);
	}

	//when using access transformers is too easy
	public EntityPlayer getPlayer(SlotCrafting slotCrafting) throws IllegalAccessException, NoSuchFieldException {
		Field field = ReflectionHelper.findField(slotCrafting.getClass(), "player", "field_75238_b", "b");
		field.setAccessible(true);
		return (EntityPlayer) field.get(slotCrafting);
	}

	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (final IContainerListener listener : this.listeners) {

			int i = 0;
			if (!this.shortValues.isEmpty())
				for (final MutableTriple<IntSupplier, IntConsumer, Short> value : this.shortValues) {
					final short supplied = (short) value.getLeft().getAsInt();
					if (supplied != value.getRight()) {

						listener.sendWindowProperty(this, i, supplied);
						value.setRight(supplied);
					}
					i++;
				}

			if (!this.integerValues.isEmpty())
				for (final MutableTriple<IntSupplier, IntConsumer, Integer> value : this.integerValues) {
					final int supplied = value.getLeft().getAsInt();
					if (supplied != value.getRight()) {

						listener.sendWindowProperty(this, i, supplied >> 16);
						listener.sendWindowProperty(this, i + 1, (short) (supplied & 0xFFFF));
						value.setRight(supplied);
					}
					i += 2;
				}
		}
	}

	@Override
	public void addListener(final IContainerListener listener) {
		super.addListener(listener);

		int i = 0;
		if (!this.shortValues.isEmpty())
			for (final MutableTriple<IntSupplier, IntConsumer, Short> value : this.shortValues) {
				final short supplied = (short) value.getLeft().getAsInt();

				listener.sendWindowProperty(this, i, supplied);
				value.setRight(supplied);
				i++;
			}

		if (!this.integerValues.isEmpty())
			for (final MutableTriple<IntSupplier, IntConsumer, Integer> value : this.integerValues) {
				final int supplied = value.getLeft().getAsInt();

				listener.sendWindowProperty(this, i, supplied >> 16);
				listener.sendWindowProperty(this, i + 1, (short) (supplied & 0xFFFF));
				value.setRight(supplied);
				i += 2;
			}

	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(final int id, final int value) {

		if (id < this.shortValues.size()) {
			this.shortValues.get(id).getMiddle().accept((short) value);
			this.shortValues.get(id).setRight((short) value);
		} else if (id - this.shortValues.size() < this.integerValues.size() * 2) {

			if ((id - this.shortValues.size()) % 2 == 0)
				this.integerParts[(id - this.shortValues.size()) / 2] = value;
			else {
				this.integerValues.get((id - this.shortValues.size()) / 2).getMiddle().accept(
					(this.integerParts[(id - this.shortValues.size()) / 2] & 0xFFFF) << 16 | value & 0xFFFF);
			}
		}
	}

}
