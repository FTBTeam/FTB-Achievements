package me.modmuss50.ftba.client.hud;


import me.modmuss50.ftba.client.KeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ClientHintRender {

    private static boolean isHidden;
    private static boolean hasLoaded;

    public static File mcFile;

    public static void render(RenderGameOverlayEvent.Post event){
        if(!hasLoaded){
            loadIsHidden();
        }
        if(!show()){
            return;
        }
        HUDModes mode = HUDModes.NOARMAL;
        int height = 30;
        int xPos = event.getResolution().getScaledWidth() - (105);
        int yPos = event.getResolution().getScaledHeight() - 60;
        if(ClientHudRenderer.state % 2 == 0){
            xPos = 5;
        }
        if(ClientHudRenderer.state > 1){
            yPos = 30;
        }

        Gui.drawRect(xPos - 2, yPos - 2, xPos + 102, yPos + height + 2, mode.backgroundRGBA);
        Gui.drawRect(xPos, yPos, xPos + 100, yPos + height, mode.secondaryColor);

        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        fontRenderer.drawString("HUD controls:", xPos + 5, yPos +1, Color.WHITE.getRGB());
        fontRenderer.drawString("Press " + KeyBindings.toggleHUDRender.getDisplayName() + " to hide", xPos + 5, yPos + 11, Color.WHITE.getRGB());
        fontRenderer.drawString("Press " + KeyBindings.toggleHUD.getDisplayName() + " to move", xPos + 5, yPos + 21, Color.WHITE.getRGB());
    }


    public static boolean show(){
        return !isHidden;
    }

    public static void hide() throws IOException {
        if(!getFile().exists()){
            FileUtils.touch(getFile());
        }
    }

    public static void loadIsHidden(){
        isHidden = getFile().exists();
    }

    public static File getFile(){
        return new File(mcFile, "ftba_hud.dat");
    }

}
