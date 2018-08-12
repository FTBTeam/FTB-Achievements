package me.modmuss50.ftba.modCompat.jei;

import me.modmuss50.ftba.FTBAchievements;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ResGenRecipeCategory implements IRecipeCategory<ResGenRecipeWrapper> {

	//TODO fix this
	public static final ResourceLocation texture = new ResourceLocation("ftbachievements", "textures/gui/jei.png");
	private final IDrawable background;

	public ResGenRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(texture, 0, 172, 116, 30);
	}

	@Override
	public String getUid() {
		return JEICompact.ResGen;
	}

	@Override
	public String getTitle() {
		return "Resource Generator";
	}

	@Override
	public String getModName() {
		return "ftbachievements";
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, ResGenRecipeWrapper resGenRecipeWrapper, IIngredients iIngredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(0, true, 3, 7);
		guiItemStacks.init(1, true, 30, 7);

		guiItemStacks.set(0, resGenRecipeWrapper.recipe.stack.copy());
		guiItemStacks.set(1, new ItemStack(FTBAchievements.blockResGen));
	}
}
