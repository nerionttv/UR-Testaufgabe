package de.universalrebirth.plugin.commands;

import de.universalrebirth.plugin.managers.MuteManager;
import de.universalrebirth.testaufgabe.command.framework.commands.RegisterCommands;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * UnmuteCommand
 * /unmute <player>
 */
public final class UnmuteCommand extends RegisterCommands.AbstractCommand {

    private final MuteManager muteManager;

    public UnmuteCommand(final JavaPlugin plugin, final MuteManager muteManager) {
        super("unmute");

        this.muteManager = Objects.requireNonNull(muteManager);

        description("Unmute a player");
        permission("test.mute");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission(permission())) {
            sender.sendMessage("§cYou do not have permission.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("§cUsage: /" + label + " <player>");
            return true;
        }

        final Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found: " + args[0]);
            return true;
        }

        if (!muteManager.isMuted(target.getUniqueId())) {
            sender.sendMessage("§cPlayer " + target.getName() + " is not muted.");
            return true;
        }

        muteManager.unmutePlayer(target.getUniqueId(), target.getName(), sender.getName());

        target.sendMessage("§aYou have been unmuted!");
        sender.sendMessage("§a✓ Player §f" + target.getName() + " §ahas been unmuted.");
        Bukkit.broadcastMessage("§a[UNMUTE] §f" + target.getName() + " §awas unmuted.");

        return true;
    }
}
