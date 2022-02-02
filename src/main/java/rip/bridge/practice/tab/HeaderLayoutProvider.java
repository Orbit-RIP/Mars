package rip.bridge.practice.tab;

import rip.bridge.practice.Practice;
import rip.bridge.practice.match.MatchHandler;
import rip.bridge.qlib.tab.TabLayout;
import rip.bridge.qlib.util.PlayerUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

final class HeaderLayoutProvider implements BiConsumer<Player, TabLayout> {

    @Override
    public void accept(Player player, TabLayout tabLayout) {
        MatchHandler matchHandler = Practice.getInstance().getMatchHandler();

        header: {
            tabLayout.set(1, 0, ChatColor.GOLD.toString() + ChatColor.BOLD + "Practice");
        }

        status: {
            tabLayout.set(0, 1, ChatColor.GRAY + "Online: " + Bukkit.getOnlinePlayers().size());
            tabLayout.set(1, 1, ChatColor.GRAY + "Your Connection", Math.max(((PlayerUtils.getPing(player) + 5) / 10) * 10, 1));
            tabLayout.set(2, 1, ChatColor.GRAY + "In Fights: " + matchHandler.countPlayersPlayingInProgressMatches());
        }
    }

}