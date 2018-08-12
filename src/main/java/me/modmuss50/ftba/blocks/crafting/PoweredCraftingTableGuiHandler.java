package me.modmuss50.ftba.blocks.crafting;

import me.modmuss50.ftba.FTBAchievements;
import me.modmuss50.ftba.client.GuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.api.tile.IMachineGuiHandler;

public class PoweredCraftingTableGuiHandler implements IMachineGuiHandler {
	@Override
	public void open(EntityPlayer entityPlayer, BlockPos blockPos, World world) {
		if (!world.isRemote) {
			entityPlayer.openGui(FTBAchievements.INSTANCE, GuiHandler.craftingID, entityPlayer.world, blockPos.getX(), blockPos.getY(), blockPos.getZ());
		}
	}
}
