package me.modmuss50.ftba.modCompat.sc;

import me.modmuss50.ftba.AchievementManager;
import me.modmuss50.ftba.ConfigManager;
import me.modmuss50.ftba.files.config.FTBAchievement;
import me.modmuss50.ftba.files.config.SCTrigger;
import me.modmuss50.ftba.util.AchievementUser;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vswe.stevescarts.modules.data.ModuleData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mark on 22/02/2017.
 */
public class StevesCartsCompat {

	public static List<String> modules;
	public static HashMap<String, String> nameMap;

	public static void loadModules() {
		modules = new ArrayList<>();
		nameMap = new HashMap<>();
		for (ModuleData moduleData : ModuleData.getModules()) {
			modules.add(moduleData.getModuleClass().getSimpleName().replace("Module", ""));
			nameMap.put(moduleData.getModuleClass().getSimpleName().replace("Module", ""), moduleData.getName());
		}
		Collections.sort(modules, null);
	}

	@SubscribeEvent
	public static void rightClick(PlayerInteractEvent.RightClickBlock event) {
		if (event.getWorld().isRemote) {
			return;
		}
		if (event.getItemStack() != null && !event.getWorld().isRemote) {
			if (event.getItemStack().getItem().getRegistryName().toString().equals("stevescarts:ModularCart")) {
				if (event.getItemStack().getTagCompound().hasKey("maxTime")) {
					return;
				}
				IBlockState state = event.getWorld().getBlockState(event.getPos());
				if (!(state.getBlock() instanceof BlockRailBase)) {
					return;
				}
				List<String> moduleList = new ArrayList<>();
				NBTTagByteArray moduleIDTag = (NBTTagByteArray) event.getItemStack().getTagCompound().getTag("Modules");
				for (final byte id : moduleIDTag.getByteArray()) {
					if (ConfigManager.getConfig().scTriggers != null && !ConfigManager.getConfig().scTriggers.isEmpty()) {
						moduleList.add(ModuleData.getList().get(id).getModuleClass().getSimpleName().replace("Module", ""));
					}
				}
				for (FTBAchievement achievement : ConfigManager.getConfig().achievements) {
					List<SCTrigger> triggers = new ArrayList<>();
					for (SCTrigger trigger : ConfigManager.getConfig().scTriggers) {
						if (trigger.achievement.equals(achievement.name)) {
							triggers.add(trigger);
						}
					}
					boolean hasAll = !triggers.isEmpty();
					for (SCTrigger trigger : triggers) {
						if (!moduleList.contains(trigger.moduleName)) {
							hasAll = false;
						}
					}
					if (hasAll) {
						AchievementManager.processAchievement(achievement, new AchievementUser(event.getEntityPlayer().getDisplayNameString(), event.getWorld()));
					}
				}
			}
		}
	}

}
