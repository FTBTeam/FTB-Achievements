package me.modmuss50.ftba.util;

import me.modmuss50.ftba.packets.PacketSendTimerData;
import net.minecraft.entity.player.EntityPlayerMP;
import reborncore.common.network.INetworkPacket;
import reborncore.common.network.NetworkManager;

/**
 * Created by modmuss50 on 14/02/2017.
 */
public class TimerServerHandler {

	static long startSystemTime;
	static long countedTime;
	static boolean active;

	static long stoppedTime;

	public static long getTimeDifference() {
		return System.currentTimeMillis() - startSystemTime + countedTime;
	}

	public static String getNiceTimeFromLong(long time) {
		final int hour = (int) (time / 3600000L);
		time -= hour * 3600000;
		final int min = (int) (time / 60000L);
		time -= min * 60000;
		final int sec = (int) (time / 1000L);
		time -= sec * 1000;
		return String.format("%02d:%02d:%02d", hour, min, sec);
	}

	public static void startTimer(long startTime) {
		countedTime = startTime;
		startSystemTime = System.currentTimeMillis();
		active = true;
	}

	public static void stop() {
		active = false;
		stoppedTime = getTimeDifference();
	}

	public static void reset() {
		startSystemTime = System.currentTimeMillis();
		countedTime = 0;
		active = false;
		stoppedTime = 0;
	}

	public static void load(TimerSaveDataFormat format) {
		reset();
		active = format.active;
		if (format.active) {
			startTimer(format.time);
		} else {
			countedTime = format.time;
			stoppedTime = format.time;
		}
	}

	public static TimerSaveDataFormat save() {
		TimerSaveDataFormat saveDataFormat = new TimerSaveDataFormat();
		if (!active) {
			saveDataFormat.time = stoppedTime;
		} else {
			saveDataFormat.time = getTimeDifference();
		}
		saveDataFormat.active = active;
		return saveDataFormat;
	}

	public static long getStoppedTime() {
		return stoppedTime;
	}

	public static boolean isActive() {
		return active;
	}

	public static long getCountedTime() {
		return countedTime;
	}

	public static void syncWithAll() {
		NetworkManager.sendToAll(getNetworkPacket());
	}

	public static void syncWith(EntityPlayerMP playerMP) {
		NetworkManager.sendToPlayer(getNetworkPacket(), playerMP);
	}

	private static INetworkPacket getNetworkPacket() {
		long time = getTimeDifference();
		if (!active) {
			time = stoppedTime;
		}
		return new PacketSendTimerData(time, active);
	}

	public static void main(String[] args) {
		System.out.println(getNiceTimeFromLong(6022949));
	}

}
