package me.modmuss50.ftba.util.recipes;

import me.modmuss50.ftba.api.IPoweredRecipe;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nullable;

public class PoweredRecipes {
	public static class PoweredShapelessRecipe extends ShapelessRecipes implements IPoweredRecipe {

		public int power;

		public PoweredShapelessRecipe(ResourceLocation resourceLocation, ItemStack output, int power, Object... ingredients) {
			super(resourceLocation.toString(), output, buildInput(ingredients));
			this.power = power;
		}

		@Override
		public int powerUsage() {
			return power;
		}
	}

	public class PoweredShapelessOreRecipe extends ShapelessOreRecipe implements IPoweredRecipe {

		public int power;

		public PoweredShapelessOreRecipe(ResourceLocation resourceLocation, ItemStack output, int power, Object... ingredients) {
			super(resourceLocation, output, ingredients);
			this.power = power;
		}

		@Override
		public int powerUsage() {
			return power;
		}
	}

	public class PoweredShapedOreRecipe extends ShapedOreRecipe implements IPoweredRecipe {

		public int power;

		public PoweredShapedOreRecipe(ResourceLocation resourceLocation, ItemStack output, int power, Object... ingredients) {
			super(resourceLocation, output, ingredients);
			this.power = power;
		}

		@Override
		public int powerUsage() {
			return power;
		}
	}

	public class PoweredShapedRecipe extends ShapedRecipes implements IPoweredRecipe {

		public int power;

		public PoweredShapedRecipe(ResourceLocation resourceLocation, ItemStack output, int power, Object... ingredients) {
			this(resourceLocation, output, power, CraftingHelper.parseShaped(ingredients), ingredients);
			this.power = power;
		}

		private PoweredShapedRecipe(ResourceLocation resourceLocation, ItemStack output, int power, CraftingHelper.ShapedPrimer primer, Object... ingredients) {
			super(resourceLocation.toString(), primer.width, primer.height, primer.input, output);
			this.power = power;
		}

		@Override
		public int powerUsage() {
			return power;
		}
	}

	public static class PoweredParentRecipe implements IPoweredRecipe {

		IRecipe parentRecipe;
		int power;

		public PoweredParentRecipe(IRecipe parentRecipe, int power) {
			this.parentRecipe = parentRecipe;
			this.power = power;
		}

		@Override
		public int powerUsage() {
			return power;
		}

		@Override
		public boolean matches(InventoryCrafting inv, World worldIn) {
			return parentRecipe.matches(inv, worldIn);
		}

		@Override
		public ItemStack getCraftingResult(InventoryCrafting inv) {
			return parentRecipe.getCraftingResult(inv);
		}

		@Override
		public boolean canFit(int width, int height) {
			return parentRecipe.canFit(width, height);
		}

		@Override
		public ItemStack getRecipeOutput() {
			return parentRecipe.getRecipeOutput();
		}

		@Override
		public IRecipe setRegistryName(ResourceLocation name) {
			return parentRecipe.setRegistryName(name);
		}

		@Nullable
		@Override
		public ResourceLocation getRegistryName() {
			return parentRecipe.getRegistryName();
		}

		@Override
		public Class<IRecipe> getRegistryType() {
			return parentRecipe.getRegistryType();
		}

		public IRecipe getParentRecipe() {
			return parentRecipe;
		}
	}

	private static NonNullList<Ingredient> buildInput(Object[] input) {
		NonNullList<Ingredient> list = NonNullList.create();
		for (Object obj : input) {
			if (obj instanceof Ingredient) {
				list.add((Ingredient) obj);
			} else {
				Ingredient ingredient = CraftingHelper.getIngredient(obj);
				if (ingredient == null) {
					ingredient = Ingredient.EMPTY;
				}
				list.add(ingredient);
			}
		}
		return list;
	}
}
