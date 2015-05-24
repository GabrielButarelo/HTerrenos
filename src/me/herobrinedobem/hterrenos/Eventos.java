package me.herobrinedobem.hterrenos;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class Eventos implements Listener{

	private HTerrenos instance = HTerrenos.getHTerrenos();
	
	@EventHandler
	private void onPlayerInteractSignEvent(PlayerInteractEvent e){
		Player p = e.getPlayer();
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK && (e.getClickedBlock().getType() == Material.WALL_SIGN || e.getClickedBlock().getType() == Material.SIGN_POST)){
			Sign s = (Sign) e.getClickedBlock();
			if(!s.getLine(0).isEmpty() && s.getLine(1).equals("Vendendo") && s.getLine(2).equals("Preco") && !s.getLine(3).isEmpty()){
				RegionManager regionManager = instance.worldGuard.getRegionManager(p.getWorld());
					ProtectedRegion region = regionManager.getRegion(areaName.toLowerCase());
					region.getMembers().addPlayer(amigo.getName().toLowerCase());
			}
		}
	}
	
}
