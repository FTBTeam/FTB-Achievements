package me.modmuss50.ftba.files.config;

import net.minecraft.util.text.TextFormatting;

import java.util.List;

/**
 * Created by modmuss50 on 13/02/2017.
 */
public class CrusherTrigger extends BaseTrigger implements IRequirementProvider {

	@Override
	public void getRequirements(List<String> lines, FTBAchievement ftbAchievement) {
		lines.add("");
		lines.add(TextFormatting.AQUA + "Requires Crusher");
	}

}
