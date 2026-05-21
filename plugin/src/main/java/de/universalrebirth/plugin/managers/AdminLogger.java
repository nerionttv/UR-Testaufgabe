package de.universalrebirth.plugin.managers;

import org.bukkit.Bukkit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * AdminLogger
 * Loggt alle Admin-Aktionen in die Konsole
 */
public final class AdminLogger {

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Log eine Admin-Aktion
     *
     * @param adminName Name des Admins
     * @param action    Aktion (BAN, KICK, MUTE, etc.)
     * @param target    Ziel (Spieler)
     * @param reason    Grund
     */
    public void log(final String adminName, final String action, final String target, final String reason) {
        Objects.requireNonNull(adminName);
        Objects.requireNonNull(action);
        Objects.requireNonNull(target);

        final String timestamp = LocalDateTime.now().format(formatter);
        final String message = String.format(
                "[%s] [ADMIN] [%s] Admin: %s | Target: %s | Reason: %s",
                timestamp,
                action,
                adminName,
                target,
                reason
        );

        Bukkit.getLogger().info(message);
    }
}
