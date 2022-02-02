package rip.bridge.practice.party.commands;

import rip.bridge.practice.PracticeLang;
import rip.bridge.practice.Practice;
import rip.bridge.practice.party.Party;
import rip.bridge.practice.party.PartyAccessRestriction;
import rip.bridge.qlib.command.Command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class PartyOpenCommand {

    @Command(names = {"party open", "p open", "t open", "team open", "f open", "party unlock", "p unlock", "t unlock", "team unlock", "f unlock"}, permission = "")
    public static void partyOpen(Player sender) {
        Party party = Practice.getInstance().getPartyHandler().getParty(sender);

        if (party == null) {
            sender.sendMessage(PracticeLang.NOT_IN_PARTY);
        } else if (!party.isLeader(sender.getUniqueId())) {
            sender.sendMessage(PracticeLang.NOT_LEADER_OF_PARTY);
        } else if (party.getAccessRestriction() == PartyAccessRestriction.PUBLIC) {
            sender.sendMessage(ChatColor.RED + "Your party is already open.");
        } else {
            party.setAccessRestriction(PartyAccessRestriction.PUBLIC);
            sender.sendMessage(ChatColor.YELLOW + "Your party is now " + ChatColor.GREEN + "open" + ChatColor.YELLOW + ".");
        }
    }

}
