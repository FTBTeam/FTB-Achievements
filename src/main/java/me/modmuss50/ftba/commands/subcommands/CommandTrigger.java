package me.modmuss50.ftba.commands.subcommands;

import me.modmuss50.ftba.AchievementManager;
import me.modmuss50.ftba.ConfigManager;
import me.modmuss50.ftba.commands.FTBCommandBase;
import me.modmuss50.ftba.files.config.FTBAchievement;
import me.modmuss50.ftba.util.AchievementUser;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

/**
 * Created by modmuss50 on 15/02/2017.
 */
public class CommandTrigger extends FTBCommandBase {
	@Override
	public String getName() {
		return "trigger";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "trigger";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 0) {
			sender.sendMessage(new TextComponentString("Please provide a name"));
			return;
		}
		FTBAchievement achievement = ConfigManager.getAchivementFromName(args[0]);
		if (achievement == null) {
			sender.sendMessage(new TextComponentString("FTBAchievement not found"));
			return;
		}
		AchievementManager.processAchievement(achievement, new AchievementUser(sender.getName(), sender.getEntityWorld()));
		super.execute(server, sender, args);
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public int[] achievementNameLocation() {
		return new int[] { 0 };
	}
}
