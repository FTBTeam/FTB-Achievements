package me.modmuss50.ftba.commands.subcommands;

import me.modmuss50.ftba.ConfigManager;
import me.modmuss50.ftba.commands.FTBCommandBase;
import me.modmuss50.ftba.files.config.FTBAchievement;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;

/**
 * Created by modmuss50 on 06/02/2017.
 */
public class CommandAddReward extends FTBCommandBase {
	@Override
	public String getName() {
		return "addReward";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "addReward";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 0) {
			sender.sendMessage(new TextComponentString("Please provide a name"));
			return;
		}
		if (sender instanceof EntityPlayer) {
			ItemStack heldStack = ((EntityPlayer) sender).getHeldItem(EnumHand.MAIN_HAND);
			if (heldStack != null) {
				FTBAchievement achievement = ConfigManager.getAchivementFromName(args[0]);
				if (achievement == null) {
					sender.sendMessage(new TextComponentString("FTBAchievement not found"));
					return;
				}
				if (achievement.rewards == null) {
					achievement.rewards = new ArrayList<>();
				}
				if (args.length == 2 && args[1].equalsIgnoreCase("true")) {
					achievement.giveToAll = true;
					sender.sendMessage(new TextComponentString("Stack will be given to all players"));
				}
				achievement.rewards.add(heldStack.copy());
				sender.sendMessage(new TextComponentString("Added " + heldStack.getDisplayName() + " to " + achievement.name + "'s rewards list"));
			} else {
				sender.sendMessage(new TextComponentString("Must be holding an item stack in you hand"));
			}
		} else {
			sender.sendMessage(new TextComponentString("Must be used by a player"));
		}
		super.execute(server, sender, args);
	}

	public int[] achievementNameLocation() {
		return new int[] { 0 };
	}
}
