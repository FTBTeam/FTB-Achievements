package me.modmuss50.ftba.items;

import me.modmuss50.ftba.FTBAchievements;
import me.modmuss50.ftba.api.IResGenHandler;
import me.modmuss50.ftba.api.IResGenUpgrade;
import me.modmuss50.ftba.config.ConfigResGenUpgrades;
import me.modmuss50.ftba.config.ResGenUpgrade;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by mark on 16/07/2017.
 */
public class ItemUpgrades extends Item implements IResGenUpgrade {

	public ItemUpgrades() {
		setHasSubtypes(true);
		setCreativeTab(FTBAchievements.Tab.FTBA_TAB);
		setHasSubtypes(true);
	}

	public ResGenUpgrade getUpgradeFromStack(ItemStack stack) {
		return ConfigResGenUpgrades.INSTANCE.upgrades.get(stack.getItemDamage());
	}

	@Override
	public boolean processUpgrade(ItemStack stack, IResGenHandler upgradeHandler) {
		ResGenUpgrade upgrade = getUpgradeFromStack(stack);
		if (upgrade != null) {
			upgradeHandler.addSpeedModifier(upgrade.speedModifier);
			upgradeHandler.addLuckModifier(upgrade.luck);
			upgradeHandler.addPowerModifier(upgrade.powerModifier);
		}
		return true;
	}

	@Override
	// gets Unlocalized Name depending on meta data
	public String getUnlocalizedName(final ItemStack itemStack) {
		if (ConfigResGenUpgrades.INSTANCE == null || ConfigResGenUpgrades.INSTANCE.upgrades == null) {
			//BAD THINGS HAVE HAPPENED IF THIS HAPPENS!
			return "an_error_has_occurred";
		}
		int meta = itemStack.getItemDamage();
		if (meta < 0 || meta >= ConfigResGenUpgrades.INSTANCE.upgrades.size()) {
			meta = 0;
		}

		return "ftba." + ConfigResGenUpgrades.INSTANCE.upgrades.get(meta).name;
	}

	@Override
	public void getSubItems(CreativeTabs creativeTabs, NonNullList list) {
		if (ConfigResGenUpgrades.INSTANCE == null || ConfigResGenUpgrades.INSTANCE.upgrades == null) {
			//BAD THINGS HAVE HAPPENED IF THIS HAPPENS!
			return;
		}
		if (isInCreativeTab(creativeTabs)) {
			for (int meta = 0; meta < ConfigResGenUpgrades.INSTANCE.upgrades.size(); ++meta) {
				list.add(new ItemStack(this, 1, meta));
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack,
	                           @Nullable
		                           World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		ResGenUpgrade upgrade = ConfigResGenUpgrades.INSTANCE.upgrades.get(stack.getItemDamage());
		if (upgrade != null) {
			if (upgrade.speedModifier > 0) {
				tooltip.add(TextFormatting.YELLOW + "Speed modifier: " + TextFormatting.BLUE + "+" + Math.round(upgrade.speedModifier * 100) + "%");
			}
			if (upgrade.powerModifier > 0) {
				tooltip.add(TextFormatting.YELLOW + "Power modifier: " + TextFormatting.BLUE + "-" + Math.round(upgrade.powerModifier * 100) + "%");
			}
			if (upgrade.luck > 0) {
				tooltip.add(TextFormatting.YELLOW + "Luck modifier: " + TextFormatting.BLUE + "+" + Math.round(upgrade.luck) + "%");
			}
		}
	}
}
