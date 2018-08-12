package me.modmuss50.ftba.commands.subcommands.block;

import me.modmuss50.ftba.FTBAchievements;
import me.modmuss50.ftba.commands.FTBCommandBase;
import me.modmuss50.ftba.files.worldData.WorldFormat;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by modmuss50 on 06/02/2017.
 */
public class CommandClear extends FTBCommandBase {
	@Override
	public String getName() {
		return "clear";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "set";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (!(sender instanceof EntityPlayer)) {
			sender.sendMessage(new TextComponentString("Sender must be a player"));
			return;
		}
		WorldFormat data = FTBAchievements.dataManager.getFormatServer();
		if (data.unbreakableBlocks == null) {
			data.unbreakableBlocks = new HashMap<>();
		}
		if (!data.unbreakableBlocks.containsKey(((EntityPlayer) sender).world.provider.getDimension())) {
			data.unbreakableBlocks.put(((EntityPlayer) sender).world.provider.getDimension(), new ArrayList<>());
		}
		data.unbreakableBlocks.replace(((EntityPlayer) sender).world.provider.getDimension(), new ArrayList<>());
		sender.sendMessage(new TextComponentString("Reset locked blocks"));
		super.execute(server, sender, args);

	}
}
