package me.modmuss50.ftba.util;

import com.google.common.io.Files;
import me.modmuss50.ftba.FTBAchievements;
import me.modmuss50.ftba.team.ScriptType;
import me.modmuss50.ftba.team.TeamEnum;
import me.modmuss50.ftba.team.TeamManager;
import net.minecraft.command.FunctionObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.scoreboard.ScorePlayerTeam;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by modmuss50 on 22/05/2017.
 */
public class CommandScript {

	public static File scriptDir = new File(FTBAchievements.CONFIG_DIR, "scripts");

	static {
		if (!scriptDir.exists()) {
			scriptDir.mkdir();
		}
	}

	public static void runCommandScript(ICommandSender sender, TeamEnum teamEnum, ScriptType scriptType) throws IOException {
		File file = new File(new File(scriptDir, teamEnum.getName()), scriptType.getName() + ".mcfunction");
		if (!file.exists()) {
			System.out.println("File not found: " + scriptType.getName());
			return;
		}
		List<String> functionList = new ArrayList<>();
		for (String s : Files.readLines(file, StandardCharsets.UTF_8)) {
			String line = s;
			ScorePlayerTeam playersTeam = TeamManager.getPlayersTeam(sender);
			if (playersTeam != null) {
				line = line.replace("{team_color}", playersTeam.getColor() + "");
				line = line.replace("{team_name}", playersTeam.getName());
				line = line.replace("{team_size}", playersTeam.getMembershipCollection().size() + "");
			}
			line = line.replace("{sender_name}", sender.getName());
			functionList.add(line);
		}
		FunctionObject functionObject = FunctionObject.create(sender.getServer().getFunctionManager(), functionList);
		sender.getServer().getFunctionManager().execute(functionObject, sender.getServer());
	}

}
