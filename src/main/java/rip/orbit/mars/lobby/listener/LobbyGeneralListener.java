package rip.orbit.mars.lobby.listener;

import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.InventoryHolder;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import rip.orbit.mars.Mars;
import rip.orbit.mars.lobby.LobbyHandler;
import rip.orbit.mars.lobby.listener.LobbyParkourListener.Parkour;
import cc.fyre.proton.menu.Menu;
import rip.orbit.nebula.util.CC;

import java.util.Arrays;
import java.util.List;

public final class LobbyGeneralListener implements Listener {

    private final LobbyHandler lobbyHandler;

    public LobbyGeneralListener(LobbyHandler lobbyHandler) {
        this.lobbyHandler = lobbyHandler;
    }

    @EventHandler
    public void onPlayerSpawnLocation(PlayerSpawnLocationEvent event) {
        Parkour parkour = LobbyParkourListener.getParkourMap().get(event.getPlayer().getUniqueId());
        if (parkour != null && parkour.getCheckpoint() != null) {
            event.setSpawnLocation(parkour.getCheckpoint().getLocation());
            return;
        }

        event.setSpawnLocation(lobbyHandler.getLobbyLocation());
    }

    List<String> welcomeMessage = Arrays.asList(
            CC.CHAT_BAR,
            "&6&lPractice &7[Season 1]",
            CC.CHAT_BAR,
            "&6&l┃ &fSeason Started: &6" + Mars.getInstance().getConfig().getString("started", "February 20th, 2021"),
            "&6&l┃ &fSeason Ending: &6" + Mars.getInstance().getConfig().getString("ending", "March 20th, 2021"),
            "&6&l┃ &fTournaments: &6Start Every 30 minutes",
            "&6&l┃ &e&l* NEW LADDERS * &fBase Raiding, Varied Base Raiding, PearlFight, and Boxing",
            " ",
            "&7&oEdit a ladders kit to your liking by",
            "&7&oclicking the book in your hot bar!",
            CC.CHAT_BAR
    );

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        lobbyHandler.returnToLobby(event.getPlayer());

        for (String s : welcomeMessage) {
            event.getPlayer().sendMessage(CC.translate(s));
        }

    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player) event.getEntity();

        if (lobbyHandler.isInLobby(player)) {
            if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                lobbyHandler.returnToLobby(player);
            }

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (lobbyHandler.isInLobby((Player) event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if (lobbyHandler.isInLobby(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if (!lobbyHandler.isInLobby(player)) {
            return;
        }

        Menu openMenu = Menu.getCurrentlyOpenedMenus().get(player.getUniqueId());

        // just remove the item for players in these menus, so they can 'drop' items to remove them
        // same thing for admins in build mode, just pretend to drop the item
        if (player.hasMetadata("Build") || (openMenu != null && openMenu.isNoncancellingInventory())) {
            event.getItemDrop().remove();
        } else {
            event.setCancelled(true);
        }
    }

    // cancel inventory interaction in the lobby except for menus
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player clicked = (Player) event.getWhoClicked();

        if (!lobbyHandler.isInLobby(clicked) || clicked.hasMetadata("Build") || Menu.getCurrentlyOpenedMenus().containsKey(clicked.getUniqueId())) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Player clicked = (Player) event.getWhoClicked();

        if (!lobbyHandler.isInLobby(clicked) || clicked.hasMetadata("Build") || Menu.getCurrentlyOpenedMenus().containsKey(clicked.getUniqueId())) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (lobbyHandler.isInLobby(event.getEntity())) {
            event.getDrops().clear();
        }
    }

    @EventHandler
    public void onInventoryMove(InventoryMoveItemEvent event) {
        InventoryHolder inventoryHolder = event.getSource().getHolder();

        if (inventoryHolder instanceof Player) {
            Player player = (Player) inventoryHolder;

            if (!lobbyHandler.isInLobby(player) || Menu.getCurrentlyOpenedMenus().containsKey(player.getUniqueId())) {
                return;
            }

            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        GameMode gameMode = event.getPlayer().getGameMode();

        if (lobbyHandler.isInLobby(event.getPlayer()) && gameMode != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            return;
        }

        if (lobbyHandler.isInLobby(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

}