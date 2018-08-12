package me.modmuss50.ftba.events;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mark on 30/03/2017.
 */
public class WorldEvent {

	public static HashMap<Integer, List<ItemStack>> stacks;

	@SubscribeEvent
	public static void tick(TickEvent.WorldTickEvent event) {
		int dim = event.world.provider.getDimension();
		if (stacks.containsKey(dim)) {
			List<ItemStack> stackList = new ArrayList<>(stacks.get(dim));
			List<ItemStack> addedStacks = new ArrayList<>();
			for (ItemStack stack : stackList) {
				//TODO
				addedStacks.add(stack);
			}
			stackList.removeAll(addedStacks);
			if (stackList.isEmpty()) {
				stackList.remove(stacks.get(dim));
			} else {
				stacks.replace(dim, stackList);
			}
		}
	}
}
