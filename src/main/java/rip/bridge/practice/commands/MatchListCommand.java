package rip.bridge.practice.commands;

import rip.bridge.practice.Practice;
import rip.bridge.practice.match.Match;
import rip.bridge.practice.match.MatchHandler;
import rip.bridge.qlib.command.Command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Set;

public final class MatchListCommand {

    @Command(names = {"match list"}, permission = "op")
    public static void matchList(Player sender) {
        MatchHandler matchHandler = Practice.getInstance().getMatchHandler();
        Match match = matchHandler.getMatchPlayingOrSpectating(sender);

            if (match != null) {
                sender.sendMessage(ChatColor.DARK_PURPLE.toString() + ChatColor.STRIKETHROUGH + "---------------------------------------------");
                sender.sendMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "Match List");
                sender.sendMessage(" ");
                sender.sendMessage(ChatColor.RED + match.getSimpleDescription(true));
            } else {
                sender.sendMessage(ChatColor.DARK_PURPLE.toString() + ChatColor.STRIKETHROUGH + "---------------------------------------------");
                sender.sendMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "Match List");
                sender.sendMessage(" ");
                sender.sendMessage(ChatColor.RED + ChatColor.ITALIC.toString() + "No matches found...");
            }
            sender.sendMessage(ChatColor.DARK_PURPLE.toString() + ChatColor.STRIKETHROUGH + "---------------------------------------------");
        }
    }