package me.modmuss50.ftba.config;

import net.minecraft.item.ItemStack;

/**
 * Created by modmuss50 on 22/06/2017.
 */
public class DuplicationResource {

	public ItemStack stack;

	public int powerUsage;

	public int processTime;

	public DuplicationResource(ItemStack stack, int powerUsage, int processTime) {
		this.stack = stack;
		this.powerUsage = powerUsage;
		this.processTime = processTime;
	}

}
