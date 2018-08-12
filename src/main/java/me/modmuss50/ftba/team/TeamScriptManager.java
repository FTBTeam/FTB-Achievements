package me.modmuss50.ftba.team;

import me.modmuss50.ftba.util.CommandScript;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by modmuss50 on 22/05/2017.
 */
public class TeamScriptManager {

	public static void init() {
		for (ScriptType scriptType : ScriptType.values()) {
			for (TeamEnum teamEnum : scriptType.getForTeams()) {
				createFileIfNeeded(teamEnum, scriptType);
			}
		}
	}

	private static void createFileIfNeeded(TeamEnum teamEnum, ScriptType type) {
		File teamScript = new File(new File(CommandScript.scriptDir, teamEnum.getName()), type.getName() + ".mcfunction");
		if (!teamScript.exists()) {
			try {
				FileUtils.touch(teamScript);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
