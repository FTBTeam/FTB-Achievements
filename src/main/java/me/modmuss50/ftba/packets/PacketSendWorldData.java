package me.modmuss50.ftba.packets;

import me.modmuss50.ftba.ConfigManager;
import me.modmuss50.ftba.FTBAchievements;
import me.modmuss50.ftba.client.ClientDataManager;
import me.modmuss50.ftba.files.worldData.WorldFormat;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import reborncore.common.network.ExtendedPacketBuffer;
import reborncore.common.network.INetworkPacket;

import java.io.IOException;

/**
 * Created by Mark on 10/02/2017.
 */
public class PacketSendWorldData implements INetworkPacket<PacketSendWorldData> {

	String data;
	String hash;

	public PacketSendWorldData() {
	}

	public PacketSendWorldData(String data, String hash) {
		this.data = data;
		this.hash = hash;
	}

	@Override
	public void writeData(ExtendedPacketBuffer extendedPacketBuffer) throws IOException {
		extendedPacketBuffer.writeInt(data.length());
		extendedPacketBuffer.writeString(data);

		extendedPacketBuffer.writeInt(hash.length());
		extendedPacketBuffer.writeString(hash);
	}

	@Override
	public void readData(ExtendedPacketBuffer extendedPacketBuffer) throws IOException {
		data = extendedPacketBuffer.readString(extendedPacketBuffer.readInt());
		hash = extendedPacketBuffer.readString(extendedPacketBuffer.readInt());
	}

	@Override
	public void processData(PacketSendWorldData packetSendData, MessageContext messageContext) {
		ClientDataManager.setWorldFormat(ConfigManager.TINY_GSON.fromJson(packetSendData.data, WorldFormat.class));
		ClientDataManager.setWorldHash(packetSendData.hash);
		FTBAchievements.proxy.reloadAchivements();
	}
}
