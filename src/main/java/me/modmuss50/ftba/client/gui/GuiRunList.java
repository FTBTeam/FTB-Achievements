package me.modmuss50.ftba.client.gui;

import me.modmuss50.ftba.files.runs.RunData;
import me.modmuss50.ftba.files.runs.RunManager;
import me.modmuss50.ftba.util.TimerServerHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class GuiRunList extends GuiScreen {
	private static final Logger logger = LogManager.getLogger();
	private static final int CANCEL_BUTTON_ID = 1;
	private static final int UPLOAD_BUTTON_ID = 0;
	public GuiScreen parent;
	private List<RunData> runList;
	private RunList list;
	private int selectedSlot;
	private GuiButton upload;

	public GuiRunList(GuiScreen parent) {
		this.parent = parent;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		super.initGui();
		initSaveList();
		this.list = new RunList();

		this.buttonList.add(new GuiButton(CANCEL_BUTTON_ID, this.width / 2 + 5, this.height - 28, 150, 20, I18n.format("gui.cancel")));
		this.buttonList.add(this.upload = new GuiButton(UPLOAD_BUTTON_ID, this.width / 2 - 155, this.height - 28, 150, 20, "Upload"));
	}

	private void initSaveList() {
		this.runList = RunManager.getRuns();
		Collections.sort(this.runList, (r1, r2) -> (int) (r1.totalTime - r2.totalTime));
		this.selectedSlot = -1;
	}

	@Override
	public void actionPerformed(GuiButton guiButton) throws IOException {
		super.actionPerformed(guiButton);
		if (guiButton.id == CANCEL_BUTTON_ID) {
			Minecraft.getMinecraft().displayGuiScreen(parent);
		}
		if (guiButton.id == UPLOAD_BUTTON_ID) {
			uploadMap();
		}
		list.actionPerformed(guiButton);
	}

	@Override
	protected void keyTyped(char keyChar, int keyCode) throws IOException {
		if (keyCode == 28 || keyCode == 156) {
			this.actionPerformed(this.buttonList.get(0));
		}
	}

	/**
	 * Handles mouse input.
	 */
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		this.list.handleMouseInput();
	}

	@Override
	public void drawScreen(int x, int y, float f) {
		list.drawScreen(x, y, f);
		this.drawCenteredString(this.fontRenderer, TextFormatting.BLUE + "F" + TextFormatting.GREEN + "T" + TextFormatting.RED + "B" + TextFormatting.WHITE + " Achievements", this.width / 2, 20, -1);
		this.drawCenteredString(this.fontRenderer, "This is a list of all the runs that you have completed", this.width / 2, 40, -1);
		this.drawCenteredString(this.fontRenderer, "You can upload your best run to the global leaderboard.", this.width / 2, 50, -1);
		super.drawScreen(x, y, f);
	}

	private class RunList extends GuiSlot {
		public RunList() {
			super(GuiRunList.this.mc, GuiRunList.this.width, GuiRunList.this.height, 78, GuiRunList.this.height - 32, 36);
		}

		@Override
		protected int getSize() {
			return GuiRunList.this.runList.size();
		}

		protected void elementClicked(int slot, boolean doubleClicked, int mouseX, int mouseY) {
			GuiRunList.this.selectedSlot = slot;

			if (doubleClicked) {
				GuiRunList.this.uploadMap();
			}
		}

		@Override
		protected boolean isSelected(int slot) {
			return (slot == GuiRunList.this.selectedSlot);
		}

		@Override
		protected void drawBackground() {
			GuiRunList.this.drawDefaultBackground();
		}

		@Override
		public int getListWidth() {
			return GuiRunList.this.width - 40;
		}

		@Override
		protected int getScrollBarX() {
			return (this.width / 2) + (this.getListWidth() / 2) - 6;
		}

		@Override
		protected void drawSlot(int slot, int x, int y, int slotHeight, int mouseX, int mouseY, float ticks) {
			RunData runData = GuiRunList.this.runList.get(slot);

			String displayName = TimerServerHandler.getNiceTimeFromLong(runData.totalTime);
			String by = runData.userName;
			String topLine = displayName + ", " + by;

			String date = runData.runDate;
			String middleLine = "(" + date.replace("T", " ") + ") ";

			String bottomLine = "Completed " + runData.achievementData.size() + " achievements";
			if (runData.players != null && runData.players.size() > 1) {
				int extraPlayers = (runData.players.size() - 1);
				bottomLine = bottomLine + " with " + extraPlayers + " other player" + (extraPlayers > 1 ? "s" : "");
			}

			GuiRunList.this.drawString(GuiRunList.this.fontRenderer, topLine, x + 34, y + 1, 16777215);
			GuiRunList.this.drawString(GuiRunList.this.fontRenderer, middleLine, x + 34, y + 12, 8421504);
			GuiRunList.this.drawString(GuiRunList.this.fontRenderer, bottomLine, x + 34, y + 12 + 10, 8421504);
		}
	}

	private RunData getRun() {
		return GuiRunList.this.runList.get(this.selectedSlot);
	}

	private void uploadMap() {
		if (this.selectedSlot == -1) {
			return;
		}
		Minecraft.getMinecraft().displayGuiScreen(new GuiUpload(parent, getRun()));
	}

}
