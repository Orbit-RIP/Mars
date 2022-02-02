package rip.bridge.practice.rematch.listener;

import rip.bridge.practice.Practice;
import rip.bridge.practice.match.event.MatchTerminateEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class RematchGeneralListener implements Listener {

    @EventHandler
    public void onMatchTerminate(MatchTerminateEvent event) {
        Practice.getInstance().getRematchHandler().registerRematches(event.getMatch());
    }

}