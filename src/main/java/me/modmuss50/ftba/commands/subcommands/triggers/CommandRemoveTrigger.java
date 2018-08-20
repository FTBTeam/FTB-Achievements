package me.modmuss50.ftba.commands.subcommands.triggers;

import me.modmuss50.ftba.ConfigManager;
import me.modmuss50.ftba.commands.FTBCommandBase;
import me.modmuss50.ftba.files.config.BaseTrigger;
import me.modmuss50.ftba.files.config.ConfigFormat;
import me.modmuss50.ftba.files.config.FTBAchievement;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by modmuss50 on 06/02/2017.
 */
public class CommandRemoveTrigger extends FTBCommandBase {
	@Override
	public String getName() {
		return "removeTrigger";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "removeTrigger";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 0) {
			sender.sendMessage(new TextComponentString("Please provide a achievement name"));
			return;
		}

		FTBAchievement achievement = ConfigManager.getAchivementFromName(args[0]);
		if (achievement == null) {
			sender.sendMessage(new TextComponentString("Could not find achievement"));
			return;
		}

		ConfigFormat format = ConfigManager.getConfig();
		checkRemove(format.craftingTriggers, achievement.name);
		checkRemove(format.inputTriggers, achievement.name);
		checkRemove(format.sieveTriggers, achievement.name);
		checkRemove(format.guiTriggers, achievement.name);
		checkRemove(format.bmTriggers, achievement.name);
		checkRemove(format.deTriggers, achievement.name);
		checkRemove(format.vmTriggers, achievement.name);
		checkRemove(format.crusherTriggers, achievement.name);

		sender.sendMessage(new TextComponentString("Removed all triggers for achievement"));
		super.execute(server, sender, args);
	}

	private void checkRemove(List list, String name) {
		if (list == null || list.isEmpty()) {
			return;
		}
		List toRemove = new ArrayList();
		for (Object object : list) {
			if (object instanceof BaseTrigger) {
				BaseTrigger trigger = (BaseTrigger) object;
				if (trigger.achievement.equals(name)) {
					toRemove.add(trigger);
				}
			}
		}
		list.removeAll(toRemove);
	}

	public int[] achievementNameLocation() {
		return new int[] { 0 };
	}
}
