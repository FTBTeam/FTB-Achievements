package me.modmuss50.ftba.commands.subcommands;

import me.modmuss50.ftba.ConfigManager;
import me.modmuss50.ftba.commands.FTBCommandBase;
import me.modmuss50.ftba.files.config.FTBAchievement;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

/**
 * Created by Mark on 10/02/2017.
 */
public class CommandRemove extends FTBCommandBase {
	@Override
	public String getName() {
		return "remove";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "remove <name>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 0) {
			sender.sendMessage(new TextComponentString("Please provide a name"));
		} else {
			FTBAchievement achievement = ConfigManager.getAchivementFromName(args[0]);
			if (achievement == null) {
				sender.sendMessage(new TextComponentString("FTBAchievement not found"));
			} else {
				ConfigManager.getConfig().achievements.remove(achievement);
				sender.sendMessage(new TextComponentString("FTBAchievement removed"));
			}
		}
		super.execute(server, sender, args);
	}

	public int[] achievementNameLocation() {
		return new int[] { 0 };
	}
}
