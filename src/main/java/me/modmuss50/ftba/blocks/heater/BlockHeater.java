package me.modmuss50.ftba.blocks.heater;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import me.modmuss50.ftba.FTBAchievements;
import me.modmuss50.ftba.modCompat.top.ITopInfo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import prospector.shootingstar.ShootingStar;
import prospector.shootingstar.model.ModelCompound;
import reborncore.api.tile.IMachineGuiHandler;
import reborncore.common.blocks.BlockMachineBase;

import javax.annotation.Nullable;
import java.util.List;

public class BlockHeater extends BlockMachineBase implements ITopInfo {

	public BlockHeater() {
		setUnlocalizedName("heater");
		setCreativeTab(FTBAchievements.Tab.FTBA_TAB);
		ShootingStar.registerModel(new ModelCompound("ftbachievements", this, ""));
	}

	@Override
	public IMachineGuiHandler getGui() {
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileHeater();
	}

	@Deprecated
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		NonNullList<ItemStack> items = NonNullList.create();
		super.getDrops(items, world, pos, state, fortune);
		return items;
	}

	@Override
	public void addProbeInfo(ProbeMode mode,
	                         IProbeInfo probeInfo,
	                         EntityPlayer player,
	                         World world,
	                         IBlockState blockState,
	                         IProbeHitData data,
	                         @Nullable
		                         TileEntity tileEntity) {
		if(tileEntity instanceof TileHeater){
			probeInfo.progress(((TileHeater) tileEntity).temp, 1000, probeInfo.defaultProgressStyle().suffix(" Temperature"));
		}

	}
}
