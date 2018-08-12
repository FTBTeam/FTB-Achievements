package me.modmuss50.ftba.util.recipes;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RecipeJsonLoader {

	private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	public static void load(File configDir) throws IOException {
		File recipes = new File(configDir, "recipes");
		if (!recipes.exists()) {
			recipes.mkdir();
		}

		if (recipes.listFiles() == null || recipes.listFiles().length == 0) {
			return;
		}

		JsonContext context = new JsonContext("ftbachievements");

		File constants = new File(recipes, "_constants.json");
		if (constants.exists()) {
			JsonObject[] json = GSON.fromJson(FileUtils.readFileToString(constants, Charsets.UTF_8), JsonObject[].class);

			try {
				injectContext(context, json);
			} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
				throw new RuntimeException("Failed to load contants.json", e);
			}
		}

		for (File file : recipes.listFiles()) {
			if (!file.isDirectory() && file.getName().endsWith(".json") && !file.getName().startsWith("_")) {
				String fileContents = FileUtils.readFileToString(file, Charsets.UTF_8);
				JsonObject jsonObject = GSON.fromJson(fileContents, JsonObject.class);
				loadRecipes(jsonObject, context, file.getName());
			}
		}
	}

	public static void loadRecipes(JsonObject jsonObject, JsonContext context, String name) {
		IRecipe recipe = CraftingHelper.getRecipe(jsonObject, context);
		if (!jsonObject.has("power")) {
			throw new RuntimeException(name + " does not have a power value set!");
		}
		if (!jsonObject.has("ftba_group")) {
			throw new RuntimeException(name + " does not have a ftba_group set!");
		}
		if (!jsonObject.has("tier")) {
			throw new RuntimeException(name + " does not have a tier set!");
		}
		PoweredRecipes.PoweredParentRecipe poweredParentRecipe = new PoweredRecipes.PoweredParentRecipe(recipe, jsonObject.get("power").getAsInt());
		if (jsonObject.has("tier")) {
			poweredParentRecipe = new PoweredTierRecipe.PoweredParentTierRecipe(recipe, jsonObject.get("power").getAsInt(), jsonObject.get("ftba_group").getAsString(), jsonObject.get("tier").getAsInt());
		}
		ResourceLocation resourceLocation = new ResourceLocation("ftbachievements", name.replace(".json", ""));
		poweredParentRecipe.setRegistryName(resourceLocation);
		PoweredCraftingManager.register(resourceLocation, poweredParentRecipe);
	}

	private static void injectContext(JsonContext context, JsonObject[] objects) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Method method = context.getClass().getDeclaredMethod("loadConstants", JsonObject[].class);
		method.setAccessible(true);
		method.invoke(context, (Object) objects);
	}

}
