package rip.bridge.practice.match.listener;

import rip.bridge.practice.Practice;
import rip.bridge.practice.match.Match;
import rip.bridge.practice.match.MatchHandler;
import rip.bridge.practice.party.event.PartyMemberJoinEvent;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class MatchPartySpectateListener implements Listener {

    @EventHandler
    public void onPartyMemberJoin(PartyMemberJoinEvent event) {
        MatchHandler matchHandler = Practice.getInstance().getMatchHandler();
        Match leaderMatch = matchHandler.getMatchPlayingOrSpectating(Bukkit.getPlayer(event.getParty().getLeader()));

        if (leaderMatch != null) {
            leaderMatch.addSpectator(event.getMember(), null);
        }
    }

}