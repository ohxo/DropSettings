package me.aezily.dropsettings;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class DropSettings extends JavaPlugin {
    private File playerDataFile;
    private FileConfiguration playerData;
    private FileConfiguration messagesConfig;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveResource("messages.yml", false);

        playerDataFile = new File(getDataFolder(), "data.yml");
        if (!playerDataFile.exists()) {
            saveResource("data.yml", false);
        }
        playerData = YamlConfiguration.loadConfiguration(playerDataFile);

        config = this.getConfig();

        getCommand("dropsettings").setExecutor(new DropSettingsCommand(this));
        Bukkit.getPluginManager().registerEvents(new InventoryListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ItemPickupListener(this), this);
    }

    public FileConfiguration getMessages() {
        return yamlConfigurationLoader("messages.yml");
    }

    public FileConfiguration getPlayerData() {
        return playerData;
    }

    public FileConfiguration getConfigData() {
        return config;
    }

    public void setMessages(FileConfiguration config) {
        this.messagesConfig = config;
    }

    public void savePlayerData() {
        try {
            playerData.save(playerDataFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FileConfiguration yamlConfigurationLoader(String resource) {
        return YamlConfiguration.loadConfiguration(new File(getDataFolder(), resource));
    }

    public void reloadConfigFiles() {
        reloadConfig();
        config = getConfig();
        this.savePlayerData();
    }
}
