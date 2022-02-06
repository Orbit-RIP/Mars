package rip.orbit.mars.tab;

import cc.fyre.proton.tab.construct.TabLayout;
import rip.orbit.mars.Mars;
import rip.orbit.mars.match.MatchHandler;
import cc.fyre.proton.util.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

final class HeaderLayoutProvider implements BiConsumer<Player, TabLayout> {

    @Override
    public void accept(Player player, TabLayout tabLayout) {
        header: {
            tabLayout.set(0, 1, ChatColor.GRAY + "Online: " + Bukkit.getOnlinePlayers().size());
            tabLayout.set(1, 0, "&6&lPractice");
            MatchHandler matchHandler = Mars.getInstance().getMatchHandler();
            tabLayout.set(2, 1, ChatColor.GRAY + "In Fights: " + matchHandler.countPlayersPlayingInProgressMatches());
        }

        status: {
            tabLayout.set(1, 1, "&7Your Connection", Math.max(((PlayerUtils.getPing(player) + 5) / 10) * 10, 1));
        }
    }
}
