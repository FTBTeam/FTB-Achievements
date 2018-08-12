package me.modmuss50.ftba.commands.subcommands.resgen;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

/**
 * Created by modmuss50 on 26/06/2017.
 */
public class CommandResgen extends CommandTreeBase {

	public CommandResgen() {
		addSubcommand(new CommandResgenReload());
		addSubcommand(new CommandResgenAdd());
		addSubcommand(new CommandResgenRemove());
		addSubcommand(new CommandResgenPrintList());
		addSubcommand(new CommandResgenReset());
	}

	@Override
	public String getName() {
		return "resgen";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "resgen";
	}
}
