package me.modmuss50.ftba.modCompat.jei;

import me.modmuss50.ftba.client.hud.ClientHudRenderer;
import me.modmuss50.ftba.config.DuplicationResource;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import reborncore.common.RebornCoreConfig;

public class ResGenRecipeWrapper implements IRecipeWrapper {

	DuplicationResource recipe;

	public ResGenRecipeWrapper(DuplicationResource recipe) {
		this.recipe = recipe;
	}

	@Override
	public void getIngredients(IIngredients iIngredients) {
		iIngredients.setOutput(ItemStack.class, recipe.stack.copy());
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		String str;
		if (ClientHudRenderer.clientTick < 40) {
			str = recipe.powerUsage + "EU";
		} else {
			str = recipe.powerUsage * RebornCoreConfig.euPerFU +  "RF";
		}
		if (ClientHudRenderer.clientTick >= 80) {
			ClientHudRenderer.clientTick = 0;
		}

		minecraft.fontRenderer.drawString(str, (recipeWidth / 2) + 5, 5, 0x44444);

		str = recipe.processTime + " ticks";
		minecraft.fontRenderer.drawString(str, (recipeWidth / 2) + 5, 15, 0x44444);
	}
}
