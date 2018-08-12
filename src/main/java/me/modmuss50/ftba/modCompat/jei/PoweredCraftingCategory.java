package me.modmuss50.ftba.modCompat.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.plugins.vanilla.crafting.CraftingRecipeCategory;
import net.minecraft.client.Minecraft;

public class PoweredCraftingCategory extends CraftingRecipeCategory {
	public PoweredCraftingCategory(IGuiHelper guiHelper) {
		super(guiHelper);
	}

	@Override
	public String getUid() {
		return JEICompact.PoweredCrafting;
	}

	@Override
	public String getModName() {
		return "FTBAchievements";
	}

	@Override
	public void drawExtras(Minecraft minecraft) {

	}
}
