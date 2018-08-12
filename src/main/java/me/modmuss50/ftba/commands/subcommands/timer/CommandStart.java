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
public class CommandStart extends FTBCommandBase {
	@Override
	public String getName() {
		return "start";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "start";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		TimerServerHandler.startTimer(TimerServerHandler.getStoppedTime());
		sender.sendMessage(new TextComponentString("Timer started"));
		super.execute(server, sender, args);
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
}
