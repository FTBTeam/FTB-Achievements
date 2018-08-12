package me.modmuss50.ftba.proxy;

import me.modmuss50.ftba.FTBAchievements;
import me.modmuss50.ftba.client.GuiEvent;
import me.modmuss50.ftba.client.KeyBindings;
import me.modmuss50.ftba.client.gui.PlayerInventory;
import me.modmuss50.ftba.client.hud.ClientHudRenderer;
import me.modmuss50.ftba.config.ConfigResGenUpgrades;
import me.modmuss50.ftba.util.ClientWorldPlayerCountHandler;
import me.modmuss50.ftba.util.builder.IconSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import prospector.shootingstar.ShootingStar;

/**
 * Created by modmuss50 on 09/02/2017.
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		MinecraftForge.EVENT_BUS.register(new PlayerInventory());
		MinecraftForge.EVENT_BUS.register(new ClientHudRenderer());
		MinecraftForge.EVENT_BUS.register(KeyBindings.class);
		MinecraftForge.EVENT_BUS.register(GuiEvent.class);
		MinecraftForge.EVENT_BUS.register(new IconSupplier());
		KeyBindings.init();
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		for (int meta = 0; meta < ConfigResGenUpgrades.INSTANCE.upgrades.size(); ++meta) {
			String name = ConfigResGenUpgrades.INSTANCE.upgrades.get(meta).name;
			registerBlockstate(FTBAchievements.itemUpgrades, meta, name, "");
		}
		ShootingStar.registerModels("ftbachievements");
	}

	private static void registerBlockstate(Item i, int meta, String variant, String dir) {
		ResourceLocation loc = new ResourceLocation("ftbachievements", dir + i.getRegistryName().getResourcePath());
		ModelLoader.setCustomModelResourceLocation(i, meta, new ModelResourceLocation(loc, "type=" + variant));
	}

	@Override
	public void reloadAchivements() {
		super.reloadAchivements();
	}

	@Override
	public String getWorldSalt() {
		return Minecraft.getMinecraft().getSession().getProfile().getId().toString();
	}

	@Override
	public EntityPlayer getPlayer() {
		return Minecraft.getMinecraft().player;
	}

	@Override
	public void clientSideTask(Runnable runnable) {
		Minecraft.getMinecraft().addScheduledTask(runnable);
	}

	@Override
	public int getPlayerCount() {
		return ClientWorldPlayerCountHandler.getPlayerCount();
	}
}
