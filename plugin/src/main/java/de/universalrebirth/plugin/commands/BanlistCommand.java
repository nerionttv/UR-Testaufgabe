package de.universalrebirth.plugin.commands;

import de.universalrebirth.testaufgabe.command.framework.commands.RegisterCommands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * BanlistCommand
 * /banlist
 */
public final class BanlistCommand extends RegisterCommands.AbstractCommand {

    public BanlistCommand(final JavaPlugin plugin) {
        super("banlist");

        description("Show the list of banned players");
        permission("test.ban");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Permission Check
        if (!sender.hasPermission(permission())) {
            sender.sendMessage("§cYou do not have permission.");
            return true;
        }

        sender.sendMessage("§e========== BANLIST ==========");
        sender.sendMessage("§7(Feature coming soon)");
        sender.sendMessage("§7Check plugins/UniversalRebirth-Core/bans.yml");
        sender.sendMessage("§e=============================");

        return true;
    }
}
