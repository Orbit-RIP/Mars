package rip.orbit.mars.match.listener;

import rip.orbit.mars.Mars;
import rip.orbit.mars.match.Match;
import rip.orbit.mars.match.MatchHandler;
import rip.orbit.mars.setting.Setting;
import rip.orbit.mars.setting.event.SettingUpdateEvent;
import rip.orbit.mars.util.VisibilityUtils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;

public final class SpectatorPreventionListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        MatchHandler matchHandler = Mars.getInstance().getMatchHandler();
        Match match = matchHandler.getMatchSpectating(event.getPlayer());

        if (match != null) {
            match.removeSpectator(event.getPlayer());
        }
    }

    /**
     * Prevent spectator items from dropping in the rare case spectators die
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        MatchHandler matchHandler = Mars.getInstance().getMatchHandler();

        if (matchHandler.isSpectatingMatch(event.getEntity())) {
            event.setKeepInventory(true);
        }
    }

    /**
     * Prevent damage caused by spectators
     */
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            MatchHandler matchHandler = Mars.getInstance().getMatchHandler();
            Player damager = (Player) event.getDamager();

            if (matchHandler.isSpectatingMatch(damager)) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Prevent item drops by spectators
     */
    @EventHandler
    public void onPlayerDropitem(PlayerDropItemEvent event) {
        MatchHandler matchHandler = Mars.getInstance().getMatchHandler();

        if (matchHandler.isSpectatingMatch(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    /**
     * Prevent item pickups by spectators
     */
    @EventHandler
    public void onPlayerPickupitem(PlayerPickupItemEvent event) {
        MatchHandler matchHandler = Mars.getInstance().getMatchHandler();

        if (matchHandler.isSpectatingMatch(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        MatchHandler matchHandler = Mars.getInstance().getMatchHandler();

        if (matchHandler.isSpectatingMatch(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        MatchHandler matchHandler = Mars.getInstance().getMatchHandler();

        if (matchHandler.isSpectatingMatch(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        MatchHandler matchHandler = Mars.getInstance().getMatchHandler();

        if (matchHandler.isSpectatingMatch((Player) event.getWhoClicked())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        MatchHandler matchHandler = Mars.getInstance().getMatchHandler();

        if (matchHandler.isSpectatingMatch((Player) event.getWhoClicked())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {
        InventoryHolder inventoryHolder = event.getSource().getHolder();

        if (inventoryHolder instanceof Player) {
            MatchHandler matchHandler = Mars.getInstance().getMatchHandler();

            if (matchHandler.isSpectatingMatch((Player) inventoryHolder)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onSettingUpdate(SettingUpdateEvent event) {
        if (event.getSetting() == Setting.VIEW_OTHER_SPECTATORS) {
            VisibilityUtils.updateVisibility(event.getPlayer());
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Entity shooter = (Entity) event.getEntity().getShooter();

        if (shooter instanceof Player) {
            MatchHandler matchHandler = Mars.getInstance().getMatchHandler();

            if (matchHandler.isSpectatingMatch((Player) shooter)) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Don't let spectators be affected by potions dropped near them
     */
    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {
        MatchHandler matchHandler = Mars.getInstance().getMatchHandler();

        for (LivingEntity entity : event.getAffectedEntities()) {
            if (entity instanceof Player && matchHandler.isSpectatingMatch((Player) entity)) {
                event.setIntensity(entity, 0F);
            }
        }
    }

}