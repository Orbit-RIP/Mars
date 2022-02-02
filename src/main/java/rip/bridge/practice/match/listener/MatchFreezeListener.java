package rip.bridge.practice.match.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import rip.bridge.practice.Practice;
import rip.bridge.practice.match.Match;
import rip.bridge.practice.match.MatchState;

public class MatchFreezeListener implements Listener {

    @EventHandler
    public void onCountdownEnd(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        Location from = event.getFrom();
        Location to = event.getTo();

        if (from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ()) return;

        Match match = Practice.getInstance().getMatchHandler().getMatchPlaying(player);

        if (match == null || !match.getKitType().getId().equals("SUMO") || match.getState() != MatchState.COUNTDOWN) return;

        event.getPlayer().teleport(from);
    }

}
