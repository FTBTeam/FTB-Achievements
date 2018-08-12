package me.modmuss50.ftba.team;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.text.TextFormatting;
import reborncore.common.network.ExtendedPacketBuffer;

/**
 * Created by modmuss50 on 22/05/2017.
 */
public enum TeamEnum {

	SPECTATOR("spectator", "Spectator Team", TextFormatting.WHITE),
	RED("red", "Red Team", TextFormatting.RED),
	GREEN("green", "Green Team", TextFormatting.GREEN),
	BLUE("blue", "Blue Team", TextFormatting.BLUE),
	YELLOW("yellow", "Yellow Team", TextFormatting.YELLOW),
	SINGLEPLAYER("singleplayer", "SinglePlayer", TextFormatting.WHITE);

	private String name;
	private String displayName;
	private TextFormatting color;

	TeamEnum(String name, String displayName, TextFormatting color) {
		this.name = name;
		this.displayName = displayName;
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public TextFormatting getColor() {
		return color;
	}

	public void writeToBuffer(ExtendedPacketBuffer packetBuffer) {
		packetBuffer.writeInt(this.ordinal());
	}

	public static TeamEnum readFromBuffer(ExtendedPacketBuffer packetBuffer) {
		return TeamEnum.values()[packetBuffer.readInt()];
	}

	public static TeamEnum getTeamFormPlayer(EntityPlayer entityPlayer) {
		ScorePlayerTeam team = TeamManager.getPlayersTeam(entityPlayer);
		for (TeamEnum teamEnum : values()) {
			if (team.getName().equalsIgnoreCase(teamEnum.getName())) {
				return teamEnum;
			}
		}
		return null;
	}

}
