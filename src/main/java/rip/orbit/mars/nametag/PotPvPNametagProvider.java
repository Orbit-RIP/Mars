package rip.orbit.mars.nametag;

import cc.fyre.proton.nametag.NametagInfo;
import cc.fyre.proton.nametag.NametagProvider;
import com.qrakn.morpheus.game.Game;
import com.qrakn.morpheus.game.GameQueue;
import com.qrakn.morpheus.game.GameState;
import rip.orbit.mars.Mars;
import rip.orbit.mars.follow.FollowHandler;
import rip.orbit.mars.match.Match;
import rip.orbit.mars.match.MatchHandler;
import rip.orbit.mars.match.MatchTeam;
import rip.orbit.mars.pvpclasses.pvpclasses.ArcherClass;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class PotPvPNametagProvider {

//    public PotPvPNametagProvider() {
//        super("PotPvP Provider", 5);
//    }
//
//    @Override
//    public NametagInfo fetchNametag(Player toRefresh, Player refreshFor) {
//        Game game = GameQueue.INSTANCE.getCurrentGame(toRefresh);
//
//        if (game != null && game.getPlayers().contains(toRefresh) && game.getPlayers().contains(refreshFor) && game.getState() != GameState.ENDED) {
//            return createNametag(game.getEvent().getNameTag(game, toRefresh, refreshFor), "");
//        }
//
//        ChatColor prefixColor = getNameColor(toRefresh, refreshFor);
//        return createNametag(prefixColor.toString(), "");
//    }

    public static ChatColor getNameColor(Player toRefresh, Player refreshFor) {
        MatchHandler matchHandler = Mars.getInstance().getMatchHandler();

        if (matchHandler.isPlayingOrSpectatingMatch(toRefresh)) {
            return getNameColorMatch(toRefresh, refreshFor);
        } else {
            return getNameColorLobby(toRefresh, refreshFor);
        }
    }

    private static ChatColor getNameColorMatch(Player toRefresh, Player refreshFor) {
        MatchHandler matchHandler = Mars.getInstance().getMatchHandler();

        Match toRefreshMatch = matchHandler.getMatchPlayingOrSpectating(toRefresh);
        MatchTeam toRefreshTeam = toRefreshMatch.getTeam(toRefresh.getUniqueId());

        // they're a spectator, so we see them as gray
        if (toRefreshTeam == null) {
            return ChatColor.GRAY;
        }

        MatchTeam refreshForTeam = toRefreshMatch.getTeam(refreshFor.getUniqueId());

        // if we can't find a current team, check if they have any
        // previously teams we can use for this
        if (refreshForTeam == null) {
            refreshForTeam = toRefreshMatch.getPreviousTeam(refreshFor.getUniqueId());
        }

        // if we were/are both on teams display a friendly/enemy color
        if (refreshForTeam != null) {
            if (toRefreshTeam == refreshForTeam) {
                return ChatColor.GREEN;
            } else {
                if (ArcherClass.getMarkedPlayers().containsKey(toRefresh.getName()) && System.currentTimeMillis() < ArcherClass.getMarkedPlayers().get(toRefresh.getName())) {
                    return ChatColor.YELLOW;
                }
                return ChatColor.RED;
            }
        }

        // if we're a spectator just display standard colors
        List<MatchTeam> teams = toRefreshMatch.getTeams();

        // we have predefined colors for 'normal' matches
        if (teams.size() == 2) {
            // team 1 = LIGHT_PURPLE, team 2 = AQUA
            if (toRefreshTeam == teams.get(0)) {
                return ChatColor.LIGHT_PURPLE;
            } else {
                return ChatColor.AQUA;
            }
        } else {
            // we don't have colors defined for larger matches
            // everyone is just red for spectators
            return ChatColor.RED;
        }
    }

    private static ChatColor getNameColorLobby(Player toRefresh, Player refreshFor) {
        FollowHandler followHandler = Mars.getInstance().getFollowHandler();

        Optional<UUID> following = followHandler.getFollowing(refreshFor);
        boolean refreshForFollowingTarget = following.isPresent() && following.get().equals(toRefresh.getUniqueId());

        if (refreshForFollowingTarget) {
            return ChatColor.AQUA;
        } else {
            return ChatColor.GREEN;
        }
    }
}