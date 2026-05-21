package de.universalrebirth.plugin.commands;

import de.universalrebirth.plugin.managers.BanManager;
import de.universalrebirth.testaufgabe.command.framework.commands.RegisterCommands;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Objects;

/**
 * BanCommand
 * /ban <player> [reason]
 */
public final class BanCommand extends RegisterCommands.AbstractCommand {

    private final BanManager banManager;

    public BanCommand(final JavaPlugin plugin, final BanManager banManager) {
        super("ban");

        this.banManager = Objects.requireNonNull(banManager);

        description("Ban a player from the server");
        permission("test.ban");
        aliases("tempban");
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
            sender.sendMessage("§cUsage: /" + label + " <player> [reason]");
            return true;
        }

        // Get Target Player
        final Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found: " + args[0]);
            return true;
        }

        // Get Reason
        final String reason = args.length > 1
                ? String.join(" ", Arrays.copyOfRange(args, 1, args.length))
                : "No reason";

        // Ban Player
        banManager.banPlayer(target.getUniqueId(), target.getName(), reason, sender.getName());

        // Kick Player
        target.kickPlayer("§cYou have been banned!\n§cReason: " + reason);

        // Send Message
        sender.sendMessage("§a✓ Player §f" + target.getName() + " §ahas been banned.");

        // Broadcast
        Bukkit.broadcastMessage("§c[BAN] §f" + target.getName() + " §cwas banned.");

        return true;
    }
}
