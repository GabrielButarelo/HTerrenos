package me.herobrinedobem.hterrenos;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class HTerrenos extends JavaPlugin{

	public WorldGuardPlugin worldGuard;
	public WorldEditPlugin worldEdit;
	public TerrenoUtils terrenoUtils;
	
	@Override
	public void onEnable() {
		worldEdit = getWorldEdit();
		worldGuard = getWorldGuard();
		terrenoUtils = new TerrenoUtils();
		/*if(!new File(getDataFolder(), "config.yml").exists()){
			saveDefaultConfig();
		}*/
		getCommand("terreno").setExecutor(new Comandos());
		System.out.println("[HTerrenos] Plugin Habilitado | By Herobrinedobem");
	}

	@Override
	public void onDisable() {
		System.out.println("[HTerrenos] Plugin Desabilitado | By Herobrinedobem");
	}

	public static HTerrenos getHTerrenos(){
		return (HTerrenos) Bukkit.getServer().getPluginManager().getPlugin("HTerrenos");
	}
	
	private WorldGuardPlugin getWorldGuard() {
	    Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
	    if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
	        return null;
	    }
	    return (WorldGuardPlugin) plugin;
	}

	private WorldEditPlugin getWorldEdit() {
		Plugin plugin = getServer().getPluginManager().getPlugin("WorldEdit");
		if (plugin == null || !(plugin instanceof WorldEditPlugin)) {
			return null;
		}
		return (WorldEditPlugin) plugin;
	}
	
}
