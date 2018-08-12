package me.modmuss50.ftba.commands.subcommands.block;

import me.modmuss50.ftba.FTBAchievements;
import me.modmuss50.ftba.commands.FTBCommandBase;
import me.modmuss50.ftba.files.worldData.WorldFormat;
import me.modmuss50.ftba.util.RayTracer;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by modmuss50 on 06/02/2017.
 */
public class CommandSet extends FTBCommandBase {
	@Override
	public String getName() {
		return "set";
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
		RayTraceResult traceResult = RayTracer.retraceHit((EntityPlayer) sender);
		WorldFormat data = FTBAchievements.dataManager.getFormatServer();
		BlockPos pos = traceResult.getBlockPos();
		if (data.unbreakableBlocks == null) {
			data.unbreakableBlocks = new HashMap<>();
		}
		if (!data.unbreakableBlocks.containsKey(((EntityPlayer) sender).world.provider.getDimension())) {
			data.unbreakableBlocks.put(((EntityPlayer) sender).world.provider.getDimension(), new ArrayList<>());
		}
		List<BlockPos> blockPosList = data.unbreakableBlocks.get(((EntityPlayer) sender).world.provider.getDimension());
		if (blockPosList.contains(pos)) {
			sender.sendMessage(new TextComponentString("Block already locked."));
			return;
		}
		data.unbreakableBlocks.replace(((EntityPlayer) sender).world.provider.getDimension(), blockPosList);
		blockPosList.add(pos);
		sender.sendMessage(new TextComponentString("Locked block"));
		super.execute(server, sender, args);
	}
}
