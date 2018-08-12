package me.modmuss50.ftba.packets;

import me.modmuss50.ftba.client.ClientDataManager;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.network.ExtendedPacketBuffer;
import reborncore.common.network.INetworkPacket;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by modmuss50 on 21/03/2017.
 */
public class PacketAchievementProgress implements INetworkPacket<PacketAchievementProgress> {

	public PacketAchievementProgress() {
	}

	public PacketAchievementProgress(String value, String achievement) {
		this.value = value;
		this.achievement = achievement;
	}

	public String value;
	public String achievement;

	@Override
	public void writeData(ExtendedPacketBuffer extendedPacketBuffer) throws IOException {
		extendedPacketBuffer.writeInt(value.length());
		extendedPacketBuffer.writeString(value);
		extendedPacketBuffer.writeInt(achievement.length());
		extendedPacketBuffer.writeString(achievement);
	}

	@Override
	public void readData(ExtendedPacketBuffer extendedPacketBuffer) throws IOException {
		value = extendedPacketBuffer.readString(extendedPacketBuffer.readInt());
		achievement = extendedPacketBuffer.readString(extendedPacketBuffer.readInt());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void processData(PacketAchievementProgress packetGuiTrigger, MessageContext messageContext) {
		if (ClientDataManager.percentageMap == null) {
			ClientDataManager.percentageMap = new HashMap<>();
		}
		if (ClientDataManager.percentageMap.containsKey(packetGuiTrigger.achievement)) {
			ClientDataManager.percentageMap.replace(packetGuiTrigger.achievement, packetGuiTrigger.value);
		} else {
			ClientDataManager.percentageMap.put(packetGuiTrigger.achievement, packetGuiTrigger.value);
		}
	}
}
