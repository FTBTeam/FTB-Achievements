package me.modmuss50.ftba.modCompat.top;

import net.minecraftforge.fml.common.event.FMLInterModComms;

public class TopCompact {
	private static boolean registered;

	public static void register() {
		if (registered)
			return;
		registered = true;
		FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "me.modmuss50.ftba.modCompat.top.TopProbeGetter");
	}
}
