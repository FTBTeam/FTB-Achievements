package me.modmuss50.ftba.client;

import me.modmuss50.ftba.files.config.GuiTrigger;
import me.modmuss50.ftba.packets.PacketGuiTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.network.NetworkManager;

/**
 * Created by modmuss50 on 13/02/2017.
 */
@SideOnly(Side.CLIENT)
public class GuiEvent {

	private static boolean isMenuOpen = false;
	private static int guiScale = -1;

	@SubscribeEvent
	public static void guiOpen(GuiScreenEvent.InitGuiEvent event) {
		if (event.getGui() == null || ClientDataManager.getConfigFormat() == null || ClientDataManager.getConfigFormat().guiTriggers == null) {
			return;
		}
		for (GuiTrigger trigger : ClientDataManager.getConfigFormat().guiTriggers) {
			if (trigger.guiClassName.equalsIgnoreCase(event.getGui().getClass().getCanonicalName())) {
				NetworkManager.sendToServer(new PacketGuiTrigger(event.getGui().getClass().getCanonicalName()));
			}
		}
	}

	@SubscribeEvent
	public static void initGuiPre(GuiScreenEvent.InitGuiEvent.Pre event){
		if(event.getGui() instanceof GuiMainMenu || event.getGui().getClass().getName().equals("lumien.custommainmenu.gui.GuiCustom")){
			openMainMenu();
		} else {
			closeMainMenu();
		}
	}

	@SubscribeEvent
	public static void guiRender(RenderGameOverlayEvent event) {
		if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD) {
			//			event.setCanceled(true);
		}
	}


	public static void openMainMenu(){
		if(isMenuOpen){
			return;
		}
		isMenuOpen = true;
		guiScale = Minecraft.getMinecraft().gameSettings.guiScale;
		Minecraft.getMinecraft().gameSettings.guiScale = 0;
		updateRes();
	}

	public static void closeMainMenu(){
		if(!isMenuOpen){
			return;
		}
		isMenuOpen = false;
		Minecraft.getMinecraft().gameSettings.guiScale = guiScale;
		updateRes();
	}

	public static void updateRes(){
		Minecraft minecraft = Minecraft.getMinecraft();
		ScaledResolution scaledresolution = new ScaledResolution(minecraft);
		int i = scaledresolution.getScaledWidth();
		int j = scaledresolution.getScaledHeight();
		minecraft.currentScreen.setWorldAndResolution(minecraft, i, j);
	}


}
