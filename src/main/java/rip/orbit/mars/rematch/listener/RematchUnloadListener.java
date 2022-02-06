package rip.orbit.mars.rematch.listener;

import rip.orbit.mars.Mars;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class RematchUnloadListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Mars.getInstance().getRematchHandler().unloadRematchData(event.getPlayer());
    }

}