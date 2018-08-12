package me.modmuss50.ftba.events;

import me.modmuss50.ftba.ConfigManager;
import me.modmuss50.ftba.FTBAchievements;
import me.modmuss50.ftba.packets.PacketSendData;
import me.modmuss50.ftba.util.TimerServerHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import reborncore.common.network.NetworkManager;

/**
 * Created by Mark on 10/02/2017.
 */
public class PlayerLoginEvent {
	@SubscribeEvent
	public static void login(PlayerEvent.PlayerLoggedInEvent event) {
		NetworkManager.sendToPlayer(new PacketSendData(ConfigManager.TINY_GSON.toJson(ConfigManager.getConfig())), (EntityPlayerMP) event.player);
		TimerServerHandler.syncWith((EntityPlayerMP) event.player);
		FTBAchievements.dataManager.syncFormat((EntityPlayerMP) event.player);
	}

}
