package de.universalrebirth.plugin.listeners;

import de.universalrebirth.plugin.managers.BanManager;
import de.universalrebirth.plugin.managers.WhitelistManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

/**
 * PlayerJoinListener
 * - Ban-Check
 * - Whitelist-Check
 * - Welcome Message
 */
public class PlayerJoinListener implements Listener {

    private final BanManager banManager;
    private final WhitelistManager whitelistManager;

    public PlayerJoinListener(final BanManager banManager, final WhitelistManager whitelistManager) {
        this.banManager = Objects.requireNonNull(banManager);
        this.whitelistManager = Objects.requireNonNull(whitelistManager);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // 1. Ban-Check
        if (banManager.isBanned(player.getUniqueId())) {
            final BanManager.BanInfo banInfo = banManager.getBanInfo(player.getUniqueId());
            player.kickPlayer("§c✗ You are banned!\n§cReason: " + (banInfo != null ? banInfo.reason() : "Unknown"));
            return;
        }

        // 2. Whitelist-Check
        if (whitelistManager.isEnabled() && !whitelistManager.isWhitelisted(player.getUniqueId())) {
            player.kickPlayer("§c✗ You are not whitelisted!\n§cContact an administrator.");
            return;
        }

        // 3. Welcome Message
        player.sendMessage("§a✓ Welcome to §bUNIVERSAL REBIRTH§a!");
        event.setJoinMessage("§e[JOIN] §f" + player.getName() + " §ejoined the game.");
    }
}
