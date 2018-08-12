package me.modmuss50.ftba.util;

import net.minecraft.world.World;

/**
 * Created by Mark on 04/02/2017.
 */
public class AchievementUser {

	public String name;
	public World world;

	public AchievementUser(String username, World world) {
		this.name = username;
		this.world = world;

	}

}
