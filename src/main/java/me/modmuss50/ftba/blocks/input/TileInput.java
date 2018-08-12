package me.modmuss50.ftba.blocks.input;

import me.modmuss50.ftba.AchievementManager;
import me.modmuss50.ftba.ConfigManager;
import me.modmuss50.ftba.files.config.BlockTrigger;
import me.modmuss50.ftba.packets.PacketAchievementProgress;
import me.modmuss50.ftba.util.AchievementUser;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.energy.EnergyStorage;

import javax.annotation.Nullable;

/**
 * Created by Mark on 04/02/2017.
 */
public class TileInput extends TileEntity implements ITickable {

	public BlockTrigger trigger;
	String username;
	public EnergyStorage storage = new EnergyStorage(32000);
	String percenatage;

	@Override
	public void update() {
		if (world.isRemote) {
			return;
		}
		if (trigger == null) {
			trigger = ConfigManager.getTriggerFromMeta(world.getBlockState(pos).getValue(BlockInput.META));
		} else {
			if (trigger.type.equals("redstone")) {
				if (world.isBlockIndirectlyGettingPowered(pos) > 0) {
					if (username != null && !username.isEmpty()) {
						AchievementManager.processAchievement(ConfigManager.getAchivementFromName(trigger.achievement), new AchievementUser("A Player", world));
					}
				}
			} else if (trigger.type.equals("rf")) {
				if (trigger.requirement > storage.getMaxEnergyStored()) {
					//storage.setCapacity((int) (trigger.requirement * 1.5));
				}
				if (storage.getEnergyStored() >= trigger.requirement) {
					AchievementManager.processAchievement(ConfigManager.getAchivementFromName(trigger.achievement), new AchievementUser("A Player", world));
				}
				if (storage.getEnergyStored() > 0) {
					String value = Math.round((storage.getEnergyStored() * 100.0f) / trigger.requirement) + "%";
					if (!value.equals(percenatage)) {
						reborncore.common.network.NetworkManager.sendToAll(new PacketAchievementProgress(value, trigger.achievement));
						percenatage = value;
					}
				}
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		//this.storage.readFromNBT(compound);
		username = compound.getString("username");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		//this.storage.writeToNBT(compound);
		if (username != null && !username.isEmpty()) {
			compound.setString("username", username);
		}
		return compound;
	}

	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.getPos(), 0, this.writeToNBT(new NBTTagCompound()));
	}

	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		this.readFromNBT(pkt.getNbtCompound());
	}

	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	//RF
	public boolean canConnectEnergy(EnumFacing from) {
		return true;
	}

	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
		return this.storage.receiveEnergy(maxReceive, simulate);
	}

	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
		return this.storage.extractEnergy(maxExtract, simulate);
	}

	public int getEnergyStored(EnumFacing from) {
		return this.storage.getEnergyStored();
	}

	public int getMaxEnergyStored(EnumFacing from) {
		return this.storage.getMaxEnergyStored();
	}

}
