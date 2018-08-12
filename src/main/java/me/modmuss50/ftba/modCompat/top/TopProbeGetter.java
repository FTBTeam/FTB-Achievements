package me.modmuss50.ftba.modCompat.top;

import mcjty.theoneprobe.api.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Function;

public class TopProbeGetter implements Function<ITheOneProbe, Void> {

	@Nullable
	@Override
	public Void apply(ITheOneProbe theOneProbe) {
		theOneProbe.registerProvider(new IProbeInfoProvider() {
			@Override
			public String getID() {
				return "ftba:topcompact";
			}

			@Override
			public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
				if (blockState.getBlock() instanceof ITopInfo) {
					TileEntity tileEntity = world.getTileEntity(data.getPos());
					((ITopInfo) blockState.getBlock()).addProbeInfo(mode, probeInfo, player, world, blockState, data, tileEntity);
				}

			}
		});
		return null;
	}
}
