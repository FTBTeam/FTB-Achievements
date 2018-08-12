package me.modmuss50.ftba.commands.subcommands.triggers;

import me.modmuss50.ftba.ConfigManager;
import me.modmuss50.ftba.commands.FTBCommandBase;
import me.modmuss50.ftba.files.config.FTBAchievement;
import me.modmuss50.ftba.files.config.VMTrigger;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;

/**
 * Created by modmuss50 on 06/02/2017.
 */
public class CommandVMTrigger extends FTBCommandBase {
	@Override
	public String getName() {
		return "vm";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "vm";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length != 2) {
			sender.sendMessage(new TextComponentString("Please provide a achievement name and a tier"));
			return;
		}
		if (sender instanceof EntityPlayer) {
			FTBAchievement achievement = ConfigManager.getAchivementFromName(args[0]);
			if (achievement == null) {
				sender.sendMessage(new TextComponentString("Could not find achievement"));
				return;
			}
			VMTrigger trigger = new VMTrigger();
			trigger.achievement = achievement.name;
			trigger.tier = parseInt(args[1]);
			if (ConfigManager.getConfig().vmTriggers == null) {
				ConfigManager.getConfig().vmTriggers = new ArrayList<>();
			}
			ConfigManager.getConfig().vmTriggers.add(trigger);
			sender.sendMessage(new TextComponentString("Added void miner to " + achievement.name + "'s trigger list"));
		} else {
			sender.sendMessage(new TextComponentString("Must be used by a player"));
		}
		super.execute(server, sender, args);
	}

	public int[] achievementNameLocation() {
		return new int[] { 0 };
	}
}
