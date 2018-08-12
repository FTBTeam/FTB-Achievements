package me.modmuss50.ftba.team;

import me.modmuss50.ftba.packets.PacketSendJEIRecipeData;
import me.modmuss50.ftba.util.FTBTeamUtil;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by modmuss50 on 22/05/2017.
 */
public class TeamManager {

	public static void joinPlayerToTeam(EntityPlayer player, TeamEnum teamEnum) {
		if (player.world.isRemote) {
			throw new Error("Cannot interract with scoreboard on the client");
		}
		Scoreboard scoreboard = getScoreBoard(player.world);
		setupDefaultTeams(scoreboard);
		ScorePlayerTeam team = getTeam(scoreboard, teamEnum.getName());
		team.setColor(teamEnum.getColor());
		team.setPrefix(teamEnum.getColor().toString());
		team.setSuffix(TextFormatting.RESET.toString());
		ScorePlayerTeam playerTeam = getPlayersTeam(player);
		if(playerTeam != null){
			getScoreBoard(player.getEntityWorld()).removePlayerFromTeams(player.getName());
		}
		scoreboard.addPlayerToTeam(player.getName(), teamEnum.getName());
		scoreboard.broadcastTeamInfoUpdate(team);
		ScriptType.PLAYER_JOIN_TEAM.handle(player, teamEnum);
		FTBTeamUtil.joinPlayerToTeam(player, teamEnum);
		//Sync the ResGen teirs
		if(player instanceof EntityPlayerMP){
			PacketSendJEIRecipeData.sendJEIPacket((EntityPlayerMP) player);
		}
	}

	@Nullable
	public static ScorePlayerTeam getPlayersTeam(ICommandSender sender) {
		return getScoreBoard(sender.getEntityWorld()).getPlayersTeam(sender.getName());
	}

	//Should be fine to call this even if the teams are setup
	public static void setupDefaultTeams(Scoreboard scoreboard) {
		for (TeamEnum team : TeamEnum.values()) {
			createTeamIfNeeded(scoreboard, team);
		}
	}

	public static Scoreboard getScoreBoard(World world) {
		return world.getMinecraftServer().getWorld(0).getScoreboard();
	}

	public static void createTeamIfNeeded(Scoreboard scoreboard, TeamEnum teamEnum) {
		ScorePlayerTeam team = scoreboard.getTeam(teamEnum.getName());
		if (team == null) {
			team = scoreboard.createTeam(teamEnum.getName());
		}
		team.setDisplayName(teamEnum.getDisplayName());
		team.setColor(teamEnum.getColor());
	}

	public static ScorePlayerTeam getTeam(Scoreboard scoreboard, String team) {
		return scoreboard.getTeam(team);
	}

}
