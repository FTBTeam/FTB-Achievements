package me.modmuss50.ftba.files.config;

import net.minecraft.util.text.TextFormatting;

import java.util.List;

/**
 * Created by modmuss50 on 13/02/2017.
 */
public class VMTrigger extends BaseTrigger implements IRequirementProvider {

	public int tier;

	@Override
	public void getRequirements(List<String> lines, FTBAchievement ftbAchievement) {
		lines.add("");
		lines.add(TextFormatting.AQUA + "Requires tier " + tier + " void ore miner");
	}

}
