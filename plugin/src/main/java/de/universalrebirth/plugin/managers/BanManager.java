package de.universalrebirth.plugin.managers;

import de.universalrebirth.testaufgabe.command.framework.config.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.UUID;

/**
 * BanManager
 * Verwaltet Ban-System
 * - Permanente Bans speichern
 * - Ban/Unban Operationen
 * - Ban-Checks bei Join
 */
public final class BanManager {

    private final Configuration bans;
    private final AdminLogger logger;

    public BanManager(final JavaPlugin plugin, final AdminLogger logger) {
        this.bans = new Configuration(plugin, "bans.yml");
        this.logger = Objects.requireNonNull(logger);
    }

    /**
     * Ban einen Spieler
     *
     * @param playerUuid UUID des Spielers
     * @param playerName Name des Spielers
     * @param reason     Grund für Ban
     * @param adminName  Name des Admins
     */
    public void banPlayer(final UUID playerUuid, final String playerName, final String reason, final String adminName) {
        Objects.requireNonNull(playerUuid);
        Objects.requireNonNull(playerName);

        final String path = "bans." + playerUuid;

        bans.set(path + ".name", playerName);
        bans.set(path + ".reason", reason != null ? reason : "No reason");
        bans.set(path + ".admin", adminName);
        bans.set(path + ".timestamp", System.currentTimeMillis());
        bans.save();

        logger.log(adminName, "BAN", playerName, reason != null ? reason : "No reason");
    }

    /**
     * Unban einen Spieler
     *
     * @param playerUuid UUID des Spielers
     * @param playerName Name des Spielers
     * @param adminName  Name des Admins
     */
    public void unbanPlayer(final UUID playerUuid, final String playerName, final String adminName) {
        Objects.requireNonNull(playerUuid);

        final String path = "bans." + playerUuid;

        if (!bans.contains(path)) {
            return;
        }

        bans.set(path, null);
        bans.save();

        logger.log(adminName, "UNBAN", playerName, "Unban");
    }

    /**
     * Check ob Spieler gebannt ist
     *
     * @param playerUuid UUID des Spielers
     * @return true wenn gebannt
     */
    public boolean isBanned(final UUID playerUuid) {
        return bans.contains("bans." + playerUuid);
    }

    /**
     * Get Ban-Information
     *
     * @param playerUuid UUID des Spielers
     * @return Ban-Info oder null
     */
    public BanInfo getBanInfo(final UUID playerUuid) {
        final String path = "bans." + playerUuid;

        if (!isBanned(playerUuid)) {
            return null;
        }

        return new BanInfo(
                bans.getString(path + ".name", "Unknown"),
                bans.getString(path + ".reason", "No reason"),
                bans.getString(path + ".admin", "Unknown"),
                bans.getLong(path + ".timestamp", 0)
        );
    }

    /**
     * Ban-Information Record
     */
    public record BanInfo(
            String playerName,
            String reason,
            String adminName,
            long timestamp
    ) {
    }
}
