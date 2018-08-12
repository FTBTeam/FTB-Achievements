package me.modmuss50.ftba.web;

import me.modmuss50.ftba.client.RunComparison;
import me.modmuss50.ftba.client.hud.ClientHudRenderer;
import me.modmuss50.ftba.files.config.ConfigFormat;
import me.modmuss50.ftba.files.config.FTBAchievement;
import me.modmuss50.ftba.files.runs.AchievementData;
import me.modmuss50.ftba.files.runs.RunData;
import me.modmuss50.ftba.files.worldData.WorldFormat;
import me.modmuss50.ftba.util.TimerServerHandler;

import java.io.IOException;

/**
 * Created by Mark on 09/04/2017.
 */
public class PageBuilder {

	private static String entry;

	public static String getData(WebServer server, ConfigFormat format, WorldFormat worldFormat) {
		if (entry == null || entry.isEmpty()) {
			try {
				loadData(server);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String entryData = "";
		for (FTBAchievement achievement : format.achievements) {
			entryData = entryData + buildEntry(achievement, worldFormat);
		}
		return entryData;
	}

	public static String buildEntry(FTBAchievement achievement, WorldFormat worldFormat) {
		String eCopy = entry;
		String data = "";
		boolean completed = worldFormat.triggedAchivements != null && !worldFormat.triggedAchivements.isEmpty() && worldFormat.triggedAchivements.contains(achievement.name);
		RunData runData = RunComparison.getRunData();
		String completedTime = "";

		if (!completed) {
			eCopy = eCopy.replace(" list-group-item-success", "");
		} else {
			data = "Achievement completed in: " + TimerServerHandler.getNiceTimeFromLong(worldFormat.achivementTimes.get(achievement.name));
			if (runData != null) {
				long time = worldFormat.achivementTimes.get(achievement.name);
				long fastestTime = 0;
				for (AchievementData fastData : runData.achievementData) {
					if (fastData.achievement.equals(achievement.name)) {
						fastestTime = fastData.time;
					}
				}
				long timeDiff = fastestTime - time;
				boolean negative = timeDiff < 0;
				String prefix = "You were faster than " + runData.userName + " by: ";
				if (negative) {
					timeDiff = Math.abs(timeDiff);
					prefix = "You were slower than " + runData.userName + " by: ";
					eCopy = eCopy.replace("list-group-item-success", "list-group-item-danger");
				}
				data = prefix + TimerServerHandler.getNiceTimeFromLong(timeDiff);
				completedTime = " (" + TimerServerHandler.getNiceTimeFromLong(worldFormat.achivementTimes.get(achievement.name)) + ")";
			}
		}

		return eCopy.replace("%ACH_NAME%", ClientHudRenderer.getBookTitle(achievement) + completedTime).replace("%ACH_STRING%", data);
	}

	private static void loadData(WebServer server) throws IOException {
		entry = server.readFile("entry.html");
	}

}
