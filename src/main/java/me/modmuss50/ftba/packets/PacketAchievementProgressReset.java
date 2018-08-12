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
public class PacketAchievementProgressReset implements INetworkPacket<PacketAchievementProgressReset> {

	public PacketAchievementProgressReset() {
	}

	@Override
	public void writeData(ExtendedPacketBuffer extendedPacketBuffer) throws IOException {

	}

	@Override
	public void readData(ExtendedPacketBuffer extendedPacketBuffer) throws IOException {

	}

	@SideOnly(Side.CLIENT)
	@Override
	public void processData(PacketAchievementProgressReset packetGuiTrigger, MessageContext messageContext) {
		ClientDataManager.percentageMap = new HashMap<>();
	}
}
