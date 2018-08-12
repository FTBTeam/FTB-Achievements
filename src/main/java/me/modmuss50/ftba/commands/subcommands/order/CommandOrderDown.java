package me.modmuss50.ftba.commands.subcommands.order;

import me.modmuss50.ftba.ConfigManager;
import me.modmuss50.ftba.commands.FTBCommandBase;
import me.modmuss50.ftba.files.config.FTBAchievement;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.Collections;
import java.util.List;

/**
 * Created by modmuss50 on 06/02/2017.
 */
public class CommandOrderDown extends FTBCommandBase {
	@Override
	public String getName() {
		return "down";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "down";
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

		List<FTBAchievement> achievements = ConfigManager.getConfig().achievements;
		int pos = achievements.indexOf(achievement);
		if (pos == achievements.size() - 1) {
			sender.sendMessage(new TextComponentString("Achievement is all ready at the bottom"));
			return;
		}
		Collections.swap(achievements, pos, pos + 1);
		super.execute(server, sender, args);
	}

	public int[] achievementNameLocation() {
		return new int[] { 0 };
	}
}
