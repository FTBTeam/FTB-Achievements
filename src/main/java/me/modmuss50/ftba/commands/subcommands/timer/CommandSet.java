package me.modmuss50.ftba.commands.subcommands.timer;

import me.modmuss50.ftba.commands.FTBCommandBase;
import me.modmuss50.ftba.util.TimerSaveDataFormat;
import me.modmuss50.ftba.util.TimerServerHandler;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

/**
 * Created by modmuss50 on 14/02/2017.
 */
public class CommandSet extends FTBCommandBase {
	@Override
	public String getName() {
		return "set";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "set";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 0) {
			sender.sendMessage(new TextComponentString("Please provide a time in milliseconds"));
			return;
		}
		TimerServerHandler.load(new TimerSaveDataFormat(parseLong(args[0]), false));
		sender.sendMessage(new TextComponentString("Timer stopped"));
		super.execute(server, sender, args);
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
}
