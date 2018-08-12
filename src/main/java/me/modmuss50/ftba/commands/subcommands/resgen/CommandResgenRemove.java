package me.modmuss50.ftba.commands.subcommands.resgen;

import me.modmuss50.ftba.commands.FTBCommandBase;
import me.modmuss50.ftba.config.ConfigResourceGen;
import me.modmuss50.ftba.config.DuplicationResource;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by modmuss50 on 26/06/2017.
 */
public class CommandResgenRemove extends FTBCommandBase {
	@Override
	public String getName() {
		return "remove";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "remove";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length != 1) {
			sender.sendMessage(new TextComponentString("Please provide a block or item resource name"));
			return;
		}
		String[] argsParts = args[0].split(":");
		ResourceLocation resourceLocation = new ResourceLocation(argsParts[0] + ":" + argsParts[1]);
		List<DuplicationResource> toRemove = new ArrayList<>();
		ConfigResourceGen.duplicationResources.forEach(duplicationResource -> {
			if (duplicationResource.stack.getItem().getRegistryName().equals(resourceLocation) &&
				duplicationResource.stack.getItemDamage() == Integer.parseInt(argsParts[2])) {
				toRemove.add(duplicationResource);
				sender.sendMessage(new TextComponentString("Removed " + duplicationResource.stack.getDisplayName()));
			}
		});
		if (toRemove.isEmpty()) {
			sender.sendMessage(new TextComponentString("Nothing to remove"));
		}
		ConfigResourceGen.duplicationResources.removeAll(toRemove);
		try {
			ConfigResourceGen.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.execute(server, sender, args);
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server,
	                                      ICommandSender sender,
	                                      String[] args,
	                                      @Nullable
		                                      BlockPos pos) {
		int[] nameArgLoc = new int[] { 0 };
		for (int argPos : nameArgLoc) {
			if (args.length == argPos + 1) {
				List<String> nameList = new ArrayList<>();
				for (DuplicationResource duplicationResource : ConfigResourceGen.duplicationResources) {
					nameList.add(duplicationResource.stack.getItem().getRegistryName().toString() + ":" + duplicationResource.stack.getItemDamage());
				}
				Collections.sort(nameList, null);
				return getListOfStringsMatchingLastWord(args, nameList);
			}
		}
		return super.getTabCompletions(server, sender, args, pos);
	}
}
