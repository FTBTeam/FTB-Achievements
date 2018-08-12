package me.modmuss50.ftba.modCompat.top;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface ITopInfo {
	void addProbeInfo(ProbeMode mode,
	                  IProbeInfo probeInfo,
	                  EntityPlayer player,
	                  World world,
	                  IBlockState blockState,
	                  IProbeHitData data,
	                  @Nullable
		                  TileEntity tileEntity);
}
