package me.modmuss50.ftba.packets;

import me.modmuss50.ftba.AchievementManager;
import me.modmuss50.ftba.ConfigManager;
import me.modmuss50.ftba.files.config.GuiTrigger;
import me.modmuss50.ftba.util.AchievementUser;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import reborncore.common.network.ExtendedPacketBuffer;
import reborncore.common.network.INetworkPacket;

import java.io.IOException;

/**
 * Created by modmuss50 on 13/02/2017.
 */
public class PacketGuiTrigger implements INetworkPacket<PacketGuiTrigger> {

	public PacketGuiTrigger() {
	}

	public PacketGuiTrigger(String gui) {
		this.gui = gui;
	}

	String gui;

	@Override
	public void writeData(ExtendedPacketBuffer extendedPacketBuffer) throws IOException {
		extendedPacketBuffer.writeInt(gui.length());
		extendedPacketBuffer.writeString(gui);
	}

	@Override
	public void readData(ExtendedPacketBuffer extendedPacketBuffer) throws IOException {
		gui = extendedPacketBuffer.readString(extendedPacketBuffer.readInt());
	}

	@Override
	public void processData(PacketGuiTrigger packetGuiTrigger, MessageContext messageContext) {
		for (GuiTrigger trigger : ConfigManager.getConfig().guiTriggers) {
			if (trigger.guiClassName.equalsIgnoreCase(gui)) {
				AchievementManager.processAchievement(ConfigManager.getAchivementFromName(trigger.achievement), new AchievementUser(messageContext.getServerHandler().player.getDisplayNameString(), messageContext.getServerHandler().player.world));
			}
		}
	}
}
