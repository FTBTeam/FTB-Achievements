package me.modmuss50.ftba.commands.subcommands.triggers;

import me.modmuss50.ftba.modCompat.sc.CommandStevesCarts;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.server.command.CommandTreeBase;

/**
 * Created by modmuss50 on 06/02/2017.
 */
public class CommandAddTrigger extends CommandTreeBase {
	public CommandAddTrigger() {
		addSubcommand(new CommandCraftingTrigger());
		addSubcommand(new CommandRedstoneTrigger());
		addSubcommand(new CommandRFTrigger());
		addSubcommand(new CommandGuiTrigger());
		if (Loader.isModLoaded("stevescarts")) {
			addSubcommand(new CommandStevesCarts());
		}
		addSubcommand(new CommandMeshTrigger());
		addSubcommand(new CommandDETrigger());
		addSubcommand(new CommandBMTrigger());
		addSubcommand(new CommandVMTrigger());
		addSubcommand(new CommandCrusherTrigger());
	}

	@Override
	public String getName() {
		return "addTrigger";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "addTrigger";
	}
}
