package me.modmuss50.ftba.client.gui.poweredCrafting;

import me.modmuss50.ftba.api.IPoweredRecipe;
import me.modmuss50.ftba.blocks.crafting.TilePoweredCraftingTable;
import me.modmuss50.ftba.util.recipes.PoweredCraftingManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.List;

public class SlotPoweredCrafting extends SlotCrafting {

	private final InventoryCrafting craftMatrix;
	private final EntityPlayer player;
	private int amountCrafted;
	ContainerCraftingTable containerCraftingTable;

	public SlotPoweredCrafting(EntityPlayer player,
	                           InventoryCrafting craftingInventory,
	                           IInventory inventoryIn,
	                           int slotIndex,
	                           int xPosition, int yPosition, ContainerCraftingTable containerCraftingTable) {
		super(player, craftingInventory, inventoryIn, slotIndex, xPosition, yPosition);
		this.craftMatrix = craftingInventory;
		this.player = player;
		this.containerCraftingTable = containerCraftingTable;
	}

	@Override
	public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
		net.minecraftforge.common.ForgeHooks.setCraftingPlayer(thePlayer);
		List<IPoweredRecipe> matchingRecipes = PoweredCraftingManager.findAllMatchingRecipe(this.craftMatrix, thePlayer.world);
		IPoweredRecipe matchingRecipe = matchingRecipes.stream().filter(recipe -> recipe.enabled(thePlayer)).findFirst().orElse(null);
		if (matchingRecipe != null && (matchingRecipe.enabled(thePlayer) || thePlayer.world.isRemote)) {
			TilePoweredCraftingTable tile = (TilePoweredCraftingTable) containerCraftingTable.world.getTileEntity(containerCraftingTable.pos);
			if (!tile.canUseEnergy(matchingRecipe.powerUsage()) && !thePlayer.world.isRemote) {
				return stack;
			}
		}
		this.onCrafting(stack);
		net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);

		//if this doesnt work I dont know what will?
		for (int i = 0; i < 9; ++i) {
			this.craftMatrix.decrStackSize(i, 1);
		}

		return stack;
	}

	@Override
	protected void onCrafting(ItemStack stack) {
		if (this.amountCrafted > 0) {
			stack.onCrafting(player.world, player, this.amountCrafted);
			net.minecraftforge.fml.common.FMLCommonHandler.instance().firePlayerCraftingEvent(this.player, stack, craftMatrix);
		}

		this.amountCrafted = 0;
		InventoryCraftResult inventorycraftresult = (InventoryCraftResult) this.inventory;
		IPoweredRecipe irecipe = (IPoweredRecipe) inventorycraftresult.getRecipeUsed();

		if (irecipe != null) {
			TilePoweredCraftingTable tile = (TilePoweredCraftingTable) containerCraftingTable.world.getTileEntity(containerCraftingTable.pos);
			tile.useEnergy(irecipe.powerUsage());
			irecipe.onCraft(this.player);
		}

		if (irecipe != null && !irecipe.isDynamic()) {
			inventorycraftresult.setRecipeUsed(null);
		}
	}

	@Override
	public ItemStack decrStackSize(int amount) {
		if (this.getHasStack()) {
			this.amountCrafted += Math.min(amount, this.getStack().getCount());
		}

		return super.decrStackSize(amount);
	}

	@Override
	protected void onCrafting(ItemStack stack, int amount) {
		this.amountCrafted += amount;
		this.onCrafting(stack);
	}

	@Override
	protected void onSwapCraft(int size) {
		this.amountCrafted += size;
	}
}
