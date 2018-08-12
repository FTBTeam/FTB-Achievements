package me.modmuss50.ftba.commands.subcommands.block;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

/**
 * Created by modmuss50 on 06/02/2017.
 */
public class CommandLockBlock extends CommandTreeBase {
	public CommandLockBlock() {
		addSubcommand(new CommandSet());
		addSubcommand(new CommandClear());
	}

	@Override
	public String getName() {
		return "lock";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "lock";
	}
}
