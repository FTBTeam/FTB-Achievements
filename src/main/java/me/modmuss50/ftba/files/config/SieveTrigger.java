package me.modmuss50.ftba.files.config;

import net.minecraft.util.text.TextFormatting;
import reborncore.common.util.StringUtils;

import java.util.List;

/**
 * Created by modmuss50 on 09/02/2017.
 */
public class SieveTrigger extends BaseTrigger implements IRequirementProvider {

	public String meshType;

	@Override
	public void getRequirements(List<String> lines, FTBAchievement ftbAchievement) {
		lines.add("");
		lines.add(TextFormatting.AQUA + "Use a sieve with " + TextFormatting.YELLOW + StringUtils.toFirstCapital(meshType) + TextFormatting.AQUA + " mesh");
	}
}
