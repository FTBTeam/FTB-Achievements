package me.modmuss50.ftba.commands.subcommands.resgen;

import me.modmuss50.ftba.commands.FTBCommandBase;
import me.modmuss50.ftba.config.ConfigResourceGen;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.io.IOException;

/**
 * Created by modmuss50 on 26/06/2017.
 */
public class CommandResgenReload extends FTBCommandBase {
	@Override
	public String getName() {
		return "reload";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "reload";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		try {
			ConfigResourceGen.load();
			sender.sendMessage(new TextComponentString("Reloaded resource generator from disk."));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
