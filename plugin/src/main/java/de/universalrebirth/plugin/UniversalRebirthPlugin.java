package de.universalrebirth.plugin;

import de.universalrebirth.plugin.commands.*;
import de.universalrebirth.plugin.listeners.PlayerChatListener;
import de.universalrebirth.plugin.listeners.PlayerJoinListener;
import de.universalrebirth.plugin.managers.*;
import de.universalrebirth.testaufgabe.command.framework.commands.RegisterCommands;
import de.universalrebirth.testaufgabe.command.framework.listeners.RegisterListeners;
import org.bukkit.plugin.java.JavaPlugin;

public final class UniversalRebirthPlugin
        extends JavaPlugin {

    private RegisterCommands commands;
    private RegisterListeners listeners;

    // Managers
    private AdminLogger adminLogger;
    private BanManager banManager;
    private MuteManager muteManager;
    private WhitelistManager whitelistManager;
    private WarnManager warnManager;

    @Override
    public void onEnable() {

        /** Initialize Managers */
        this.adminLogger = new AdminLogger();
        this.banManager = new BanManager(this, adminLogger);
        this.muteManager = new MuteManager(this, adminLogger);
        this.whitelistManager = new WhitelistManager(this, adminLogger);
        this.warnManager = new WarnManager(this, adminLogger);

        getLogger().info("[UniversalRebirth-Core] Managers initialized.");

        /** Command Framework */
        this.commands = new RegisterCommands(this);
        this.listeners = new RegisterListeners(this);

        /** Register all Commands with Universal Framework */

        // Heal Command
        commands.register(new HealCommand(this));

        // Ban Commands
        commands.register(new BanCommand(this, banManager));
        commands.register(new UnbanCommand(this, banManager));
        commands.register(new BanlistCommand(this));

        // Kick Command
        commands.register(new KickCommand(this, adminLogger));

        // Mute Commands
        commands.register(new MuteCommand(this, muteManager));
        commands.register(new UnmuteCommand(this, muteManager));

        // Warn Commands
        commands.register(new WarnCommand(this, warnManager));
        commands.register(new WarningsCommand(this, warnManager));

        getLogger().info("[UniversalCommands] All commands registered successfully.");

        /** Register all Listeners with Universal Framework */

        listeners.register(new PlayerJoinListener(banManager, whitelistManager));
        listeners.register(new PlayerChatListener(muteManager));

        getLogger().info("[UniversalListeners] All listeners registered successfully.");
    }

    @Override
    public void onDisable() {

        /** Unregister all Commands */
        commands.unregister("heal");
        commands.unregister("ban");
        commands.unregister("unban");
        commands.unregister("banlist");
        commands.unregister("kick");
        commands.unregister("mute");
        commands.unregister("unmute");
        commands.unregister("warn");
        commands.unregister("warnings");

        getLogger().info("[UniversalRebirth-Core] Plugin disabled.");
    }
}