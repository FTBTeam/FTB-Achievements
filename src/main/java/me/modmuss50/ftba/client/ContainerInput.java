package me.modmuss50.ftba.client;

import me.modmuss50.ftba.blocks.input.TileInput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.container.RebornContainer;

/**
 * Created by Mark on 05/02/2017.
 */
public class ContainerInput extends RebornContainer {
	TileInput input;

	int power;
	int requirement;

	public ContainerInput(TileInput input) {
		this.input = input;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (int i = 0; i < this.listeners.size(); i++) {
			IContainerListener IContainerListener = this.listeners.get(i);
			if (input.storage.getEnergyStored() != power) {
				IContainerListener.sendWindowProperty(this, 0, input.storage.getEnergyStored());
			}
			if (input.trigger.requirement != requirement) {
				IContainerListener.sendWindowProperty(this, 1, input.trigger.requirement);
			}
		}
	}

	@Override
	public void addListener(IContainerListener crafting) {
		super.addListener(crafting);
		crafting.sendWindowProperty(this, 0, input.storage.getEnergyStored());
		crafting.sendWindowProperty(this, 1, input.trigger.requirement);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int value) {
		if (id == 0) {
			this.power = value;
		} else if (id == 1) {
			this.requirement = value;
		}
	}
}
