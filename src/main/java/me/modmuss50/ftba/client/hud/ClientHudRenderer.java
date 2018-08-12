package me.modmuss50.ftba.client.hud;

import me.modmuss50.ftba.client.ClientDataManager;
import me.modmuss50.ftba.client.KeyBindings;
import me.modmuss50.ftba.config.ConfigFeatures;
import me.modmuss50.ftba.files.config.FTBAchievement;
import me.modmuss50.ftba.files.runs.AchievementData;
import me.modmuss50.ftba.files.runs.RunData;
import me.modmuss50.ftba.files.worldData.WorldFormat;
import me.modmuss50.ftba.util.ClientWorldPlayerCountHandler;
import me.modmuss50.ftba.util.TimerServerHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static me.modmuss50.ftba.client.gui.achivementGui.AchievementButton.isValidBook;

/**
 * Created by Mark on 11/02/2017.
 */
public class ClientHudRenderer {

    public static int state = 3;
    public static boolean visible = true;
    private static boolean lastEnabled = true;
    public static int clientTick = 0;

    //TODO Cache this
    @SideOnly(Side.CLIENT)
    public static String getBookTitle(FTBAchievement ftbAchievement) {
        if (isValidBook(ftbAchievement.bookStack)) {
            NBTTagCompound nbttagcompound = ftbAchievement.bookStack.getTagCompound();
            String s = nbttagcompound.getString("title");

            if (!StringUtils.isNullOrEmpty(s)) {
                return s;
            }
        }
        return ftbAchievement.name;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event) {
        clientTick++;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void renderGameEvent(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }
        Minecraft minecraft = Minecraft.getMinecraft();
        if (ConfigFeatures.INSTANCE.TeamSelection && TeamHUDManager.shouldShow()) {
            Gui.drawRect(3, 5, 137, 22, Color.BLACK.getRGB());
            Gui.drawRect(5, 7, 135, 20, Color.GRAY.getRGB());
            minecraft.fontRenderer.drawString("Press " + KeyBindings.teamSlection.getDisplayName() + " to join a team", 10, 10, Color.WHITE.getRGB());
        }
        HUDModes mode = HUDModes.NOARMAL;
        if (minecraft.currentScreen != null && ConfigFeatures.INSTANCE.ShowRunHash) {
            minecraft.fontRenderer.drawString("#" + ClientDataManager.getWorldHash(), 5, 5, mode.textColor);
        }
        if (!ConfigFeatures.INSTANCE.AchievementHUD) {
            return;
        }
        if (!visible || ClientWorldPlayerCountHandler.getPlayerCount() != 1) {
            return;
        }
        if (minecraft.gameSettings.showDebugInfo || minecraft.currentScreen != null && !(minecraft.currentScreen instanceof GuiChat)) {
            return;
        }
        List<FTBAchievement> achievements = Collections.emptyList();
        if (ClientDataManager.getConfigFormat().achievements != null) {
            achievements = new ArrayList<>(ClientDataManager.getConfigFormat().achievements);
        }

        ClientHintRender.render(event);

        int height = Math.max(15, (achievements.size() * 12) + 11);

        int xPos = event.getResolution().getScaledWidth() - (50);
        int yPos = event.getResolution().getScaledHeight() - 25;
        if(state % 2 == 0){
            xPos = 5;
        }
        if(state > 1){
            yPos = 5;
        }

        Gui.drawRect(xPos - 2, yPos - 2, xPos + 45, yPos + height + 2, mode.backgroundRGBA);
        Gui.drawRect(xPos, yPos, xPos + 43, yPos + height, mode.secondaryColor);

        minecraft.fontRenderer.drawString(Timer.getNiceTime(), xPos + 2, yPos + 4, mode.textColor);

        WorldFormat format = ClientDataManager.getWorldFormat();

        Collections.sort(achievements, (achievement1, achievement2) -> {
            if (format == null || format.triggedAchivements == null || achievement1 == null || achievement2 == null) {
                return 0;
            }
            if (format.triggedAchivements.contains(achievement1.name) && format.triggedAchivements.contains(achievement2.name)) {
                return (int) (format.achivementTimes.get(achievement1.name) - format.achivementTimes.get(achievement2.name));
            }
            return 0;
        });

        Collections.sort(achievements, (achievement1, achievement2) -> {
            if (format == null || format.triggedAchivements == null || achievement1 == null || achievement2 == null) {
                return 0;
            }
            if (format.triggedAchivements.contains(achievement1.name) && format.triggedAchivements.contains(achievement2.name)) {
                return 0;
            }
            if (format.triggedAchivements.contains(achievement1.name) && !format.triggedAchivements.contains(achievement2.name)) {
                return 1;
            }
            if (!format.triggedAchivements.contains(achievement1.name) && format.triggedAchivements.contains(achievement2.name)) {
                return -1;
            }
            return 0;
        });

        RunData fastest = null;


        int i = 0;
        for (FTBAchievement achievement : achievements) {
            i++;
            String title = getBookTitle(achievement);
            String name =
                    format != null && format.triggedAchivements != null && format.triggedAchivements.contains(achievement.name) ? TextFormatting.GRAY + "" + TextFormatting.STRIKETHROUGH + title
                            : title;
            boolean isComplete = false;
            if (format.triggedAchivements != null && format.triggedAchivements.contains(achievement.name)) {
                long time = format.achivementTimes.get(achievement.name);
                isComplete = true;
                String timeStr = TimerServerHandler.getNiceTimeFromLong(time);
                boolean hasFastest = false;
                if (fastest != null) {
                    for (AchievementData data : fastest.achievementData) {
                        if (data.achievement.equals(achievement.name)) {
                            hasFastest = true;
                            long fastestTime = data.time;
                            TextFormatting color = TextFormatting.RED;
                            if (fastestTime > time) {
                                color = TextFormatting.GREEN;
                            }
                            long timeDiff = fastestTime - time;
                            boolean negative = timeDiff < 0;
                            String prefix = "+";
                            if (negative) {
                                timeDiff = Math.abs(timeDiff);
                                prefix = "-";
                            }
                            timeStr = color + prefix + TimerServerHandler.getNiceTimeFromLong(timeDiff);
                        }
                    }
                }
                int strTrimLenght = 8;
                if (hasFastest) {
                    strTrimLenght = 7;
                }
                if (title.length() >= strTrimLenght) {
                    name = name.substring(0, Math.min(name.length(), strTrimLenght + 1)) + TextFormatting.RESET + TextFormatting.GRAY + "...";
                }
                name = name + TextFormatting.RESET + " " + TextFormatting.GREEN;

                if (!hasFastest) {
                    minecraft.fontRenderer.drawString(TextFormatting.GREEN + timeStr, xPos + 40, yPos + (i * 12) + 12, mode.textColor);
                } else {
                    minecraft.fontRenderer.drawString(TextFormatting.GREEN + timeStr, xPos + 33, yPos + (i * 12) + 12, mode.textColor);
                }

            }

            if (!isComplete) {
                if (ClientDataManager.percentageMap != null && ClientDataManager.percentageMap.containsKey(achievement.name)) {
                    String percentage = TextFormatting.AQUA + ClientDataManager.percentageMap.get(achievement.name);
                    if (!ClientDataManager.percentageMap.get(achievement.name).equals("150%")) {
                        if (percentage.length() > 5) {
                            minecraft.fontRenderer.drawString(percentage, xPos + 70, yPos + (i * 12) + 12, mode.textColor);
                        } else {
                            minecraft.fontRenderer.drawString(percentage, xPos + 80, yPos + (i * 12) + 12, mode.textColor);
                        }
                    }
                }
            }

            minecraft.fontRenderer.drawString(name, xPos + 1, yPos + (i * 12) + 12, mode.textColor);
        }

    }

}
