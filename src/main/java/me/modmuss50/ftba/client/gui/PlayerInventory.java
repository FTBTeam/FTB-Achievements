package me.modmuss50.ftba.client.gui;

import me.modmuss50.ftba.client.ClientDataManager;
import me.modmuss50.ftba.client.gui.achivementGui.GuiAchievements;
import me.modmuss50.ftba.config.ConfigFeatures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by modmuss50 on 09/02/2017.
 */
public class PlayerInventory {

	public static final int buttonID = 542;
	public static final String customMainMenu = "lumien.custommainmenu.gui.GuiFakeMain";

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void openGui(GuiScreenEvent.InitGuiEvent event) {
		if (!ConfigFeatures.INSTANCE.MainMenuButton) {
			return;
		}
		if (event.getGui() != null) {
			if (event.getGui() instanceof GuiInventory) {
				GuiInventory gui = (GuiInventory) event.getGui();
				int guiLeft = (gui.width - 176) / 2;
				int guiTop = (gui.height - 166) / 2;
				event.getButtonList().add(new ItemButton(buttonID, guiLeft + 150, guiTop + 58, new ItemStack(Items.BOOK)));
			}
			if (event.getGui() instanceof GuiContainerCreative) {
				GuiContainerCreative gui = (GuiContainerCreative) event.getGui();
				int guiLeft = (gui.width - 136) / 2;
				int guiTop = (gui.height - 195) / 2;
				event.getButtonList().add(new ItemButton(buttonID, guiLeft + 120, guiTop - 20, new ItemStack(Items.BOOK)));
			}
			if (event.getGui() instanceof GuiMainMenu || isMainMenu(event.getGui())) {
				if (ClientDataManager.percentageMap != null) {
					ClientDataManager.percentageMap.clear();
				}
				event.getButtonList().add(new ItemButton(buttonID, 10, 10, new ItemStack(Items.FIREWORKS), "", false));
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void buttonClick(GuiScreenEvent.ActionPerformedEvent event) {
		if (!ConfigFeatures.INSTANCE.MainMenuButton) {
			return;
		}
		if (event.getButton().id == buttonID) {
			if (event.getGui() instanceof GuiInventory || event.getGui() instanceof GuiContainerCreative) {
				Minecraft.getMinecraft().displayGuiScreen(new GuiAchievements());
			}
			if (event.getGui() instanceof GuiMainMenu || isMainMenu(event.getGui())) {
				Minecraft.getMinecraft().displayGuiScreen(new GuiRunList(event.getGui()));
			}
		}
	}

	public boolean isMainMenu(GuiScreen screen) {
		return screen.getClass().getName().equals(customMainMenu) || screen.getClass().getName().equals("lumien.custommainmenu.gui.GuiCustom");
	}
}
