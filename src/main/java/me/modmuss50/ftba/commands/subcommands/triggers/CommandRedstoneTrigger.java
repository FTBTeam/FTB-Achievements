package me.modmuss50.ftba.commands.subcommands.triggers;

import me.modmuss50.ftba.ConfigManager;
import me.modmuss50.ftba.FTBAchievements;
import me.modmuss50.ftba.commands.FTBCommandBase;
import me.modmuss50.ftba.files.config.BlockTrigger;
import me.modmuss50.ftba.files.config.FTBAchievement;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

/**
 * Created by modmuss50 on 06/02/2017.
 */
public class CommandRedstoneTrigger extends FTBCommandBase {
	@Override
	public String getName() {
		return "redstone";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "redstone";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 0) {
			sender.sendMessage(new TextComponentString("Please provide a achievement name"));
			return;
		}
		if (!(sender instanceof EntityPlayer)) {
			sender.sendMessage(new TextComponentString("You must be a player!"));
			return;
		}
		FTBAchievement achievement = ConfigManager.getAchivementFromName(args[0]);
		if (achievement == null) {
			sender.sendMessage(new TextComponentString("Could not find achievement"));
		}
		BlockTrigger blockTrigger = new BlockTrigger();
		blockTrigger.achievement = achievement.name;
		blockTrigger.type = "redstone";
		ConfigManager.getConfig().inputTriggers.add(blockTrigger);
		EntityPlayer player = (EntityPlayer) sender;
		ItemStack stack = new ItemStack(FTBAchievements.blockInput, 1, ConfigManager.getMetaFromTrigger(blockTrigger));
		player.inventory.addItemStackToInventory(stack);
		sender.sendMessage(new TextComponentString("Created a redstone trigger for " + achievement.name + ". Restart game for textures to take affect"));
		super.execute(server, sender, args);
	}

	public int[] achievementNameLocation() {
		return new int[] { 0 };
	}
}
