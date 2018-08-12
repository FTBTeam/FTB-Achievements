package me.modmuss50.ftba.config;

import me.modmuss50.ftba.FTBAchievements;
import org.apache.commons.io.FileUtils;
import reborncore.common.util.serialization.SerializationUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created by modmuss50 on 22/05/2017.
 */
public class NewConfigManager {

	public static void loadAll() {
		ConfigResGenUpgrades.INSTANCE = ConfigResGenUpgrades.getDefault();
	}

	public static Object loadConfig(Object config, String name) throws IOException {
		File file = new File(FTBAchievements.CONFIG_DIR, name + ".json");
		Object con = config;
		if (file.exists()) {
			con = SerializationUtil.GSON.fromJson(FileUtils.readFileToString(file, StandardCharsets.UTF_8), config.getClass());
			return con;
		} else {
			return save(config, file);
		}
	}

	public static File getFile(String name) {
		return new File(FTBAchievements.CONFIG_DIR, name + ".json");
	}

	public static Object save(Object config, File file) throws IOException {
		FileUtils.writeStringToFile(file, SerializationUtil.GSON.toJson(config), StandardCharsets.UTF_8);
		return config;
	}
}
