package rip.orbit.mars.morpheus;

import cc.fyre.proton.Proton;
import cc.fyre.proton.nametag.FrozenNametagHandler;
import com.qrakn.morpheus.game.Game;
import com.qrakn.morpheus.game.GameState;
import com.qrakn.morpheus.game.bukkit.event.GameStateChangeEvent;
import com.qrakn.morpheus.game.bukkit.event.PlayerGameInteractionEvent;
import com.qrakn.morpheus.game.bukkit.event.PlayerJoinGameEvent;
import com.qrakn.morpheus.game.bukkit.event.PlayerQuitGameEvent;
import rip.orbit.mars.Mars;
import rip.orbit.mars.morpheus.menu.EventsMenu;
import rip.orbit.mars.util.VisibilityUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class EventListeners implements Listener {

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getItem() != null && event.getItem().equals(EventItems.getEventItem()) && Mars.getInstance().getLobbyHandler().isInLobby(player)) {
            /*if (GameQueue.INSTANCE.getCurrentGames().size() == 1) {
                Game game = GameQueue.INSTANCE.getCurrentGames().get(0);
                if (game.getState() == GameState.STARTING) {
                    if (game.getMaxPlayers() > 0 && game.getPlayers().size() >= game.getMaxPlayers()) {
                        player.sendMessage(ChatColor.RED + "This event is currently full! Sorry!");
                        return;
                    }
                    game.add(player);
                } else {
                    game.addSpectator(player);
                }
                return;
            }*/
            new EventsMenu().openMenu(player);
        }

    }

    @EventHandler
    public void onGameStateChangeEvent(GameStateChangeEvent event) {
        Game game = event.getGame();

        if (event.getTo() == GameState.ENDED) {
            Mars.getInstance().getArenaHandler().releaseArena(game.getArena());
            for (Player player : game.getPlayers()) {
                FrozenNametagHandler.reloadPlayer(player);
                FrozenNametagHandler.reloadOthersFor(player);
                VisibilityUtils.updateVisibility(player);
                Mars.getInstance().getLobbyHandler().returnToLobby(player);
            }
        }
    }

    @EventHandler
    public void onPlayerJoinGameEvent(PlayerJoinGameEvent event) {
        FrozenNametagHandler.reloadPlayer(event.getPlayer());
        FrozenNametagHandler.reloadOthersFor(event.getPlayer());
        for (Player player : event.getGame().getPlayers()) {
            VisibilityUtils.updateVisibility(player);
        }
    }

    @EventHandler
    public void onPlayerQuitGameEvent(PlayerQuitGameEvent event) {
        FrozenNametagHandler.reloadPlayer(event.getPlayer());
        FrozenNametagHandler.reloadOthersFor(event.getPlayer());
        Mars.getInstance().getLobbyHandler().returnToLobby(event.getPlayer());
    }

    @EventHandler
    public void onPlayerGameInteractionEvent(PlayerGameInteractionEvent event) {
        FrozenNametagHandler.reloadPlayer(event.getPlayer());
        FrozenNametagHandler.reloadOthersFor(event.getPlayer());
        VisibilityUtils.updateVisibility(event.getPlayer());
    }

}
