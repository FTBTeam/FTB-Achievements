package me.modmuss50.ftba.util;

import com.google.common.base.Charsets;
import me.modmuss50.ftba.packets.PacketSyncPlayerCount;
import me.modmuss50.ftba.team.TeamEnum;
import me.modmuss50.ftba.team.TeamManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.apache.commons.io.FileUtils;
import reborncore.RebornCore;
import reborncore.common.network.NetworkManager;
import reborncore.common.util.serialization.SerializationUtil;

import java.io.File;
import java.io.IOException;

public class WorldPlayerCountHandler {

    public static int playerCount = 4;


    @SubscribeEvent
    public static void loadWorld(WorldEvent.Load event) throws IOException {
        if(event.getWorld().provider.getDimension() == 0 && !event.getWorld().isRemote){
            readWorldType(event.getWorld());
        }
    }

    @SubscribeEvent
    public static void playerLogin(PlayerEvent.PlayerLoggedInEvent event){
        if(event.player.world.isRemote){
            return;
        }
        if(!(event.player instanceof EntityPlayerMP)){
            return;
        }
        NetworkManager.sendToPlayer(new PacketSyncPlayerCount(playerCount), (EntityPlayerMP) event.player);
        if(playerCount == 1){
            handleSinglePlayer((EntityPlayerMP) event.player);
        }
    }

    public static void readWorldType(World world) throws IOException {
        File worldTypeFile = new File(world.getSaveHandler().getWorldDirectory(), "ftba_worldtype.json");
        if(!worldTypeFile.exists()){
            FileUtils.writeStringToFile(worldTypeFile, SerializationUtil.GSON.toJson(new WorldTypeData()), Charsets.UTF_8);
        } else {
            String json = FileUtils.readFileToString(worldTypeFile, Charsets.UTF_8);
            WorldTypeData worldTypeData = SerializationUtil.GSON.fromJson(json, WorldTypeData.class);
            playerCount = worldTypeData.playerCount;
        }
        RebornCore.logHelper.info("Map player count set to: " + playerCount);
    }

    public static void handleSinglePlayer(EntityPlayerMP entityPlayerMP){
        if(FTBTeamUtil.getTeam(entityPlayerMP) != null && FTBTeamUtil.getTeam(entityPlayerMP).isValid()){
            return;
        }
        RebornCore.logHelper.info("Adding player to singleplayer team");
        TeamManager.joinPlayerToTeam(entityPlayerMP, TeamEnum.SINGLEPLAYER);
    }

    public static class WorldTypeData {
        int playerCount = 4;
    }

}
