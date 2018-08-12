package me.modmuss50.ftba.team;

import me.modmuss50.ftba.util.CommandScript;
import net.minecraft.entity.player.EntityPlayer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public enum ScriptType {
	PLAYER_JOIN_TEAM("playerJoin", TeamEnum.values());

	private String name;

	private List<TeamEnum> forTeams;

	ScriptType(String name, TeamEnum... teamsFor) {
		this.name = name;
		this.forTeams = Arrays.asList(teamsFor);
	}

	public List<TeamEnum> getForTeams() {
		return forTeams;
	}

	public String getName() {
		return name;
	}

	public void handle(EntityPlayer player, TeamEnum teamEnum) {
		if (player.world.isRemote) {
			return;
		}
		try {
			CommandScript.runCommandScript(player, teamEnum, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
