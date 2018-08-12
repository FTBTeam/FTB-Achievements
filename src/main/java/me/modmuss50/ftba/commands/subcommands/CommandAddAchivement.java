package me.modmuss50.ftba.commands.subcommands;

import me.modmuss50.ftba.ConfigManager;
import me.modmuss50.ftba.FTBAchievements;
import me.modmuss50.ftba.commands.FTBCommandBase;
import me.modmuss50.ftba.files.config.FTBAchievement;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

/**
 * Created by modmuss50 on 06/02/2017.
 */
public class CommandAddAchivement extends FTBCommandBase {
	@Override
	public String getName() {
		return "add";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "add <name>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 0) {
			sender.sendMessage(new TextComponentString("Please provide a name"));
		} else {
			FTBAchievement lookupAch = ConfigManager.getAchivementFromName(args[0]);
			if (lookupAch != null) {
				sender.sendMessage(new TextComponentString("An achievement with that names exists!"));
				return;
			}
			FTBAchievement achievement = new FTBAchievement();
			achievement.name = args[0];
			achievement.chatMessage = "FTBAchievement get! : " + achievement.name;
			if (args.length == 2) {
				FTBAchievement parent = ConfigManager.getAchivementFromName(args[1]);
				if (parent != null) {
					achievement.dependsOn = parent.name;
					sender.sendMessage(new TextComponentString("Parent achievement set to: " + achievement.dependsOn));
				} else {
					sender.sendMessage(new TextComponentString("Parent achievement not found"));
					return;
				}
			}
			ConfigManager.getConfig().achievements.add(achievement);
			sender.sendMessage(new TextComponentString("Added an achievement with the name: " + achievement.name));
			FTBAchievements.proxy.reloadAchivements();
		}
		super.execute(server, sender, args);
	}

	public int[] achievementNameLocation() {
		return new int[] { 1 };
	}
}
