package me.modmuss50.ftba.client;

import me.modmuss50.ftba.files.config.ConfigFormat;
import me.modmuss50.ftba.files.worldData.WorldFormat;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.HashMap;

@SideOnly(Side.CLIENT)
public class ClientDataManager {

	@Nullable
	private static ConfigFormat configFormat;

	private static WorldFormat worldFormat;

	private static String worldHash;

	@Nullable
	public static HashMap<String, String> percentageMap;

	@Nullable
	public static ConfigFormat getConfigFormat() {
		return configFormat;
	}

	public static void setConfigFormat(ConfigFormat configFormat) {
		ClientDataManager.configFormat = configFormat;
	}

	public static WorldFormat getWorldFormat() {
		return worldFormat;
	}

	public static void setWorldFormat(WorldFormat worldFormat) {
		ClientDataManager.worldFormat = worldFormat;
	}

	public static String getWorldHash() {
		return worldHash;
	}

	public static void setWorldHash(String worldHash) {
		ClientDataManager.worldHash = worldHash;
	}

}
