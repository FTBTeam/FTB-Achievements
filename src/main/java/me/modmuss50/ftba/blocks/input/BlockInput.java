package me.modmuss50.ftba.blocks.input;

import me.modmuss50.ftba.FTBAchievements;
import me.modmuss50.ftba.client.GuiHandler;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Mark on 04/02/2017.
 */
public class BlockInput extends BlockContainer {

	public static final PropertyInteger META = PropertyInteger.create("meta", 0, 15);

	public BlockInput() {
		super(Material.IRON);
		setUnlocalizedName("ftbainput");
		this.setDefaultState(this.getDefaultState().withProperty(META, 0));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileInput();
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(META, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(META);
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, META);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		TileInput input = (TileInput) worldIn.getTileEntity(pos);
		input.username = placer.getName();
	}

	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean onBlockActivated(World worldIn,
	                                BlockPos pos,
	                                IBlockState state,
	                                EntityPlayer playerIn,
	                                EnumHand hand,
	                                EnumFacing side,
	                                float hitX,
	                                float hitY,
	                                float hitZ) {
		if (!worldIn.isRemote) {
			playerIn.openGui(FTBAchievements.INSTANCE, GuiHandler.inputID, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}
}
