package me.modmuss50.ftba.commands;

import me.modmuss50.ftba.commands.subcommands.*;
import me.modmuss50.ftba.commands.subcommands.block.CommandLockBlock;
import me.modmuss50.ftba.commands.subcommands.order.CommandSetOrder;
import me.modmuss50.ftba.commands.subcommands.resgen.CommandResgen;
import me.modmuss50.ftba.commands.subcommands.timer.CommandTimer;
import me.modmuss50.ftba.commands.subcommands.triggers.CommandAddTrigger;
import me.modmuss50.ftba.commands.subcommands.triggers.CommandRemoveTrigger;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

/**
 * Created by Mark on 05/02/2017.
 */
public class FTBACommand extends CommandTreeBase {
	public FTBACommand() {
		addSubcommand(new CommandReset());
		addSubcommand(new CommandSetChest());
		addSubcommand(new CommandUnlink());
		addSubcommand(new CommandAddAchivement());
		addSubcommand(new CommandReload());
		addSubcommand(new CommandAddReward());
		addSubcommand(new CommandAddTrigger());
		addSubcommand(new CommandRemoveTrigger());
		addSubcommand(new CommandRemove());
		addSubcommand(new CommandSetIcon());
		addSubcommand(new CommandSetDescription());
		addSubcommand(new CommandTimer());
		addSubcommand(new CommandTrigger());
		addSubcommand(new CommandSetStart());
		addSubcommand(new CommandSetOrder());
		addSubcommand(new CommandLockBlock());
		addSubcommand(new CommandResgen());
	}

	@Override
	public String getName() {
		return "ftba";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "ftba";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
}
