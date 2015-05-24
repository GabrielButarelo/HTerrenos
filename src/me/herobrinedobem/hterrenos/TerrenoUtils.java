package me.herobrinedobem.hterrenos;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class TerrenoUtils {

	private HTerrenos instance = HTerrenos.getHTerrenos();
	
	public void createFence(Player p, int tamanho){
		try{
			Location last = new Location(p.getLocation().getWorld(), p.getLocation().getX() + tamanho, p.getLocation().getY(), p.getLocation().getZ() + tamanho);
			Cuboid cubo = new Cuboid(p.getLocation(), last);
			for(Block b : cubo.getBlocks()){
				if(b.getType() == Material.AIR){
					b.setType(Material.FENCE);
				}
			}
			Location p1 = new Location(p.getLocation().getWorld(), p.getLocation().getX() + 1, p.getLocation().getY(), p.getLocation().getZ() + 1);
			Location p2 = new Location(p.getLocation().getWorld(), p.getLocation().getX() + tamanho - 1, p.getLocation().getY(), p.getLocation().getZ() + tamanho - 1);
			Cuboid cubo2 = new Cuboid(p1, p2);
			for(Block b : cubo2.getBlocks()){
				if(b.getType() == Material.FENCE){
					b.setType(Material.AIR);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void buyTerrain(Player p, String area, int tamanho){
		try{
			Location last = new Location(p.getLocation().getWorld(), p.getLocation().getX() + tamanho, p.getLocation().getY(), p.getLocation().getZ() + tamanho);
			Cuboid cubo = new Cuboid(p.getLocation(), last);
			RegionManager regionManager = instance.worldGuard.getRegionManager(p.getWorld());
			if(terrainExist(p.getName() + "_" + area, p.getWorld().getName())){
				p.sendMessage("ß6[Terreno] ßeJa existe um terreno com esse nome, tente outro.");
				return;
			}else{
				BlockVector bv1 = new BlockVector(p.getLocation().getX(), 0, p.getLocation().getZ());
			    BlockVector bv2 = new BlockVector(cubo.getUpperX(), 360, + cubo.getUpperZ());
			    ProtectedCuboidRegion pr = new ProtectedCuboidRegion(p.getName().toLowerCase() + "_" + area.toLowerCase(), bv1, bv2);
			    DefaultDomain dd = new DefaultDomain();
			    regionManager.addRegion(pr);
			    pr.setPriority(100);
			    dd.addPlayer(p.getName());
			    pr.setOwners(dd);
			    
			    ApplicableRegionSet regions2 = regionManager.getApplicableRegions(pr);
			    LocalPlayer localPlayer2 = instance.worldGuard.wrapPlayer(p);
			    if(!regions2.isOwnerOfAll(localPlayer2)){
			    	p.sendMessage("ß6[Terreno] ßeExiste um terreno perto, va mais longe.");
			    	regionManager.removeRegion(p.getName() + "_" + area.toLowerCase());
			    	return;
			    }else{
			    	 pr.setFlag(DefaultFlag.PVP, DefaultFlag.PVP.parseInput(instance.worldGuard, p, "allow"));
			         pr.setFlag(DefaultFlag.USE, DefaultFlag.USE.parseInput(instance.worldGuard, p, "deny"));
			         pr.setFlag(DefaultFlag.ENDER_BUILD, DefaultFlag.ENDER_BUILD.parseInput(instance.worldGuard, p, "deny"));
			         pr.setFlag(DefaultFlag.CREEPER_EXPLOSION, DefaultFlag.CREEPER_EXPLOSION.parseInput(instance.worldGuard, p, "deny"));
			         regionManager.save();
			         createFence(p, tamanho);
			         p.sendMessage("ß6[Terreno] ßeTerreno comprado com sucesso.");
			    }
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void renameTerrain(Player p, String area, String newArea){
		try{
			RegionManager regionManager = instance.worldGuard.getRegionManager(p.getWorld());
			if(regionManager.hasRegion(area.toLowerCase())){
				ProtectedRegion region = regionManager.getRegion(area.toLowerCase());
				Location min = new Location(p.getLocation().getWorld(), region.getMinimumPoint().getBlockX(), region.getMinimumPoint().getBlockY(), region.getMinimumPoint().getBlockZ());
				Location max = new Location(p.getLocation().getWorld(), region.getMaximumPoint().getBlockX(), region.getMaximumPoint().getBlockY(), region.getMaximumPoint().getBlockZ());
				Cuboid cubo = new Cuboid(min, max);
				if(!terrainExist(newArea.toLowerCase(), p.getWorld().getName())){
					regionManager.removeRegion(area);
					BlockVector bv1 = new BlockVector(cubo.getLowerX(), cubo.getLowerY(), cubo.getLowerZ());
					BlockVector bv2 = new BlockVector(cubo.getUpperX(), cubo.getUpperY(), + cubo.getUpperZ());
					ProtectedCuboidRegion pr = new ProtectedCuboidRegion(newArea.toLowerCase(), bv1, bv2);
					DefaultDomain dd = new DefaultDomain();
					regionManager.addRegion(pr);
					pr.setPriority(100);
					dd.addPlayer(p.getName());
					pr.setOwners(dd);
					pr.setFlag(DefaultFlag.PVP, DefaultFlag.PVP.parseInput(instance.worldGuard, p, "allow"));
					pr.setFlag(DefaultFlag.USE, DefaultFlag.USE.parseInput(instance.worldGuard, p, "deny"));
					pr.setFlag(DefaultFlag.ENDER_BUILD, DefaultFlag.ENDER_BUILD.parseInput(instance.worldGuard, p, "deny"));
					pr.setFlag(DefaultFlag.CREEPER_EXPLOSION, DefaultFlag.CREEPER_EXPLOSION.parseInput(instance.worldGuard, p, "deny"));
					regionManager.save();
					p.sendMessage("ß6[Terreno] ßeTerreno renomeado com sucesso!");
					return;
				}else{
					p.sendMessage("ß6[Terreno] ßeJa existe um terreno com esse nome, tente outro.");
					return;
				}
			}else{
				p.sendMessage("ß6[Terreno] ße¡rea nao encontrada.");
				return;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("rawtypes")
	public boolean terrainExist(String area, String mundo){
	    RegionManager terreno = instance.worldGuard.getGlobalRegionManager().get(instance.getServer().getWorld(mundo));
	    Map regions = terreno.getRegions();
	    Object[] set = regions.keySet().toArray();
	    for (Object id : set){
	      if (id.toString().equalsIgnoreCase(area.toLowerCase())){
	        return true;
	      }
	    }
	    return false;
	}
	
	public void addFriend(Player p, Player amigo, String areaName){
		RegionManager regionManager = instance.worldGuard.getRegionManager(p.getWorld());
		if(regionManager.hasRegion(areaName.toLowerCase())){
			ProtectedRegion region = regionManager.getRegion(areaName.toLowerCase());
			if(region.getId().contains(p.getName().toLowerCase())){
				region.getMembers().addPlayer(amigo.getName().toLowerCase());
				try {
					regionManager.save();
				} catch (ProtectionDatabaseException e) {
					e.printStackTrace();
				}
				p.sendMessage("ß6[Terreno] ßeAmigo adicionado com sucesso ao seu terreno.");
			}else{
				p.sendMessage("ß6[Terreno] ßeEssa area nao È sua.");
			}
		}else{
			p.sendMessage("ß6[Terreno] ßeArea nao encontrada.");
		}
	}

	public void removeFriend(Player p, String amigo, String areaName){
		RegionManager regionManager = instance.worldGuard.getRegionManager(p.getWorld());
		if(regionManager.hasRegion(areaName.toLowerCase())){
			ProtectedRegion region = regionManager.getRegion(areaName.toLowerCase());
			if(region.getId().contains(p.getName().toLowerCase())){
				if(region.getMembers().contains(amigo)){
					region.getMembers().removePlayer(amigo.toLowerCase());
					try {
						regionManager.save();
					} catch (ProtectionDatabaseException e) {
						e.printStackTrace();
					}
					p.sendMessage("ß6[Terreno] ßeAmigo removido com sucesso do seu terreno.");
				}else{
					p.sendMessage("ß6[Terreno] ßeEsse jogador nao esta adicionado na sua area.");
				}
			}else{
				p.sendMessage("ß6[Terreno] ßeEssa area nao È sua.");
			}
		}else{
			p.sendMessage("ß6[Terreno] ßeArea nao encontrada.");
		}
	}

	public void setPVPEnable(Player p, String pvpEnable){
		RegionManager regionManager = instance.worldGuard.getRegionManager(p.getWorld());
		ApplicableRegionSet set = regionManager.getApplicableRegions(p.getLocation());
		if(set.size() == 0){
	        p.sendMessage("ß6[Terreno] ßeArea nao encontrada.");
	        return;
	    }
		set.toString().toLowerCase();
		String id = ((ProtectedRegion)set.iterator().next()).getId();
        ProtectedRegion region = regionManager.getRegion(id);
		if(region.getId().contains(p.getName().toLowerCase())){
			try {
				State a = region.getFlag(DefaultFlag.PVP);
				if(a == State.ALLOW && pvpEnable.equals("allow")){
					p.sendMessage("ß6[Terreno] ßeO PVP ja esta ativado nesse terreno.");
					return;
				}else if(a == State.DENY && pvpEnable.equals("deny")){
					p.sendMessage("ß6[Terreno] ßeO PVP ja esta desativado nesse terreno.");
					return;
				}
				region.setFlag(DefaultFlag.PVP, DefaultFlag.PVP.parseInput(instance.worldGuard, p, pvpEnable));
				regionManager.save();
			} catch (ProtectionDatabaseException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(pvpEnable.equals("deny")){
				p.sendMessage("ß6[Terreno] ßeO PVP foi desativado no seu terreno.");
			}else if(pvpEnable.equals("allow")){
				p.sendMessage("ß6[Terreno] ßeO PVP foi ativado no seu terreno.");
			}
		}else{
			p.sendMessage("ß6[Terreno] ßeEssa area nao È sua.");
		}
	}

	public void setEnterMessage(Player p, String mensagem){
		RegionManager regionManager = instance.worldGuard.getRegionManager(p.getWorld());
		ApplicableRegionSet set = regionManager.getApplicableRegions(p.getLocation());
		if(set.size() == 0){
			p.sendMessage("ß6[Terreno] ßeArea nao encontrada.");
			return;
		}
		set.toString().toLowerCase();
		String id = ((ProtectedRegion)set.iterator().next()).getId();
		ProtectedRegion region = regionManager.getRegion(id);
		if(region.getId().contains(p.getName().toLowerCase())){
			try {
				region.setFlag(DefaultFlag.GREET_MESSAGE, DefaultFlag.GREET_MESSAGE.parseInput(instance.worldGuard, p, ChatColor.DARK_AQUA + mensagem));
				regionManager.save();
				p.sendMessage("ß6[Terreno] ßeMensagem de boas vindas da sua area atualizada.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			p.sendMessage("ß6[Terreno] ßeEssa area nao È sua.");
		}
	}

	public void setDisabledCommands(Player p, String mensagem){
		RegionManager regionManager = instance.worldGuard.getRegionManager(p.getWorld());
		ApplicableRegionSet set = regionManager.getApplicableRegions(p.getLocation());
		if(set.size() == 0){
			p.sendMessage("ß6[Terreno] ßeArea nao encontrada.");
			return;
		}
		set.toString().toLowerCase();
		String id = ((ProtectedRegion)set.iterator().next()).getId();
		ProtectedRegion region = regionManager.getRegion(id);
		if(region.getId().contains(p.getName().toLowerCase())){
			try {
				region.setFlag(DefaultFlag.BLOCKED_CMDS, DefaultFlag.BLOCKED_CMDS.parseInput(instance.worldGuard, p, mensagem));
				regionManager.save();
				p.sendMessage("ß6[Terreno] ßeComando bloqueado com sucesso.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			p.sendMessage("ß6[Terreno] ßeEssa area nao È sua.");
		}
	}

	public void deleteTerrain(Player p, String areaName){
		RegionManager regionManager = instance.worldGuard.getRegionManager(p.getWorld());
		if(regionManager.hasRegion(areaName.toLowerCase())){
			ProtectedRegion region = regionManager.getRegion(areaName.toLowerCase());
			if(region.getId().contains(p.getName().toLowerCase())){
				try {
					regionManager.removeRegion(areaName.toLowerCase());
					regionManager.save();
				} catch (ProtectionDatabaseException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				p.sendMessage("ß6[Terreno] ßeTerreno deletado com sucesso.");
			}else{
				p.sendMessage("ß6[Terreno] ßeEssa area nao È sua.");
			}
		}else{
			p.sendMessage("ß6[Terreno] ßearea nao encontrada.");
		}
	}
	
	public void tpArea(Player p, String areaName){
		RegionManager regionManager = instance.worldGuard.getRegionManager(p.getWorld());
		if(regionManager.hasRegion(areaName.toLowerCase())){
			ProtectedRegion region = regionManager.getRegion(areaName.toLowerCase());
			if(region.getId().contains(p.getName().toLowerCase())){
				try {
					p.teleport(new Location(p.getWorld(), region.getMaximumPoint().getX(), p.getWorld().getHighestBlockYAt(region.getMaximumPoint().getBlockX(), region.getMaximumPoint().getBlockZ()), region.getMaximumPoint().getZ()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				p.sendMessage("ß6[Terreno] ßeTeletransportado para o seu terreno.");
			}else{
				p.sendMessage("ß6[Terreno] ßeEssa area nao È sua.");
			}
		}else{
			p.sendMessage("ß6[Terreno] ßeArea nao encontrada.");
		}
	}
	
	public String getAreas(Player p){
		Map<String, ProtectedRegion> mgr = instance.worldGuard.getGlobalRegionManager().get(p.getWorld()).getRegions();
		StringBuilder builder = new StringBuilder();
		for(ProtectedRegion region : mgr.values()) {
		    if(region.isOwner(p.getName())) {
		    	builder.append("ß6" + replaceAreaInPlayerName(region.getId(), p.getName()) + "ße, ");
		    }
		}
		return builder.toString();
	}
	
	public int getAreasNumber(Player p){
		int i = 0;
		Map<String, ProtectedRegion> mgr = instance.worldGuard.getGlobalRegionManager().get(p.getWorld()).getRegions();
		for(ProtectedRegion region : mgr.values()) {
		    if(region.isOwner(p.getName())) {
		    	i++;
		    }
		}
		return i;
	}
	
	public String replaceAreaInAreaName(String area, String playerName){
		if(playerName.endsWith("_")){
			String s = area.split("__")[1];
			return s + "_";
		}else if(playerName.endsWith("__")){
			String s = area.split("___")[1];
			return s + "__";
		}else if(playerName.endsWith("___")){
			String s = area.split("____")[1];
			return s + "___";
		}else if(playerName.endsWith("____")){
			String s = area.split("_____")[1];
			return s + "____";
		}else if(playerName.endsWith("_____")){
			String s = area.split("______")[1];
			return s + "_____";
		}else{
			return playerName;
		}
	}
	
	public String replaceAreaInPlayerName(String area, String playerName){
		if(playerName.endsWith("_")){
			String s = area.split("__")[0];
			return s + "_";
		}else if(playerName.endsWith("__")){
			String s = area.split("___")[0];
			return s + "__";
		}else if(playerName.endsWith("___")){
			String s = area.split("____")[0];
			return s + "___";
		}else if(playerName.endsWith("____")){
			String s = area.split("_____")[0];
			return s + "____";
		}else if(playerName.endsWith("_____")){
			String s = area.split("______")[0];
			return s + "_____";
		}else{
			return playerName;
		}
	}
	
	public void sellTerrain(Player p, int quantia){
		RegionManager regionManager = instance.worldGuard.getRegionManager(p.getWorld());
		ApplicableRegionSet set = regionManager.getApplicableRegions(p.getLocation());
		if(set.size() == 0){
	        p.sendMessage("ß6[Terreno] ßeArea nao encontrada.");
	        return;
	    }
		set.toString().toLowerCase();
		String id = ((ProtectedRegion)set.iterator().next()).getId();
        ProtectedRegion region = regionManager.getRegion(id);
		if(region.getId().contains(p.getName().toLowerCase())){
			Location l = new Location(p.getLocation().getWorld(), region.getMinimumPoint().getX(), region.getMinimumPoint().getY(), region.getMinimumPoint().getZ());
			if(instance.getServer().getWorld(p.getLocation().getWorld().getName()).getHighestBlockAt(l).getType() == Material.SIGN_POST || instance.getServer().getWorld(p.getLocation().getWorld().getName()).getHighestBlockAt(l).getType() == Material.WALL_SIGN){
				Sign s = (Sign)instance.getServer().getWorld(p.getLocation().getWorld().getName()).getHighestBlockAt(l);
				if(s.getLine(0).equals(p.getName()) && s.getLine(1).equals("Vendendo") && s.getLine(2).equals("Preco") && !s.getLine(3).isEmpty()){
					p.sendMessage("ß6[Terreno] ßeEsse terreno ja esta sendo vendido.");
					return;
				}
			}else{
				instance.getServer().getWorld(p.getLocation().getWorld().getName()).getHighestBlockAt(l).setType(Material.SIGN_POST);
				Sign s = (Sign)instance.getServer().getWorld(p.getLocation().getWorld().getName()).getHighestBlockAt(l);
				s.setLine(0, p.getName());
				s.setLine(1, "ßcVendendo");
				s.setLine(3, "ßcPreco");
				s.setLine(4, String.valueOf(quantia));
				p.sendMessage("ß6[Terreno] ßeTerreno colocado ‡ venda com sucesso.");
				instance.getServer().broadcastMessage("   ");
				instance.getServer().broadcastMessage("ß6[Terreno] ßeO jogador ß6" + p.getName() + " ßecolocou seu terreno ß6" + region.getId() + " ße‡ venda.");
				instance.getServer().broadcastMessage("   ");
				return;
			}
		}else{
			p.sendMessage("ß6[Terreno] ßeEssa ·rea nao È sua.");
		}
	}
	
}
