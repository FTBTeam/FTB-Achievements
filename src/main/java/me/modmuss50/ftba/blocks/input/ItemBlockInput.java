package me.modmuss50.ftba.blocks.input;

import me.modmuss50.ftba.ConfigManager;
import me.modmuss50.ftba.FTBAchievements;
import me.modmuss50.ftba.files.config.BlockTrigger;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import reborncore.common.itemblock.ItemBlockBase;

/**
 * Created by Mark on 04/02/2017.
 */
public class ItemBlockInput extends ItemBlockBase {
	public ItemBlockInput(Block block) {
		super(FTBAchievements.blockInput, FTBAchievements.blockInput, new String[] { "test" });
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		BlockTrigger trigger = ConfigManager.getTriggerFromMeta(stack.getItemDamage());
		if (trigger == null) {
			return super.getUnlocalizedName(stack);
		}
		return super.getUnlocalizedName(stack) + "-" + trigger.achievement;
	}
}
