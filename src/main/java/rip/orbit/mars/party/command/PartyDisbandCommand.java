package rip.orbit.mars.party.command;

import rip.orbit.mars.MarsLang;
import rip.orbit.mars.Mars;
import rip.orbit.mars.party.Party;
import cc.fyre.proton.command.Command;

import org.bukkit.entity.Player;

public final class PartyDisbandCommand {

    @Command(names = {"party disband", "p disband", "t disband", "team disband", "f disband"}, permission = "")
    public static void partyDisband(Player sender) {
        Party party = Mars.getInstance().getPartyHandler().getParty(sender);

        if (party == null) {
            sender.sendMessage(MarsLang.NOT_IN_PARTY);
            return;
        }

        if (!party.isLeader(sender.getUniqueId())) {
            sender.sendMessage(MarsLang.NOT_LEADER_OF_PARTY);
            return;
        }

        party.disband();
    }

}