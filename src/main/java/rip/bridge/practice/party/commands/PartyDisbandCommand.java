package rip.bridge.practice.party.commands;

import rip.bridge.practice.PracticeLang;
import rip.bridge.practice.Practice;
import rip.bridge.practice.chat.modes.PartyChatMode;
import rip.bridge.practice.party.Party;
import rip.bridge.qlib.chat.ChatHandler;
import rip.bridge.qlib.chat.ChatPlayer;
import rip.bridge.qlib.command.Command;

import org.bukkit.entity.Player;

public final class PartyDisbandCommand {

    @Command(names = {"party disband", "p disband", "t disband", "team disband", "f disband"}, permission = "")
    public static void partyDisband(Player sender) {
        Party party = Practice.getInstance().getPartyHandler().getParty(sender);

        if (party == null) {
            sender.sendMessage(PracticeLang.NOT_IN_PARTY);
            return;
        }

        if (!party.isLeader(sender.getUniqueId())) {
            sender.sendMessage(PracticeLang.NOT_LEADER_OF_PARTY);
            return;
        }

        ChatPlayer chatPlayer = ChatHandler.getChatPlayer(sender.getUniqueId());
        chatPlayer.removeProvider(new PartyChatMode());
        party.disband();
    }

}