package me.modmuss50.ftba.client.gui;

import me.modmuss50.ftba.files.runs.RunData;
import me.modmuss50.ftba.util.AchievementConnection;
import me.modmuss50.ftba.util.TimerServerHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.CertificateHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by modmuss50 on 01/03/2017.
 */
public class GuiUpload extends GuiScreen {

	private static final int CANCEL_BUTTON_ID = 1;
	private static final int UPLOAD_BUTTON_ID = 0;
	public GuiScreen parent;
	GuiButton uploadButton;
	RunData data;

	private GuiTextField urlField;

	private String[] validDomains = new String[] { "youtube.com", "twitch.tv", "beam.pro" };

	public GuiUpload(GuiScreen parent, RunData data) {
		this.parent = parent;
		this.data = data;
	}

	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.add(new GuiButton(CANCEL_BUTTON_ID, this.width / 2 + 5, this.height - 35, 150, 20, I18n.format("gui.cancel")));
		this.buttonList.add(uploadButton = new GuiButton(UPLOAD_BUTTON_ID, this.width / 2 - 155, this.height - 35, 150, 20, "Upload"));
		this.urlField = new GuiTextField(9, this.fontRenderer, this.width / 2 - 100, 60, 200, 20);
		urlField.setMaxStringLength(500);
		this.urlField.setFocused(true);
		this.urlField.setText("");
	}

	@Override
	public void actionPerformed(GuiButton guiButton) throws IOException {
		super.actionPerformed(guiButton);
		if (guiButton.id == CANCEL_BUTTON_ID) {
			Minecraft.getMinecraft().displayGuiScreen(parent);
		}
		if (guiButton.id == UPLOAD_BUTTON_ID) {
			RunData rdata = data;
			if (!urlField.getText().isEmpty()) {
				rdata.videoURL = urlField.getText();
			}
			if (rdata.players == null || rdata.players.isEmpty()) {
				rdata.players = new ArrayList<>();
				rdata.players.add(new RunData.PlayerData(rdata.userName, rdata.uuid));
			}
			rdata.modFingerprint = getModFingerprint();
			if (rdata.videoURL == null || rdata.videoURL.isEmpty()) {
				rdata.videoURL = "https://ftb.world/";
				Minecraft.getMinecraft().displayGuiScreen(new GuiUploadConfimation(parent, rdata));
			} else {
				Minecraft.getMinecraft().displayGuiScreen(new GuiPostUpload(parent, AchievementConnection.postData(rdata)));
			}
		}
	}

	private String getModFingerprint() {
		ModContainer container = null;
		for (ModContainer modContainer : Loader.instance().getModList()) {
			if (modContainer.getModId().equals("ftbachievements")) {
				container = modContainer;
				break;
			}
		}
		if (container.getSigningCertificate() == null) {
			return "UNKNOWN";
		}
		return CertificateHelper.getFingerprint(container.getSigningCertificate());
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		this.urlField.updateCursorCounter();
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		this.urlField.textboxKeyTyped(typedChar, keyCode);
		if (urlField.getText().isEmpty()) {
			uploadButton.enabled = true;
		} else {
			boolean isValid = false;
			for (String domain : validDomains) {
				if (urlField.getText().contains(domain)) {
					isValid = true;
					break;
				}
			}
			uploadButton.enabled = isValid;
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.urlField.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.urlField.drawTextBox();
		this.drawString(this.fontRenderer, I18n.format("Enter Video URL"), this.width / 2 - 100, 47, -6250336);
		this.drawCenteredString(this.fontRenderer, I18n.format(TextFormatting.BLUE + "F" + TextFormatting.GREEN + "T" + TextFormatting.RED + "B" + TextFormatting.WHITE + " Achievement submit form"), this.width / 2, 20, -1);

		this.drawString(this.fontRenderer, "Run information:", this.width / 2 - 100, 90, -1);
		this.drawString(this.fontRenderer, "Player: " + data.userName, this.width / 2 - 90, 100, -1);
		this.drawString(this.fontRenderer, "Time completed in: " + TimerServerHandler.getNiceTimeFromLong(data.totalTime), this.width / 2 - 90, 110, -1);
		this.drawString(this.fontRenderer, "Date completed: " + data.runDate.replace("T", " "), this.width / 2 - 90, 120, -1);

		int offset = 140;
		if (data.players != null && !data.players.isEmpty()) {
			this.drawString(this.fontRenderer, "Players:", this.width / 2 - 100, offset, -1);
			for (RunData.PlayerData player : data.players) {
				this.drawString(this.fontRenderer, player.name, this.width / 2 - 90, offset += 10, -6250336);
			}
		}

		this.drawString(this.fontRenderer, "Unique run hash: " + data.runHash, 10, this.height - 10, -6250336);
	}
}
