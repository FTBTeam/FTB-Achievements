package me.modmuss50.ftba.proxy;

import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.google.gson.Gson;
import me.modmuss50.ftba.ConfigManager;
import me.modmuss50.ftba.FTBAchievements;
import me.modmuss50.ftba.modCompat.top.TopCompact;
import me.modmuss50.ftba.packets.PacketSendData;
import me.modmuss50.ftba.util.FTBTeamUtil;
import me.modmuss50.ftba.util.WorldPlayerCountHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import reborncore.common.network.NetworkManager;

/**
 * Created by modmuss50 on 09/02/2017.
 */
public class CommonProxy {

	private static String currentTeamName = "UNKNOWN";

	private static Gson gson = new Gson();

	public void init(FMLInitializationEvent event) {

	}

	public void preInit(FMLPreInitializationEvent event) {
		if (Loader.isModLoaded("theoneprobe")) {
			TopCompact.register();
		}
	}

	public void reloadAchivements() {

	}

	public void syncWithClients() {
		PacketSendData dataPacket = new PacketSendData(ConfigManager.TINY_GSON.toJson(ConfigManager.getConfig()));
		NetworkManager.sendToAll(dataPacket);
		FTBAchievements.dataManager.syncFormat();
	}

	public String getWorldSalt() {
		return "server";
	}

	public EntityPlayer getPlayer() {
		return null;
	}

	public String getTeamName(EntityPlayer player) {
		if (player.world.isRemote)
			if (!(player instanceof EntityPlayerMP)) {
				return currentTeamName;
			}
		ForgeTeam team = FTBTeamUtil.getTeam(player);
		if (team == null || !team.isValid()) {
			return "UNKNOWN";
		}
		return team.getTitle().getUnformattedText();
	}

	public void updateTeamName(String name) {
		currentTeamName = name;
	}

	public void clientSideTask(Runnable runnable) {
		throw new UnsupportedOperationException("Not supported on the server side");
	}

	public int getPlayerCount(){
		return WorldPlayerCountHandler.playerCount;
	}

}
