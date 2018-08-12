package me.modmuss50.ftba.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by modmuss50 on 22/03/2017.
 */
public class FarmLandFix {

	public static void fix() throws IllegalAccessException, NoSuchFieldException {
		Field field = ReflectionHelper.findField(BlockFarmland.class, "FARMLAND_AABB", "field_185665_b", "b");
		field.setAccessible(true);
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		field.set(null, Block.FULL_BLOCK_AABB);
	}
}
