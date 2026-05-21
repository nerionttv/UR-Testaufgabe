package de.universalrebirth.plugin.commands;

import de.universalrebirth.plugin.managers.WarnManager;
import de.universalrebirth.testaufgabe.command.framework.commands.RegisterCommands;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Objects;

/**
 * WarnCommand
 * /warn <player> [reason]
 */
public final class WarnCommand extends RegisterCommands.AbstractCommand {

    private final WarnManager warnManager;

    public WarnCommand(final JavaPlugin plugin, final WarnManager warnManager) {
        super("warn");

        this.warnManager = Objects.requireNonNull(warnManager);

        description("Warn a player");
        permission("test.warn");
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

        warnManager.warnPlayer(target.getUniqueId(), target.getName(), reason, sender.getName());

        final int warnCount = warnManager.getWarningCount(target.getUniqueId());
        target.sendMessage("§cYou have been warned! (Total warnings: " + warnCount + ")");
        sender.sendMessage("§a✓ Player §f" + target.getName() + " §ahas been warned. (§f" + warnCount + " §awarnings)");
        Bukkit.broadcastMessage("§d[WARN] §f" + target.getName() + " §dwas warned. (§f" + warnCount + " §dwarnings)");

        return true;
    }
}
