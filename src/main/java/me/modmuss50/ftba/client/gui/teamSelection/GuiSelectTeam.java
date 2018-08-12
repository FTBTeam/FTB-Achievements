package me.modmuss50.ftba.client.gui.teamSelection;

import me.modmuss50.ftba.packets.PacketPlayerJoinTeam;
import me.modmuss50.ftba.team.TeamEnum;
import me.modmuss50.ftba.util.ClientWorldPlayerCountHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import reborncore.client.guibuilder.GuiBuilder;
import reborncore.common.network.NetworkManager;

import java.awt.*;
import java.io.IOException;

/**
 * Created by modmuss50 on 22/05/2017.
 */
public class GuiSelectTeam extends GuiScreen {
    GuiBuilder builder = new GuiBuilder(GuiBuilder.defaultTextureSheet);

    int xSize;
    int ySize;
    int guiLeft;
    int guiTop;

    @Override
    public void initGui() {
        super.initGui();
        xSize = 130;
        ySize = 150;

        guiLeft = (this.width - this.xSize) / 2;
        guiTop = (this.height - this.ySize) / 2;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        builder.drawDefaultBackground(this, guiLeft, guiTop, xSize, ySize);
        super.drawScreen(mouseX, mouseY, partialTicks);

        //TODO use a better button system, as it was bad enough before, but this is getting stupid now
        if (ClientWorldPlayerCountHandler.getPlayerCount() == 4) {
            drawTeamBox(guiLeft + 10, guiTop + 10, +guiLeft + 60, guiTop + 60, Color.blue, mouseX, mouseY);
            drawString(fontRenderer, "Join Blue", guiLeft + 13, guiTop + 30, Color.white.getRGB());

            drawTeamBox(guiLeft + 10 + 60, guiTop + 10 + 60, +guiLeft + 60 + 60, guiTop + 60 + 60, Color.yellow, mouseX, mouseY);
            drawString(fontRenderer, "Join Yellow", guiLeft + 8 + 60, guiTop + 30 + 60, Color.white.getRGB());

            drawTeamBox(guiLeft + 10 + 60, guiTop + 10, +guiLeft + 60 + 60, guiTop + 60, Color.green, mouseX, mouseY);
            drawString(fontRenderer, "Join Green", guiLeft + 8 + 60, guiTop + 30, Color.white.getRGB());

            drawTeamBox(guiLeft + 10, guiTop + 10 + 60, +guiLeft + 60, guiTop + 60 + 60, Color.red, mouseX, mouseY);
            drawString(fontRenderer, "Join Red", guiLeft + 13, guiTop + 30 + 60, Color.white.getRGB());
        } else if(ClientWorldPlayerCountHandler.getPlayerCount() == 2){
            drawTeamBox(guiLeft + 10, guiTop + 10, +guiLeft + 60, guiTop + 60, Color.blue, mouseX, mouseY);
            drawString(fontRenderer, "Join Blue", guiLeft + 13, guiTop + 30, Color.white.getRGB());

            drawTeamBox(guiLeft + 10 + 60, guiTop + 10, +guiLeft + 60 + 60, guiTop + 60, Color.yellow, mouseX, mouseY);
            drawString(fontRenderer, "Join Yellow", guiLeft + 8 + 60, guiTop + 30, Color.white.getRGB());

            drawString(fontRenderer, "2 teams disabled", guiLeft + 20, guiTop + 85, Color.white.getRGB());
            drawString(fontRenderer, "due to map settings.", guiLeft + 15, guiTop + 95, Color.white.getRGB());
        } else if (ClientWorldPlayerCountHandler.getPlayerCount() == 1){
            drawString(fontRenderer, "Singleplayer", guiLeft + 5, guiTop + 10, Color.white.getRGB());
            drawString(fontRenderer, "team selection disabled.", guiLeft + 5, guiTop + 20, Color.white.getRGB());
            return; //We dont want spectator
        }

        drawTeamBox(guiLeft + 10, guiTop + 10 + 115, +guiLeft + 60 + 60, guiTop + 60 + 80, Color.gray, mouseX, mouseY);
        drawString(fontRenderer, "Spectate", guiLeft + 45, guiTop + 130, Color.white.getRGB());
    }

    private void drawTeamBox(int left, int top, int right, int bottom, Color color, int mouseX, int mouseY) {
        if (isInRect(left, top, right - left, bottom - top, mouseX, mouseY)) {
            drawRect(left, top, right, bottom, color.darker().darker().getRGB());
        } else {
            drawRect(left, top, right, bottom, color.getRGB());
        }

    }

    public boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + xSize && mouseY >= y && mouseY <= y + ySize;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        //TODO make this less ify (see what I did there :P)
        if(ClientWorldPlayerCountHandler.getPlayerCount() == 1){
            return;
        }
        if(ClientWorldPlayerCountHandler.getPlayerCount() == 4){
            if (isMouseInBounds(guiLeft + 10, guiTop + 10, +guiLeft + 60, guiTop + 60, mouseX, mouseY)) {
                NetworkManager.sendToServer(new PacketPlayerJoinTeam(TeamEnum.BLUE));
                Minecraft.getMinecraft().displayGuiScreen(null);
            } else if (isMouseInBounds(guiLeft + 10 + 60, guiTop + 10, +guiLeft + 60 + 60, guiTop + 60, mouseX, mouseY)) {
                NetworkManager.sendToServer(new PacketPlayerJoinTeam(TeamEnum.GREEN));
                Minecraft.getMinecraft().displayGuiScreen(null);
            } else if (isMouseInBounds(guiLeft + 10, guiTop + 10 + 60, +guiLeft + 60, guiTop + 60 + 60, mouseX, mouseY)) {
                NetworkManager.sendToServer(new PacketPlayerJoinTeam(TeamEnum.RED));
                Minecraft.getMinecraft().displayGuiScreen(null);
            } else if (isMouseInBounds(guiLeft + 10 + 60, guiTop + 10 + 60, +guiLeft + 60 + 60, guiTop + 60 + 60, mouseX, mouseY)) {
                NetworkManager.sendToServer(new PacketPlayerJoinTeam(TeamEnum.YELLOW));
                Minecraft.getMinecraft().displayGuiScreen(null);
            }
        } else if (ClientWorldPlayerCountHandler.getPlayerCount() == 2){
            if (isMouseInBounds(guiLeft + 10, guiTop + 10, +guiLeft + 60, guiTop + 60, mouseX, mouseY)) {
                NetworkManager.sendToServer(new PacketPlayerJoinTeam(TeamEnum.BLUE));
                Minecraft.getMinecraft().displayGuiScreen(null);
            } else if (isMouseInBounds(guiLeft + 10 + 60, guiTop + 10, +guiLeft + 60 + 60, guiTop + 60, mouseX, mouseY)) {
                NetworkManager.sendToServer(new PacketPlayerJoinTeam(TeamEnum.YELLOW));
                Minecraft.getMinecraft().displayGuiScreen(null);
            }
        }
        if (isMouseInBounds(guiLeft + 10, guiTop + 10 + 115, +guiLeft + 60 + 60, guiTop + 60 + 80, mouseX, mouseY)) {
            NetworkManager.sendToServer(new PacketPlayerJoinTeam(TeamEnum.SPECTATOR));
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
    }

    private boolean isMouseInBounds(int left, int top, int right, int bottom, int mouseX, int mouseY) {
        return isInRect(left, top, right - left, bottom - top, mouseX, mouseY);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
