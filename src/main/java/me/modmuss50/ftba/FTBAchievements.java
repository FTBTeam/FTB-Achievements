package me.modmuss50.ftba;

import me.modmuss50.ftba.blocks.crafting.BlockPoweredCraftingTable;
import me.modmuss50.ftba.blocks.crafting.TilePoweredCraftingTable;
import me.modmuss50.ftba.blocks.heater.BlockHeater;
import me.modmuss50.ftba.blocks.heater.TileHeater;
import me.modmuss50.ftba.blocks.input.BlockInput;
import me.modmuss50.ftba.blocks.input.ItemBlockInput;
import me.modmuss50.ftba.blocks.input.TileInput;
import me.modmuss50.ftba.blocks.resGen.BlockResGen;
import me.modmuss50.ftba.blocks.resGen.TileResGen;
import me.modmuss50.ftba.client.GuiHandler;
import me.modmuss50.ftba.client.hud.ClientHintRender;
import me.modmuss50.ftba.commands.FTBACommand;
import me.modmuss50.ftba.config.ConfigFeatures;
import me.modmuss50.ftba.config.ConfigResourceGen;
import me.modmuss50.ftba.config.NewConfigManager;
import me.modmuss50.ftba.events.BlockEvent;
import me.modmuss50.ftba.events.CraftingEvent;
import me.modmuss50.ftba.events.PlayerLoginEvent;
import me.modmuss50.ftba.files.worldData.WorldDataManager;
import me.modmuss50.ftba.items.ItemUpgrades;
import me.modmuss50.ftba.modCompat.ModCompat;
import me.modmuss50.ftba.packets.*;
import me.modmuss50.ftba.proxy.CommonProxy;
import me.modmuss50.ftba.team.TeamScriptManager;
import me.modmuss50.ftba.util.FTBTeamUtil;
import me.modmuss50.ftba.util.FarmLandFix;
import me.modmuss50.ftba.util.TimerServerHandler;
import me.modmuss50.ftba.util.WorldPlayerCountHandler;
import me.modmuss50.ftba.util.recipes.PoweredTierRecipe;
import me.modmuss50.ftba.util.recipes.RecipeJsonLoader;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.GameData;
import reborncore.RebornRegistry;
import reborncore.common.network.RegisterPacketEvent;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@Mod(name = "FTBAchievements", modid = "ftbachievements", version = "1.0.0", certificateFingerprint = "8727a3141c8ec7f173b87aa78b9b9807867c4e6b", dependencies = "required-after:reborncore")
public class FTBAchievements {

	public static BlockInput blockInput;
	public static BlockResGen blockResGen;
	public static BlockPoweredCraftingTable poweredCraftingTable;
	public static BlockHeater blockHeater;
	public static ItemUpgrades itemUpgrades;
	public static WorldDataManager dataManager;
	@Mod.Instance("ftbachievements")
	public static FTBAchievements INSTANCE;
	@SidedProxy(clientSide = "me.modmuss50.ftba.proxy.ClientProxy", serverSide = "me.modmuss50.ftba.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static File CONFIG_DIR;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) throws IOException {
		CONFIG_DIR = new File(event.getSuggestedConfigurationFile().getParent(), "ftbachievements");
		if (!CONFIG_DIR.exists()) {
			CONFIG_DIR.mkdir();
		}
		NewConfigManager.loadAll();
		ConfigManager.configFile = new File(CONFIG_DIR, "ftbachievements.json");
		ConfigResourceGen.configFile = new File(CONFIG_DIR, "resgendata.nbt");
		ClientHintRender.mcFile = CONFIG_DIR.getParentFile().getParentFile();
		if (ConfigFeatures.INSTANCE.TeamSelection) {
			TeamScriptManager.init();
		}

//		blockInput = new BlockInput();
//		registerBlock(blockInput, ItemBlockInput.class, new ResourceLocation("ftbachievements", "input"));
//		GameRegistry.registerTileEntity(TileInput.class, "ftbachievements.input");

		blockResGen = new BlockResGen();
		RebornRegistry.registerBlock(blockResGen, new ResourceLocation("ftbachievements", "resgen"));
		GameRegistry.registerTileEntity(TileResGen.class, "ftbachievements.resgen");

		blockHeater = new BlockHeater();
		RebornRegistry.registerBlock(blockHeater, new ResourceLocation("ftbachievements", "heater"));
		GameRegistry.registerTileEntity(TileHeater.class, "ftbachievements.heater");

		itemUpgrades = new ItemUpgrades();
		RebornRegistry.registerItem(itemUpgrades, new ResourceLocation("ftbachievements", "upgrades"));

		poweredCraftingTable = new BlockPoweredCraftingTable();
		RebornRegistry.registerBlock(poweredCraftingTable, new ResourceLocation("ftbachievements", "poweredcraftingtable"));
		GameRegistry.registerTileEntity(TilePoweredCraftingTable.class, "ftbachievements.poweredcraftingtable");

		dataManager = new WorldDataManager();
		FMLCommonHandler.instance().bus().register(dataManager);
		FMLCommonHandler.instance().bus().register(BlockEvent.class);
		NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
		FMLCommonHandler.instance().bus().register(this);
		FMLCommonHandler.instance().bus().register(FTBTeamUtil.class);
		FMLCommonHandler.instance().bus().register(PoweredTierRecipe.class);
		FMLCommonHandler.instance().bus().register(WorldPlayerCountHandler.class);
		proxy.preInit(event);
		//PoweredCraftingManager.testRecipes();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		FMLCommonHandler.instance().bus().register(new CraftingEvent());
		FMLCommonHandler.instance().bus().register(new ModCompat());
		FMLCommonHandler.instance().bus().register(PlayerLoginEvent.class);
		proxy.init(event);
		TimerServerHandler.reset(); //Sets this to 0 and what not
		try {
			FarmLandFix.fix();
		} catch (IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	@Mod.EventHandler
	public void load(FMLPostInitializationEvent event) throws IOException {
		ConfigManager.load();
		ConfigResourceGen.load();
		RecipeJsonLoader.load(CONFIG_DIR);
	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new FTBACommand());
	}

	public static void registerBlock(Block block, Class<? extends ItemBlock> itemclass, ResourceLocation name) {
		block.setRegistryName(name);
		GameData.register_impl(block);

		try {
			ItemBlock e = itemclass.getConstructor(new Class[] { Block.class }).newInstance(block);
			e.setRegistryName(name);
			GameData.register_impl(e);
		} catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException var4) {
			var4.printStackTrace();
		}

	}

	@SubscribeEvent
	public void loadPacket(RegisterPacketEvent event) {
		event.registerPacket(PacketSendData.class, Side.CLIENT);
		event.registerPacket(PacketGuiTrigger.class, Side.SERVER);
		event.registerPacket(PacketSendTimerData.class, Side.CLIENT);
		event.registerPacket(PacketSaveData.class, Side.CLIENT);
		event.registerPacket(PacketSendWorldData.class, Side.CLIENT);
		event.registerPacket(PacketAchievementProgress.class, Side.CLIENT);
		event.registerPacket(PacketAchievementProgressReset.class, Side.CLIENT);
		event.registerPacket(PacketPlayerJoinTeam.class, Side.SERVER);
		event.registerPacket(PacketSyncFTBTeam.class, Side.CLIENT);
		event.registerPacket(PacketSendJEIRecipeData.class, Side.CLIENT);
		event.registerPacket(PacketSyncPlayerCount.class, Side.CLIENT);
	}

	@Mod.EventHandler
	public void onFingerprintViolation(FMLFingerprintViolationEvent event) {
		FMLLog.warning("Invalid fingerprint detected for FTBAchievements!");
	}

	public static class Tab extends CreativeTabs {

		public static Tab FTBA_TAB = new Tab();

		public Tab() {
			super("ftbachievements");
		}

		@Nonnull
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(blockResGen);
		}
	}

}
