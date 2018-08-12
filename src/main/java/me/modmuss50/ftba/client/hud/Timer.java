package me.modmuss50.ftba.client.hud;

import me.modmuss50.ftba.util.TimerServerHandler;

/**
 * Created by modmuss50 on 13/02/2017.
 */
public class Timer {

	static long startSystemTime;
	static long countedTime;
	static boolean active;

	public static String getNiceTime() {
		if (!active) {
			return TimerServerHandler.getNiceTimeFromLong(countedTime);
		}
		return TimerServerHandler.getNiceTimeFromLong(getTimeDifference());
	}

	public static long getTimeDifference() {
		return System.currentTimeMillis() - startSystemTime + countedTime;
	}

	public static void startTimer(long startTime) {
		startSystemTime = System.currentTimeMillis();
		countedTime = startTime;
		active = true;
	}

	public static void setData(Long startTime, boolean isActive) {
		startSystemTime = System.currentTimeMillis();
		countedTime = startTime;
		active = isActive;
	}

	public static boolean isActive() {
		return active;
	}
}
