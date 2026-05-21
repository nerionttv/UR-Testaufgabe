package de.universalrebirth.plugin.commands;

import de.universalrebirth.plugin.managers.WarnManager;
import de.universalrebirth.testaufgabe.command.framework.commands.RegisterCommands;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;

/**
 * WarningsCommand
 * /warnings <player>
 */
public final class WarningsCommand extends RegisterCommands.AbstractCommand {

    private final WarnManager warnManager;

    public WarningsCommand(final JavaPlugin plugin, final WarnManager warnManager) {
        super("warnings");

        this.warnManager = Objects.requireNonNull(warnManager);

        description("Show warnings for a player");
        permission("test.warn");
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

        final List<WarnManager.WarningInfo> warnings = warnManager.getWarnings(target.getUniqueId());

        sender.sendMessage("§e======= WARNINGS FOR " + target.getName() + " =======");
        sender.sendMessage("§7Total warnings: §f" + warnings.size());

        if (warnings.isEmpty()) {
            sender.sendMessage("§7No warnings.");
        } else {
            for (final WarnManager.WarningInfo warn : warnings) {
                sender.sendMessage("§7[§f" + warn.number() + "§7] §cReason: §f" + warn.reason() +
                        "§7 | §cAdmin: §f" + warn.adminName());
            }
        }

        sender.sendMessage("§e=============================");

        return true;
    }
}
