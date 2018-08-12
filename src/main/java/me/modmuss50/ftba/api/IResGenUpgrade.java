package me.modmuss50.ftba.api;

import net.minecraft.item.ItemStack;

/**
 * Created by mark on 16/07/2017.
 */
public interface IResGenUpgrade {

	boolean processUpgrade(ItemStack stack, IResGenHandler upgradeHandler);

}
