package me.herobrinedobem.hterrenos;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Comandos implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
		Player p;
		if (sender instanceof Player) {
			p = (Player) sender;

			if (cmd.getName().equalsIgnoreCase("terreno")) {
				if ((args.length == 2) && (args[0].equalsIgnoreCase("nome"))) {
					if (HTerrenos.getHTerrenos().getTerrenoManager().getTerrenosNomes().containsKey(p.getName())) {
						final String nome = args[1];
						if (!HTerrenos.getHTerrenos().getTerrenoManager().hasPlayerArea(p.getName(), nome, p.getWorld().getName())) {
							HTerrenos.getHTerrenos().getTerrenoManager().comprarTerreno(p, HTerrenos.getHTerrenos().getTerrenoManager().getTerrenosNomes().get(p.getName()), nome);
							HTerrenos.getHTerrenos().getTerrenoManager().getTerrenosNomes().remove(p.getName());
							return false;
						} else {
							p.sendMessage(HTerrenos.getHTerrenos().getConfig().getString("Mensagens.Ja_Existe_Nome").replace("&", "§"));
							HTerrenos.getHTerrenos().getTerrenoManager().getTerrenosNomes().remove(p.getName());
							return false;
						}
					} else {
						p.sendMessage(HTerrenos.getHTerrenos().getConfig().getString("Mensagens.Nao_Esta_Comprando").replace("&", "§"));
						return false;
					}
				} else {
					final ApplicableRegionSet regions = HTerrenos.getHTerrenos().getWorldGuard().getRegionManager(p.getWorld()).getApplicableRegions(p.getLocation());
					if (regions.size() > 0) {
						for (final ProtectedRegion r : regions) {
							if (r.getId().toLowerCase().contains(p.getName().toLowerCase())) {
								try {
									HTerrenos.getHTerrenos().getGuiManager().openMainInventoryOwner(p);
								} catch (NumberFormatException | InvalidFlagFormat e) {
									e.printStackTrace();
								}
							} else {
								HTerrenos.getHTerrenos().getGuiManager().openMainInventory(p);
							}
						}
					} else {
						HTerrenos.getHTerrenos().getGuiManager().openMainInventory(p);
					}
				}
			}

		}
		return false;
	}

}
