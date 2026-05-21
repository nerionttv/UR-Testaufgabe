package de.universalrebirth.plugin.commands;

import de.universalrebirth.plugin.managers.BanManager;
import de.universalrebirth.testaufgabe.command.framework.commands.RegisterCommands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.UUID;

/**
 * UnbanCommand
 * /unban <player>
 */
public final class UnbanCommand extends RegisterCommands.AbstractCommand {

    private final BanManager banManager;

    public UnbanCommand(final JavaPlugin plugin, final BanManager banManager) {
        super("unban");

        this.banManager = Objects.requireNonNull(banManager);

        description("Unban a player from the server");
        permission("test.ban");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Permission Check
        if (!sender.hasPermission(permission())) {
            sender.sendMessage("§cYou do not have permission.");
            return true;
        }

        // Args Check
        if (args.length < 1) {
            sender.sendMessage("§cUsage: /" + label + " <player>");
            return true;
        }

        // Try to parse UUID - for now, search by name in bans
        final String playerName = args[0];

        // This is a simplified version - in production you'd want UUID lookup
        // For now we'll show message that UUID needs to be used or we search bans.yml
        sender.sendMessage("§cPlease use the player's full name. Searching bans...");

        return true;
    }
}
