package me.modmuss50.ftba.util;

public class ClientWorldPlayerCountHandler {

    private static int playerCount = 4;

    public static int getPlayerCount() {
        return playerCount;
    }

    public static void setPlayerCount(int playerCount) {
        ClientWorldPlayerCountHandler.playerCount = playerCount;
    }
}
