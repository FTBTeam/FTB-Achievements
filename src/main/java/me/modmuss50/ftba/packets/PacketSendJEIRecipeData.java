package me.modmuss50.ftba.packets;

import me.modmuss50.ftba.FTBAchievements;
import me.modmuss50.ftba.api.IPoweredRecipe;
import me.modmuss50.ftba.modCompat.jei.JEICompact;
import me.modmuss50.ftba.util.recipes.PoweredCraftingManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import reborncore.common.network.ExtendedPacketBuffer;
import reborncore.common.network.INetworkPacket;
import reborncore.common.network.NetworkManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PacketSendJEIRecipeData implements INetworkPacket<PacketSendJEIRecipeData> {

	List<ResourceLocation> hiddenRecipes;
	List<ResourceLocation> visibleRecipes;

	public PacketSendJEIRecipeData(List<ResourceLocation> hiddenRecipes, List<ResourceLocation> visibleRecipes) {
		this.hiddenRecipes = hiddenRecipes;
		this.visibleRecipes = visibleRecipes;
	}

	public PacketSendJEIRecipeData() {
	}

	@Override
	public void writeData(ExtendedPacketBuffer extendedPacketBuffer) throws IOException {
		NBTTagCompound tagCompound = new NBTTagCompound();
		listToNBT(hiddenRecipes, tagCompound);
		extendedPacketBuffer.writeCompoundTag(tagCompound);

		tagCompound = new NBTTagCompound();
		listToNBT(visibleRecipes, tagCompound);
		extendedPacketBuffer.writeCompoundTag(tagCompound);
	}

	public void listToNBT(List<ResourceLocation> list, NBTTagCompound compound) {
		compound.setInteger("size", list.size());
		NBTTagCompound nbtList = new NBTTagCompound();
		for (int i = 0; i < list.size(); i++) {
			nbtList.setString(i + "", list.get(i).toString());
		}
		compound.setTag("data", nbtList);
	}

	public List<ResourceLocation> listFromNBT(NBTTagCompound compound) {
		int size = compound.getInteger("size");
		NBTTagCompound data = compound.getCompoundTag("data");
		List<ResourceLocation> list = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			list.add(new ResourceLocation(data.getString(i + "")));
		}
		return list;
	}

	@Override
	public void readData(ExtendedPacketBuffer extendedPacketBuffer) throws IOException {
		NBTTagCompound hiddenTag = extendedPacketBuffer.readCompoundTag();
		NBTTagCompound visibleTag = extendedPacketBuffer.readCompoundTag();

		hiddenRecipes = listFromNBT(hiddenTag);
		visibleRecipes = listFromNBT(visibleTag);
	}

	@Override
	public void processData(PacketSendJEIRecipeData packetSendJEIRecipeData, MessageContext messageContext) {
		FTBAchievements.proxy.clientSideTask(() -> {
			for (ResourceLocation resourceLocation : hiddenRecipes) {
				IRecipe recipe = PoweredCraftingManager.getRecipe(resourceLocation);
				JEICompact.recipeRegistry.hideRecipe(JEICompact.wrapperMap.get(recipe));
			}

			for (ResourceLocation resourceLocation : visibleRecipes) {
				IRecipe recipe = PoweredCraftingManager.getRecipe(resourceLocation);
				JEICompact.recipeRegistry.unhideRecipe(JEICompact.wrapperMap.get(recipe));
			}
		});
	}

	public static PacketSendJEIRecipeData buildPacket(EntityPlayer player) {
		List<ResourceLocation> enabledRecipes = new ArrayList<>();
		List<ResourceLocation> disabledRecipes = new ArrayList<>();
		for (IPoweredRecipe recipe : PoweredCraftingManager.getAllRecipes()) {
			if (recipe.enabled(player)) {
				enabledRecipes.add(PoweredCraftingManager.getRecipeName(recipe));
			} else {
				disabledRecipes.add(PoweredCraftingManager.getRecipeName(recipe));
			}
		}
		return new PacketSendJEIRecipeData(disabledRecipes, enabledRecipes);
	}

	public static void sendJEIPacket(EntityPlayerMP player) {
		PacketSendJEIRecipeData packet = buildPacket(player);
		NetworkManager.sendToPlayer(packet, player);
	}
}
