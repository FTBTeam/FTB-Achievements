package me.modmuss50.ftba.util.recipes;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import me.modmuss50.ftba.FTBAchievements;
import me.modmuss50.ftba.api.IPoweredRecipe;
import me.modmuss50.ftba.client.gui.poweredCrafting.ContainerCraftingTable;
import me.modmuss50.ftba.packets.PacketSendJEIRecipeData;
import me.modmuss50.ftba.util.FTBTeamUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.ThreadedFileIOBase;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.apache.commons.io.FileUtils;
import reborncore.RebornCore;
import reborncore.common.util.serialization.SerializationUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PoweredTierRecipe {

	static final List<RecipeGroupTracker> groupTracker = new ArrayList<>();
	static final Map<String, Integer> maxTierMap = new LinkedHashMap<>();

	public static void addTierdShapedRecipe(String id, int teir, ItemStack output, int power, Object... ingredients) {
		ResourceLocation resourceLocation = new ResourceLocation("ftbachievements", id + "_" + teir);
		TiredShapedRecipe recipe = new TiredShapedRecipe(resourceLocation, id, teir, output, power, ingredients);
		PoweredCraftingManager.register(resourceLocation, recipe);
	}

	public static void addTierdShapelessRecipe(String id, int teir, ItemStack output, int power, Object... ingredients) {
		ResourceLocation resourceLocation = new ResourceLocation("ftbachievements", id + "_" + teir);
		TiredShapelessRecipe recipe = new TiredShapelessRecipe(resourceLocation, id, teir, output, power, ingredients);
		PoweredCraftingManager.register(resourceLocation, recipe);
	}

	public static String getTeamID(EntityPlayer player) {
		return FTBAchievements.proxy.getTeamName(player);
	}

	public static RecipeGroupTracker getTrackerForPlayer(EntityPlayer player, String recipe) {
		String teamID = getTeamID(player);
		for (RecipeGroupTracker tracker : groupTracker) {
			if (tracker.teamID.equals(teamID) && tracker.recipeGroup.equals(recipe)) {
				return tracker;
			}
		}
		RecipeGroupTracker tracker = new RecipeGroupTracker();
		tracker.setRecipeGroup(recipe);
		tracker.setTeamID(teamID);
		groupTracker.add(tracker);
		return tracker;
	}

	public static List<RecipeGroupTracker> getTrackersForPlayers(EntityPlayer player) {
		String teamID = getTeamID(player);
		return groupTracker.stream().filter((Predicate<RecipeGroupTracker>) input -> input.teamID.equals(teamID)).collect(Collectors.toList());
	}

	private static void updateMaxTier(String id, int tier) {
		if (maxTierMap.containsKey(id) && maxTierMap.get(id) > tier) {
			return;
		}
		if (maxTierMap.containsKey(id)) {
			maxTierMap.remove(id);
		}
		maxTierMap.put(id, tier);
	}

	@SubscribeEvent
	public static void worldSaveEvent(WorldEvent.Save event) {
		save(event.getWorld().getSaveHandler());
	}

	@SubscribeEvent
	public static void worldLoadEvent(WorldEvent.Load event) throws IOException {
		load(event.getWorld().getSaveHandler());
	}

	public static void save(final ISaveHandler saveHandler) {
		// run in a separate thread to offload work from the server thread
		final SaveFormat saveFormat = new SaveFormat(groupTracker, "0");
		ThreadedFileIOBase.getThreadedIOInstance().queueIO(() -> {
			File file = new File(saveHandler.getWorldDirectory(), "craftingTiers.json");
			try {
				FileUtils.writeStringToFile(file, SerializationUtil.GSON.toJson(saveFormat), Charsets.UTF_8);
			} catch (IOException ioe) {
				RebornCore.logHelper.error("Failed to write the craftingTiers.json file");
				RebornCore.logHelper.error(ioe);
			}
			return false;
		});
	}

	public static void load(ISaveHandler saveHandler) throws IOException {
		if (saveHandler == null || saveHandler.getWorldDirectory() == null) {
			RebornCore.logHelper.error("Failed to read crafting teirs json");
			groupTracker.clear();
			return;
		}
		File file = new File(saveHandler.getWorldDirectory(), "craftingTiers.json");
		if (!file.exists()) {
			groupTracker.clear();
			return;
		}
		String json = FileUtils.readFileToString(file, Charsets.UTF_8);
		SaveFormat saveFormat = SerializationUtil.GSON.fromJson(json, SaveFormat.class);
		if (saveFormat != null && saveFormat.groupTracker != null) {
			groupTracker.clear();
			groupTracker.addAll(saveFormat.groupTracker);
		}
	}

	public static class TiredShapedRecipe extends ShapedOreRecipe implements IPoweredRecipe, ITieredRecipe {

		public int power;
		String id;
		int tier;

		public TiredShapedRecipe(ResourceLocation resourceLocation, String id, int tier, ItemStack output, int power, Object... ingredients) {
			super(resourceLocation, output, ingredients);
			this.power = power;
			this.id = id;
			this.tier = tier;
			updateMaxTier(id, tier);
		}

		@Override
		public int powerUsage() {
			return power;
		}

		@Override
		public boolean enabled(EntityPlayer player) {
			RecipeGroupTracker tracker = getTrackerForPlayer(player, id);
			if (tracker.currentTier == maxTierMap.get(id)) {
				return tracker.currentTier == tier;
			}
			return tracker.currentTier == tier;
		}

		@Override
		public void onCraft(EntityPlayer player) {
			RecipeGroupTracker tracker = getTrackerForPlayer(player, id);
			tracker.increaseTier(player);
		}

		@Override
		public int getTier() {
			return tier;
		}

		@Override
		public String getTierGroup() {
			return id;
		}

	}

	public static class TiredShapelessRecipe extends ShapelessOreRecipe implements IPoweredRecipe, ITieredRecipe {

		public int power;
		String id;
		int tier;

		public TiredShapelessRecipe(ResourceLocation resourceLocation, String id, int tier, ItemStack output, int power, Object... ingredients) {
			super(resourceLocation, output, ingredients);
			this.power = power;
			this.id = id;
			this.tier = tier;
			updateMaxTier(id, tier);
		}

		@Override
		public int powerUsage() {
			return power;
		}

		@Override
		public boolean enabled(EntityPlayer player) {
			RecipeGroupTracker tracker = getTrackerForPlayer(player, id);
			if (tracker.currentTier == maxTierMap.get(id)) {
				return tracker.currentTier == tier;
			}
			return tracker.currentTier == tier;
		}

		@Override
		public void onCraft(EntityPlayer player) {
			RecipeGroupTracker tracker = getTrackerForPlayer(player, id);
			tracker.increaseTier(player);
		}

		@Override
		public int getTier() {
			return tier;
		}

		@Override
		public String getTierGroup() {
			return id;
		}

	}

	public static class PoweredParentTierRecipe extends PoweredRecipes.PoweredParentRecipe implements ITieredRecipe {

		String id;
		int tier;

		public PoweredParentTierRecipe(IRecipe parentRecipe, int power, String id, int tier) {
			super(parentRecipe, power);
			Preconditions.checkArgument(!id.isEmpty());
			this.id = id;
			this.tier = tier;
			updateMaxTier(id, tier);
		}

		@Override
		public boolean enabled(EntityPlayer player) {
			RecipeGroupTracker tracker = getTrackerForPlayer(player, id);
			if (tracker.currentTier == maxTierMap.get(id)) {
				return tracker.currentTier == tier;
			}
			return tracker.currentTier == tier;
		}

		@Override
		public void onCraft(EntityPlayer player) {
			RecipeGroupTracker tracker = getTrackerForPlayer(player, id);
			tracker.increaseTier(player);
		}

		@Override
		public int getTier() {
			return tier;
		}

		@Override
		public String getTierGroup() {
			return id;
		}

	}

	public static class RecipeGroupTracker {

		private String recipeGroup;
		private String teamID;
		public int currentTier;

		public void increaseTier(EntityPlayer player) {
			if (maxTierMap.get(recipeGroup) == currentTier) {
				if(player instanceof EntityPlayerMP){
					PacketSendJEIRecipeData.sendJEIPacket((EntityPlayerMP) player);
				}
				return;
			}
			currentTier++;
			FTBTeamUtil.handleOnlineMemebers(player, player1 -> {
				player1.sendMessage(new TextComponentString(TextFormatting.YELLOW + "The recipe difficultly has been increased"));
				PacketSendJEIRecipeData.sendJEIPacket((EntityPlayerMP) player1);
				if (player1.openContainer != null) {
					if (player1.openContainer instanceof ContainerCraftingTable) {
						//Updates the current container in case another player has the grid open
						player1.openContainer.onCraftMatrixChanged(null);
					}
				}
			});
		}

		public void resetTeir(EntityPlayer player) {
			currentTier = 0;
			FTBTeamUtil.handleOnlineMemebers(player, player1 -> {
				PacketSendJEIRecipeData.sendJEIPacket((EntityPlayerMP) player1);
				if (player1.openContainer != null) {
					if (player1.openContainer instanceof ContainerCraftingTable) {
						//Updates the current container in case another player has the grid open
						player1.openContainer.onCraftMatrixChanged(null);
					}
				}
			});
		}

		public String getRecipeGroup() {
			return recipeGroup;
		}

		public void setRecipeGroup(String recipeGroup) {
			Preconditions.checkArgument(!recipeGroup.isEmpty());
			this.recipeGroup = recipeGroup;
		}

		public String getTeamID() {
			return teamID;
		}

		public void setTeamID(String teamID) {
			this.teamID = teamID;
		}

		public int getCurrentTier() {
			return currentTier;
		}
	}

	public static class SaveFormat {
		List<RecipeGroupTracker> groupTracker;
		String saveVersion;

		public SaveFormat(List<RecipeGroupTracker> groupTracker, String saveVersion) {
			this.groupTracker = groupTracker;
			this.saveVersion = saveVersion;
		}

		public SaveFormat() {
		}

		public List<RecipeGroupTracker> getGroupTracker() {
			return groupTracker;
		}

		public void setGroupTracker(List<RecipeGroupTracker> groupTracker) {
			this.groupTracker = groupTracker;
		}

		public String getSaveVersion() {
			return saveVersion;
		}

		public void setSaveVersion(String saveVersion) {
			this.saveVersion = saveVersion;
		}
	}

}
