package me.modmuss50.ftba.modCompat.sc;

import me.modmuss50.ftba.ConfigManager;
import me.modmuss50.ftba.commands.FTBCommandBase;
import me.modmuss50.ftba.files.config.FTBAchievement;
import me.modmuss50.ftba.files.config.SCTrigger;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 22/02/2017.
 */
public class CommandStevesCarts extends FTBCommandBase {
	@Override
	public String getName() {
		return "sc";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "sc";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (!Loader.isModLoaded("stevescarts")) {
			sender.sendMessage(new TextComponentString("Steve's Carts must be installed for this to work!"));
		}
		if (args.length != 2) {
			sender.sendMessage(new TextComponentString("Please provide a achievement name"));
			return;
		}
		if (sender instanceof EntityPlayer) {
			FTBAchievement achievement = ConfigManager.getAchivementFromName(args[0]);
			if (achievement == null) {
				sender.sendMessage(new TextComponentString("Could not find achievement"));
				return;
			}

			SCTrigger trigger = new SCTrigger();
			trigger.achievement = achievement.name;
			if (!StevesCartsCompat.modules.contains(args[1])) {
				sender.sendMessage(new TextComponentString("Module not found!"));
				return;
			}
			trigger.moduleName = args[1];
			if (ConfigManager.getConfig().scTriggers == null) {
				ConfigManager.getConfig().scTriggers = new ArrayList<>();
			}
			ConfigManager.getConfig().scTriggers.add(trigger);
			sender.sendMessage(new TextComponentString("Added " + trigger.moduleName + " to " + achievement.name + "'s trigger list"));
		} else {
			sender.sendMessage(new TextComponentString("Must be used by a player"));
		}
	}

	public int[] achievementNameLocation() {
		return new int[] { 0 };
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server,
	                                      ICommandSender sender,
	                                      String[] args,
	                                      @Nullable
		                                      BlockPos pos) {
		int[] nameArgLoc = scModuleLocation();
		for (int argPos : nameArgLoc) {
			if (args.length == argPos + 1) {
				return getListOfStringsMatchingLastWord(args, StevesCartsCompat.modules);
			}
		}
		return super.getTabCompletions(server, sender, args, pos);
	}

	public int[] scModuleLocation() {
		return new int[] { 1 };
	}
}
