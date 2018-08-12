package me.modmuss50.ftba.packets;

import me.modmuss50.ftba.team.TeamEnum;
import me.modmuss50.ftba.team.TeamManager;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import reborncore.common.network.ExtendedPacketBuffer;
import reborncore.common.network.INetworkPacket;

import java.io.IOException;

/**
 * Created by modmuss50 on 22/05/2017.
 */
public class PacketPlayerJoinTeam implements INetworkPacket<PacketPlayerJoinTeam> {

	TeamEnum team;

	public PacketPlayerJoinTeam(TeamEnum team) {
		this.team = team;
	}

	public PacketPlayerJoinTeam() {
	}

	@Override
	public void writeData(ExtendedPacketBuffer extendedPacketBuffer) throws IOException {
		team.writeToBuffer(extendedPacketBuffer);
	}

	@Override
	public void readData(ExtendedPacketBuffer extendedPacketBuffer) throws IOException {
		team = TeamEnum.readFromBuffer(extendedPacketBuffer);
	}

	@Override
	public void processData(PacketPlayerJoinTeam packetTeamJoin, MessageContext messageContext) {
		TeamManager.joinPlayerToTeam(messageContext.getServerHandler().player, packetTeamJoin.team);
	}
}
