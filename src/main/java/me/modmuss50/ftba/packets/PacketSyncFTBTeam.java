package me.modmuss50.ftba.packets;

import me.modmuss50.ftba.FTBAchievements;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import reborncore.common.network.ExtendedPacketBuffer;
import reborncore.common.network.INetworkPacket;

import java.io.IOException;

public class PacketSyncFTBTeam implements INetworkPacket<PacketSyncFTBTeam> {

	String teamName;

	public PacketSyncFTBTeam(String teamName) {
		this.teamName = teamName;
	}

	public PacketSyncFTBTeam() {

	}

	@Override
	public void writeData(ExtendedPacketBuffer extendedPacketBuffer) throws IOException {
		extendedPacketBuffer.writeInt(teamName.length());
		extendedPacketBuffer.writeString(teamName);
	}

	@Override
	public void readData(ExtendedPacketBuffer extendedPacketBuffer) throws IOException {
		teamName = extendedPacketBuffer.readString(extendedPacketBuffer.readInt());
	}

	@Override
	public void processData(PacketSyncFTBTeam packetSyncFTBTeam, MessageContext messageContext) {
		FTBAchievements.proxy.updateTeamName(teamName);
	}
}
