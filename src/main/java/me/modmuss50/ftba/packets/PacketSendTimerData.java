package me.modmuss50.ftba.packets;

import me.modmuss50.ftba.client.hud.Timer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import reborncore.common.network.ExtendedPacketBuffer;
import reborncore.common.network.INetworkPacket;

import java.io.IOException;

/**
 * Created by modmuss50 on 14/02/2017.
 */
public class PacketSendTimerData implements INetworkPacket<PacketSendTimerData> {

	long countedTime;
	boolean active;

	public PacketSendTimerData(long countedTime, boolean active) {
		this.countedTime = countedTime;
		this.active = active;
	}

	public PacketSendTimerData() {
	}

	@Override
	public void writeData(ExtendedPacketBuffer extendedPacketBuffer) throws IOException {
		extendedPacketBuffer.writeLong(countedTime);
		extendedPacketBuffer.writeBoolean(active);
	}

	@Override
	public void readData(ExtendedPacketBuffer extendedPacketBuffer) throws IOException {
		countedTime = extendedPacketBuffer.readLong();
		active = extendedPacketBuffer.readBoolean();
	}

	@Override
	public void processData(PacketSendTimerData packetSendTimerData, MessageContext messageContext) {
		Timer.setData(packetSendTimerData.countedTime, active);
	}
}
