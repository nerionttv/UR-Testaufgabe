package de.universalrebirth.plugin.commands;

import de.universalrebirth.plugin.managers.MuteManager;
import de.universalrebirth.testaufgabe.command.framework.commands.RegisterCommands;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Objects;

/**
 * MuteCommand
 * /mute <player> [duration] [reason]
 * Duration: permanent, 1m, 5m, 1h, etc.
 */
public final class MuteCommand extends RegisterCommands.AbstractCommand {

    private final MuteManager muteManager;

    public MuteCommand(final JavaPlugin plugin, final MuteManager muteManager) {
        super("mute");

        this.muteManager = Objects.requireNonNull(muteManager);

        description("Mute a player");
        permission("test.mute");
        aliases("silence");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission(permission())) {
            sender.sendMessage("§cYou do not have permission.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("§cUsage: /" + label + " <player> [duration] [reason]");
            return true;
        }

        final Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found: " + args[0]);
            return true;
        }

        long duration = 0; // 0 = permanent
        String reason = "No reason";

        if (args.length > 1 && !args[1].equalsIgnoreCase("permanent")) {
            try {
                final long minutes = Long.parseLong(args[1].replaceAll("[^0-9]", ""));
                duration = minutes;
            } catch (Exception e) {
                duration = 0;
            }
        }

        if (args.length > 2) {
            reason = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
        }

        muteManager.mutePlayer(target.getUniqueId(), target.getName(), duration, reason, sender.getName());

        target.sendMessage("§cYou have been muted! You cannot chat.");
        sender.sendMessage("§a✓ Player §f" + target.getName() + " §ahas been muted.");
        Bukkit.broadcastMessage("§6[MUTE] §f" + target.getName() + " §6was muted.");

        return true;
    }
}
