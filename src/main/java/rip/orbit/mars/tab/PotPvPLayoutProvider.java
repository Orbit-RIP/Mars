package rip.orbit.mars.tab;

import cc.fyre.proton.tab.construct.TabLayout;
import cc.fyre.proton.tab.provider.LayoutProvider;
import rip.orbit.mars.Mars;
import rip.orbit.mars.match.Match;
import cc.fyre.proton.util.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.function.BiConsumer;

public final class PotPvPLayoutProvider implements LayoutProvider {

    static final int MAX_TAB_Y = 20;
    private static boolean testing = true;

    private final BiConsumer<Player, TabLayout> headerLayoutProvider = new HeaderLayoutProvider();
    private final BiConsumer<Player, TabLayout> lobbyLayoutProvider = new LobbyLayoutProvider();
    private final BiConsumer<Player, TabLayout> matchSpectatorLayoutProvider = new MatchSpectatorLayoutProvider();
    private final BiConsumer<Player, TabLayout> matchParticipantLayoutProvider = new MatchParticipantLayoutProvider();
    private final BiConsumer<Player, TabLayout> onlinePlayersLayoutProvider = new OnlinePlayersLayoutProvider();
    private final BiConsumer<Player, TabLayout> alternateOnlinePlayersLayoutProvider = new AlternateOnlinePlayersLayoutProvider();

    @Override
    public TabLayout provide(Player player) {
        if (Mars.getInstance() == null) return TabLayout.create(player);
        TabLayout tabLayout = TabLayout.create(player);

        if (Mars.getInstance().getDominantColor() == ChatColor.LIGHT_PURPLE) {
            alternateOnlinePlayersLayoutProvider.accept(player, tabLayout);
        } else {
            Match match = Mars.getInstance().getMatchHandler().getMatchPlayingOrSpectating(player);
            headerLayoutProvider.accept(player, tabLayout);

            if (match != null) {
                if (match.isSpectator(player.getUniqueId())) {
                    matchSpectatorLayoutProvider.accept(player, tabLayout);
                } else {
                    matchParticipantLayoutProvider.accept(player, tabLayout);
                }
            } else {
                lobbyLayoutProvider.accept(player, tabLayout);
            }

            onlinePlayersLayoutProvider.accept(player, tabLayout);
        }

        onlinePlayersLayoutProvider.accept(player, tabLayout);

        return tabLayout;
    }

    static int getPingOrDefault(UUID check) {
        Player player = Bukkit.getPlayer(check);
        return player != null ? PlayerUtils.getPing(player) : 0;
    }

}