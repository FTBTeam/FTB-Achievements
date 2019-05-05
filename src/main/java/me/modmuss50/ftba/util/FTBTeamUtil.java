package me.modmuss50.ftba.util;

import com.feed_the_beast.ftblib.events.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftblib.events.universe.UniverseLoadedEvent;
import com.feed_the_beast.ftblib.lib.EnumTeamColor;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.TeamType;
import com.feed_the_beast.ftblib.lib.data.Universe;
import me.modmuss50.ftba.packets.PacketSendJEIRecipeData;
import me.modmuss50.ftba.packets.PacketSyncFTBTeam;
import me.modmuss50.ftba.team.TeamEnum;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import reborncore.RebornCore;
import reborncore.common.network.NetworkManager;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FTBTeamUtil {

	public static final HashMap<TeamEnum, String> teamNameMap = new HashMap<>();

	public static void handleOnlineMemebers(EntityPlayer player, Consumer<EntityPlayer> playerRunnable) {
		if (player.world.isRemote) {
			return;
		}
		getTeamMemebersOnServer(player, player.world.getMinecraftServer()).forEach(playerRunnable);
	}

	public static List<EntityPlayer> getTeamMemebersOnServer(EntityPlayer player, MinecraftServer server) {
		ForgeTeam team = getTeam(player);
		return server.getPlayerList().getPlayers().stream()
			.filter(playerMP -> getTeam(playerMP) != null && getTeam(playerMP).getTitle().getUnformattedText()
				.equals(team.getTitle().getUnformattedText())).filter(Objects::nonNull)
			.collect(Collectors.toList());
	}

	private static EntityPlayerMP getPlayerFromUUID(UUID uuid, MinecraftServer server) {
		for (EntityPlayerMP playerMP : server.getPlayerList().getPlayers()) {
			if (playerMP.getUniqueID().equals(uuid)) {
				return playerMP;
			}
		}
		return null; // Player isnt online
	}

	public static ForgePlayer getForgePlayer(EntityPlayer player) {
		if (!Universe.get().players.containsKey(player.getUniqueID())) {
			return null;
		}
		ForgePlayer forgePlayer = Universe.get().getPlayer(player);
		return forgePlayer;
	}

	@Nullable
	public static ForgeTeam getTeam(EntityPlayer player) {
		ForgePlayer forgePlayer = Universe.get().getPlayer(player);
		if(!forgePlayer.hasTeam()){
			return null;
		}
		return forgePlayer.team;
	}

	@SubscribeEvent
	public static void createTeams(UniverseLoadedEvent.CreateServerTeams event){
		teamNameMap.clear();
		Arrays.stream(TeamEnum.values()).forEach(teamEnum -> {
            Universe universe = event.getUniverse();
            ForgeTeam team = new ForgeTeam(universe, universe.generateTeamUID((short) teamEnum.ordinal()), teamEnum.getName(), TeamType.SERVER);
            team.setColor(getFTBTeamColor(teamEnum));
            team.setTitle(teamEnum.getDisplayName());
            //We dont want people adding them self to the team
            team.setFreeToJoin(false);
            //TODO get some nice icons for the teams
			universe.addTeam(team);
            teamNameMap.put(teamEnum, teamEnum.getName());
        });
	}

	private static EnumTeamColor getFTBTeamColor(TeamEnum teamEnum){
		switch (teamEnum){
			case RED:
				return EnumTeamColor.RED;
			case GREEN:
				return EnumTeamColor.GREEN;
			case BLUE:
				return EnumTeamColor.BLUE;
			case YELLOW:
				return EnumTeamColor.YELLOW;
			case SPECTATOR:
				return EnumTeamColor.GRAY;
			case SINGLEPLAYER:
				return EnumTeamColor.GRAY;
		}
		return EnumTeamColor.GRAY;
	}

	public static void joinPlayerToTeam(EntityPlayer player, TeamEnum teamEnum) {
		ForgeTeam team = Universe.get().getTeam(teamNameMap.get(teamEnum));
		team.setFreeToJoin(true);
		ForgePlayer forgePlayer = getForgePlayer(player);
		if (forgePlayer.hasTeam()) {
			forgePlayer.team.removeMember(forgePlayer);
		}
		team.addMember(forgePlayer, false);
		team.setFreeToJoin(false);
	}

	@SubscribeEvent
	public static void playerJoinTeamEvent(ForgeTeamPlayerJoinedEvent event) {
		ForgePlayer player = (ForgePlayer) event.getPlayer();
		updatePlatyerTeamDetails(player.getPlayer(), event.getTeam().getTitle().getUnformattedText());
	}

	@SubscribeEvent
	public static void playerJoinedWorldEvent(PlayerEvent.PlayerLoggedInEvent event) {
		if (!(event.player instanceof EntityPlayerMP) || event.player.world.isRemote) {
			return;
		}
		PacketSendJEIRecipeData.sendJEIPacket((EntityPlayerMP) event.player);
		if (getTeam(event.player) == null) {
			RebornCore.logHelper.info(event.player.getDisplayNameString() + " has joined the server world, but is currently not part of a team");
			return;
		}
		updatePlatyerTeamDetails((EntityPlayerMP) event.player, getTeam(event.player).getTitle().getUnformattedText());
	}

	public static void updatePlatyerTeamDetails(EntityPlayerMP playerMP, String newTeam) {
		RebornCore.logHelper.info(newTeam + " : Team name has been sent to the client " + playerMP.getDisplayNameString());
		NetworkManager.sendToPlayer(new PacketSyncFTBTeam(newTeam), playerMP);
	}

}
