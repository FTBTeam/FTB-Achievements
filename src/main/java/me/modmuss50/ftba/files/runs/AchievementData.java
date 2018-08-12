package me.modmuss50.ftba.files.runs;

import java.io.Serializable;

/**
 * Created by modmuss50 on 28/02/2017.
 */
public class AchievementData implements Serializable {

	public String achievement;

	public long time;

	public AchievementData(String achievement, long time) {
		this.achievement = achievement;
		this.time = time;
	}
}
