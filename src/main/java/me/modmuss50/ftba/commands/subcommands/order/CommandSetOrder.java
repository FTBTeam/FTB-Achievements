package me.modmuss50.ftba.commands.subcommands.order;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

/**
 * Created by modmuss50 on 06/02/2017.
 */
public class CommandSetOrder extends CommandTreeBase {
	public CommandSetOrder() {
		addSubcommand(new CommandOrderUp());
		addSubcommand(new CommandOrderDown());
	}

	@Override
	public String getName() {
		return "order";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "order";
	}
}
