package de.universalrebirth.plugin.managers;

import de.universalrebirth.testaufgabe.command.framework.config.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.UUID;

/**
 * MuteManager
 * Verwaltet Mute-System
 * - Permanente & Temporäre Mutes
 * - Mute/Unmute Operationen
 * - Chat-Checks
 */
public final class MuteManager {

    private final Configuration mutes;
    private final AdminLogger logger;

    public MuteManager(final JavaPlugin plugin, final AdminLogger logger) {
        this.mutes = new Configuration(plugin, "mutes.yml");
        this.logger = Objects.requireNonNull(logger);
    }

    /**
     * Mute einen Spieler
     *
     * @param playerUuid   UUID des Spielers
     * @param playerName   Name des Spielers
     * @param durationMins Dauer in Minuten (0 = permanent)
     * @param reason       Grund für Mute
     * @param adminName    Name des Admins
     */
    public void mutePlayer(final UUID playerUuid, final String playerName,
                           final long durationMins, final String reason, final String adminName) {
        Objects.requireNonNull(playerUuid);
        Objects.requireNonNull(playerName);

        final String path = "mutes." + playerUuid;
        final long expiresAt = durationMins > 0
                ? System.currentTimeMillis() + (durationMins * 60 * 1000)
                : 0; // 0 = permanent

        mutes.set(path + ".name", playerName);
        mutes.set(path + ".reason", reason != null ? reason : "No reason");
        mutes.set(path + ".admin", adminName);
        mutes.set(path + ".timestamp", System.currentTimeMillis());
        mutes.set(path + ".expiresAt", expiresAt);
        mutes.save();

        final String durationStr = durationMins > 0
                ? durationMins + " minutes"
                : "permanent";
        logger.log(adminName, "MUTE", playerName, reason + " (" + durationStr + ")");
    }

    /**
     * Unmute einen Spieler
     *
     * @param playerUuid UUID des Spielers
     * @param playerName Name des Spielers
     * @param adminName  Name des Admins
     */
    public void unmutePlayer(final UUID playerUuid, final String playerName, final String adminName) {
        Objects.requireNonNull(playerUuid);

        final String path = "mutes." + playerUuid;

        if (!mutes.contains(path)) {
            return;
        }

        mutes.set(path, null);
        mutes.save();

        logger.log(adminName, "UNMUTE", playerName, "Unmute");
    }

    /**
     * Check ob Spieler gemuted ist
     *
     * @param playerUuid UUID des Spielers
     * @return true wenn gemuted
     */
    public boolean isMuted(final UUID playerUuid) {
        if (!mutes.contains("mutes." + playerUuid)) {
            return false;
        }

        final long expiresAt = mutes.getLong("mutes." + playerUuid + ".expiresAt", 0);

        // Wenn expiresAt = 0, ist es permanent
        if (expiresAt == 0) {
            return true;
        }

        // Wenn expiresAt > jetzt, ist es noch aktiv
        if (System.currentTimeMillis() < expiresAt) {
            return true;
        }

        // Mute ist abgelaufen - automatisch entfernen
        unmutePlayer(playerUuid, mutes.getString("mutes." + playerUuid + ".name", "Unknown"), "System");
        return false;
    }

    /**
     * Get Mute-Information
     *
     * @param playerUuid UUID des Spielers
     * @return Mute-Info oder null
     */
    public MuteInfo getMuteInfo(final UUID playerUuid) {
        final String path = "mutes." + playerUuid;

        if (!isMuted(playerUuid)) {
            return null;
        }

        final long expiresAt = mutes.getLong(path + ".expiresAt", 0);
        final long remainingMs = expiresAt > 0 ? expiresAt - System.currentTimeMillis() : 0;

        return new MuteInfo(
                mutes.getString(path + ".name", "Unknown"),
                mutes.getString(path + ".reason", "No reason"),
                mutes.getString(path + ".admin", "Unknown"),
                mutes.getLong(path + ".timestamp", 0),
                expiresAt,
                remainingMs
        );
    }

    /**
     * Mute-Information Record
     */
    public record MuteInfo(
            String playerName,
            String reason,
            String adminName,
            long timestamp,
            long expiresAt,
            long remainingMs
    ) {
        public boolean isPermanent() {
            return expiresAt == 0;
        }
    }
}
