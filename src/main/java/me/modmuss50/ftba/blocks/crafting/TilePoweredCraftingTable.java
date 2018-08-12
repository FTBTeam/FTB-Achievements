package me.modmuss50.ftba.blocks.crafting;

import net.minecraft.util.EnumFacing;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.powerSystem.TilePowerAcceptor;

public class TilePoweredCraftingTable extends TilePowerAcceptor {

	public TilePoweredCraftingTable() {
		super();
	}

	//5 million
	//
	@Override
	public double getBaseMaxPower() {
		return 15500000;
	}

	@Override
	public double getBaseMaxOutput() {
		return 0;
	}

	@Override
	public double getBaseMaxInput() {
		return 150000;
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
	public EnumPowerTier getTier() {
		return EnumPowerTier.LOW;
	}
}
