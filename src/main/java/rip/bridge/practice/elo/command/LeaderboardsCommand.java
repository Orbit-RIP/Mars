package rip.bridge.practice.elo.command;

import rip.bridge.practice.lobby.menu.StatisticsMenu;
import rip.bridge.qlib.command.Command;
import org.bukkit.entity.Player;

public class LeaderboardsCommand {

    @Command(names = {"leaderboards"}, permission = "")
    public static void leaderboards(Player sender) {

        new StatisticsMenu().openMenu(sender);
    }

}
