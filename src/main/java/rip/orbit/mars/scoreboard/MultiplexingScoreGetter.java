package rip.orbit.mars.scoreboard;

import cc.fyre.proton.scoreboard.construct.ScoreGetter;
import com.qrakn.morpheus.game.Game;
import com.qrakn.morpheus.game.GameQueue;
import com.qrakn.morpheus.game.GameState;
import rip.orbit.mars.Mars;
import rip.orbit.mars.match.MatchHandler;
import rip.orbit.mars.setting.Setting;
import rip.orbit.mars.setting.SettingHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.function.BiConsumer;

final class MultiplexingScoreGetter implements ScoreGetter {

    private final BiConsumer<Player, LinkedList<String>> matchScoreGetter;
    private final BiConsumer<Player, LinkedList<String>> lobbyScoreGetter;
    private final BiConsumer<Player, LinkedList<String>> gameScoreGetter;

    MultiplexingScoreGetter(
        BiConsumer<Player, LinkedList<String>> matchScoreGetter,
        BiConsumer<Player, LinkedList<String>> lobbyScoreGetter,
        BiConsumer<Player, LinkedList<String>> gameScoreGetter
    ) {
        this.matchScoreGetter = matchScoreGetter;
        this.lobbyScoreGetter = lobbyScoreGetter;
        this.gameScoreGetter = gameScoreGetter;
    }

    @Override
    public String[] getScores(LinkedList<String> scores, Player player) {
        if (Mars.getInstance() == null) return new String[0];
        MatchHandler matchHandler = Mars.getInstance().getMatchHandler();
        SettingHandler settingHandler = Mars.getInstance().getSettingHandler();

        if (settingHandler.getSetting(player, Setting.SHOW_SCOREBOARD)) {
            if (matchHandler.isPlayingOrSpectatingMatch(player)) {
                matchScoreGetter.accept(player, scores);
            } else {
                Game game = GameQueue.INSTANCE.getCurrentGame(player);

                if (game != null && game.getPlayers().contains(player) && game.getState() != GameState.ENDED) {
                    gameScoreGetter.accept(player, scores);
                } else {
                    lobbyScoreGetter.accept(player, scores);
                }
            }
        }

        if (!scores.isEmpty()) {
            scores.addFirst("&a&7&m--------------------");
            scores.add("&7&b&4");
            scores.add(ChatColor.GRAY.toString() + ChatColor.ITALIC + "orbit.rip");
            scores.add("&f&7&m--------------------");
        }
        return new String[0];
    }

}