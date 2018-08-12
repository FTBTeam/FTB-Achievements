package me.modmuss50.ftba.commands.subcommands;

import me.modmuss50.ftba.FTBAchievements;
import me.modmuss50.ftba.commands.FTBCommandBase;
import me.modmuss50.ftba.files.worldData.WorldFormat;
import me.modmuss50.ftba.util.RayTracer;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;

/**
 * Created by modmuss50 on 06/02/2017.
 */
public class CommandSetChest extends FTBCommandBase {
	@Override
	public String getName() {
		return "setChest";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "setChest";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (!(sender instanceof EntityPlayer)) {
			sender.sendMessage(new TextComponentString("Sender must be a player"));
			return;
		}
		Vec3d traceResult = RayTracer.retrace((EntityPlayer) sender).hitVec;
		TileEntity tileEntity = ((EntityPlayer) sender).world.getTileEntity(new BlockPos(traceResult));
		if (tileEntity instanceof IInventory) {
			WorldFormat data = FTBAchievements.dataManager.getFormatServer();
			BlockPos pos = new BlockPos(traceResult);
			data.chestX = pos.getX();
			data.chestY = pos.getY();
			data.chestZ = pos.getZ();
			data.chestDimID = ((EntityPlayer) sender).world.provider.getDimension();
			sender.sendMessage(new TextComponentString("Set block to reward chest"));
		} else {
			sender.sendMessage(new TextComponentString("Block must be an inventory"));
		}
		super.execute(server, sender, args);
	}
}
