package me.modmuss50.ftba.files.runs;

import java.io.Serializable;
import java.util.List;

/**
 * Created by modmuss50 on 28/02/2017.
 */
public class RunData implements Serializable {

	public long totalTime;

	public String uuid;

	public String userName;

	public String runHash;

	public String runDate;

	public String videoURL;

	public String configHash;

	public String packName;

	public String modFingerprint;

	public List<AchievementData> achievementData;

	public List<PlayerData> players;

	//public boolean invalid;

	public static class PlayerData implements Serializable {
		public String name;

		public String uuid;

		public PlayerData(String name, String uuid) {
			this.name = name;
			this.uuid = uuid;
		}

		public PlayerData() {
		}
	}

}
