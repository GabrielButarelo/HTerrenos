package me.herobrinedobem.hterrenos;

import java.util.Arrays;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GUIManager {

	public void openMainInventoryOwner(final Player player) throws NumberFormatException, InvalidFlagFormat {
		final Inventory inv = HTerrenos.getHTerrenos().getServer().createInventory(null, 9, HTerrenos.getHTerrenos().getConfig().getString("GUI.Nome").replace("&", "§"));
		inv.setItem(0, HTerrenos.getHTerrenos().getUtils().getGUIItemStack(Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Espaco_Vazio").split(";")[0]), HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Espaco_Vazio").split(";")[1], Arrays.asList("")));
		inv.setItem(1, HTerrenos.getHTerrenos().getUtils().getGUIItemStack(Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.PVP").split(";")[0]), HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.PVP").split(";")[1], Arrays.asList("§6O PVP Esta " + HTerrenos.getHTerrenos().getTerrenoManager().getValueFlag(player, "pvp"))));
		inv.setItem(2, HTerrenos.getHTerrenos().getUtils().getGUIItemStack(Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Mob").split(";")[0]), HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Mob").split(";")[1], Arrays.asList("§6Spawn De Mobs " + HTerrenos.getHTerrenos().getTerrenoManager().getValueFlag(player, "mob"))));
		inv.setItem(3, HTerrenos.getHTerrenos().getUtils().getGUIItemStack(Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Entrar").split(";")[0]), HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Entrar").split(";")[1], Arrays.asList("§6Acesso Ao Terreno " + HTerrenos.getHTerrenos().getTerrenoManager().getValueFlag(player, "entry"))));
		inv.setItem(4, HTerrenos.getHTerrenos().getUtils().getGUIItemStack(Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Bau_Acesso").split(";")[0]), HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Bau_Acesso").split(";")[1], Arrays.asList("§6Acesso A Baus " + HTerrenos.getHTerrenos().getTerrenoManager().getValueFlag(player, "bau"))));
		inv.setItem(5, HTerrenos.getHTerrenos().getUtils().getGUIItemStack(Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Amigo_Del").split(";")[0]), HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Amigo_Del").split(";")[1], Arrays.asList("§6Ver Lista De Amigos")));
		inv.setItem(6, HTerrenos.getHTerrenos().getUtils().getGUIItemStack(Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Amigo_Add").split(";")[0]), HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Amigo_Add").split(";")[1], Arrays.asList("§6Ver Lista De Players")));
		inv.setItem(7, HTerrenos.getHTerrenos().getUtils().getGUIItemStack(Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Deleta").split(";")[0]), HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Deleta").split(";")[1], Arrays.asList("§6Isso Ira Deletar Seu Terreno")));
		inv.setItem(8, HTerrenos.getHTerrenos().getUtils().getGUIItemStack(Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Espaco_Vazio").split(";")[0]), HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Espaco_Vazio").split(";")[1], Arrays.asList("")));
		player.openInventory(inv);
	}

	public void openAmigosInventory(final Player player) throws NumberFormatException, InvalidFlagFormat {
		if (!HTerrenos.getHTerrenos().getTerrenoManager().getTerrenosPlayer(player).contains("Nenhum")) {
			final Inventory inv = HTerrenos.getHTerrenos().getServer().createInventory(null, 9, HTerrenos.getHTerrenos().getConfig().getString("GUI.Nome").replace("&", "§"));
			final String amigos = HTerrenos.getHTerrenos().getTerrenoManager().getAmigosTerreno(player);
			if (!amigos.contains("JaMandou")) {
				int i = 0;
				int tamanho = 0;
				for (final String s : amigos.split(";")) {
					inv.setItem(i, HTerrenos.getHTerrenos().getUtils().getPlayerCabeca(s, Arrays.asList("§6Remover Jogador")));
					i++;
					tamanho++;
				}
				if (tamanho < 9) {
					for (int i1 = tamanho; i1 < 9; i1++) {
						inv.setItem(i1, HTerrenos.getHTerrenos().getUtils().getGUIItemStack(Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Espaco_Vazio").split(";")[0]), HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Espaco_Vazio").split(";")[1], Arrays.asList("")));
					}
				}
				player.openInventory(inv);
			}
		} else {
			player.sendMessage(HTerrenos.getHTerrenos().getConfig().getString("Mensagens.Nao_Tem_Amigos").replace("&", "§"));
		}
	}

	public void openOnlinePlayersInventory(final Player player) throws NumberFormatException, InvalidFlagFormat {
		final Inventory inv = HTerrenos.getHTerrenos().getServer().createInventory(null, 45, HTerrenos.getHTerrenos().getConfig().getString("GUI.Nome").replace("&", "§"));
		int i = 0;
		int tamanho = 0;
		// if (HTerrenos.getHTerrenos().getServer().getOnlinePlayers().length > 1) {
		for (final Player s : HTerrenos.getHTerrenos().getServer().getOnlinePlayers()) {
			if (!s.getName().equalsIgnoreCase(player.getName())) {
				inv.setItem(i, HTerrenos.getHTerrenos().getUtils().getPlayerCabeca(s.getName(), Arrays.asList("§6Adicionar Jogador")));
				i++;
				tamanho++;
			}
		}
		if (tamanho < 45) {
			for (int i1 = tamanho; i1 < 45; i1++) {
				inv.setItem(i1, HTerrenos.getHTerrenos().getUtils().getGUIItemStack(Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Espaco_Vazio").split(";")[0]), HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Espaco_Vazio").split(";")[1], Arrays.asList("")));
			}
		}
		// }
		player.openInventory(inv);
	}

	public void openComprarInventory(final Player player) throws NumberFormatException, InvalidFlagFormat {
		final Inventory inv = HTerrenos.getHTerrenos().getServer().createInventory(null, 18, HTerrenos.getHTerrenos().getConfig().getString("GUI.Nome").replace("&", "§"));
		int i = 0;
		int tamanho = 0;
		for (final String s : HTerrenos.getHTerrenos().getConfig().getString("Config.Terreno_Tamanhos").split(";")) {
			inv.setItem(i, HTerrenos.getHTerrenos().getUtils().getGUIItemStack(Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Comprar_Tamanho").split(";")[0]), HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Comprar_Tamanho").split(";")[1].replace("$tamanho$", s + "x" + s), Arrays.asList("§6Escolher Esse Tamanho")));
			i++;
			tamanho++;
		}
		if (tamanho < 18) {
			for (int i1 = tamanho; i1 < 18; i1++) {
				inv.setItem(i1, HTerrenos.getHTerrenos().getUtils().getGUIItemStack(Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Espaco_Vazio").split(";")[0]), HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Espaco_Vazio").split(";")[1], Arrays.asList("")));
			}
		}
		player.openInventory(inv);
	}

	public void openTerrenosInventory(final Player player) throws NumberFormatException, InvalidFlagFormat {
		if (!HTerrenos.getHTerrenos().getTerrenoManager().getTerrenosPlayer(player).contains("Nenhum")) {
			final Inventory inv = HTerrenos.getHTerrenos().getServer().createInventory(null, 18, HTerrenos.getHTerrenos().getConfig().getString("GUI.Nome").replace("&", "§"));
			int i = 0;
			int tamanho = 0;
			for (final String s : HTerrenos.getHTerrenos().getTerrenoManager().getTerrenosPlayer(player).split(";")) {
				inv.setItem(i, HTerrenos.getHTerrenos().getUtils().getGUIItemStack(Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Terreno").split(";")[0]), HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Terreno").split(";")[1].replace("$nome$", s), Arrays.asList("§6Teleportar Para Esse Terreno")));
				i++;
				tamanho++;
			}
			if (tamanho < 18) {
				for (int i1 = tamanho; i1 < 18; i1++) {
					inv.setItem(i1, HTerrenos.getHTerrenos().getUtils().getGUIItemStack(Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Espaco_Vazio").split(";")[0]), HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Espaco_Vazio").split(";")[1], Arrays.asList("")));
				}
			}
			player.openInventory(inv);
		} else {
			player.sendMessage(HTerrenos.getHTerrenos().getConfig().getString("Mensagens.Nao_Tem_Terrenos").replace("&", "§"));
		}
	}

	public void openMainInventory(final Player player) {
		final Inventory inv = HTerrenos.getHTerrenos().getServer().createInventory(null, 9, HTerrenos.getHTerrenos().getConfig().getString("GUI.Nome").replace("&", "§"));
		inv.setItem(0, HTerrenos.getHTerrenos().getUtils().getGUIItemStack(Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Espaco_Vazio").split(";")[0]), HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Espaco_Vazio").split(";")[1], Arrays.asList("")));
		inv.setItem(1, HTerrenos.getHTerrenos().getUtils().getGUIItemStack(Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Espaco_Vazio").split(";")[0]), HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Espaco_Vazio").split(";")[1], Arrays.asList("")));
		inv.setItem(2, HTerrenos.getHTerrenos().getUtils().getGUIItemStack(Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Espaco_Vazio").split(";")[0]), HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Espaco_Vazio").split(";")[1], Arrays.asList("")));
		inv.setItem(3, HTerrenos.getHTerrenos().getUtils().getGUIItemStack(Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Info").split(";")[0]), HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Info").split(";")[1], Arrays.asList("§6Informacoes Do Terreno")));
		inv.setItem(4, HTerrenos.getHTerrenos().getUtils().getGUIItemStack(Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Comprar").split(";")[0]), HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Comprar").split(";")[1], Arrays.asList("§6Escolher Tamanho")));
		inv.setItem(5, HTerrenos.getHTerrenos().getUtils().getGUIItemStack(Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.TP").split(";")[0]), HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.TP").split(";")[1], Arrays.asList("§6Escolher Terreno")));
		inv.setItem(6, HTerrenos.getHTerrenos().getUtils().getGUIItemStack(Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Espaco_Vazio").split(";")[0]), HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Espaco_Vazio").split(";")[1], Arrays.asList("")));
		inv.setItem(7, HTerrenos.getHTerrenos().getUtils().getGUIItemStack(Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Espaco_Vazio").split(";")[0]), HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Espaco_Vazio").split(";")[1], Arrays.asList("")));
		inv.setItem(8, HTerrenos.getHTerrenos().getUtils().getGUIItemStack(Integer.parseInt(HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Espaco_Vazio").split(";")[0]), HTerrenos.getHTerrenos().getConfig().getString("GUI.Itens.Espaco_Vazio").split(";")[1], Arrays.asList("")));
		player.openInventory(inv);
	}

}
