package me.aezily.dropsettings;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class DropSettingsGUI {
    public static void open(Player player, DropSettings plugin) {
        String title = plugin.getConfig().getString("gui.title").replace("&", "§");
        int size = plugin.getConfig().getInt("gui.size");
        Inventory inv = Bukkit.createInventory(null, size, title);

        ConfigurationSection itemsSection = plugin.getConfig().getConfigurationSection("items");
        for (String key : itemsSection.getKeys(false)) {
            int slot = itemsSection.getInt(key + ".slot");
            Material mat = Material.valueOf(itemsSection.getString(key + ".material"));
            String displayName = itemsSection.getString(key + ".display-name").replace("&", "§");
            List<String> lore = new ArrayList<>();
            boolean enabled = plugin.getPlayerData().getBoolean(player.getUniqueId().toString() + "." + key, true);
            for (String line : itemsSection.getStringList(key + ".lore")) {
                String status = enabled ? "§aAttivo" : "§cDisattivo";
                lore.add(line.replace("%status%", status).replace("&", "§"));
            }

            ItemStack item = new ItemStack(mat);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(displayName);
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(slot, item);
        }

        player.openInventory(inv);
    }
}