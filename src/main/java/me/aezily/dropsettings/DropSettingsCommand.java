package me.aezily.dropsettings;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DropSettingsCommand implements CommandExecutor {
    private final DropSettings plugin;

    public DropSettingsCommand(DropSettings plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Solo giocatori possono usare questo comando.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfigFiles();
            player.sendMessage("§aConfigurazione ricaricata con successo!");
            return true;
        }

        player.sendMessage(plugin.getMessages().getString("open-message").replace("&", "§"));
        DropSettingsGUI.open(player, plugin);
        return true;
    }
}
