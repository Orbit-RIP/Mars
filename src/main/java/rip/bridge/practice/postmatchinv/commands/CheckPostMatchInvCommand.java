package rip.bridge.practice.postmatchinv.commands;

import rip.bridge.practice.Practice;
import rip.bridge.practice.postmatchinv.PostMatchInvHandler;
import rip.bridge.practice.postmatchinv.PostMatchPlayer;
import rip.bridge.practice.postmatchinv.menu.PostMatchMenu;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;
import rip.bridge.qlib.util.UUIDUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public final class CheckPostMatchInvCommand {

    @Command(names = { "checkPostMatchInv", "_" }, permission = "")
    public static void checkPostMatchInv(Player sender, @Param(name = "target") Player target) {
        PostMatchInvHandler postMatchInvHandler = Practice.getInstance().getPostMatchInvHandler();
        Map<UUID, PostMatchPlayer> players = postMatchInvHandler.getPostMatchData(sender.getUniqueId());

        if (players.containsKey(target.getUniqueId())) {
            new PostMatchMenu(players.get(target.getUniqueId())).openMenu(sender);
        } else {
            sender.sendMessage(ChatColor.RED + "Data for " + UUIDUtils.name(target.getUniqueId()) + " not found.");
        }
    }

}