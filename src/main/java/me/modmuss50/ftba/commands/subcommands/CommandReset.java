package me.modmuss50.ftba.commands.subcommands;

import me.modmuss50.ftba.FTBAchievements;
import me.modmuss50.ftba.commands.FTBCommandBase;
import me.modmuss50.ftba.packets.PacketAchievementProgressReset;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import reborncore.common.network.NetworkManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by modmuss50 on 06/02/2017.
 */
public class CommandReset extends FTBCommandBase {
	@Override
	public String getName() {
		return "reset";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "reset";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		FTBAchievements.dataManager.getFormatServer().triggedAchivements = new ArrayList<>();
		FTBAchievements.dataManager.getFormatServer().achivementTimes = new HashMap<>();
		FTBAchievements.dataManager.getFormatServer().craftingProgress = new HashMap<>();
		FTBAchievements.dataManager.getFormatServer().players = null;
		NetworkManager.sendToAll(new PacketAchievementProgressReset());
		sender.sendMessage(new TextComponentString("Reset achievements"));
		super.execute(server, sender, args);
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
}
