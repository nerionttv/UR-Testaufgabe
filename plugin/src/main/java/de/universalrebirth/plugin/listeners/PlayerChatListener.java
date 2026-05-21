package de.universalrebirth.plugin.listeners;

import de.universalrebirth.plugin.managers.MuteManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Objects;

/**
 * PlayerChatListener
 * - Mute-Check
 * - Prevents muted players from chatting
 */
public class PlayerChatListener implements Listener {

    private final MuteManager muteManager;

    public PlayerChatListener(final MuteManager muteManager) {
        this.muteManager = Objects.requireNonNull(muteManager);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (muteManager.isMuted(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cYou are muted and cannot chat.");
        }
    }
}
