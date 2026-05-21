package de.universalrebirth.plugin.commands;

import de.universalrebirth.testaufgabe.command.framework.commands.RegisterCommands;
import de.universalrebirth.testaufgabe.command.framework.config.Configuration;
import org.bukkit.entity.Player;

public final class HealCommand {

    private final RegisterCommands commands;
    private final Configuration config;

    public HealCommand(RegisterCommands commands, Configuration config) {
        this.commands = commands;
        this.config = config;

        register();
    }

    /**
     * Registers the heal command.
     */
    private void register() {

        commands.command(config.get("command"))
                .description(config.get("command_description"))
                .permission(config.get("command_permission"))
                .aliases(config.get("command_aliases"))
                .executor((ctx, args) -> {

                    if (!ctx.isPlayer()) {
                        ctx.reply(config.get("command_reply_only_players"));
                        return;
                    }

                    Player player = ctx.player();

                    player.setHealth(20.0);

                    ctx.reply(config.get("command_reply_success"));
                })
                .register();
    }
}