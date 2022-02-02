package rip.bridge.practice.party.commands;

import rip.bridge.practice.PracticeLang;
import rip.bridge.practice.Practice;
import rip.bridge.practice.party.Party;
import rip.bridge.practice.party.PartyHandler;
import rip.bridge.practice.party.PartyUtils;
import rip.bridge.qlib.command.Command;

import org.bukkit.entity.Player;

public final class PartyTeamSplitCommand {

    @Command(names = {"party teamsplit", "p teamsplit", "t teamsplit", "team teamsplit", "f teamsplit"}, permission = "")
    public static void partyTeamSplit(Player sender) {
        PartyHandler partyHandler = Practice.getInstance().getPartyHandler();
        Party party = partyHandler.getParty(sender);

        if (party == null) {
            sender.sendMessage(PracticeLang.NOT_IN_PARTY);
        } else if (!party.isLeader(sender.getUniqueId())) {
            sender.sendMessage(PracticeLang.NOT_LEADER_OF_PARTY);
        } else {
            PartyUtils.startTeamSplit(party, sender);
        }
    }

}