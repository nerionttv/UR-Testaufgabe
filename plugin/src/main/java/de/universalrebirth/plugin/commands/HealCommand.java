package de.universalrebirth.plugin.commands;

import de.universalrebirth.testaufgabe.command.framework.commands.RegisterCommands;
import de.universalrebirth.testaufgabe.command.framework.config.Configuration;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class HealCommand
        extends RegisterCommands.AbstractCommand {

    private final Configuration configuration;

    public HealCommand(final JavaPlugin plugin) {

        super("heal");

        /*
         * =========================================================
         * COMMAND META
         * =========================================================
         */

        description("Heals a player");
        permission("test.heal");

        aliases("hp", "healme");

        /*
         * =========================================================
         * CONFIG
         * =========================================================
         */

        this.configuration =
                new Configuration(
                        plugin,
                        "config.yml"
                );
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        /*
         * =========================================================
         * PLAYER CHECK
         * =========================================================
         */

        if (!(sender instanceof Player player)) {

            sender.sendMessage(
                    color(configuration.getString(
                            "messages.only-player",
                            "&cOnly players can use this command."
                    ))
            );

            return true;
        }

        /*
         * =========================================================
         * PERMISSION CHECK
         * =========================================================
         */

        if (!player.hasPermission(permission())) {

            player.sendMessage(
                    color(configuration.getString(
                            "messages.no-permission",
                            "&cYou do not have permission."
                    ))
            );

            return true;
        }

        /*
         * =========================================================
         * GAMEMODE CHECK
         * =========================================================
         */

        if (player.getGameMode() == GameMode.SPECTATOR) {

            player.sendMessage(
                    color(configuration.getString(
                            "messages.no-spectator",
                            "&cYou cannot heal in spectator mode."
                    ))
            );

            return true;
        }

        /*
         * =========================================================
         * HEAL PLAYER
         * =========================================================
         */

        player.setHealth(player.getMaxHealth());

        player.setFoodLevel(20);
        player.setSaturation(20F);
        player.setFireTicks(0);

        /*
         * =========================================================
         * SUCCESS MESSAGE
         * =========================================================
         */

        player.sendMessage(
                color(configuration.getString(
                        "messages.heal-success",
                        "&aYou have been healed."
                ))
        );

        return true;
    }

    /*
     * =========================================================
     * COLOR UTIL
     * =========================================================
     */

    private String color(final String text) {

        return text.replace("&", "§");
    }
}