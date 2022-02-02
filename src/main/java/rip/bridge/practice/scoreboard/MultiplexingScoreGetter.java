package rip.bridge.practice.scoreboard;

import rip.bridge.practice.Practice;
import rip.bridge.practice.match.MatchHandler;
import rip.bridge.practice.setting.Setting;
import rip.bridge.practice.setting.SettingHandler;
import rip.bridge.qlib.scoreboard.ScoreGetter;
import rip.bridge.qlib.util.LinkedList;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

final class MultiplexingScoreGetter implements ScoreGetter {

    private final BiConsumer<Player, LinkedList<String>> matchScoreGetter;
    private final BiConsumer<Player, LinkedList<String>> LobbyScoreGetter;

    MultiplexingScoreGetter(
            BiConsumer<Player, LinkedList<String>> matchScoreGetter,
            BiConsumer<Player, LinkedList<String>> LobbyScoreGetter
    ) {
        this.matchScoreGetter = matchScoreGetter;
        this.LobbyScoreGetter = LobbyScoreGetter;
    }

    @Override
    public void getScores(LinkedList<String> scores, Player player) {
        if (Practice.getInstance() == null) return;
        MatchHandler matchHandler = Practice.getInstance().getMatchHandler();
        SettingHandler settingHandler = Practice.getInstance().getSettingHandler();

        if (settingHandler.getSetting(player, Setting.SHOW_SCOREBOARD)) {
            if (matchHandler.isPlayingOrSpectatingMatch(player)) {
                matchScoreGetter.accept(player, scores);
                } else {
                    LobbyScoreGetter.accept(player, scores);
                }
        }

        if (!scores.isEmpty()) {
            scores.addFirst("&a&7&m--------------------");
            scores.add("&f&7&m--------------------");
        }
    }

}