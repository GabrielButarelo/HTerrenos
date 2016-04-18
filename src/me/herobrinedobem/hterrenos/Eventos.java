package me.herobrinedobem.hterrenos;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Eventos implements Listener {

	@EventHandler
	private void onInventoryMoveItemEvent(final InventoryMoveItemEvent e) {
		if (e.getSource().getTitle().contains(HTerrenos.getHTerrenos().getConfig().getString("GUI.Nome").replaceAll("&", "§"))) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	private void onPlayerClickInventoryEvent(final InventoryClickEvent e) throws InvalidFlagFormat {
		if (e.getInventory().getName().contains(HTerrenos.getHTerrenos().getConfig().getString("GUI.Nome").replace("&", "§"))) {
			final Player p = (Player) e.getWhoClicked();
			if ((e.getCurrentItem().getTypeId() == Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Info").split(";")[0])) && e.getCurrentItem().getItemMeta().getDisplayName().contains(HTerrenos.getHTerrenos().getUtils().removeColor(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Info").split(";")[1]))) {
				final StringBuilder donos = new StringBuilder();
				final ApplicableRegionSet regions = HTerrenos.getHTerrenos().getWorldGuard().getRegionManager(p.getWorld()).getApplicableRegions(p.getLocation());
				if (regions.size() > 0) {
					for (final ProtectedRegion r : regions) {
						for (final String players : r.getOwners().getPlayers()) {
							donos.append(players);
						}
					}
					for (final String s : HTerrenos.getHTerrenos().getConfig().getStringList("Mensagens.Terreno_Info")) {
						p.sendMessage(s.replaceAll("&", "§").replace("$dono$", donos.toString()));
					}
				} else {
					p.sendMessage(HTerrenos.getHTerrenos().getConfig().getString("Mensagens.Sem_Area").replace("&", "§"));
				}
				e.getWhoClicked().closeInventory();
				e.setCancelled(true);
			} else if ((e.getCurrentItem().getTypeId() == Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Comprar").split(";")[0])) && e.getCurrentItem().getItemMeta().getDisplayName().contains(HTerrenos.getHTerrenos().getUtils().removeColor(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Comprar").split(";")[1]))) {
				e.getWhoClicked().closeInventory();
				HTerrenos.getHTerrenos().getGuiManager().openComprarInventory(p);
				e.setCancelled(true);
			} else if ((e.getCurrentItem().getTypeId() == Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.PVP").split(";")[0])) && e.getCurrentItem().getItemMeta().getDisplayName().contains(HTerrenos.getHTerrenos().getUtils().removeColor(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.PVP").split(";")[1]))) {
				if (HTerrenos.getHTerrenos().getEconomy().getBalance(p.getName()) >= HTerrenos.getHTerrenos().getConfig().getInt("Config.Preco_Alterar_PVP")) {
					final ApplicableRegionSet regions = HTerrenos.getHTerrenos().getWorldGuard().getRegionManager(p.getWorld()).getApplicableRegions(p.getLocation());
					for (final ProtectedRegion r : regions) {
						if (r.getFlag(DefaultFlag.PVP) == State.ALLOW) {
							r.setFlag(DefaultFlag.PVP, DefaultFlag.PVP.parseInput(HTerrenos.getHTerrenos().getWorldGuard(), p, "deny"));
						} else {
							r.setFlag(DefaultFlag.PVP, DefaultFlag.PVP.parseInput(HTerrenos.getHTerrenos().getWorldGuard(), p, "allow"));
						}
					}
					p.sendMessage(HTerrenos.getHTerrenos().getConfig().getString("Mensagens.PVP_Alterado").replace("&", "§"));
					HTerrenos.getHTerrenos().getEconomy().withdrawPlayer(p.getName(), HTerrenos.getHTerrenos().getConfig().getInt("Config.Preco_Alterar_PVP"));
					e.getWhoClicked().closeInventory();
					e.setCancelled(true);
				} else {
					p.sendMessage(HTerrenos.getHTerrenos().getConfig().getString("Mensagens.Sem_Money_Suficiente").replace("&", "§").replace("$quantidade$", String.valueOf(HTerrenos.getHTerrenos().getConfig().getInt("Config.Preco_Alterar_PVP"))));
					e.getWhoClicked().closeInventory();
					e.setCancelled(true);
					return;
				}
			} else if ((e.getCurrentItem().getTypeId() == Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Mob").split(";")[0])) && e.getCurrentItem().getItemMeta().getDisplayName().contains(HTerrenos.getHTerrenos().getUtils().removeColor(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Mob").split(";")[1]))) {
				if (HTerrenos.getHTerrenos().getEconomy().getBalance(p.getName()) >= HTerrenos.getHTerrenos().getConfig().getInt("Config.Preco_Alterar_Mob_Spawn")) {
					final ApplicableRegionSet regions = HTerrenos.getHTerrenos().getWorldGuard().getRegionManager(p.getWorld()).getApplicableRegions(p.getLocation());
					for (final ProtectedRegion r : regions) {
						if (r.getFlag(DefaultFlag.MOB_SPAWNING) == State.ALLOW) {
							r.setFlag(DefaultFlag.MOB_SPAWNING, DefaultFlag.MOB_SPAWNING.parseInput(HTerrenos.getHTerrenos().getWorldGuard(), p, "deny"));
						} else {
							r.setFlag(DefaultFlag.MOB_SPAWNING, DefaultFlag.MOB_SPAWNING.parseInput(HTerrenos.getHTerrenos().getWorldGuard(), p, "allow"));
						}
					}
					p.sendMessage(HTerrenos.getHTerrenos().getConfig().getString("Mensagens.Mob_Spawn_Alterado").replace("&", "§"));
					HTerrenos.getHTerrenos().getEconomy().withdrawPlayer(p.getName(), HTerrenos.getHTerrenos().getConfig().getInt("Config.Preco_Alterar_Mob_Spawn"));
					e.getWhoClicked().closeInventory();
					e.setCancelled(true);
				} else {
					p.sendMessage(HTerrenos.getHTerrenos().getConfig().getString("Mensagens.Sem_Money_Suficiente").replace("&", "§").replace("$quantidade$", String.valueOf(HTerrenos.getHTerrenos().getConfig().getInt("Config.Preco_Alterar_Mob_Spawn"))));
					e.getWhoClicked().closeInventory();
					e.setCancelled(true);
					return;
				}
			} else if ((e.getCurrentItem().getTypeId() == Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Entrar").split(";")[0])) && e.getCurrentItem().getItemMeta().getDisplayName().contains(HTerrenos.getHTerrenos().getUtils().removeColor(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Entrar").split(";")[1]))) {
				if (HTerrenos.getHTerrenos().getEconomy().getBalance(p.getName()) >= HTerrenos.getHTerrenos().getConfig().getInt("Config.Preco_Alterar_Entrar")) {
					final ApplicableRegionSet regions = HTerrenos.getHTerrenos().getWorldGuard().getRegionManager(p.getWorld()).getApplicableRegions(p.getLocation());
					for (final ProtectedRegion r : regions) {
						if (r.getFlag(DefaultFlag.ENTRY) == State.ALLOW) {
							r.setFlag(DefaultFlag.ENTRY, DefaultFlag.ENTRY.parseInput(HTerrenos.getHTerrenos().getWorldGuard(), p, "deny"));
						} else {
							r.setFlag(DefaultFlag.ENTRY, DefaultFlag.ENTRY.parseInput(HTerrenos.getHTerrenos().getWorldGuard(), p, "allow"));
						}
					}
					p.sendMessage(HTerrenos.getHTerrenos().getConfig().getString("Mensagens.Entrar_Alterado").replace("&", "§"));
					HTerrenos.getHTerrenos().getEconomy().withdrawPlayer(p.getName(), HTerrenos.getHTerrenos().getConfig().getInt("Config.Preco_Alterar_Entrar"));
					e.getWhoClicked().closeInventory();
					e.setCancelled(true);
				} else {
					p.sendMessage(HTerrenos.getHTerrenos().getConfig().getString("Mensagens.Sem_Money_Suficiente").replace("&", "§").replace("$quantidade$", String.valueOf(HTerrenos.getHTerrenos().getConfig().getInt("Config.Preco_Alterar_Entrar"))));
					e.getWhoClicked().closeInventory();
					e.setCancelled(true);
					return;
				}
			} else if ((e.getCurrentItem().getTypeId() == Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Bau_Acesso").split(";")[0])) && e.getCurrentItem().getItemMeta().getDisplayName().contains(HTerrenos.getHTerrenos().getUtils().removeColor(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Bau_Acesso").split(";")[1]))) {
				if (HTerrenos.getHTerrenos().getEconomy().getBalance(p.getName()) >= HTerrenos.getHTerrenos().getConfig().getInt("Config.Preco_Alterar_Bau_Acesso")) {
					final ApplicableRegionSet regions = HTerrenos.getHTerrenos().getWorldGuard().getRegionManager(p.getWorld()).getApplicableRegions(p.getLocation());
					for (final ProtectedRegion r : regions) {
						if (r.getFlag(DefaultFlag.CHEST_ACCESS) == State.ALLOW) {
							r.setFlag(DefaultFlag.CHEST_ACCESS, DefaultFlag.CHEST_ACCESS.parseInput(HTerrenos.getHTerrenos().getWorldGuard(), p, "deny"));
						} else {
							r.setFlag(DefaultFlag.CHEST_ACCESS, DefaultFlag.CHEST_ACCESS.parseInput(HTerrenos.getHTerrenos().getWorldGuard(), p, "allow"));
						}
					}
					p.sendMessage(HTerrenos.getHTerrenos().getConfig().getString("Mensagens.Bau_Acesso_Alterado").replace("&", "§"));
					HTerrenos.getHTerrenos().getEconomy().withdrawPlayer(p.getName(), HTerrenos.getHTerrenos().getConfig().getInt("Config.Preco_Alterar_Bau_Acesso"));
					e.getWhoClicked().closeInventory();
					e.setCancelled(true);
				} else {
					p.sendMessage(HTerrenos.getHTerrenos().getConfig().getString("Mensagens.Sem_Money_Suficiente").replace("&", "§").replace("$quantidade$", String.valueOf(HTerrenos.getHTerrenos().getConfig().getInt("Config.Preco_Alterar_Bau_Acesso"))));
					e.getWhoClicked().closeInventory();
					e.setCancelled(true);
					return;
				}
			} else if ((e.getCurrentItem().getTypeId() == Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Amigo_Del").split(";")[0])) && e.getCurrentItem().getItemMeta().getDisplayName().contains(HTerrenos.getHTerrenos().getUtils().removeColor(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Amigo_Del").split(";")[1]))) {
				e.getWhoClicked().closeInventory();
				HTerrenos.getHTerrenos().getGuiManager().openAmigosInventory(p);
				e.setCancelled(true);
			} else if ((e.getCurrentItem().getTypeId() == Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Amigo_Add").split(";")[0])) && e.getCurrentItem().getItemMeta().getDisplayName().contains(HTerrenos.getHTerrenos().getUtils().removeColor(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Amigo_Add").split(";")[1]))) {
				e.getWhoClicked().closeInventory();
				HTerrenos.getHTerrenos().getGuiManager().openOnlinePlayersInventory(p);
				e.setCancelled(true);
			} else if ((e.getCurrentItem().getTypeId() == Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.TP").split(";")[0])) && e.getCurrentItem().getItemMeta().getDisplayName().contains(HTerrenos.getHTerrenos().getUtils().removeColor(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.TP").split(";")[1]))) {
				e.getWhoClicked().closeInventory();
				HTerrenos.getHTerrenos().getGuiManager().openTerrenosInventory(p);
				e.setCancelled(true);
			} else if ((e.getCurrentItem().getTypeId() == Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Terreno").split(";")[0])) && e.getCurrentItem().getItemMeta().getDisplayName().contains(HTerrenos.getHTerrenos().getUtils().removeColor(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Terreno").split(";")[1].split(" ")[0]))) {
				e.getWhoClicked().closeInventory();
				p.teleport(HTerrenos.getHTerrenos().getTerrenoManager().getLocationTerreno(HTerrenos.getHTerrenos().getTerrenoManager().getTerrenoPlayerWorld(p.getName().toLowerCase() + HTerrenos.getHTerrenos().getConfig().getString("Config.Separador_Nome") + e.getCurrentItem().getItemMeta().getDisplayName().split(" ")[1]), p.getName().toLowerCase() + HTerrenos.getHTerrenos().getConfig().getString("Config.Separador_Nome") + e.getCurrentItem().getItemMeta().getDisplayName().split(" ")[1]));
				e.setCancelled(true);
			} else if ((e.getCurrentItem().getTypeId() == Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Comprar_Tamanho").split(";")[0])) && e.getCurrentItem().getItemMeta().getDisplayName().contains(HTerrenos.getHTerrenos().getUtils().removeColor(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Comprar_Tamanho").split(";")[1].split(" ")[0]))) {
				e.getWhoClicked().closeInventory();
				final int tamanho = Integer.parseInt(HTerrenos.getHTerrenos().getUtils().removeColor(e.getCurrentItem().getItemMeta().getDisplayName().split(" ")[1].split("x")[1]));
				String configQuant = "";
				if (p.hasPermission("hterrenos.vip")) {
					configQuant = "Max_Areas_VIP";
				} else {
					configQuant = "Max_Areas_Normal";
				}
				if (HTerrenos.getHTerrenos().getTerrenoManager().getPlayerTerrenosQuantidade(p.getName(), p.getWorld().getName()) < HTerrenos.getHTerrenos().getConfig().getInt("Config." + configQuant)) {
					if (HTerrenos.getHTerrenos().getEconomy().getBalance(p.getName()) >= (tamanho * HTerrenos.getHTerrenos().getConfig().getInt("Config.Preco_Por_Bloco"))) {
						HTerrenos.getHTerrenos().getTerrenoManager().getTerrenosNomes().put(p.getName(), tamanho);
						p.sendMessage(HTerrenos.getHTerrenos().getConfig().getString("Mensagens.Terreno_Nome").replace("&", "§"));
					} else {
						p.sendMessage(HTerrenos.getHTerrenos().getConfig().getString("Mensagens.Sem_Money_Suficiente").replace("&", "§").replace("$quantidade$", String.valueOf(tamanho * HTerrenos.getHTerrenos().getConfig().getInt("Config.Preco_Por_Bloco"))));
					}
				} else {
					p.sendMessage(HTerrenos.getHTerrenos().getConfig().getString("Mensagens.Maximo_Terrenos").replace("&", "§").replace("$quantidade$", String.valueOf(HTerrenos.getHTerrenos().getConfig().getInt("Config." + configQuant))));
				}
				e.setCancelled(true);
			} else if ((e.getCurrentItem().getTypeId() == Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Deleta").split(";")[0])) && e.getCurrentItem().getItemMeta().getDisplayName().contains(HTerrenos.getHTerrenos().getUtils().removeColor(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Deleta").split(";")[1]))) {
				HTerrenos.getHTerrenos().getTerrenoManager().deletarTerreno(p);
				p.sendMessage(HTerrenos.getHTerrenos().getConfig().getString("Mensagens.Terreno_Deletado").replace("&", "§"));
				e.getWhoClicked().closeInventory();
				e.setCancelled(true);
			} else if ((e.getCurrentItem().getType() == Material.SKULL_ITEM) && (e.getCurrentItem().getItemMeta().getLore() != null) && e.getCurrentItem().getItemMeta().getLore().get(0).contains("Remover Jogador")) {
				HTerrenos.getHTerrenos().getTerrenoManager().removeAmigoTerreno(p, HTerrenos.getHTerrenos().getUtils().removeColor(e.getCurrentItem().getItemMeta().getDisplayName()));
				p.sendMessage(HTerrenos.getHTerrenos().getConfig().getString("Mensagens.Amigo_Removido").replace("&", "§").replace("$nick$", HTerrenos.getHTerrenos().getUtils().removeColor(e.getCurrentItem().getItemMeta().getDisplayName())));
				p.closeInventory();
				e.setCancelled(true);
			} else if ((e.getCurrentItem().getType() == Material.SKULL_ITEM) && (e.getCurrentItem().getItemMeta().getLore() != null) && e.getCurrentItem().getItemMeta().getLore().get(0).contains("Adicionar Jogador")) {
				HTerrenos.getHTerrenos().getTerrenoManager().addAmigoTerreno(p, HTerrenos.getHTerrenos().getUtils().removeColor(e.getCurrentItem().getItemMeta().getDisplayName()));
				p.sendMessage(HTerrenos.getHTerrenos().getConfig().getString("Mensagens.Amigo_Adicionado").replace("&", "§").replace("$nick$", HTerrenos.getHTerrenos().getUtils().removeColor(e.getCurrentItem().getItemMeta().getDisplayName())));
				p.closeInventory();
				e.setCancelled(true);
			} else {
				p.closeInventory();
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	private void onPlayerMoveEvent(final PlayerMoveEvent e) {
		if (HTerrenos.getHTerrenos().getTerrenoManager().getTerrenosNomes().containsKey(e.getPlayer().getName())) {
			if (e.getTo() != e.getFrom()) {
				e.getPlayer().sendMessage(HTerrenos.getHTerrenos().getConfig().getString("Mensagens.Compra_Cancelada").replace("&", "§"));
				HTerrenos.getHTerrenos().getTerrenoManager().getTerrenosNomes().remove(e.getPlayer().getName());
			}
		}
	}

	@EventHandler
	private void onPlayerQuitEvent(final PlayerQuitEvent e) {
		if (HTerrenos.getHTerrenos().getTerrenoManager().getTerrenosNomes().containsKey(e.getPlayer().getName())) {
			e.getPlayer().sendMessage(HTerrenos.getHTerrenos().getConfig().getString("Mensagens.Compra_Cancelada").replace("&", "§"));
			HTerrenos.getHTerrenos().getTerrenoManager().getTerrenosNomes().remove(e.getPlayer().getName());
		}
	}

	@EventHandler
	private void onPlayerTeleportEvent(final PlayerTeleportEvent e) {
		if (HTerrenos.getHTerrenos().getTerrenoManager().getTerrenosNomes().containsKey(e.getPlayer().getName())) {
			e.getPlayer().sendMessage(HTerrenos.getHTerrenos().getConfig().getString("Mensagens.Compra_Cancelada").replace("&", "§"));
			HTerrenos.getHTerrenos().getTerrenoManager().getTerrenosNomes().remove(e.getPlayer().getName());
		}
	}

	@EventHandler
	private void onPlayerCommandPreprocessEvent(final PlayerCommandPreprocessEvent e) {
		if (HTerrenos.getHTerrenos().getTerrenoManager().getTerrenosNomes().containsKey(e.getPlayer().getName())) {
			if (!e.getMessage().contains("terreno") && !e.getMessage().contains("nome")) {
				e.getPlayer().sendMessage(HTerrenos.getHTerrenos().getConfig().getString("Mensagens.Compra_Cancelada").replace("&", "§"));
				HTerrenos.getHTerrenos().getTerrenoManager().getTerrenosNomes().remove(e.getPlayer().getName());
				e.setCancelled(true);
			}
		}
	}

}
