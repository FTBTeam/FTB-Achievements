package me.modmuss50.ftba.packets;

import me.modmuss50.ftba.util.ClientWorldPlayerCountHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import reborncore.common.network.ExtendedPacketBuffer;
import reborncore.common.network.INetworkPacket;

import java.io.IOException;

public class PacketSyncPlayerCount implements INetworkPacket<PacketSyncPlayerCount> {

    int playerCount;

    public PacketSyncPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public PacketSyncPlayerCount() {
    }

    @Override
    public void writeData(ExtendedPacketBuffer extendedPacketBuffer) throws IOException {
        extendedPacketBuffer.writeInt(playerCount);
    }

    @Override
    public void readData(ExtendedPacketBuffer extendedPacketBuffer) throws IOException {
        playerCount = extendedPacketBuffer.readInt();
    }

    @Override
    public void processData(PacketSyncPlayerCount packetSyncPlayerCount, MessageContext messageContext) {
        ClientWorldPlayerCountHandler.setPlayerCount(packetSyncPlayerCount.playerCount);
    }
}
