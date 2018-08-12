package me.modmuss50.ftba.files.config;

import net.minecraft.util.text.TextFormatting;

import java.util.List;

/**
 * Created by Mark on 04/02/2017.
 */
public class BlockTrigger extends BaseTrigger implements IRequirementProvider {

	public String type;

	public int requirement;

	@Override
	public void getRequirements(List<String> lines, FTBAchievement ftbAchievement) {
		lines.add("");
		if (type.equals("rf")) {
			lines.add(TextFormatting.AQUA + "Requires " + requirement + " RF");
		} else if (type.equals("redstone")) {
			lines.add(TextFormatting.AQUA + "Requires a redstone signal");
		}

	}

}
