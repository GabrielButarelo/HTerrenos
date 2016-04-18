package me.herobrinedobem.hterrenos;

import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class Utils {

	public ItemStack getPlayerCabeca(final String nick, final List<String> lore) {
		final ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		final SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
		skullMeta.setDisplayName(nick);
		skullMeta.setOwner(nick);
		skullMeta.setLore(lore);
		skull.setItemMeta(skullMeta);
		return skull;
	}

	public boolean isNumber(final String value) {
		try {
			final int i = Integer.parseInt(value);
			if (i == 0) {
				return true;
			} else {
				return true;
			}
		} catch (final NumberFormatException e) {
			return false;
		}
	}

	public boolean containsTerrenoTamanhoConfig(final int tamanho) {
		boolean contains = false;
		final String[] tamanhos = HTerrenos.getHTerrenos().getConfig().getString("Config.Terreno_Tamanhos").split(";");
		for (final String tam : tamanhos) {
			if (Integer.parseInt(tam) == tamanho) {
				contains = true;
			}
		}
		return contains;
	}

	public ItemStack getGUIItemStack(final int material, final String name, final List<String> lore) {
		final ItemStack item = new ItemStack(Material.getMaterial(material));
		final ItemMeta im = item.getItemMeta();
		im.setDisplayName(name.replace("&", "§"));
		im.setLore(lore);
		item.setItemMeta(im);
		return item;
	}

	public String removeColor(final String msg) {
		return msg.replace("&1", "").replace("&2", "").replace("&3", "").replace("&4", "").replace("&5", "").replace("&6", "").replace("&7", "").replace("&8", "").replace("&9", "").replace("&a", "").replace("&b", "").replace("&c", "").replace("&d", "").replace("&e", "").replace("&f", "").replace("&l", "");
	}

}
