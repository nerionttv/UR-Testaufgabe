package de.universalrebirth.plugin.managers;

import de.universalrebirth.testaufgabe.command.framework.config.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.UUID;

/**
 * WhitelistManager
 * Verwaltet Whitelist
 * - Enable/Disable Whitelist-Modus
 * - Add/Remove Spieler
 * - Whitelist-Checks
 */
public final class WhitelistManager {

    private final Configuration whitelist;
    private final AdminLogger logger;

    public WhitelistManager(final JavaPlugin plugin, final AdminLogger logger) {
        this.whitelist = new Configuration(plugin, "whitelist.yml");
        this.logger = Objects.requireNonNull(logger);
    }

    /**
     * Toggle Whitelist-Modus
     */
    public void toggleWhitelist() {
        final boolean newState = !isEnabled();
        whitelist.set("settings.enabled", newState);
        whitelist.save();

        logger.log("System", "WHITELIST_TOGGLE", "", newState ? "Enabled" : "Disabled");
    }

    /**
     * Enable/Disable Whitelist
     *
     * @param enabled true = enable, false = disable
     */
    public void setEnabled(final boolean enabled) {
        whitelist.set("settings.enabled", enabled);
        whitelist.save();

        logger.log("System", "WHITELIST_" + (enabled ? "ENABLED" : "DISABLED"), "", "");
    }

    /**
     * Check ob Whitelist enabled ist
     *
     * @return true wenn Whitelist active
     */
    public boolean isEnabled() {
        return whitelist.getBoolean("settings.enabled", false);
    }

    /**
     * Whitelist einen Spieler
     *
     * @param playerUuid UUID des Spielers
     * @param playerName Name des Spielers
     * @param adminName  Name des Admins
     */
    public void addPlayer(final UUID playerUuid, final String playerName, final String adminName) {
        Objects.requireNonNull(playerUuid);
        Objects.requireNonNull(playerName);

        final String path = "whitelisted." + playerUuid;

        whitelist.set(path + ".name", playerName);
        whitelist.set(path + ".addedAt", System.currentTimeMillis());
        whitelist.set(path + ".addedBy", adminName);
        whitelist.save();

        logger.log(adminName, "WHITELIST_ADD", playerName, "");
    }

    /**
     * Entferne Spieler aus Whitelist
     *
     * @param playerUuid UUID des Spielers
     * @param playerName Name des Spielers
     * @param adminName  Name des Admins
     */
    public void removePlayer(final UUID playerUuid, final String playerName, final String adminName) {
        Objects.requireNonNull(playerUuid);

        final String path = "whitelisted." + playerUuid;

        if (!whitelist.contains(path)) {
            return;
        }

        whitelist.set(path, null);
        whitelist.save();

        logger.log(adminName, "WHITELIST_REMOVE", playerName, "");
    }

    /**
     * Check ob Spieler whitelisted ist
     *
     * @param playerUuid UUID des Spielers
     * @return true wenn whitelisted
     */
    public boolean isWhitelisted(final UUID playerUuid) {
        return whitelist.contains("whitelisted." + playerUuid);
    }

    /**
     * Get Whitelist-Info
     *
     * @param playerUuid UUID des Spielers
     * @return Whitelist-Info oder null
     */
    public WhitelistInfo getInfo(final UUID playerUuid) {
        final String path = "whitelisted." + playerUuid;

        if (!isWhitelisted(playerUuid)) {
            return null;
        }

        return new WhitelistInfo(
                whitelist.getString(path + ".name", "Unknown"),
                whitelist.getLong(path + ".addedAt", 0),
                whitelist.getString(path + ".addedBy", "Unknown")
        );
    }

    /**
     * Whitelist-Information Record
     */
    public record WhitelistInfo(
            String playerName,
            long addedAt,
            String addedBy
    ) {
    }
}
