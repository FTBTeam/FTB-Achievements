package me.modmuss50.ftba.util;

/**
 * Created by modmuss50 on 14/02/2017.
 */
public class TimerSaveDataFormat {

	public long time;

	public boolean active;

	public TimerSaveDataFormat(long time, boolean active) {
		this.time = time;
		this.active = active;
	}

	public TimerSaveDataFormat() {
	}
}
