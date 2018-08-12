package me.modmuss50.ftba.client.hud;

import java.awt.*;

/**
 * Created by modmuss50 on 01/03/2017.
 */
public enum HUDModes {

	NOARMAL(1056964608, 2139127936, true, Color.WHITE.getRGB()),
	GREEN(Color.GREEN.getRGB(), Color.GREEN.getRGB(), false, Color.BLACK.getRGB());

	public int backgroundRGBA;
	public int secondaryColor;
	public boolean coloredFTB;
	public int textColor;

	HUDModes(int backgroundRGBA, int secondaryColor, boolean coloredFTB, int textColor) {
		this.backgroundRGBA = backgroundRGBA;
		this.secondaryColor = secondaryColor;
		this.coloredFTB = coloredFTB;
		this.textColor = textColor;
	}
}
