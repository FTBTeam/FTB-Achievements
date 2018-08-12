package me.modmuss50.ftba.api;

/**
 * Created by mark on 16/07/2017.
 */
public interface IResGenHandler {

	void reset();

	void addSpeedModifier(float speed);

	void addPowerModifier(float power);

	void addLuckModifier(int luck);

}
