package me.modmuss50.ftba.client.hud;

import me.modmuss50.ftba.FTBAchievements;
import net.minecraft.client.Minecraft;

public class TeamHUDManager {

	public static boolean shouldShow(){
		return FTBAchievements.proxy.getTeamName(Minecraft.getMinecraft().player).equals("UNKNOWN") || FTBAchievements.proxy.getTeamName(Minecraft.getMinecraft().player).isEmpty();
	}

}
