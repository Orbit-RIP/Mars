package rip.bridge.practice.commands;

import rip.bridge.practice.Practice;
import rip.bridge.practice.match.Match;
import rip.bridge.practice.match.MatchHandler;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class MatchStatusCommand {

    @Command(names = { "match status" }, permission = "")
    public static void matchStatus(CommandSender sender, @Param(name = "target") Player target) {
        MatchHandler matchHandler = Practice.getInstance().getMatchHandler();
        Match match = matchHandler.getMatchPlayingOrSpectating(target);

        if (match == null) {
            sender.sendMessage(ChatColor.RED + target.getName() + " is not playing in or spectating a match.");
            return;
        }

        for (String line : Practice.getGson().toJson(match).split("\n")) {
            sender.sendMessage("  " + ChatColor.GRAY + line);
        }
    }

}