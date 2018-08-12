package me.modmuss50.ftba.commands.subcommands;

import me.modmuss50.ftba.ConfigManager;
import me.modmuss50.ftba.commands.FTBCommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.io.IOException;

/**
 * Created by modmuss50 on 06/02/2017.
 */
public class CommandReload extends FTBCommandBase {
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
			ConfigManager.reload();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sender.sendMessage(new TextComponentString("Reloaded from file"));
		super.execute(server, sender, args);
	}
}
