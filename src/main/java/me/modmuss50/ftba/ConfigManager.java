package me.modmuss50.ftba;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.modmuss50.ftba.files.config.BlockTrigger;
import me.modmuss50.ftba.files.config.ConfigFormat;
import me.modmuss50.ftba.files.config.FTBAchievement;
import me.modmuss50.ftba.files.config.LeaderboardData;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import org.apache.commons.io.FileUtils;
import reborncore.common.util.serialization.ItemStackSerializer;
import reborncore.common.util.serialization.SerializationUtil;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Mark on 04/02/2017.
 */
public class ConfigManager {

	//Should not be called on client, not ever
	private static ConfigFormat config;

	public static File configFile;

	public static final Gson TINY_GSON = (new GsonBuilder()).enableComplexMapKeySerialization().registerTypeAdapter(ItemStack.class, new ItemStackSerializer()).create();

	public static void load() throws IOException {
		if (!configFile.exists()) {
			genDefault();
			FileUtils.writeStringToFile(configFile, SerializationUtil.GSON.toJson(config));
		} else {
			config = SerializationUtil.GSON.fromJson(FileUtils.readFileToString(configFile), ConfigFormat.class);
		}
	}

	public static void save() throws IOException {
		FileUtils.writeStringToFile(configFile, SerializationUtil.GSON.toJson(config));
	}

	public static void reload() throws IOException {
		config = SerializationUtil.GSON.fromJson(FileUtils.readFileToString(configFile), ConfigFormat.class);
	}

	@Nullable
	public static FTBAchievement getAchivementFromName(String name) {
		for (FTBAchievement achievement : config.achievements) {
			if (achievement.name.equals(name)) {
				return achievement;
			}
		}
		//TODO crash?
		return null;
	}

	public static int getMaxInputs() {
		if (config.inputTriggers == null) {
			return 0;
		}
		return config.inputTriggers.size();
	}

	public static ConfigFormat getConfig() {
		return config;
	}

	public static File getConfigFile() {
		return configFile;
	}

	@Nullable
	public static BlockTrigger getTriggerFromMeta(int meta) {
		if (config.inputTriggers == null || config.inputTriggers.size() <= meta) {
			return null;
		}
		return config.inputTriggers.get(meta);
	}

	public static int getMetaFromTrigger(BlockTrigger trigger) {
		if (!config.inputTriggers.contains(trigger)) {
			return 0;
		}
		return config.inputTriggers.indexOf(trigger);
	}

	private static void genDefault() {
		config = new ConfigFormat();
		config.achievements = new ArrayList<>();
		config.craftingTriggers = new ArrayList<>();
		config.inputTriggers = new ArrayList<>();
		config.guiTriggers = new ArrayList<>();

		//		FTBAchievement achievement = new FTBAchievement();
		//		achievement.name = "achievement01";
		//		achievement.rewards = new ArrayList<>();
		//		achievement.rewards.add(new ItemStack(Items.GOLDEN_APPLE));
		//		achievement.chatMessage = "Well done, you unlocked a golden apple";
		//		config.achievements.add(achievement);
		//
		//		FTBAchievement achievement2 = new FTBAchievement();
		//		achievement2.name = "generate_power";
		//		achievement2.rewards = new ArrayList<>();
		//		achievement2.rewards.add(new ItemStack(Blocks.DIAMOND_ORE, 10));
		//		achievement2.chatMessage = "Well done, you gained a diamond block!";
		//		config.achievements.add(achievement2);
		//
		//		CraftingTrigger trigger = new CraftingTrigger();
		//		trigger.achievement = achievement.name;
		//		trigger.triggerItem = new ItemStack(Blocks.FURNACE);
		//		config.craftingTriggers.add(trigger);

		LeaderboardData leaderboardData = new LeaderboardData();
		leaderboardData.packName = "UNNAMED";

		config.leaderboard = leaderboardData;

		//		BlockTrigger blockTrigger = new BlockTrigger();
		//		blockTrigger.achievement = achievement.name;
		//		blockTrigger.type = "redstone";
		//		config.inputTriggers.add(blockTrigger);
		//
		//		BlockTrigger blockTrigger2 = new BlockTrigger();
		//		blockTrigger2.achievement = achievement2.name;
		//		blockTrigger2.type = "rf";
		//		blockTrigger2.requirement = 10000;
		//		config.inputTriggers.add(blockTrigger2);
	}

	public static String getConfigHash() throws IOException {
		FileInputStream fis = new FileInputStream(configFile);
		String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
		fis.close();
		return md5;
	}

}
