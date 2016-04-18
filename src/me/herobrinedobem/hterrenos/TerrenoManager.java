package me.herobrinedobem.hterrenos;

import java.util.HashMap;
import java.util.Map;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class TerrenoManager {

	private final HashMap<String, Integer> terrenosNomes = new HashMap<>();

	public void comprarTerreno(final Player player, final int tamanho, final String nome) {
		/*
		 * if (this.hasTerrenoNasProximidades(player, tamanho, nome)) {
		 * player.sendMessage(HTerrenos.getHTerrenos().getConfig().getString("Mensagens.Contem_Terreno_Perto").replace("&", "§"));
		 * } else {
		 */
		final RegionManager rm = HTerrenos.getHTerrenos().getWorldGuard().getRegionManager(player.getWorld());
		final Location loc = player.getLocation();
		final int locx = loc.getBlockX();
		final int locz = loc.getBlockZ();
		final BlockVector bv1 = new BlockVector(locx - (tamanho / 2), Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("Config.Y_Max")), locz - (tamanho / 2));
		final BlockVector bv2 = new BlockVector(locx + (tamanho / 2), Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("Config.Y_Min")), locz + (tamanho / 2));
		final ProtectedCuboidRegion pr = new ProtectedCuboidRegion(player.getName().toLowerCase() + HTerrenos.getHTerrenos().getConfig().getString("Config.Separador_Nome") + nome.toLowerCase(), bv1, bv2);
		final DefaultDomain dd = new DefaultDomain();
		rm.addRegion(pr);
		pr.setPriority(100);
		dd.addPlayer(player.getName());
		pr.setOwners(dd);
		try {
			pr.setFlag(DefaultFlag.PVP, DefaultFlag.PVP.parseInput(HTerrenos.getHTerrenos().getWorldGuard(), player, "allow"));
			pr.setFlag(DefaultFlag.USE, DefaultFlag.USE.parseInput(HTerrenos.getHTerrenos().getWorldGuard(), player, "deny"));
			pr.setFlag(DefaultFlag.ENDER_BUILD, DefaultFlag.ENDER_BUILD.parseInput(HTerrenos.getHTerrenos().getWorldGuard(), player, "deny"));
			pr.setFlag(DefaultFlag.CREEPER_EXPLOSION, DefaultFlag.CREEPER_EXPLOSION.parseInput(HTerrenos.getHTerrenos().getWorldGuard(), player, "deny"));
		} catch (final InvalidFlagFormat e) {
			e.printStackTrace();
		}

		final ApplicableRegionSet regions = rm.getApplicableRegions(pr);
		final LocalPlayer localPlayer = HTerrenos.getHTerrenos().getWorldGuard().wrapPlayer(player);

		if (!regions.isOwnerOfAll(localPlayer)) {
			player.sendMessage(HTerrenos.getHTerrenos().getConfig().getString("Mensagens.Contem_Terreno_Perto").replace("&", "§"));
			rm.removeRegion(player.getName().toLowerCase() + HTerrenos.getHTerrenos().getConfig().getString("Config.Separador_Nome") + nome.toLowerCase());
			return;
		} else {
			try {
				rm.save();
			} catch (final ProtectionDatabaseException e) {
				e.printStackTrace();
			}
			this.makeParede(player, tamanho);
			HTerrenos.getHTerrenos().getEconomy().withdrawPlayer(player.getName(), tamanho * HTerrenos.getHTerrenos().getConfig().getInt("Config.Preco_Por_Bloco"));
			for (final String s : HTerrenos.getHTerrenos().getConfig().getStringList("Mensagens.Comprou_Sucesso")) {
				player.sendMessage(s.replace("&", "§").replace("$quantidade$", String.valueOf(tamanho * HTerrenos.getHTerrenos().getConfig().getInt("Config.Preco_Por_Bloco"))));
			}
			return;
		}
	}

	public boolean hasTerrenoNasProximidades(final Player player, final int tamanho, final String nome) {
		final RegionManager rm = HTerrenos.getHTerrenos().getWorldGuard().getRegionManager(player.getWorld());
		final Location loc2 = player.getLocation();
		final int x = loc2.getBlockX();
		final int z = loc2.getBlockZ();

		final BlockVector bv1 = new BlockVector(x - tamanho - 1, Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("Config.Y_Max")), z - tamanho - 1);
		final BlockVector bv2 = new BlockVector(x + tamanho + 1, Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("Config.Y_Min")), z + tamanho + 1);

		final ProtectedCuboidRegion pr = new ProtectedCuboidRegion(player.getName().toLowerCase() + HTerrenos.getHTerrenos().getConfig().getString("Config.Separador_Nome") + nome.toLowerCase(), bv1, bv2);
		final DefaultDomain dd = new DefaultDomain();
		rm.addRegion(pr);
		pr.setPriority(100);
		dd.addPlayer(player.getName());
		pr.setOwners(dd);

		final ApplicableRegionSet regions = rm.getApplicableRegions(pr);
		final LocalPlayer localPlayer = HTerrenos.getHTerrenos().getWorldGuard().wrapPlayer(player);

		if (!regions.isOwnerOfAll(localPlayer)) {
			rm.removeRegion(player.getName().toLowerCase() + HTerrenos.getHTerrenos().getConfig().getString("Config.Separador_Nome") + nome.toLowerCase());
			return true;
		} else {
			rm.removeRegion(player.getName().toLowerCase() + HTerrenos.getHTerrenos().getConfig().getString("Config.Separador_Nome") + nome.toLowerCase());
			return false;
		}
	}

	public String getAmigosTerreno(final Player player) throws InvalidFlagFormat {
		final ApplicableRegionSet regions = HTerrenos.getHTerrenos().getWorldGuard().getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation());
		for (final ProtectedRegion r : regions) {
			final StringBuilder builder = new StringBuilder();
			for (final String s : r.getMembers().getPlayers()) {
				builder.append(s + ";");
			}
			if (builder.toString().isEmpty() || builder.toString().equalsIgnoreCase("") || (builder.toString() == null)) {
				builder.append("Nenhum Player Adicionado Na Área");
			}
			if (builder.toString().contains("Nenhum")) {
				player.closeInventory();
				player.sendMessage(HTerrenos.getHTerrenos().getConfig().getString("Mensagens.Nao_Tem_Amigos").replace("&", "§"));
				return "JaMandou";
			} else {
				return builder.toString();
			}
		}
		return null;
	}

	public void removeAmigoTerreno(final Player player, final String amigo) throws InvalidFlagFormat {
		final RegionManager rm = HTerrenos.getHTerrenos().getWorldGuard().getRegionManager(player.getWorld());
		final ApplicableRegionSet regions = HTerrenos.getHTerrenos().getWorldGuard().getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation());
		for (final ProtectedRegion r : regions) {
			r.getMembers().removePlayer(amigo);
		}
		try {
			rm.save();
		} catch (final ProtectionDatabaseException e) {
			e.printStackTrace();
		}
	}

	public void addAmigoTerreno(final Player player, final String amigo) throws InvalidFlagFormat {
		final RegionManager rm = HTerrenos.getHTerrenos().getWorldGuard().getRegionManager(player.getWorld());
		final ApplicableRegionSet regions = HTerrenos.getHTerrenos().getWorldGuard().getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation());
		for (final ProtectedRegion r : regions) {
			r.getMembers().addPlayer(amigo);
		}
		try {
			rm.save();
		} catch (final ProtectionDatabaseException e) {
			e.printStackTrace();
		}
	}

	public void deletarTerreno(final Player player) throws InvalidFlagFormat {
		final ApplicableRegionSet regions = HTerrenos.getHTerrenos().getWorldGuard().getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation());
		for (final ProtectedRegion r : regions) {
			final RegionManager rm = HTerrenos.getHTerrenos().getWorldGuard().getRegionManager(player.getWorld());
			rm.removeRegion(r.getId());
			try {
				rm.save();
			} catch (final ProtectionDatabaseException e) {
				e.printStackTrace();
			}
		}
	}

	public World getTerrenoPlayerWorld(final String terreno) {
		for (final World world : HTerrenos.getHTerrenos().getServer().getWorlds()) {
			for (final String s : HTerrenos.getHTerrenos().getWorldGuard().getRegionManager(world).getRegions().keySet()) {
				if (s.equalsIgnoreCase(terreno)) {
					return world;
				}
			}
		}
		return null;
	}

	public String getTerrenosPlayer(final Player player) {
		final StringBuilder builder = new StringBuilder();
		for (final World world : HTerrenos.getHTerrenos().getServer().getWorlds()) {
			for (final String s : HTerrenos.getHTerrenos().getWorldGuard().getRegionManager(world).getRegions().keySet()) {
				if (s.startsWith(player.getName().toLowerCase() + HTerrenos.getHTerrenos().getConfig().getString("Config.Separador_Nome"))) {
					builder.append(s.split(HTerrenos.getHTerrenos().getConfig().getString("Config.Separador_Nome"))[1] + ";");
				}
			}
		}
		if ((builder.equals("")) || (builder.toString() == null) || (builder.toString().isEmpty())) {
			return "Nenhum";
		}
		return builder.toString();
	}

	public Location getLocationTerreno(final World world, final String terreno) {
		final RegionManager rm = HTerrenos.getHTerrenos().getWorldGuard().getRegionManager(world);
		final double x = rm.getRegions().get(terreno).getMinimumPoint().getX();
		final double z = rm.getRegions().get(terreno).getMinimumPoint().getZ();
		return new Location(world, x, world.getHighestBlockYAt((int) x, (int) z), z);
	}

	public String getValueFlag(final Player player, final String type) throws InvalidFlagFormat {
		final ApplicableRegionSet regions = HTerrenos.getHTerrenos().getWorldGuard().getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation());
		for (final ProtectedRegion r : regions) {
			if (type.equalsIgnoreCase("pvp")) {
				if (r.getFlag(DefaultFlag.PVP) == State.ALLOW) {
					return "Desativado";
				} else {
					return "Ativado";
				}
			} else if (type.equalsIgnoreCase("mob")) {
				if (r.getFlag(DefaultFlag.MOB_SPAWNING) == State.ALLOW) {
					return "Desativado";
				} else {
					return "Ativado";
				}
			} else if (type.equalsIgnoreCase("entry")) {
				if ((r.getFlag(DefaultFlag.ENTRY) == State.ALLOW) || (r.getFlag(DefaultFlag.CHEST_ACCESS) == null)) {
					return "Desativado";
				} else {
					return "Ativado";
				}
			} else if (type.equalsIgnoreCase("bau")) {
				if ((r.getFlag(DefaultFlag.CHEST_ACCESS) == State.ALLOW) || (r.getFlag(DefaultFlag.CHEST_ACCESS) == null)) {
					return "Desativado";
				} else {
					return "Ativado";
				}
			}
		}
		return null;
	}

	public void makeParede(final Player p, final int tamanho) {
		final Location loc = p.getLocation();
		final World w = loc.getWorld();
		for (int x = loc.getBlockX() - (tamanho / 2); x < ((loc.getBlockX() - (tamanho / 2)) + tamanho); x++) {
			final Block xb = w.getBlockAt(x, loc.getBlockY(), loc.getBlockZ() - (tamanho / 2));
			xb.setTypeId(HTerrenos.getHTerrenos().getConfig().getInt("Config.Bloco_Parede_ID"));
		}
		for (int x2 = loc.getBlockX() - (tamanho / 2); x2 <= ((loc.getBlockX() - (tamanho / 2)) + tamanho); x2++) {
			final Block xb = w.getBlockAt(x2, loc.getBlockY(), (loc.getBlockZ() - (tamanho / 2)) + tamanho);
			xb.setTypeId(HTerrenos.getHTerrenos().getConfig().getInt("Config.Bloco_Parede_ID"));
		}
		for (int z = loc.getBlockZ() - (tamanho / 2); z < ((loc.getBlockZ() - (tamanho / 2)) + tamanho); z++) {
			final Block zb = w.getBlockAt(loc.getBlockX() - (tamanho / 2), loc.getBlockY(), z);
			zb.setTypeId(HTerrenos.getHTerrenos().getConfig().getInt("Config.Bloco_Parede_ID"));
		}
		for (int z2 = loc.getBlockZ() - (tamanho / 2); z2 <= ((loc.getBlockZ() - (tamanho / 2)) + tamanho); z2++) {
			final Block zb = w.getBlockAt((loc.getBlockX() - (tamanho / 2)) + tamanho, loc.getBlockY(), z2);
			zb.setTypeId(HTerrenos.getHTerrenos().getConfig().getInt("Config.Bloco_Parede_ID"));
		}
	}

	public int getPlayerTerrenosQuantidade(final String player, final String mundo) {
		final RegionManager mgr = HTerrenos.getHTerrenos().getWorldGuard().getGlobalRegionManager().get(HTerrenos.getHTerrenos().getServer().getWorld(mundo));
		final Map<String, ProtectedRegion> regions = mgr.getRegions();
		int quant = 0;
		for (final String id : regions.keySet()) {
			if (id.startsWith(player.toLowerCase() + HTerrenos.getHTerrenos().getConfig().getString("Config.Separador_Nome"))) {
				quant++;
			}
		}
		return quant;
	}

	public boolean hasPlayerArea(final String player, final String area, final String mundo) {
		final RegionManager mgr = HTerrenos.getHTerrenos().getWorldGuard().getGlobalRegionManager().get(HTerrenos.getHTerrenos().getServer().getWorld(mundo));
		final Map<String, ProtectedRegion> regions = mgr.getRegions();
		boolean contains = false;
		for (final String id : regions.keySet()) {
			System.out.println(id);
			if (id.equalsIgnoreCase(player.toLowerCase() + HTerrenos.getHTerrenos().getConfig().getString("Config.Separador_Nome") + area.toLowerCase())) {
				contains = true;
			}
		}
		return contains;
	}

	public HashMap<String, Integer> getTerrenosNomes() {
		return this.terrenosNomes;
	}

}
