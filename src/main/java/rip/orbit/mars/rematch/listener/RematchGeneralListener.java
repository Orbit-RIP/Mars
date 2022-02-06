package rip.orbit.mars.rematch.listener;

import rip.orbit.mars.Mars;
import rip.orbit.mars.match.event.MatchTerminateEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class RematchGeneralListener implements Listener {

    @EventHandler
    public void onMatchTerminate(MatchTerminateEvent event) {
        Mars.getInstance().getRematchHandler().registerRematches(event.getMatch());
    }

}