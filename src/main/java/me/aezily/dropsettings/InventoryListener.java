package me.aezily.dropsettings;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {
    private final DropSettings plugin;

    public InventoryListener(DropSettings plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String title = plugin.getConfig().getString("gui.title").replace("&", "§");
        if (!event.getView().getTitle().equalsIgnoreCase(title)) return;

        event.setCancelled(true);
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

        int slot = event.getRawSlot();
        ConfigurationSection itemsSection = plugin.getConfig().getConfigurationSection("items");
        for (String key : itemsSection.getKeys(false)) {
            int itemSlot = itemsSection.getInt(key + ".slot");
            if (itemSlot == slot) {
                Player player = (Player) event.getWhoClicked();
                String uuid = player.getUniqueId().toString();
                boolean currentlyEnabled = plugin.getPlayerData().getBoolean(uuid + "." + key, true);
                plugin.getPlayerData().set(uuid + "." + key, !currentlyEnabled);
                plugin.savePlayerData();

                String msgKey = !currentlyEnabled ? "toggle-enabled" : "toggle-disabled";
                String itemName = itemsSection.getString(key + ".display-name").replace("&", "§");
                String message = plugin.getMessages().getString(msgKey).replace("&", "§").replace("%item%", itemName);
                player.sendMessage(message);

                Bukkit.getScheduler().runTaskLater(plugin, () -> DropSettingsGUI.open(player, plugin), 1L);
                break;
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        String title = plugin.getConfig().getString("gui.title").replace("&", "§");
        if (event.getView().getTitle().equals(title)) {
            event.setCancelled(true);
        }
    }
}