package me.modmuss50.ftba.modCompat.jei;

import me.modmuss50.ftba.api.IPoweredRecipe;
import me.modmuss50.ftba.client.hud.ClientHudRenderer;
import me.modmuss50.ftba.util.recipes.ITieredRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.crafting.IRecipe;
import org.apache.commons.lang3.Validate;
import reborncore.common.RebornCoreConfig;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PoweredRecipeWrapper implements IRecipeWrapper {

	IRecipeWrapper parentRecipeWrapper;
	IPoweredRecipe recipe;

	public PoweredRecipeWrapper(IRecipeWrapper parentRecipeWrapper, IRecipe recipe) {
		this.parentRecipeWrapper = parentRecipeWrapper;
		Validate.isTrue(recipe instanceof IPoweredRecipe);
		this.recipe = (IPoweredRecipe) recipe;
	}

	@Override
	public void getIngredients(IIngredients iIngredients) {
		parentRecipeWrapper.getIngredients(iIngredients);
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		parentRecipeWrapper.drawInfo(minecraft, recipeWidth, recipeHeight, mouseX, mouseY);
		if (ClientHudRenderer.clientTick < 40) {
			minecraft.fontRenderer.drawString(getRoundedString(recipe.powerUsage(), "EU", true), 57, 43, 0x444444);
		} else {
			minecraft.fontRenderer.drawString(getRoundedString(recipe.powerUsage() * RebornCoreConfig.euPerFU, "RF", true), 57, 43, 0x444444);
		}
		if (ClientHudRenderer.clientTick >= 80) {
			ClientHudRenderer.clientTick = 0;
		}

		if (recipe instanceof ITieredRecipe) {
			minecraft.fontRenderer.drawString("Tier " + (((ITieredRecipe) recipe).getTier() + 1), 57, 5, 0x444444);
		}
	}

	private static final char[] magnitude = new char[] { 'k', 'M', 'G', 'T' };

	//Taken from RC
	public static String getRoundedString(int euValue, String units, boolean doFormat) {
		String ret = "";
		int value = 0;
		int i = 0;
		boolean showMagnitude = true;
		if (euValue < 0) {
			ret = "-";
			euValue = -euValue;
		}

		if (euValue < 1000) {
			doFormat = false;
			value = euValue;
			showMagnitude = false;
		} else if (euValue >= 1000) {
			for (i = 0; ; i++) {
				if (euValue < 10000 && euValue % 1000 >= 100) {
					value = (euValue / 1000) + ((euValue % 1000) / 100);
					break;
				}
				euValue /= 1000;
				if (euValue < 1000) {
					value = euValue;
					break;
				}
			}
		}

		if (doFormat) {
			ret += NumberFormat
				.getIntegerInstance(Locale.forLanguageTag(
					Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode()))
				.format(value);
		} else { ret += value; }

		if (showMagnitude) {
			ret += magnitude[i];
		}

		if (units != "") { ret += " " + units; }

		return ret;
	}

	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		return parentRecipeWrapper.getTooltipStrings(mouseX, mouseY);
	}

	@Override
	public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
		return parentRecipeWrapper.handleClick(minecraft, mouseX, mouseY, mouseButton);
	}
}
