package me.aezily.dropsettings;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.file.FileConfiguration;

public class ItemPickupListener implements Listener {
    private final DropSettings plugin;

    public ItemPickupListener(DropSettings plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        ItemStack item = event.getItem().getItemStack();
        Material material = item.getType();
        String uuid = event.getPlayer().getUniqueId().toString();

        ConfigurationSection itemsSection = plugin.getConfigData().getConfigurationSection("items");
        if (itemsSection == null) return;

        for (String key : itemsSection.getKeys(false)) {
            String configMaterial = itemsSection.getString(key + ".material");

            if (configMaterial != null && material == Material.valueOf(configMaterial)) {
                boolean enabled = plugin.getPlayerData().getBoolean(uuid + "." + key, true);

                if (!enabled) {
                    event.setCancelled(true);
                }

                break;
            }
        }
    }
}