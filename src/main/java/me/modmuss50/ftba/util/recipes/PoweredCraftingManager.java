package me.modmuss50.ftba.util.recipes;

import me.modmuss50.ftba.api.IPoweredRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PoweredCraftingManager {

	private static Map<ResourceLocation, IPoweredRecipe> RECIPES = new HashMap<>();

	public static void testRecipes() {
		ResourceLocation resourceLocation = new ResourceLocation("ftbachievements", "test");
		register(resourceLocation, new PoweredRecipes.PoweredShapelessRecipe(resourceLocation, new ItemStack(Items.DIAMOND), 100, new ItemStack(Blocks.STONE)));

		PoweredTierRecipe.addTierdShapelessRecipe("test", 0, new ItemStack(Items.BOOK), 100, new ItemStack(Items.IRON_INGOT));
		PoweredTierRecipe.addTierdShapelessRecipe("test", 1, new ItemStack(Items.BOOK), 200, new ItemStack(Items.GOLD_INGOT));
		PoweredTierRecipe.addTierdShapelessRecipe("test", 3, new ItemStack(Items.BOOK), 300, new ItemStack(Items.DIAMOND));
	}

	public static void register(ResourceLocation resourceLocation, IPoweredRecipe recipe) {
		if (RECIPES.containsKey(resourceLocation)) {
			throw new RuntimeException("Recipe allready registered!");
		}
		RECIPES.put(resourceLocation, recipe);
	}

	public static List<IPoweredRecipe> getAllRecipes() {
		return RECIPES.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
	}

	public static ItemStack findMatchingResult(InventoryCrafting craftMatrix, World worldIn) {
		for (IPoweredRecipe irecipe : getAllRecipes()) {
			if (irecipe.matches(craftMatrix, worldIn)) {
				return irecipe.getCraftingResult(craftMatrix);
			}
		}

		return ItemStack.EMPTY;
	}

	@Nullable
	public static IPoweredRecipe findMatchingRecipe(InventoryCrafting craftMatrix, World worldIn) {
		for (IPoweredRecipe irecipe : getAllRecipes()) {
			if (irecipe.matches(craftMatrix, worldIn)) {
				return irecipe;
			}
		}
		return null;
	}

	public static List<IPoweredRecipe> findAllMatchingRecipe(InventoryCrafting craftMatrix, World worldIn) {
		return getAllRecipes().stream().filter(recipe -> recipe.matches(craftMatrix, worldIn)).collect(Collectors.toList());
	}

	public static NonNullList<ItemStack> getRemainingItems(InventoryCrafting craftMatrix, World worldIn, EntityPlayer player) {
		for (IPoweredRecipe irecipe : getAllRecipes()) {
			if (irecipe.matches(craftMatrix, worldIn) && irecipe.enabled(player)) {
				return irecipe.getRemainingItems(craftMatrix);
			}
		}
		NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(craftMatrix.getSizeInventory(), ItemStack.EMPTY);
		for (int i = 0; i < nonnulllist.size(); ++i) {
			nonnulllist.set(i, craftMatrix.getStackInSlot(i));
		}
		return nonnulllist;
	}

	@Nullable
	public static IPoweredRecipe getRecipe(ResourceLocation name) {
		return RECIPES.get(name);
	}

	public static ResourceLocation getRecipeName(IPoweredRecipe recipe) {
		final ResourceLocation[] name = { null };
		RECIPES.forEach((resourceLocation, iPoweredRecipe) -> {
			if (recipe == iPoweredRecipe) {
				name[0] = resourceLocation;
			}
		});
		return name[0];
	}

}
