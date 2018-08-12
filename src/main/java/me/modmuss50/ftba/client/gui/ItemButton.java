package me.modmuss50.ftba.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import reborncore.client.gui.GuiUtil;

/**
 * Created by modmuss50 on 09/02/2017.
 */
public class ItemButton extends GuiButton {
	ItemStack stack;
	String hoverText;
	boolean showHoverText;
	boolean rightSide = false;

	public ItemButton(int buttonId, int x, int y, ItemStack stack) {
		super(buttonId, x, y, 20, 20, "");
		this.hoverText = "Open Achievement's";
		this.showHoverText = true;
		this.stack = stack;
	}

	public ItemButton(int buttonId, int x, int y, ItemStack stack, String text, boolean showHoverText) {
		super(buttonId, x, y, 20, 20, "");
		this.hoverText = text;
		this.showHoverText = showHoverText;
		this.stack = stack;
	}

	public ItemButton(int buttonId, int x, int y, ItemStack stack, String text, boolean showHoverText, boolean rightSide) {
		super(buttonId, x, y, 20, 20, "");
		this.hoverText = text;
		this.showHoverText = showHoverText;
		this.stack = stack;
		this.rightSide = rightSide;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float ticks) {
		super.drawButton(mc, mouseX, mouseY, ticks);
		RenderHelper.enableStandardItemLighting();
		RenderHelper.enableGUIStandardItemLighting();
		RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
		itemRenderer.renderItemIntoGUI(this.stack, this.x + 2, this.y + 2);
		if (this.hovered && showHoverText) {
			if (!rightSide) {
				GuiUtil.drawTooltipBox(this.x - (hoverText.length() * 6) - 5, this.y + 5, (hoverText.length() * 6), 10);
				drawString(mc.fontRenderer, hoverText, this.x - (hoverText.length() * 6), this.y + 5, 0xFFFFFF);
			} else {
				GuiUtil.drawTooltipBox(this.x + 20, this.y, (hoverText.length() * 6), 10);
				drawString(mc.fontRenderer, hoverText, this.x + 21, this.y, 0xFFFFFF);
			}

		}
	}
}
