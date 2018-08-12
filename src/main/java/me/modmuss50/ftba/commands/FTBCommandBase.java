package me.modmuss50.ftba.commands;

import me.modmuss50.ftba.ConfigManager;
import me.modmuss50.ftba.FTBAchievements;
import me.modmuss50.ftba.files.config.FTBAchievement;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by modmuss50 on 13/02/2017.
 */
public abstract class FTBCommandBase extends CommandBase {

	@Override
	public List<String> getTabCompletions(MinecraftServer server,
	                                      ICommandSender sender,
	                                      String[] args,
	                                      @Nullable
		                                      BlockPos pos) {
		int[] nameArgLoc = achievementNameLocation();
		for (int argPos : nameArgLoc) {
			if (args.length == argPos + 1) {
				List<String> achievementList = new ArrayList<>();
				for (FTBAchievement achievement : ConfigManager.getConfig().achievements) {
					achievementList.add(achievement.name);
				}
				Collections.sort(achievementList, null);
				return getListOfStringsMatchingLastWord(args, achievementList);
			}
		}
		return super.getTabCompletions(server, sender, args, pos);
	}

	public int[] achievementNameLocation() {
		return new int[] {};
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		try {
			ConfigManager.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FTBAchievements.proxy.syncWithClients();
	}
}
