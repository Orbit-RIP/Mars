package rip.bridge.practice.follow.command;

import rip.bridge.practice.Practice;
import rip.bridge.practice.follow.FollowHandler;
import rip.bridge.practice.match.Match;
import rip.bridge.practice.match.MatchHandler;
import rip.bridge.qlib.command.Command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class UnfollowCommand {

    @Command(names={"unfollow"}, permission = "")
    public static void unfollow(Player sender) {
        FollowHandler followHandler = Practice.getInstance().getFollowHandler();
        MatchHandler matchHandler = Practice.getInstance().getMatchHandler();

        if (!followHandler.getFollowing(sender).isPresent()) {
            sender.sendMessage(ChatColor.RED + "You're not following anybody.");
            return;
        }

        Match spectating = matchHandler.getMatchSpectating(sender);

        if (spectating != null) {
            spectating.removeSpectator(sender);
        }

        followHandler.stopFollowing(sender);
    }

}