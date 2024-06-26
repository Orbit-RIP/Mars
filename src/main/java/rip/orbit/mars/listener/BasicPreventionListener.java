package rip.orbit.mars.listener;

import rip.orbit.mars.Mars;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import rip.orbit.mars.match.Match;
import rip.orbit.nebula.Nebula;
import rip.orbit.nebula.profile.Profile;
import rip.orbit.nebula.profile.stat.GlobalStatistic;
import rip.orbit.nebula.profile.stat.StatType;


public final class BasicPreventionListener implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        // if we have a whitelist we probably are manually
        // controlling who can log in and don't need this
        if (true || Bukkit.hasWhitelist()) {
            return;
        }


    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        if (!event.getPlayer().hasPlayedBefore()) {
            Profile profile = Nebula.getInstance().getProfileHandler().fromUuid(event.getPlayer().getUniqueId(), true);

            for (GlobalStatistic stat : profile.getGlobalStatistics()) {
                if (stat.getStatType().equals(StatType.PRACTICE)) {
                    stat.setSeasonsPlayed(stat.getSeasonsPlayed() + 1);
                    break;
                }
            }

            profile.save();
        }

    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        event.setLeaveMessage(null);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntityType() == EntityType.ARROW) {
            event.getEntity().remove();
        }
    }

    @EventHandler
    public void onThunderChange(ThunderChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        event.setDroppedExp(0);
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockForm(BlockFormEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!canInteractWithBlocks(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!canInteractWithBlocks(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    private boolean canInteractWithBlocks(Player player) {
        if (Mars.getInstance().getMatchHandler().isPlayingMatch(player)) {
            // completely ignore players in matches, MatchBuildListener handles this.
            return true;
        }

        boolean inLobby = Mars.getInstance().getLobbyHandler().isInLobby(player);
        boolean isCreative = player.getGameMode() == GameMode.CREATIVE;
        boolean isOp = player.isOp();
        boolean buildMeta = player.hasMetadata("Build");

        return inLobby && isCreative && isOp && buildMeta;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.SOIL) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (event.getWhoClicked().hasMetadata(Match.TRAPPER_METADATA)) return;
        event.setCancelled(true);
    }

}