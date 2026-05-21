package de.universalrebirth.plugin.managers;

import de.universalrebirth.testaufgabe.command.framework.config.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * WarnManager
 * Verwaltet Warning-System
 * - Warn Spieler
 * - Warnings tracken
 * - Clear Warnings
 */
public final class WarnManager {

    private final Configuration warnings;
    private final AdminLogger logger;

    public WarnManager(final JavaPlugin plugin, final AdminLogger logger) {
        this.warnings = new Configuration(plugin, "warnings.yml");
        this.logger = Objects.requireNonNull(logger);
    }

    /**
     * Warn einen Spieler
     *
     * @param playerUuid UUID des Spielers
     * @param playerName Name des Spielers
     * @param reason     Grund für Warning
     * @param adminName  Name des Admins
     */
    public void warnPlayer(final UUID playerUuid, final String playerName, final String reason, final String adminName) {
        Objects.requireNonNull(playerUuid);
        Objects.requireNonNull(playerName);

        final String basePath = "warnings." + playerUuid;
        final int count = getWarningCount(playerUuid) + 1;
        final String warnPath = basePath + ".warns." + count;

        // Spieler-Info aktualisieren falls neu
        if (!warnings.contains(basePath + ".name")) {
            warnings.set(basePath + ".name", playerName);
        }

        // Warning speichern
        warnings.set(warnPath + ".reason", reason != null ? reason : "No reason");
        warnings.set(warnPath + ".admin", adminName);
        warnings.set(warnPath + ".timestamp", System.currentTimeMillis());
        warnings.save();

        logger.log(adminName, "WARN", playerName, reason != null ? reason : "No reason");
    }

    /**
     * Get Warning-Count für einen Spieler
     *
     * @param playerUuid UUID des Spielers
     * @return Anzahl Warnings
     */
    public int getWarningCount(final UUID playerUuid) {
        Objects.requireNonNull(playerUuid);

        final String basePath = "warnings." + playerUuid + ".warns";

        if (!warnings.contains(basePath)) {
            return 0;
        }

        int count = 0;
        for (int i = 1; i <= 1000; i++) {
            if (warnings.contains(basePath + "." + i)) {
                count++;
            } else {
                break;
            }
        }

        return count;
    }

    /**
     * Get alle Warnings eines Spielers
     *
     * @param playerUuid UUID des Spielers
     * @return List von WarningInfo
     */
    public List<WarningInfo> getWarnings(final UUID playerUuid) {
        Objects.requireNonNull(playerUuid);

        final List<WarningInfo> result = new ArrayList<>();
        final String basePath = "warnings." + playerUuid + ".warns";

        int count = 1;
        while (warnings.contains(basePath + "." + count)) {
            final String warnPath = basePath + "." + count;

            result.add(new WarningInfo(
                    count,
                    warnings.getString(warnPath + ".reason", "No reason"),
                    warnings.getString(warnPath + ".admin", "Unknown"),
                    warnings.getLong(warnPath + ".timestamp", 0)
            ));

            count++;
        }

        return result;
    }

    /**
     * Clear alle Warnings eines Spielers
     *
     * @param playerUuid UUID des Spielers
     * @param playerName Name des Spielers
     * @param adminName  Name des Admins
     */
    public void clearWarnings(final UUID playerUuid, final String playerName, final String adminName) {
        Objects.requireNonNull(playerUuid);

        final String path = "warnings." + playerUuid;

        if (!warnings.contains(path)) {
            return;
        }

        warnings.set(path, null);
        warnings.save();

        logger.log(adminName, "CLEAR_WARNS", playerName, "");
    }

    /**
     * Warning-Information Record
     */
    public record WarningInfo(
            int number,
            String reason,
            String adminName,
            long timestamp
    ) {
    }
}
