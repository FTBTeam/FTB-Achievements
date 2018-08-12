package me.modmuss50.ftba.commands.subcommands.timer;

import me.modmuss50.ftba.util.TimerServerHandler;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.command.CommandTreeBase;

/**
 * Created by modmuss50 on 14/02/2017.
 */
public class CommandTimer extends CommandTreeBase {
	public CommandTimer() {
		addSubcommand(new CommandStart());
		addSubcommand(new CommandStop());
		addSubcommand(new CommandReset());
		addSubcommand(new CommandSet());
	}

	@Override
	public String getName() {
		return "timer";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "timer";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		super.execute(server, sender, args);
		TimerServerHandler.syncWithAll(); //Just always sync with this command.
		super.execute(server, sender, args);
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
}
