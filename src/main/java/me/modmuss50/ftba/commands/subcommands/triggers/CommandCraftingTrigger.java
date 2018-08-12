package me.modmuss50.ftba.commands.subcommands.triggers;

import me.modmuss50.ftba.ConfigManager;
import me.modmuss50.ftba.commands.FTBCommandBase;
import me.modmuss50.ftba.files.config.CraftingTrigger;
import me.modmuss50.ftba.files.config.FTBAchievement;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;

/**
 * Created by modmuss50 on 06/02/2017.
 */
public class CommandCraftingTrigger extends FTBCommandBase {
	@Override
	public String getName() {
		return "crafting";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "crafting";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 0) {
			sender.sendMessage(new TextComponentString("Please provide a achievement name"));
			return;
		}
		if (sender instanceof EntityPlayer) {
			ItemStack heldStack = ((EntityPlayer) sender).getHeldItem(EnumHand.MAIN_HAND);
			if (!heldStack.isEmpty()) {
				FTBAchievement achievement = ConfigManager.getAchivementFromName(args[0]);
				if (achievement == null) {
					sender.sendMessage(new TextComponentString("Could not find achievement"));
					return;
				}
				CraftingTrigger trigger = new CraftingTrigger();
				trigger.achievement = achievement.name;
				trigger.triggerItem = heldStack.copy();
				if (args.length == 2) {
					trigger.triggerItem.setCount(parseInt(args[1]));
					sender.sendMessage(new TextComponentString("Set required stack size to " + trigger.triggerItem.getCount()));
				}
				ConfigManager.getConfig().craftingTriggers.add(trigger);
				sender.sendMessage(new TextComponentString("Added " + heldStack.getDisplayName() + " to " + achievement.name + "'s trigger list"));
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
