package rip.bridge.practice.util;

import rip.bridge.practice.Practice;
import rip.bridge.practice.follow.FollowHandler;
import rip.bridge.practice.match.Match;
import rip.bridge.practice.match.MatchHandler;
import rip.bridge.practice.party.Party;
import rip.bridge.practice.party.PartyHandler;
import rip.bridge.practice.setting.Setting;
import rip.bridge.practice.setting.SettingHandler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class VisibilityUtils {

    public static void updateVisibilityFlicker(Player target) {
        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            target.hidePlayer(otherPlayer);
            otherPlayer.hidePlayer(target);
        }

        Bukkit.getScheduler().runTaskLater(Practice.getInstance(), () -> updateVisibility(target), 10L);
    }

    public static void updateVisibility(Player target) {
        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            if (shouldSeePlayer(otherPlayer, target)) {
                otherPlayer.showPlayer(target);
            } else {
                otherPlayer.hidePlayer(target);
            }

            if (shouldSeePlayer(target, otherPlayer)) {
                target.showPlayer(otherPlayer);
            } else {
                target.hidePlayer(otherPlayer);
            }
        }
    }

    private static boolean shouldSeePlayer(Player viewer, Player target) {
        SettingHandler settingHandler = Practice.getInstance().getSettingHandler();
        FollowHandler followHandler = Practice.getInstance().getFollowHandler();
        PartyHandler partyHandler = Practice.getInstance().getPartyHandler();
        MatchHandler matchHandler = Practice.getInstance().getMatchHandler();

        Match targetMatch = matchHandler.getMatchPlayingOrSpectating(target);

        if (targetMatch == null) {
            // we're not in a match so we hide other players based on their party/match
            Party targetParty = partyHandler.getParty(target);
            Optional<UUID> following = followHandler.getFollowing(viewer);

            boolean viewerPlayingMatch = matchHandler.isPlayingOrSpectatingMatch(viewer);
            boolean viewerSameParty = targetParty != null && targetParty.isMember(viewer.getUniqueId());
            boolean viewerFollowingTarget = following.isPresent() && following.get().equals(target.getUniqueId());

            return viewerPlayingMatch || viewerSameParty || viewerFollowingTarget;
        } else {
            // we're in a match so we only hide other spectators (if our settings say so)
            boolean targetIsSpectator = targetMatch.isSpectator(target.getUniqueId());
            boolean viewerSpecSetting = settingHandler.getSetting(viewer, Setting.VIEW_OTHER_SPECTATORS);
            boolean viewerIsSpectator = matchHandler.isSpectatingMatch(viewer);

            return !targetIsSpectator || (viewerSpecSetting && viewerIsSpectator && !target.hasMetadata("ModMode"));
        }
    }

}