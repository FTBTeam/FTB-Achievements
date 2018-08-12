package me.modmuss50.ftba.commands.subcommands.resgen;

import me.modmuss50.ftba.commands.FTBCommandBase;
import me.modmuss50.ftba.config.ConfigResourceGen;
import me.modmuss50.ftba.config.DuplicationResource;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by modmuss50 on 26/06/2017.
 */
public class CommandResgenAdd extends FTBCommandBase {

	@Override
	public String getName() {
		return "add";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "add power time";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length != 2) {
			sender.sendMessage(new TextComponentString("Incorrect Arguments: power time"));
			return;
		}
		if (sender instanceof EntityPlayer) {
			ItemStack heldStack = ((EntityPlayer) sender).getHeldItem(EnumHand.MAIN_HAND);
			if (!heldStack.isEmpty()) {
				List<DuplicationResource> toRemove = new ArrayList<>();
				ConfigResourceGen.duplicationResources.forEach(duplicationResource -> {
					if (duplicationResource.stack.getItem().getRegistryName().equals(heldStack.getItem().getRegistryName()) &&
						duplicationResource.stack.getItemDamage() == heldStack.getItemDamage()) {
						toRemove.add(duplicationResource);
						sender.sendMessage(new TextComponentString("Removed old entry"));
					}
				});
				ConfigResourceGen.duplicationResources.removeAll(toRemove);

				DuplicationResource resource = new DuplicationResource(heldStack.copy(), parseInt(args[0]), parseInt(args[1]));
				ConfigResourceGen.duplicationResources.add(resource);
				try {
					ConfigResourceGen.save();
				} catch (IOException e) {
					e.printStackTrace();
				}
				sender.sendMessage(new TextComponentString("Added " + heldStack.getDisplayName() + " to the resource generation config"));
			} else {
				sender.sendMessage(new TextComponentString("Must be holding an item stack in you hand"));
			}
		} else {
			sender.sendMessage(new TextComponentString("Must be used by a player"));
		}
		super.execute(server, sender, args);
	}

}
