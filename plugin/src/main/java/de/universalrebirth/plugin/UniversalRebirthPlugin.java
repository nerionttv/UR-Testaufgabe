package de.universalrebirth.plugin;

import de.universalrebirth.testaufgabe.command.framework.commands.RegisterCommands;
import de.universalrebirth.testaufgabe.command.framework.config.Configuration;
import de.universalrebirth.plugin.commands.HealCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class UniversalRebirthPlugin extends JavaPlugin {

    private RegisterCommands commands;
    private Configuration configuration;

    @Override
    public void onEnable() {

        /**
         * Create config.yml if not exists
         */
        saveDefaultConfig();

        /**
         * Init command framework
         */
        this.commands = new RegisterCommands(this);

        /**
         * FIX: pass plugin instance into configuration
         */
        this.configuration = new Configuration(this);

        /**
         * Register commands
         */
        new HealCommand(commands, configuration);

        getLogger().info("Plugin enabled!");
    }
}