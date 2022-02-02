package rip.bridge.practice.duel.listener;

import rip.bridge.practice.Practice;
import rip.bridge.practice.duel.DuelHandler;
import rip.bridge.practice.match.MatchTeam;
import rip.bridge.practice.match.event.MatchCountdownStartEvent;
import rip.bridge.practice.match.event.MatchSpectatorJoinEvent;
import rip.bridge.practice.party.Party;
import rip.bridge.practice.party.event.PartyDisbandEvent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public final class DuelListener implements Listener {

    @EventHandler
    public void onMatchSpectatorJoin(MatchSpectatorJoinEvent event) {
        DuelHandler duelHandler = Practice.getInstance().getDuelHandler();
        Player player = event.getSpectator();

        duelHandler.removeInvitesTo(player);
        duelHandler.removeInvitesFrom(player);
    }

    @EventHandler
    public void onPartyDisband(PartyDisbandEvent event) {
        DuelHandler duelHandler = Practice.getInstance().getDuelHandler();
        Party party = event.getParty();

        duelHandler.removeInvitesTo(party);
        duelHandler.removeInvitesFrom(party);
    }

    @EventHandler
    public void onMatchCountdownStart(MatchCountdownStartEvent event) {
        DuelHandler duelHandler = Practice.getInstance().getDuelHandler();

        for (MatchTeam team : event.getMatch().getTeams()) {
            for (UUID member : team.getAllMembers()) {
                Player memberPlayer = Bukkit.getPlayer(member);

                duelHandler.removeInvitesTo(memberPlayer);
                duelHandler.removeInvitesFrom(memberPlayer);
            }
        }
    }

}