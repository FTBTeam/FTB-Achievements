package me.modmuss50.ftba.client.gui.achievementView;

import me.modmuss50.ftba.client.ClientDataManager;
import me.modmuss50.ftba.files.config.BaseTrigger;
import me.modmuss50.ftba.files.config.ConfigFormat;
import me.modmuss50.ftba.files.config.FTBAchievement;
import me.modmuss50.ftba.files.config.IRequirementProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import reborncore.client.guibuilder.GuiBuilder;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 22/04/2017.
 */
public class GuiAchievementDetail extends GuiScreen {

	GuiBuilder builder = new GuiBuilder(GuiBuilder.defaultTextureSheet);

	FTBAchievement achievement;

	int xSize;
	int ySize;
	int guiLeft;
	int guiTop;

	public GuiAchievementDetail(FTBAchievement achievement) {
		this.achievement = achievement;
	}

	@Override
	public void initGui() {
		super.initGui();

		xSize = 200;
		ySize = 200;

		guiLeft = (this.width - this.xSize) / 2;
		guiTop = (this.height - this.ySize) / 2;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		builder.drawDefaultBackground(this, guiLeft, guiTop, xSize, ySize);
		super.drawScreen(mouseX, mouseY, partialTicks);
		drawCenteredString(mc.fontRenderer, achievement.getTitle(), guiLeft + (xSize / 2), guiTop + 6, 4210752);

		List<String> lines = new ArrayList<>();

		lines.add(TextFormatting.AQUA + achievement.getTitle());

		if (ClientDataManager.getWorldFormat().triggedAchivements != null && ClientDataManager.getWorldFormat().triggedAchivements.contains(achievement.name)) {
			lines.add(TextFormatting.GREEN + "Completed!");
		}

		String data = achievement.getPageData(0);
		if (!data.isEmpty()) {
			lines.add("");
			for (String str : wordwrap(achievement.getPageData(0).replace("{\"text\":\"", "").replace("\"}", ""), 38).split("\n")) {
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
		getRequiements(lines, format.scTriggers);
		getRequiements(lines, format.bmTriggers);
		getRequiements(lines, format.deTriggers);
		getRequiements(lines, format.vmTriggers);
		getRequiements(lines, format.crusherTriggers);

		int width = 0;
		for (String str : lines) {
			width = (int) Math.max(width, str.length() * 5.3D);
		}

		int linePos = -5;
		for (String str : lines) {
			mc.fontRenderer.drawString(str, guiLeft + 10, guiTop + 40 + (linePos += 10), Color.white.getRGB());
		}

		RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
		ItemStack stack = achievement.iconStack;
		if (stack == null || stack.getItem() == null) {
			stack = new ItemStack(Items.BOOK);
		}
		this.zLevel = 100.0F;
		itemRenderer.zLevel = 100.0F;
		GlStateManager.scale(2F, 2F, 2F);
		itemRenderer.renderItemIntoGUI(stack, guiLeft - 50, guiTop - 7);
		//		itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, stack, this.xPosition + 2, this.yPosition + 2, "Test");
		itemRenderer.zLevel = 0.0F;
		this.zLevel = 0.0F;
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
}
