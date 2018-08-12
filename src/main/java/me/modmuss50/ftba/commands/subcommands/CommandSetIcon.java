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

/**
 * Created by Mark on 11/02/2017.
 */
public class CommandSetIcon extends FTBCommandBase {
	@Override
	public String getName() {
		return "setIcon";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "setIcon";
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
				ItemStack copiedStack = heldStack.copy();
				copiedStack.setCount(1);
				achievement.iconStack = copiedStack;
				sender.sendMessage(new TextComponentString("Set icon to " + heldStack.getDisplayName() + " for " + achievement.name));
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
