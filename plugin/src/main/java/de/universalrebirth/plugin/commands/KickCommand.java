package de.universalrebirth.plugin.commands;

import de.universalrebirth.plugin.managers.AdminLogger;
import de.universalrebirth.testaufgabe.command.framework.commands.RegisterCommands;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Objects;

/**
 * KickCommand
 * /kick <player> [reason]
 */
public final class KickCommand extends RegisterCommands.AbstractCommand {

    private final AdminLogger logger;

    public KickCommand(final JavaPlugin plugin, final AdminLogger logger) {
        super("kick");

        this.logger = Objects.requireNonNull(logger);

        description("Kick a player from the server");
        permission("test.kick");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission(permission())) {
            sender.sendMessage("§cYou do not have permission.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("§cUsage: /" + label + " <player> [reason]");
            return true;
        }

        final Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found: " + args[0]);
            return true;
        }

        final String reason = args.length > 1
                ? String.join(" ", Arrays.copyOfRange(args, 1, args.length))
                : "No reason";

        target.kickPlayer("§cYou have been kicked!\n§cReason: " + reason);
        logger.log(sender.getName(), "KICK", target.getName(), reason);

        sender.sendMessage("§a✓ Player §f" + target.getName() + " §ahas been kicked.");
        Bukkit.broadcastMessage("§e[KICK] §f" + target.getName() + " §ewas kicked.");

        return true;
    }
}
