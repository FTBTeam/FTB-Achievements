package me.modmuss50.ftba.client.gui;

import me.modmuss50.ftba.files.runs.RunData;
import me.modmuss50.ftba.util.AchievementConnection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

/**
 * Created by modmuss50 on 01/03/2017.
 */
public class GuiUploadConfimation extends GuiScreen {

	private static final int CANCEL_BUTTON_ID = 1;
	private static final int UPLOAD_BUTTON_ID = 0;
	public GuiScreen parent;
	RunData data;

	public GuiUploadConfimation(GuiScreen parent, RunData data) {
		this.parent = parent;
		this.data = data;
	}

	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.add(new GuiButton(CANCEL_BUTTON_ID, this.width / 2 + 5, this.height - 35, 150, 20, "Cancel"));
		this.buttonList.add(new GuiButton(UPLOAD_BUTTON_ID, this.width / 2 - 155, this.height - 35, 150, 20, "Upload"));
	}

	@Override
	public void actionPerformed(GuiButton guiButton) throws IOException {
		super.actionPerformed(guiButton);
		if (guiButton.id == CANCEL_BUTTON_ID) {
			Minecraft.getMinecraft().displayGuiScreen(parent);
		} else if (guiButton.id == UPLOAD_BUTTON_ID) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiPostUpload(parent, AchievementConnection.postData(data)));
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);

		this.drawCenteredString(this.fontRenderer, "Are you sure you want to submit this run?", this.width / 2, 20, -1);

		this.drawCenteredString(this.fontRenderer, "A video URL was not provided.", this.width / 2, this.height / 2, -1);
		this.drawCenteredString(this.fontRenderer, "Our team will not be able to validate your run", this.width / 2, this.height / 2 + 12, -1);

		this.drawCenteredString(this.fontRenderer, "You will NOT be able to re-submit this run later with a video.", this.width / 2, this.height / 2 + 24, -1);
	}

}
