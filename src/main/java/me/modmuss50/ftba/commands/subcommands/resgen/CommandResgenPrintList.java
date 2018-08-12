package me.modmuss50.ftba.commands.subcommands.resgen;

import me.modmuss50.ftba.commands.FTBCommandBase;
import me.modmuss50.ftba.config.ConfigResourceGen;
import me.modmuss50.ftba.config.DuplicationResource;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandResgenPrintList extends FTBCommandBase {

	@Override
	public String getName() {
		return "printList";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "printList";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length != 0) {
			sender.sendMessage(new TextComponentString("Incorrect Arguments: power time"));
			return;
		}
		for (DuplicationResource resource : ConfigResourceGen.duplicationResources) {
			String resourceList = resource.stack.getDisplayName() + " " +
				resource.stack.getItem().getRegistryName() + ":" +
				resource.stack.getItem().getDamage(resource.stack) +
				" power: " + resource.powerUsage + " time: " + resource.processTime;
			sender.sendMessage(new TextComponentString(resourceList));
		}

	}
}