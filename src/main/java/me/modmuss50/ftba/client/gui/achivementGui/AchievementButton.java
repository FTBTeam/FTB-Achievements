package me.modmuss50.ftba.client.gui.achivementGui;

import me.modmuss50.ftba.client.ClientDataManager;
import me.modmuss50.ftba.files.config.BaseTrigger;
import me.modmuss50.ftba.files.config.ConfigFormat;
import me.modmuss50.ftba.files.config.FTBAchievement;
import me.modmuss50.ftba.files.config.IRequirementProvider;
import me.modmuss50.ftba.files.worldData.WorldFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by modmuss50 on 09/02/2017.
 */
public class AchievementButton extends GuiButton {
	FTBAchievement achievement;

	public AchievementButton(int buttonId, int x, int y, FTBAchievement achievement) {
		super(buttonId, x, y, 20, 20, "");
		this.achievement = achievement;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float ticks) {
		super.drawButton(mc, mouseX, mouseY, ticks);
		RenderHelper.enableStandardItemLighting();
		RenderHelper.enableGUIStandardItemLighting();
		RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
		ItemStack stack = achievement.iconStack;
		if (stack == null || stack.isEmpty() || stack.getItem() == null) {
			stack = new ItemStack(Items.BOOK);
		}
		this.zLevel = 100.0F;
		itemRenderer.zLevel = 100.0F;
		itemRenderer.renderItemIntoGUI(stack, this.x + 2, this.y + 2);
		//itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, stack, this.xPosition + 2, this.yPosition + 2, "Test");
		itemRenderer.zLevel = 0.0F;
		this.zLevel = 0.0F;
	}

	public void postDrawButton(Minecraft mc, int mouseX, int mouseY) {
		this.zLevel += 1000;
		if (mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height) {
			drawInfo(mc, mouseX, mouseY);
		}
		this.zLevel -= 1000;
	}

	public void drawInfo(Minecraft mc, int mouseX, int mouseY) {
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		GlStateManager.disableBlend();

		List<String> lines = new ArrayList<>();

		lines.add(TextFormatting.AQUA + getBookTitle());

		if (ClientDataManager.getWorldFormat().triggedAchivements != null && ClientDataManager.getWorldFormat().triggedAchivements.contains(achievement.name)) {
			lines.add(TextFormatting.GREEN + "Completed!");
		}

		String data = getPageData(0);
		if (!data.isEmpty()) {
			lines.add("");
			for (String str : wordwrap(getPageData(0).replace("{\"text\":\"", "").replace("\"}", ""), 45).split("\n")) {
				lines.add(str);
			}
			lines.add("");
		}

		if (achievement.rewards != null && !achievement.rewards.isEmpty()) {
			lines.add(TextFormatting.GOLD + "Rewards:");
			for (ItemStack stack : achievement.rewards) {
				if (stack == null || stack.getItem() == null) {
					continue;
				}
				lines.add(stack.getDisplayName());
			}
		}

		ConfigFormat format = ClientDataManager.getConfigFormat();
		getRequiements(lines, format.craftingTriggers);
		getRequiements(lines, format.inputTriggers);
		getRequiements(lines, format.sieveTriggers);
		getRequiements(lines, format.guiTriggers);
		getRequiements(lines, format.bmTriggers);
		getRequiements(lines, format.deTriggers);
		getRequiements(lines, format.vmTriggers);
		getRequiements(lines, format.crusherTriggers);

		int width = 0;
		for (String str : lines) {
			width = (int) Math.max(width, str.length() * 5.3D);
		}
		int height = lines.size() * 10 + 10;

		int yPos = mouseY - 2;
		int xPos = mouseX + 10;

		Gui.drawRect(xPos - 2, yPos, xPos + width, yPos + height, Color.DARK_GRAY.getRGB());

		int linePos = -5;
		for (String str : lines) {
			mc.fontRenderer.drawString(str, xPos, yPos + (linePos += 10), Color.white.getRGB());
		}

		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		GlStateManager.enableBlend();

	}

	private void getRequiements(List<String> lines, List list) {
		if (list == null || lines.isEmpty()) {
			return;
		}
		for (Object object : list) {
			if (object instanceof IRequirementProvider) {
				if (object instanceof BaseTrigger) {
					if (((BaseTrigger) object).achievement.equals(achievement.name)) {
						((IRequirementProvider) object).getRequirements(lines, achievement);
					}
				} else {
					((IRequirementProvider) object).getRequirements(lines, achievement);
				}
			}
		}
	}

	public static String wordwrap(final String input, final int length) {
		if (input == null || length < 1) {
			throw new IllegalArgumentException("Invalid input args");
		}

		final String text = input.trim();

		if (text.length() > length && text.contains(" ")) {
			final String line = text.substring(0, length);
			final int lineBreakIndex = line.indexOf("\n");
			final int lineLastSpaceIndex = line.lastIndexOf(" ");
			final int inputFirstSpaceIndex = text.indexOf(" ");

			final int breakIndex = lineBreakIndex > -1 ? lineBreakIndex :
			                       (lineLastSpaceIndex > -1 ? lineLastSpaceIndex : inputFirstSpaceIndex);

			return text.substring(0, breakIndex) + "\n" + wordwrap(text.substring(breakIndex + 1), length);
		} else {
			return text;
		}
	}

	@Override
	protected int getHoverState(boolean mouseOver) {
		WorldFormat format = ClientDataManager.getWorldFormat();
		if (format.triggedAchivements != null && format.triggedAchivements.contains(achievement.name)) {
			if (mouseOver) {
				return 1;
			}
			return 0;
		}
		return super.getHoverState(mouseOver);
	}

	public static boolean isValidBook(ItemStack stack) {
		if (stack == null || stack.getItem() != Items.WRITTEN_BOOK) {
			return false;
		}
		if (!stack.hasTagCompound()) {
			return false;
		}
		return ItemWrittenBook.validBookTagContents(stack.getTagCompound());
	}

	public String getBookTitle() {
		if (isValidBook(achievement.bookStack)) {
			NBTTagCompound nbttagcompound = achievement.bookStack.getTagCompound();
			String s = nbttagcompound.getString("title");

			if (!StringUtils.isNullOrEmpty(s)) {
				return s;
			}
		}
		return achievement.name;
	}

	public NBTTagList getBookPages(ItemStack stack) {
		if (isValidBook(stack)) {
			NBTTagCompound nbttagcompound = stack.getTagCompound();
			return nbttagcompound.getTagList("pages", 8);
		}
		return null;
	}

	private String getPageData(int page) {
		NBTTagList bookPages = getBookPages(achievement.bookStack);
		return bookPages != null && page >= 0 && page < bookPages.tagCount() ? bookPages.getStringTagAt(page) : "";
	}
}
