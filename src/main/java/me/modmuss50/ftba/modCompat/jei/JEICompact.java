package me.modmuss50.ftba.modCompat.jei;

import me.modmuss50.ftba.FTBAchievements;
import me.modmuss50.ftba.client.gui.poweredCrafting.ContainerCraftingTable;
import me.modmuss50.ftba.config.ConfigResourceGen;
import me.modmuss50.ftba.config.DuplicationResource;
import me.modmuss50.ftba.util.recipes.PoweredRecipes;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import mezz.jei.plugins.vanilla.crafting.ShapedOreRecipeWrapper;
import mezz.jei.plugins.vanilla.crafting.ShapedRecipesWrapper;
import mezz.jei.plugins.vanilla.crafting.ShapelessRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import reborncore.RebornCore;

import java.util.HashMap;

@mezz.jei.api.JEIPlugin
public class JEICompact implements IModPlugin {

	public static IRecipeRegistry recipeRegistry;
	public static String PoweredCrafting = "ftbachievements.poweredcrafting";
	public static String ResGen = "ftbachievements.resgen";
	public static HashMap<IRecipe, IRecipeWrapper> wrapperMap = new HashMap<>();

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		recipeRegistry = jeiRuntime.getRecipeRegistry();
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		final IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		final IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		registry.addRecipeCategories(new PoweredCraftingCategory(guiHelper));
		registry.addRecipeCategories(new ResGenRecipeCategory(guiHelper));
	}

	@Override
	public void register(IModRegistry registry) {
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();

		registry.addRecipes(PoweredRecipeChecker.getValidRecipes(jeiHelpers), PoweredCrafting);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerCraftingTable.class, PoweredCrafting, 1, 9, 10, 36);

		registry.handleRecipes(ShapedOreRecipe.class, recipe -> saveRecipe(recipe, new ShapedOreRecipeWrapper(jeiHelpers, recipe)), PoweredCrafting);
		registry.handleRecipes(ShapedRecipes.class, recipe -> saveRecipe(recipe, new ShapedRecipesWrapper(jeiHelpers, recipe)), PoweredCrafting);
		registry.handleRecipes(ShapelessOreRecipe.class, recipe -> saveRecipe(recipe, new ShapelessRecipeWrapper<>(jeiHelpers, recipe)), PoweredCrafting);
		registry.handleRecipes(ShapelessRecipes.class, recipe -> saveRecipe(recipe, new ShapelessRecipeWrapper<>(jeiHelpers, recipe)), PoweredCrafting);
		registry.handleRecipes(PoweredRecipes.PoweredParentRecipe.class, poweredParentRecipe -> {
			IRecipe parentRecipe = poweredParentRecipe.getParentRecipe();

			if (parentRecipe instanceof ShapedOreRecipe) {
				return saveRecipe(poweredParentRecipe, new ShapedOreRecipeWrapper(jeiHelpers, (ShapedOreRecipe) parentRecipe));
			}
			if (parentRecipe instanceof ShapelessOreRecipe) {
				return saveRecipe(poweredParentRecipe, new ShapelessRecipeWrapper(jeiHelpers, parentRecipe));
			}
			if (parentRecipe instanceof ShapedRecipes) {
				return saveRecipe(poweredParentRecipe, new ShapedRecipesWrapper(jeiHelpers, (ShapedRecipes) parentRecipe));
			}
			if (parentRecipe instanceof ShapelessRecipes) {
				return saveRecipe(poweredParentRecipe, new ShapelessRecipeWrapper(jeiHelpers, parentRecipe));
			}
			RebornCore.logHelper.error("Failed to find recipe handler for " + parentRecipe + " of child " + poweredParentRecipe);
			return null;
		}, PoweredCrafting);

		registry.addRecipeCatalyst(new ItemStack(FTBAchievements.poweredCraftingTable), PoweredCrafting);

		registry.addRecipeCatalyst(new ItemStack(FTBAchievements.blockResGen), ResGen);
		registry.addRecipes(ConfigResourceGen.duplicationResources, ResGen);
		registry.handleRecipes(DuplicationResource.class, ResGenRecipeWrapper::new, ResGen);
	}

	public static IRecipeWrapper saveRecipe(IRecipe recipe, IRecipeWrapper wrapper) {
		PoweredRecipeWrapper poweredRecipeWrapper = new PoweredRecipeWrapper(wrapper, recipe);
		wrapperMap.put(recipe, poweredRecipeWrapper);
		return poweredRecipeWrapper;
	}

}
