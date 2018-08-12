package me.modmuss50.ftba.commands.subcommands.timer;

import me.modmuss50.ftba.commands.FTBCommandBase;
import me.modmuss50.ftba.util.TimerServerHandler;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

/**
 * Created by modmuss50 on 14/02/2017.
 */
public class CommandReset extends FTBCommandBase {
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
		TimerServerHandler.reset();
		sender.sendMessage(new TextComponentString("Timer reset"));
		super.execute(server, sender, args);
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
}
