package me.modmuss50.ftba.blocks.crafting;

import me.modmuss50.ftba.FTBAchievements;
import me.modmuss50.ftba.client.GuiHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import prospector.shootingstar.ShootingStar;
import prospector.shootingstar.model.ModelCompound;
import reborncore.api.tile.IMachineGuiHandler;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.BlockWrenchEventHandler;

import java.util.List;

public class BlockPoweredCraftingTable extends BlockMachineBase {

	public BlockPoweredCraftingTable() {
		setUnlocalizedName("craftingtable");
		setCreativeTab(FTBAchievements.Tab.FTBA_TAB);
		ShootingStar.registerModel(new ModelCompound("ftbachievements", this, ""));
		BlockWrenchEventHandler.wrenableBlocks.remove(this);
	}

	@Override
	public IMachineGuiHandler getGui() {
		return new PoweredCraftingTableGuiHandler();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState state, EntityPlayer entityPlayer, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			if (FTBAchievements.proxy.getTeamName(entityPlayer).equals("UNKNOWN")) {
				entityPlayer.sendMessage(new TextComponentString(TextFormatting.RED + "You must be part of a team to use this block"));
				return true;
			}
			entityPlayer.openGui(FTBAchievements.INSTANCE, GuiHandler.craftingID, entityPlayer.world, blockPos.getX(), blockPos.getY(), blockPos.getZ());
		}
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TilePoweredCraftingTable();
	}

	@Deprecated
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		NonNullList<ItemStack> items = NonNullList.create();
		items.add(new ItemStack(this));
		return items;
	}
}
