package me.modmuss50.ftba.client.gui.poweredCrafting;

import me.modmuss50.ftba.client.gui.GuiBase;
import me.modmuss50.ftba.client.gui.GuiButtonPowerBar;
import me.modmuss50.ftba.client.gui.TRBuilder;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiCraftingTable extends GuiCrafting {

	public TRBuilder builder = new TRBuilder();
	ContainerCraftingTable containerCraftingTable;

	public GuiCraftingTable(EntityPlayer player, World worldIn, BlockPos blockPosition) {
		super(player.inventory, worldIn, blockPosition);
		inventorySlots = new ContainerCraftingTable(player, worldIn, blockPosition);
		containerCraftingTable = (ContainerCraftingTable) inventorySlots;
	}

	@Override
	public void initGui() {
		super.initGui();
		GuiButton recipeButton = null;
		for (GuiButton button : buttonList) {
			if (button instanceof GuiButtonImage) {
				recipeButton = button;
				break;
			}
		}
		if (recipeButton != null) {
			buttonList.remove(recipeButton);
		}

	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.FOREGROUND;
		GlStateManager.color(255, 255, 255, 255);
		this.builder.drawMultiEnergyBar(this, 9, 18, (int) this.containerCraftingTable.power, (int) this.containerCraftingTable.maxPower, mouseX, mouseY, 0, layer);
	}

	public void addPowerButton(int x, int y, int id, GuiBase.Layer layer) {
		if (id == 0)
			buttonList.clear();
		int factorX = 0;
		int factorY = 0;
		if (layer == GuiBase.Layer.BACKGROUND) {
			factorX = guiLeft;
			factorY = guiTop;
		}
		buttonList.add(new GuiButtonPowerBar(id, x + factorX, y + factorY, this, layer));
	}
}
