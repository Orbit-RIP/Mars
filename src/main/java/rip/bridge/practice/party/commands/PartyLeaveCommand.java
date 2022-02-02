package rip.bridge.practice.party.commands;

import rip.bridge.practice.PracticeLang;
import rip.bridge.practice.Practice;
import rip.bridge.practice.chat.modes.PartyChatMode;
import rip.bridge.practice.party.Party;
import rip.bridge.qlib.chat.ChatHandler;
import rip.bridge.qlib.chat.ChatPlayer;
import rip.bridge.qlib.command.Command;

import org.bukkit.entity.Player;

public final class PartyLeaveCommand {

    @Command(names = {"party leave", "p leave", "t leave", "team leave", "leave", "f leave"}, permission = "")
    public static void partyLeave(Player sender) {
        Party party = Practice.getInstance().getPartyHandler().getParty(sender);

        if (party == null) {
            sender.sendMessage(PracticeLang.NOT_IN_PARTY);
        } else {
            ChatPlayer chatPlayer = ChatHandler.getChatPlayer(sender.getUniqueId());
                chatPlayer.removeProvider(new PartyChatMode());
            party.leave(sender);
        }
    }

}