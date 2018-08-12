package me.modmuss50.ftba.commands.subcommands;

import me.modmuss50.ftba.FTBAchievements;
import me.modmuss50.ftba.commands.FTBCommandBase;
import me.modmuss50.ftba.files.worldData.WorldFormat;
import me.modmuss50.ftba.util.RayTracer;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;

/**
 * Created by modmuss50 on 06/02/2017.
 */
public class CommandSetStart extends FTBCommandBase {
	@Override
	public String getName() {
		return "setStart";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "setStart";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (!(sender instanceof EntityPlayer)) {
			sender.sendMessage(new TextComponentString("Sender must be a player"));
			return;
		}
		Vec3d traceResult = RayTracer.retrace((EntityPlayer) sender).hitVec;
		WorldFormat data = FTBAchievements.dataManager.getFormatServer();
		BlockPos pos = new BlockPos(traceResult);
		data.startX = pos.getX();
		data.startY = pos.getY();
		data.startZ = pos.getZ();
		data.startDimID = ((EntityPlayer) sender).world.provider.getDimension();
		sender.sendMessage(new TextComponentString("Set start block"));
		super.execute(server, sender, args);
	}
}
