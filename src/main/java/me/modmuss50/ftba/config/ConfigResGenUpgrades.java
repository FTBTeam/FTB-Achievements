package me.modmuss50.ftba.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mark on 17/07/2017.
 */
public class ConfigResGenUpgrades {

	public static ConfigResGenUpgrades INSTANCE;

	public List<ResGenUpgrade> upgrades;

	public static ConfigResGenUpgrades getDefault() {
		ConfigResGenUpgrades configResGenUpgrades = new ConfigResGenUpgrades();
		configResGenUpgrades.upgrades = new ArrayList<>();
		createUpgrade(configResGenUpgrades.upgrades, "speedmk1", 0.1F, 0.1F, 0);
		createUpgrade(configResGenUpgrades.upgrades, "speedmk2", 0.2F, 0.2F, 0);
		createUpgrade(configResGenUpgrades.upgrades, "speedmk3", 0.3F, 0.3F, 0);
		createUpgrade(configResGenUpgrades.upgrades, "speedmk4", 0.4F, 0.4F, 0);
		createUpgrade(configResGenUpgrades.upgrades, "speedmk5", 0.5F, 0.5F, 0);
		createUpgrade(configResGenUpgrades.upgrades, "powermk1", 0.0F, -0.1F, 0);
		createUpgrade(configResGenUpgrades.upgrades, "powermk2", 0.0F, -0.2F, 0);
		createUpgrade(configResGenUpgrades.upgrades, "powermk3", 0.0F, -0.3F, 0);
		createUpgrade(configResGenUpgrades.upgrades, "powermk4", 0.0F, -0.4F, 0);
		createUpgrade(configResGenUpgrades.upgrades, "powermk5", 0.0F, -0.5F, 0);
		createUpgrade(configResGenUpgrades.upgrades, "luckmk1", 0.0F, 0.1F, 20);
		createUpgrade(configResGenUpgrades.upgrades, "luckmk2", 0.0F, 0.3F, 45);
		createUpgrade(configResGenUpgrades.upgrades, "luckmk3", 0.0F, 0.5F, 80);

		return configResGenUpgrades;
	}

	private static void createUpgrade(List<ResGenUpgrade> list, String name, float speed, float power, int luck) {
		ResGenUpgrade uprgade = new ResGenUpgrade();
		uprgade.name = name;
		uprgade.speedModifier = speed;
		uprgade.powerModifier = power;
		uprgade.luck = luck;
		list.add(uprgade);
	}

}
