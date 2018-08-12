package me.modmuss50.ftba.client.gui.achivementGui;

import me.modmuss50.ftba.client.ClientDataManager;
import me.modmuss50.ftba.client.RunComparison;
import me.modmuss50.ftba.client.gui.ItemButton;
import me.modmuss50.ftba.client.gui.achievementView.GuiAchievementDetail;
import me.modmuss50.ftba.files.config.FTBAchievement;
import me.modmuss50.ftba.web.WebServer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import reborncore.client.guibuilder.GuiBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by modmuss50 on 08/03/2017.
 */
public class GuiAchievements extends GuiScreen {
	GuiBuilder builder = new GuiBuilder(GuiBuilder.defaultTextureSheet);

	int xSize;
	int ySize;
	int guiLeft;
	int guiTop;

	int coloum = 0;
	int row = 0;

	int WORLD_ID;
	int USER_ID;
	int RESET_ID;
	int EXTERNAL_ID;

	Map<Integer, FTBAchievement> achievementMap;

	@Override
	public void initGui() {
		super.initGui();

		if (ClientDataManager.getConfigFormat() == null || ClientDataManager.getConfigFormat().achievements == null || ClientDataManager.getConfigFormat().achievements.isEmpty()) {
			xSize = 100;
			ySize = 100;

			guiLeft = (this.width - this.xSize) / 2;
			guiTop = (this.height - this.ySize) / 2;
			return;
		}

		int coloums = (int) Math.round(Math.sqrt(ClientDataManager.getConfigFormat().achievements.size()));
		if (coloums == 0) {
			coloums++;
		}
		xSize = Math.max((coloums * 25) + 25, 100);
		ySize = Math.max((ClientDataManager.getConfigFormat().achievements.size() / coloums * 25), 100);

		guiLeft = (this.width - this.xSize) / 2;
		guiTop = (this.height - this.ySize) / 2;
		this.buttonList.clear();

		achievementMap = new HashMap<>();

		int buttonID = 0;
		for (FTBAchievement achievement : ClientDataManager.getConfigFormat().achievements) {
			buttonID++;
			achievementMap.put(buttonID, achievement);
			this.buttonList.add(new AchievementButton(buttonID, guiLeft + (coloum * 22) + 10, (row * 22) + guiTop + 25, achievement));
			if (coloum >= coloums) {
				row++;
				coloum = 0;
			} else {
				coloum++;
			}
		}
		int baseId = buttonID + 1;
		WORLD_ID = baseId + 1;
		USER_ID = baseId + 2;
		RESET_ID = baseId + 3;
		EXTERNAL_ID = baseId + 4;
		this.buttonList.add(new ItemButton(WORLD_ID, guiLeft + xSize + 2, guiTop + 3, new ItemStack(Items.CLOCK), "Compare with worlds fastest", true, true));
		this.buttonList.add(new ItemButton(USER_ID, guiLeft + xSize + 2, guiTop + 25, new ItemStack(Items.COMPASS), "Compare with users fastest", true, true));
		this.buttonList.add(new ItemButton(RESET_ID, guiLeft + xSize + 2, guiTop + 47, new ItemStack(Blocks.BARRIER), "Stop comparing", true, true));
		this.buttonList.add(new ItemButton(EXTERNAL_ID, guiLeft + xSize + 2, guiTop + 69, new ItemStack(Items.BOW), "Open external window", true, true));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		builder.drawDefaultBackground(this, guiLeft, guiTop, xSize, ySize);
		super.drawScreen(mouseX, mouseY, partialTicks);

		drawCenteredString(mc.fontRenderer, TextFormatting.BLUE + "F" + TextFormatting.GREEN + "T" + TextFormatting.RED + "B" + TextFormatting.WHITE + " Achievements", guiLeft + (xSize / 2), guiTop + 6, 4210752);
		for (GuiButton button : buttonList) {
			if (button instanceof AchievementButton) {
				((AchievementButton) button).postDrawButton(mc, mouseX, mouseY);
			}
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		if (achievementMap.containsKey(button.id)) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiAchievementDetail(achievementMap.get(button.id)));
		}
		if (button.id == WORLD_ID) {
			System.out.println(RunComparison.downloadFastest());
			RunComparison.setRunData(RunComparison.downloadFastest());
			if (RunComparison.getRunData() != null) {
				Minecraft.getMinecraft().player.sendMessage(new TextComponentString("You time will now be compared against the fastest in the world!"));
			} else {
				Minecraft.getMinecraft().player.sendMessage(new TextComponentString("An error occurred when retrieving the fastest run"));
			}
		} else if (button.id == USER_ID) {
			RunComparison.setRunData(RunComparison.loadUserFastest());
			if (RunComparison.getRunData() != null) {
				Minecraft.getMinecraft().player.sendMessage(new TextComponentString("You time will now be compared against your fastest run"));
			} else {
				Minecraft.getMinecraft().player.sendMessage(new TextComponentString("An error occurred when retrieving the fastest run"));
			}
		} else if (button.id == RESET_ID) {
			RunComparison.setRunData(null);
			Minecraft.getMinecraft().player.sendMessage(new TextComponentString("You time will no be compared against another run"));
		} else if (button.id == EXTERNAL_ID) {
			if (WebServer.SERVER == null) {
				try {
					WebServer.SERVER = new WebServer();
				} catch (URISyntaxException e) {
					e.printStackTrace();
					Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Webserver failed to start"));
				}
				Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Webserver started!"));
			} else {
				try {
					WebServer.SERVER.loadFiles();
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
			try {
				openWebLink(new URI("http://localhost:7123/"));
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}

	private void openWebLink(URI url) {
		try {
			Class<?> oclass = Class.forName("java.awt.Desktop");
			Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object) null);
			oclass.getMethod("browse", new Class[] { URI.class }).invoke(object, url);
		} catch (Throwable throwable1) {
			throwable1.printStackTrace();
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
