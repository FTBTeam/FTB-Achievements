package me.modmuss50.ftba.files.worldData;

import me.modmuss50.ftba.util.TimerSaveDataFormat;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Mark on 05/02/2017.
 */
public class WorldFormat {

	public int chestX, chestY, chestZ, chestDimID;

	public int startX, startY, startZ, startDimID;

	public HashMap<Integer, List<BlockPos>> unbreakableBlocks;

	public List<String> triggedAchivements;

	public HashMap<String, Long> achivementTimes;

	TimerSaveDataFormat timerData;

	public HashMap<String, Integer> craftingProgress;

	public String players;

	public boolean invalid;

}
