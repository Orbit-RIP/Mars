package rip.orbit.mars.morpheus;

import com.qrakn.morpheus.game.Game;
import com.qrakn.morpheus.game.GameQueue;
import rip.orbit.mars.Mars;
import rip.orbit.mars.lobby.LobbyHandler;
import rip.orbit.mars.lobby.LobbyUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class EventTask extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            LobbyHandler handler = Mars.getInstance().getLobbyHandler();

            if (handler.isInLobby(player)) {
                List<Game> games = GameQueue.INSTANCE.getCurrentGames();
                Game game = GameQueue.INSTANCE.getCurrentGame(player);

                if (games.isEmpty()) {
                    if (player.getInventory().contains(Material.EMERALD)) {
                        player.getInventory().remove(Material.EMERALD);
                    }
                    continue;
                }

                if (game != null) continue;

                if (!player.getInventory().contains(EventItems.getEventItem()) && !Mars.getInstance().getPartyHandler().hasParty(player)) {
                    LobbyUtils.resetInventory(player);
                }

            } else {
                Game game = GameQueue.INSTANCE.getCurrentGame(player);
                if (game != null && game.getPlayers().contains(player) && player.getInventory().contains(EventItems.getEventItem())) {
                    player.getInventory().remove(Material.EMERALD);
                }
            }

        }
    }
}
