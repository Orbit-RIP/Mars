
package rip.orbit.mars.scoreboard;

import com.qrakn.morpheus.game.Game;
import com.qrakn.morpheus.game.GameQueue;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.function.BiConsumer;

final class GameScoreGetter implements BiConsumer<Player, LinkedList<String>> {

    @Override
    public void accept(Player player, LinkedList<String> scores) {
        Game game = GameQueue.INSTANCE.getCurrentGame(player);

        if (game == null) return;
        if (!game.getPlayers().contains(player)) return;

        scores.addAll(game.getEvent().getScoreboardScores(player, game));
    }

}