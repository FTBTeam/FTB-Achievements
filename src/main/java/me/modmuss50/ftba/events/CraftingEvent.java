package me.modmuss50.ftba.events;

import me.modmuss50.ftba.AchievementManager;
import me.modmuss50.ftba.ConfigManager;
import me.modmuss50.ftba.FTBAchievements;
import me.modmuss50.ftba.files.config.CraftingTrigger;
import me.modmuss50.ftba.files.worldData.WorldFormat;
import me.modmuss50.ftba.packets.PacketAchievementProgress;
import me.modmuss50.ftba.util.AchievementUser;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import reborncore.common.network.NetworkManager;
import reborncore.common.util.ItemUtils;

import java.util.HashMap;

/**
 * Created by Mark on 04/02/2017.
 */
public class CraftingEvent {

	@SubscribeEvent
	public void crafted(PlayerEvent.ItemCraftedEvent event) {
		if (event.player.world.isRemote) {
			return;
		}
		WorldFormat format = FTBAchievements.dataManager.getFormatServer();
		if (ConfigManager.getConfig() == null || ConfigManager.getConfig().achievements == null) {
			return;
		}
		for (CraftingTrigger trigger : ConfigManager.getConfig().craftingTriggers) {
			if (trigger.triggerItem == null || trigger.triggerItem.isEmpty()) {
				continue;
			}
			if (trigger.triggerItem.getCount() > 1) {
				String name = trigger.achievement + "$" + trigger.triggerItem.getUnlocalizedName();
				int preCounted = 0;
				if (format.craftingProgress == null) {
					format.craftingProgress = new HashMap<>();
				}
				if (format.craftingProgress.containsKey(name)) {
					preCounted = format.craftingProgress.get(name);
				} else {
					format.craftingProgress.put(name, 0);
				}
				if (ItemUtils.isItemEqual(event.crafting, trigger.triggerItem, true, false, false)) {
					preCounted += 1;
					String value = preCounted + "/" + trigger.triggerItem.getCount();
					NetworkManager.sendToAll(new PacketAchievementProgress(value, trigger.achievement));
					if (preCounted >= trigger.triggerItem.getCount()) {
						AchievementManager.processAchievement(ConfigManager.getAchivementFromName(trigger.achievement), new AchievementUser(event.player.getDisplayNameString(), event.player.world));
					}
					format.craftingProgress.replace(name, preCounted);
				}

			} else {
				if (ItemUtils.isItemEqual(event.crafting, trigger.triggerItem, true, false, false)) {
					AchievementManager.processAchievement(ConfigManager.getAchivementFromName(trigger.achievement), new AchievementUser(event.player.getDisplayNameString(), event.player.world));
				}
			}

		}
	}

}
