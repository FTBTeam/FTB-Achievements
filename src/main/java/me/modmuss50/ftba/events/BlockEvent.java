package me.modmuss50.ftba.events;

import me.modmuss50.ftba.FTBAchievements;
import me.modmuss50.ftba.files.worldData.WorldFormat;
import me.modmuss50.ftba.util.TimerServerHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.StringJoiner;

/**
 * Created by Mark on 05/02/2017.
 */
public class BlockEvent {

	@SubscribeEvent
	public static void blockBreak(net.minecraftforge.event.world.BlockEvent.BreakEvent event) {
		if (event.getWorld().isRemote) {
			return;
		}
		WorldFormat format = FTBAchievements.dataManager.getFormatServer();
		if (event.getPos().getX() == 0 && event.getPos().getY() == 0 && event.getPos().getZ() == 0) {
			return;
		}
		if (event.getPos().getX() == format.chestX && event.getPos().getY() == format.chestY && event.getPos().getZ() == format.chestZ && format.chestDimID == event.getWorld().provider.getDimension()) {
			event.setCanceled(true);
			event.getPlayer().sendMessage(new TextComponentString("This chest is the reward chest, it cannot be broken. Unlink to break."));
		}
		if (format.unbreakableBlocks != null) {
			List<BlockPos> posList = format.unbreakableBlocks.get(event.getWorld().provider.getDimension());
			if (posList != null && !posList.isEmpty()) {
				for (BlockPos pos : posList) {
					if (event.getPos().getX() == pos.getX() && event.getPos().getY() == pos.getY() && event.getPos().getZ() == pos.getZ()) {
						event.setCanceled(true);
					}
				}
			}
		}

	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void blockRightClick(PlayerInteractEvent.RightClickBlock event) {
		if (event.getWorld().isRemote) {
			return;
		}
		WorldFormat format = FTBAchievements.dataManager.getFormatServer();
		if (event.getWorld().provider.getDimension() == format.startDimID) {
			BlockPos startPos = new BlockPos(format.startX, format.startY, format.startZ);
			if (startPos.equals(event.getPos())) {
				if (!TimerServerHandler.isActive()) {
					TimerServerHandler.startTimer(TimerServerHandler.getStoppedTime());
					TimerServerHandler.syncWithAll();
				}

				StringJoiner joiner = new StringJoiner(",");
				for (EntityPlayer entityPlayer : event.getWorld().getMinecraftServer().getPlayerList().getPlayers()) {
					if (!entityPlayer.isSpectator()) {
						joiner.add(entityPlayer.getDisplayNameString() + "#" + entityPlayer.getGameProfile().getId().toString());
					}
				}
				format.players = joiner.toString();
			}
		}
	}

}
