package me.modmuss50.ftba.util;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class WorldCommandSender implements ICommandSender {

	World world;

	public WorldCommandSender(World world) {
		this.world = world;
	}

	@Override
	public String getName() {
		return "FTB-Achievements";
	}

	@Override
	public boolean canUseCommand(int permLevel, String commandName) {
		return true;
	}

	@Override
	public World getEntityWorld() {
		return world;
	}

	@Nullable
	@Override
	public MinecraftServer getServer() {
		return world.getMinecraftServer();
	}
}
