package rip.bridge.practice.party.commands;

import rip.bridge.practice.PracticeLang;
import rip.bridge.practice.Practice;
import rip.bridge.practice.party.Party;
import rip.bridge.practice.party.PartyAccessRestriction;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class PartyPasswordCommand {

    @Command(names = {"party password", "p password", "t password", "team password", "party pass", "p pass", "t pass", "team pass", "f password", "f pass"}, permission = "")
    public static void partyPassword(Player sender, @Param(name = "password") String password) {
        Party party = Practice.getInstance().getPartyHandler().getParty(sender);

        if (party == null) {
            sender.sendMessage(PracticeLang.NOT_IN_PARTY);
        } else if (!party.isLeader(sender.getUniqueId())) {
            sender.sendMessage(PracticeLang.NOT_LEADER_OF_PARTY);
        } else {
            party.setAccessRestriction(PartyAccessRestriction.PASSWORD);
            party.setPassword(password);

            sender.sendMessage(ChatColor.YELLOW + "Your party's password is now " + ChatColor.AQUA + password + ChatColor.YELLOW + ".");
        }
    }

}
