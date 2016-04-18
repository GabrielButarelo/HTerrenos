package me.herobrinedobem.hterrenos;

import java.io.File;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.economy.Economy;

public class HTerrenos extends JavaPlugin {

	private GUIManager guiManager;
	private TerrenoManager terrenoManager;
	private Utils utils;
	private Economy economy;
	private WorldGuardPlugin worldGuard;
	private WorldEditPlugin worldEdit;

	@Override
	public void onEnable() {
		if (!new File(this.getDataFolder(), "config.yml").exists()) {
			this.saveDefaultConfig();
		}
		this.setupEconomy();
		this.worldEdit = this.getWorldEdita();
		this.worldGuard = this.getWorldGuarda();
		this.utils = new Utils();
		this.guiManager = new GUIManager();
		this.terrenoManager = new TerrenoManager();
		this.getCommand("terreno").setExecutor(new Comandos());
		this.getServer().getPluginManager().registerEvents(new Eventos(), this);
		System.out.println("[HTerrenos] Plugin Habilitado - Versao (" + this.getDescription().getVersion() + ")");
	}

	@Override
	public void onDisable() {
		System.out.println("[HTerrenos] Plugin Habilitado - Versao (" + this.getDescription().getVersion() + ")");
	}

	public static HTerrenos getHTerrenos() {
		return (HTerrenos) Bukkit.getServer().getPluginManager().getPlugin("HTerrenos");
	}

	private boolean setupEconomy() {
		final RegisteredServiceProvider<Economy> economyProvider = this.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			this.economy = economyProvider.getProvider();
		}

		return (this.economy != null);
	}

	private WorldGuardPlugin getWorldGuarda() {
		final Plugin plugin = this.getServer().getPluginManager().getPlugin("WorldGuard");
		if ((plugin == null) || !(plugin instanceof WorldGuardPlugin)) {
			return null;
		}
		return (WorldGuardPlugin) plugin;
	}

	private WorldEditPlugin getWorldEdita() {
		final Plugin plugin = this.getServer().getPluginManager().getPlugin("WorldEdit");
		if ((plugin == null) || !(plugin instanceof WorldGuardPlugin)) {
			return null;
		}
		return (WorldEditPlugin) plugin;
	}

	public Economy getEconomy() {
		return this.economy;
	}

	public WorldEditPlugin getWorldEdit() {
		return this.worldEdit;
	}

	public WorldGuardPlugin getWorldGuard() {
		return this.worldGuard;
	}

	public Utils getUtils() {
		return this.utils;
	}

	public TerrenoManager getTerrenoManager() {
		return this.terrenoManager;
	}

	public GUIManager getGuiManager() {
		return this.guiManager;
	}

}
