package me.modmuss50.ftba.commands.subcommands;

import me.modmuss50.ftba.FTBAchievements;
import me.modmuss50.ftba.commands.FTBCommandBase;
import me.modmuss50.ftba.files.worldData.WorldFormat;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

/**
 * Created by modmuss50 on 06/02/2017.
 */
public class CommandUnlink extends FTBCommandBase {
	@Override
	public String getName() {
		return "unlink";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "unlink";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		WorldFormat data = FTBAchievements.dataManager.getFormatServer();
		data.chestX = 0;
		data.chestY = 0;
		data.chestZ = 0;
		data.chestDimID = 0;
		super.execute(server, sender, args);
	}
}
