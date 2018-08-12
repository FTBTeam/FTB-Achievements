package me.modmuss50.ftba.files.config;

import me.modmuss50.ftba.modCompat.sc.StevesCartsCompat;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

/**
 * Created by modmuss50 on 13/02/2017.
 */
public class SCTrigger extends BaseTrigger implements IRequirementProvider {

	public String moduleName;

	@Override
	public void getRequirements(List<String> lines, FTBAchievement ftbAchievement) {
		lines.add("");
		lines.add(TextFormatting.AQUA + "Requires a cart with " + StevesCartsCompat.nameMap.get(moduleName));
	}
}
