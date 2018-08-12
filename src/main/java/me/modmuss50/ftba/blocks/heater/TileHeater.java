package me.modmuss50.ftba.blocks.heater;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import me.modmuss50.ftba.modCompat.top.ITopInfo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import reborncore.common.powerSystem.TilePowerAcceptor;

import javax.annotation.Nullable;

public class TileHeater extends TilePowerAcceptor {

	int temp = 0;
	double lastEUgen;
	private int maxtemp;

	@Override
	public void update() {
		boolean burning = false;
		int furnacecount = 0;
		for (EnumFacing side : EnumFacing.HORIZONTALS) {
			TileEntity tile = world.getTileEntity(getPos().offset(side));
			if (tile instanceof TileEntityFurnace) {
				TileEntityFurnace furnace = (TileEntityFurnace) tile;
				if (furnace.getField(0) > 0) {
					furnacecount++;
					burning = true;
				}
			}
		}
		int targettemp = furnacecount * 250;
		if (maxtemp <= targettemp) {
			maxtemp = targettemp;
		} else {
			maxtemp--;
		}
		temp+=furnacecount;
		if (temp > maxtemp) {
			temp = maxtemp;
		}
		//Lets max out the temp at 100
		if (temp < 1) {
			//nope
			lastEUgen = 0;
		} else if (temp < 250) {
			addEnergy(0.25);
			lastEUgen = 0.25;
		} else if (temp < 500) {
			addEnergy(0.50);
			lastEUgen = 0.5;
		} else if (temp < 750) {
			addEnergy(0.75);
		} else {
			addEnergy(1);
		}
		if (!burning && temp != 0) {
			temp--;
		}
		if (temp < 0) {
			temp = 0;
		}
		super.update();
	}

	@Override
	public double getBaseMaxPower() {
		return 100;
	}

	@Override
	public double getBaseMaxOutput() {
		return 10;
	}

	@Override
	public double getBaseMaxInput() {
		return 0;
	}

	@Override
	public boolean canAcceptEnergy(EnumFacing enumFacing) {
		return false;
	}

	@Override
	public boolean canProvideEnergy(EnumFacing enumFacing) {
		return true;
	}

}
