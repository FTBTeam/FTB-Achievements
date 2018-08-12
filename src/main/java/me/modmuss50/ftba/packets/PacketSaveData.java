package me.modmuss50.ftba.packets;

import me.modmuss50.ftba.ConfigManager;
import me.modmuss50.ftba.client.ClientDataManager;
import me.modmuss50.ftba.files.runs.AchievementData;
import me.modmuss50.ftba.files.runs.RunData;
import me.modmuss50.ftba.files.runs.RunManager;
import me.modmuss50.ftba.files.worldData.WorldFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import reborncore.common.network.ExtendedPacketBuffer;
import reborncore.common.network.INetworkPacket;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by modmuss50 on 01/03/2017.
 */
public class PacketSaveData implements INetworkPacket<PacketSaveData> {

	String data;
	long time;
	String players;
	String worldHash;

	public PacketSaveData() {
	}

	public PacketSaveData(String data, long time, String players, String worldHash) {
		this.data = data;
		this.time = time;
		this.players = players;
		this.worldHash = worldHash;
	}

	@Override
	public void writeData(ExtendedPacketBuffer extendedPacketBuffer) throws IOException {
		extendedPacketBuffer.writeInt(data.length());
		extendedPacketBuffer.writeString(data);
		extendedPacketBuffer.writeLong(time);
		extendedPacketBuffer.writeInt(players.length());
		extendedPacketBuffer.writeString(players);
		extendedPacketBuffer.writeInt(worldHash.length());
		extendedPacketBuffer.writeString(worldHash);
	}

	@Override
	public void readData(ExtendedPacketBuffer extendedPacketBuffer) throws IOException {
		data = extendedPacketBuffer.readString(extendedPacketBuffer.readInt());
		time = extendedPacketBuffer.readLong();
		players = extendedPacketBuffer.readString(extendedPacketBuffer.readInt());
		worldHash = extendedPacketBuffer.readString(extendedPacketBuffer.readInt());
	}

	@Override
	public void processData(PacketSaveData packetSendData, MessageContext messageContext) {
		WorldFormat configFormat = ConfigManager.TINY_GSON.fromJson(packetSendData.data, WorldFormat.class);
		RunData data = new RunData();
		data.runHash = packetSendData.worldHash;
		data.runDate = LocalDateTime.now().toString();
		data.userName = Minecraft.getMinecraft().player.getName();
		data.uuid = Minecraft.getMinecraft().player.getUniqueID().toString();
		data.totalTime = packetSendData.time;
		//data.invalid = configFormat.invalid;
		if (ClientDataManager.getConfigFormat() != null && ClientDataManager.getConfigFormat().leaderboard != null && ClientDataManager.getConfigFormat().leaderboard.packName != null && !ClientDataManager.getConfigFormat().leaderboard.packName.isEmpty()) {
			data.packName = ClientDataManager.getConfigFormat().leaderboard.packName;
		}
		data.achievementData = new ArrayList<>();
		data.players = new ArrayList<>();
		String[] players = packetSendData.players.split(",");
		for (String player : players) {
			String[] nameSplit = player.split("#");
			if (!nameSplit[0].equals(data.userName)) {
				data.players.add(new RunData.PlayerData(nameSplit[0], nameSplit[1]));
			}
		}
		try {
			data.configHash = ConfigManager.getConfigHash();
		} catch (IOException e) {
			e.printStackTrace();
			data.configHash = "UNKNOWN";
		}
		for (Map.Entry<String, Long> entry : configFormat.achivementTimes.entrySet()) {
			String name = entry.getKey();
			Long time = entry.getValue();
			data.achievementData.add(new AchievementData(name, time));
		}
		RunManager.saveRun(data);
		Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.AQUA + "Run data has been saved, use the GUI in the main menu to upload to the global leaderboard."));

	}
}
