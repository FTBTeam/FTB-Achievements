package me.modmuss50.ftba.modCompat;

import me.modmuss50.ftba.AchievementManager;
import me.modmuss50.ftba.ConfigManager;
import me.modmuss50.ftba.files.config.SieveTrigger;
import me.modmuss50.ftba.util.AchievementUser;
import me.modmuss50.ftba.util.RayTracer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by modmuss50 on 09/02/2017.
 */
public class ModCompat {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void blockRightClick(PlayerInteractEvent.RightClickBlock event) {
		if (event.getWorld() == null || event.getWorld().isRemote || ConfigManager.getConfig().sieveTriggers == null || ConfigManager.getConfig().sieveTriggers.isEmpty()) {
			return;
		}
		RayTraceResult rayTraceResult = RayTracer.retrace(event.getEntityPlayer());
		if (rayTraceResult == null || rayTraceResult.getBlockPos() == null || rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) {
			return;
		}
		BlockPos pos = rayTraceResult.getBlockPos();
		IBlockState state = event.getWorld().getBlockState(pos);
		TileEntity tileEntity = event.getWorld().getTileEntity(RayTracer.retrace(event.getEntityPlayer()).getBlockPos());
		if (tileEntity != null) {
			if (!tileEntity.getClass().getCanonicalName().equals("exnihiloadscensio.tiles.TileSieve")) {
				return;
			}
			String mesh = getMeshFromSate(state);
			int ammout = 0;
			int scanSize = 7;
			for (int xOffset = -1 * scanSize; xOffset <= scanSize; xOffset++) {
				for (int zOffset = -1 * scanSize; zOffset <= 1 * scanSize; zOffset++) {
					TileEntity entity = event.getWorld().getTileEntity(pos.add(xOffset, 0, zOffset));
					if (entity != null && entity.getClass().getCanonicalName().equals("exnihiloadscensio.tiles.TileSieve")) {
						if (getMeshFromSate(event.getWorld().getBlockState(entity.getPos())).equals(mesh)) {
							ammout++;
						}
					}
				}
			}
			for (SieveTrigger trigger : ConfigManager.getConfig().sieveTriggers) {
				if (trigger.meshType.equalsIgnoreCase(mesh)) {
					AchievementManager.processAchievement(ConfigManager.getAchivementFromName(trigger.achievement), new AchievementUser(event.getEntityPlayer().getDisplayNameString(), event.getEntityPlayer().world));
				}
			}
		}
	}

	public String getMeshFromSate(IBlockState state) {
		String stateString = state.toString();
		if (stateString.contains("mesh=")) {
			String[] split = stateString.split("=");
			return split[1].replace("]", "");
		}
		return "none";
	}

}
