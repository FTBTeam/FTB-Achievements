package me.modmuss50.ftba.client;

import me.modmuss50.ftba.client.gui.teamSelection.GuiSelectTeam;
import me.modmuss50.ftba.client.hud.ClientHintRender;
import me.modmuss50.ftba.client.hud.ClientHudRenderer;
import me.modmuss50.ftba.config.ConfigFeatures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

/**
 * Created by Mark on 12/02/2017.
 */
@SideOnly(Side.CLIENT)
public class KeyBindings {
	public static final KeyBinding toggleHUD = new KeyBinding("key.ftbachievements.toggleHUD", KeyConflictContext.IN_GAME, Keyboard.KEY_U, "FTB Achievements");
	public static final KeyBinding toggleHUDRender = new KeyBinding("key.ftbachievements.toggleHUDVisable", KeyConflictContext.IN_GAME, Keyboard.KEY_J, "FTB Achievements");
	public static final KeyBinding teamSlection = new KeyBinding("key.ftbachievements.teamSelection", KeyConflictContext.IN_GAME, Keyboard.KEY_H, "FTB Achievements");

	public static void init() {
		ClientRegistry.registerKeyBinding(toggleHUD);
		ClientRegistry.registerKeyBinding(teamSlection);
		ClientRegistry.registerKeyBinding(toggleHUDRender);
	}

	@SubscribeEvent
	public static void keyPresEvent(InputEvent.KeyInputEvent event) throws IOException {
		if (toggleHUD.isPressed()) {
			if(ClientHudRenderer.state == 3){
				ClientHudRenderer.state = 0;
			} else {
				ClientHudRenderer.state ++;
			}
			ClientHintRender.hide();
		}
		if(toggleHUDRender.isPressed()){
			ClientHudRenderer.visible = !ClientHudRenderer.visible;
			ClientHintRender.hide();
		}
		if (ConfigFeatures.INSTANCE.TeamSelection && teamSlection.isPressed()) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiSelectTeam());
		}
	}

}

