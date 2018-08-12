package me.modmuss50.ftba.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;

public interface IPoweredRecipe extends IRecipe {

	int powerUsage();

	default boolean enabled(EntityPlayer player) {
		return true;
	}

	default void onCraft(EntityPlayer player) {

	}

	@Override
	default NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
	}

}
