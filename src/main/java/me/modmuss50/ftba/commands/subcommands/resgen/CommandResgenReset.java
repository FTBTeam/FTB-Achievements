package me.modmuss50.ftba.commands.subcommands.resgen;

import me.modmuss50.ftba.commands.FTBCommandBase;
import me.modmuss50.ftba.util.recipes.PoweredTierRecipe;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.List;

/**
 * Created by modmuss50 on 26/06/2017.
 */
public class CommandResgenReset extends FTBCommandBase {
	@Override
	public String getName() {
		return "reset";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "reset";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (!(sender instanceof EntityPlayer)) {
			sender.sendMessage(new TextComponentString("Please run this as a player"));
			return;
		}
		List<PoweredTierRecipe.RecipeGroupTracker> trackers = PoweredTierRecipe.getTrackersForPlayers((EntityPlayer) sender);
		trackers.forEach(tracker -> tracker.resetTeir((EntityPlayer) sender));
		sender.sendMessage(new TextComponentString(trackers.size() + " reset"));
		super.execute(server, sender, args);
	}

}
