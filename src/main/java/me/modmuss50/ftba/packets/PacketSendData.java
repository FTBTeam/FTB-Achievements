package me.modmuss50.ftba.packets;

import me.modmuss50.ftba.ConfigManager;
import me.modmuss50.ftba.FTBAchievements;
import me.modmuss50.ftba.client.ClientDataManager;
import me.modmuss50.ftba.files.config.ConfigFormat;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.network.ExtendedPacketBuffer;
import reborncore.common.network.INetworkPacket;

import java.io.IOException;

/**
 * Created by Mark on 10/02/2017.
 */
public class PacketSendData implements INetworkPacket<PacketSendData> {

	String data;

	public PacketSendData() {
	}

	public PacketSendData(String data) {
		this.data = data;
	}

	@Override
	public void writeData(ExtendedPacketBuffer extendedPacketBuffer) throws IOException {
		extendedPacketBuffer.writeInt(data.length());
		extendedPacketBuffer.writeString(data);
	}

	@Override
	public void readData(ExtendedPacketBuffer extendedPacketBuffer) throws IOException {
		data = extendedPacketBuffer.readString(extendedPacketBuffer.readInt());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void processData(PacketSendData packetSendData, MessageContext messageContext) {
		ClientDataManager.setConfigFormat(ConfigManager.TINY_GSON.fromJson(packetSendData.data, ConfigFormat.class));
		FTBAchievements.proxy.reloadAchivements();
	}
}
