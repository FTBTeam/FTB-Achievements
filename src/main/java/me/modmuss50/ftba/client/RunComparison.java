package me.modmuss50.ftba.client;

import me.modmuss50.ftba.files.config.ConfigFormat;
import me.modmuss50.ftba.files.config.FTBAchievement;
import me.modmuss50.ftba.files.runs.AchievementData;
import me.modmuss50.ftba.files.runs.RunData;
import me.modmuss50.ftba.files.runs.RunManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;
import reborncore.common.util.serialization.SerializationUtil;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by modmuss50 on 20/03/2017.
 */
public class RunComparison {

	@Nullable
	private static RunData runData;

	@Nullable
	public static RunData getRunData() {
		return runData;
	}

	public static void setRunData(
		@Nullable
			RunData runData) {
		RunComparison.runData = runData;
	}

	@SideOnly(Side.CLIENT)
	@Nullable
	public static RunData downloadFastest() {
		try {
			String json = IOUtils.toString(new URL("http://ftb.world/fastestTime.json"));
			RunData temp = SerializationUtil.GSON.fromJson(json, RunData.class);
			if (isRunValid(temp)) {
				return temp;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SideOnly(Side.CLIENT)

	@Nullable
	public static RunData loadUserFastest() {
		RunData fastest = null;
		for (RunData runData : RunManager.getRuns()) {
			if (isRunValid(runData)) {
				if (fastest == null) {
					fastest = runData;
				} else if (runData.totalTime < fastest.totalTime) {
					fastest = runData;
				}
			}
		}
		return fastest;
	}

	@SideOnly(Side.CLIENT)
	public static boolean isRunValid(RunData data) {
		ConfigFormat format = ClientDataManager.getConfigFormat(); //TODO this might not always be the client???
		boolean hasAllAchievements = true;
		List<String> runAchs = new ArrayList<>();
		for (AchievementData achRemote : data.achievementData) {
			runAchs.add(achRemote.achievement);
		}
		for (FTBAchievement achClient : format.achievements) {
			if (!runAchs.contains(achClient.name)) {
				hasAllAchievements = false;
			}
		}
		return hasAllAchievements;
	}

	public static void main(String[] args) throws IOException {
		downloadFastest();
	}
}
