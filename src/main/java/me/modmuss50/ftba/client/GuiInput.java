package me.modmuss50.ftba.client;

import me.modmuss50.ftba.blocks.input.TileInput;
import net.minecraft.client.gui.inventory.GuiContainer;
import reborncore.client.guibuilder.GuiBuilder;

/**
 * Created by Mark on 05/02/2017.
 */
public class GuiInput extends GuiContainer {

	GuiBuilder builder = new GuiBuilder(GuiBuilder.defaultTextureSheet);

	TileInput input;

	ContainerInput containerInput;

	public GuiInput(TileInput input) {
		super(new ContainerInput(input));
		this.input = input;
		this.containerInput = (ContainerInput) this.inventorySlots;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		builder.drawDefaultBackground(this, guiLeft, guiTop, xSize, ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		//if(input.trigger.type.equals("rf")){
		this.fontRenderer.drawString(containerInput.power + "RF", 8, 6, 4210752);
		this.fontRenderer.drawString(containerInput.requirement + " needed RF", 8, 16, 4210752);
		//}
	}
}
