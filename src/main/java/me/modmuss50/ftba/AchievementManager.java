package me.modmuss50.ftba;

import me.modmuss50.ftba.files.config.FTBAchievement;
import me.modmuss50.ftba.files.worldData.WorldFormat;
import me.modmuss50.ftba.packets.PacketSaveData;
import me.modmuss50.ftba.util.AchievementUser;
import me.modmuss50.ftba.util.TimerServerHandler;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import reborncore.common.network.NetworkManager;
import reborncore.common.util.InventoryHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

import static me.modmuss50.ftba.client.gui.achivementGui.AchievementButton.isValidBook;

/**
 * Created by Mark on 04/02/2017.
 */
public class AchievementManager {

	public static void processAchievement(FTBAchievement achievement, AchievementUser user) {
		if (achievement == null || user == null || user.world == null || user.world.isRemote) {
			return;
		}
		WorldFormat format = FTBAchievements.dataManager.getFormatServer();
		if (achievement.rewards != null && !achievement.rewards.isEmpty()) {
			BlockPos pos = new BlockPos(format.chestX, format.chestY, format.chestZ);
			World world = user.world.getMinecraftServer().getWorld(format.chestDimID);
			TileEntity tileEntity = world.getTileEntity(pos);
			if (!(tileEntity instanceof IInventory)) {
				world.getMinecraftServer().getPlayerList().sendMessage(new TextComponentString("Could not find reward chest @" + pos));
				return;
			}
		}
		if (achievement.fireOnce) {
			if (format != null) {
				if (format.triggedAchivements == null) {
					format.triggedAchivements = new ArrayList<>();
				}
				if (format.achivementTimes == null) {
					format.achivementTimes = new HashMap<>();
				}
				if (format.triggedAchivements.contains(achievement.name)) {
					return;
				} else {
					format.triggedAchivements.add(achievement.name);
					format.achivementTimes.put(achievement.name, TimerServerHandler.getTimeDifference());
					try {
						FTBAchievements.dataManager.save(user.world);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		if (achievement.rewards != null && user.world != null) {
			for (ItemStack stack : achievement.rewards) {
				BlockPos pos = new BlockPos(format.chestX, format.chestY, format.chestZ);
				TileEntity tileEntity = user.world.getTileEntity(pos);
				int count = 1;
				if (achievement.giveToAll) {
					for (EntityPlayer entityPlayer : user.world.getMinecraftServer().getPlayerList().getPlayers()) {
						if (!entityPlayer.isSpectator()) {
							count++;
						}
					}
				}
				if (count > 1) {
					count--;
				}
				for (int i = 0; i < count; i++) {
					if (tileEntity instanceof IInventory) {
						if (InventoryHelper.testInventoryInsertion((IInventory) tileEntity, stack.copy(), EnumFacing.DOWN) != 0) {
							InventoryHelper.insertItemIntoInventory((IInventory) tileEntity, stack.copy());
						} else {
							EntityItem entityItem = new EntityItem(tileEntity.getWorld(), tileEntity.getPos().getX() + 0.5, tileEntity.getPos().getY() + 1.5, tileEntity.getPos().getZ() + 0.5, stack.copy());
							entityItem.setNoDespawn();
							tileEntity.getWorld().spawnEntity(entityItem);
						}

					}
				}
			}
		}

		user.world.getMinecraftServer().getPlayerList().sendMessage(new TextComponentString(TextFormatting.YELLOW + user.name + TextFormatting.WHITE + " has unlocked " + TextFormatting.GREEN + getBookTitle(achievement.bookStack, achievement) + TextFormatting.WHITE + " in " + TextFormatting.BLUE + "[" + TimerServerHandler.getNiceTimeFromLong(TimerServerHandler.getTimeDifference()) + "]"));

		boolean hasIncompleteAchievement = false;
		for (FTBAchievement ftbAchievement : ConfigManager.getConfig().achievements) {
			if (!format.triggedAchivements.contains(ftbAchievement.name)) {
				hasIncompleteAchievement = true;
			}
		}
		if (!hasIncompleteAchievement) {
			TimerServerHandler.stop();
			TimerServerHandler.syncWithAll();
			for (EntityPlayer player : user.world.getMinecraftServer().getPlayerList().getPlayers()) {
				player.sendMessage(new TextComponentString("Well done! All achievements have been completed in " + TextFormatting.GREEN + TimerServerHandler.getNiceTimeFromLong(TimerServerHandler.getStoppedTime())));
				List<String> fireWorkData = new ArrayList<>();

				//Todo add some stuff for random fireworks each time cause why not?
				fireWorkData.add("{Fireworks:{Explosions:[{Type:1,Flicker:1,Trail:1,Colors:[I;4312372,15435844,14188952],FadeColors:[I;18073150,11250603]}]}}");
				fireWorkData.add("{Fireworks:{Explosions:[{Type:1,Flicker:1,Trail:1,Colors:[I;11743532,15435844,14188952],FadeColors:[I;4408131,11250603]}]}}");
				fireWorkData.add("{Fireworks:{Explosions:[{Type:1,Flicker:1,Trail:1,Colors:[I;8073150,15435844,14188952],FadeColors:[I;14408131,11743532]}]}}");
				int sapwnRaduis = 8;
				for (String data : fireWorkData) {
					ItemStack stack = new ItemStack(Items.FIREWORKS);
					try {
						stack.setTagCompound(JsonToNBT.getTagFromJson(data));
						EntityFireworkRocket entityfireworkrocket = new EntityFireworkRocket(player.world, player.posX + player.world.rand.nextInt(sapwnRaduis) - sapwnRaduis / 2, player.posY, player.posZ + player.world.rand.nextInt(sapwnRaduis) - sapwnRaduis / 2, stack);
						player.world.spawnEntity(entityfireworkrocket);
					} catch (NBTException e) {
						e.printStackTrace();
					}
				}
				String playerData = format.players;
				if (playerData == null || playerData.isEmpty()) {
					StringJoiner joiner = new StringJoiner(",");
					for (EntityPlayer entityPlayer : user.world.getMinecraftServer().getPlayerList().getPlayers()) {
						if (!entityPlayer.isSpectator()) {
							joiner.add(entityPlayer.getDisplayNameString() + "#" + entityPlayer.getGameProfile().getId().toString());
						}
					}
					playerData = joiner.toString();
				}
				NetworkManager.sendToPlayer(new PacketSaveData(ConfigManager.TINY_GSON.toJson(FTBAchievements.dataManager.getFormatServer()), TimerServerHandler.getStoppedTime(), playerData, FTBAchievements.dataManager.getWorldHash()), (EntityPlayerMP) player);
			}
		}
		FTBAchievements.proxy.syncWithClients();
	}

	public static String getBookTitle(ItemStack stack, FTBAchievement ftbAchievement) {
		if (isValidBook(stack)) {
			NBTTagCompound nbttagcompound = stack.getTagCompound();
			String s = nbttagcompound.getString("title");

			if (!StringUtils.isNullOrEmpty(s)) {
				return s;
			}
		}
		return ftbAchievement.name;
	}
}
