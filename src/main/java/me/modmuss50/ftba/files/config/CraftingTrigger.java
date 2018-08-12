package me.modmuss50.ftba.files.config;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 04/02/2017.
 */
public class CraftingTrigger extends BaseTrigger implements IRequirementProvider {

	@Nonnull
	public ItemStack triggerItem;

	@Override
	public void getRequirements(List<String> lines, FTBAchievement ftbAchievement) {
		List<String> craftingLines = new ArrayList<>();
		if (achievement.equals(ftbAchievement.name) && !triggerItem.isEmpty()) {
			if (triggerItem.getCount() > 1) {
				craftingLines.add(triggerItem.getCount() + "x " + triggerItem.getDisplayName());
			} else {
				craftingLines.add(triggerItem.getDisplayName());
			}
		}
		if (!craftingLines.isEmpty()) {
			lines.add("");
			lines.add(TextFormatting.AQUA + "Requires crafting:");
			lines.addAll(craftingLines);
		}
	}
}
