package rip.orbit.mars.match.listener;

import org.bukkit.metadata.FixedMetadataValue;
import rip.orbit.mars.Mars;
import rip.orbit.mars.arena.Arena;
import rip.orbit.mars.match.Match;
import rip.orbit.mars.match.MatchHandler;
import rip.orbit.mars.match.MatchState;
import rip.orbit.mars.match.MatchTeam;
import rip.orbit.mars.match.event.MatchCountdownStartEvent;
import rip.orbit.mars.match.event.MatchEndEvent;
import rip.orbit.mars.match.event.MatchStartEvent;
import rip.orbit.mars.nametag.PotPvPNametagProvider;
import cc.fyre.proton.cuboid.Cuboid;
import cc.fyre.proton.util.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public final class MatchGeneralListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        MatchHandler matchHandler = Mars.getInstance().getMatchHandler();
        Player player = event.getEntity();
        Match match = matchHandler.getMatchPlaying(player);

        if (match == null) {
            return;
        }

        // creates 'proper' player death animation (of the player falling over)
        // which we don't get due to our immediate respawn
        PlayerUtils.animateDeath(player);

        match.markDead(player);
        player.teleport(player.getLocation().add(0, 2, 0));

        // if we're ending the match we don't drop pots/bowls
        if (match.getState() == MatchState.ENDING) {
            event.getDrops().removeIf(i -> i.getType() == Material.POTION || i.getType() == Material.GLASS_BOTTLE || i.getType() == Material.MUSHROOM_SOUP || i.getType() == Material.BOWL);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        MatchHandler matchHandler = Mars.getInstance().getMatchHandler();
        Player player = event.getPlayer();
        Match match = matchHandler.getMatchPlaying(player);

        if (match == null) {
            return;
        }

        MatchState state = match.getState();

        if (state == MatchState.COUNTDOWN || state == MatchState.IN_PROGRESS) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                UUID onlinePlayerUuid = onlinePlayer.getUniqueId();

                // if this player has no relation to the match skip
                if (match.getTeam(onlinePlayerUuid) == null && !match.isSpectator(onlinePlayerUuid)) {
                    continue;
                }

                ChatColor playerColor = PotPvPNametagProvider.getNameColor(player, onlinePlayer);
                String playerFormatted = playerColor + player.getName();

                onlinePlayer.sendMessage(playerFormatted + ChatColor.GRAY + " disconnected.");
            }
        }

        // run this regardless of match state

        MatchTeam team = match.getTeam(player.getUniqueId());
        if (team != null) {
            team.setLives(1);
        }
        match.markDead(player);
    }

    @EventHandler
    public void onCountdown(MatchCountdownStartEvent event) {
        for (UUID uuid : event.getMatch().getAllPlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            player.setMetadata("nomove", new FixedMetadataValue(Mars.getInstance(), true));
        }
    }

    @EventHandler
    public void onStart(MatchStartEvent event) {
        for (UUID uuid : event.getMatch().getAllPlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            player.removeMetadata("nomove", Mars.getInstance());
        }
    }

    // "natural" teleports (like enderpearls) are forwarded down and
    // treated as a move event, plugin teleports (specifically
    // those originating in this plugin) are ignored.
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        switch (event.getCause()) {
            case PLUGIN:
            case COMMAND:
            case UNKNOWN:
                return;
            default:
                break;
        }

        onPlayerMove(event);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();

        if (
            from.getBlockX() == to.getBlockX() &&
            from.getBlockY() == to.getBlockY() &&
            from.getBlockZ() == to.getBlockZ()
        ) {
            return;
        }

        MatchHandler matchHandler = Mars.getInstance().getMatchHandler();
        Match match = matchHandler.getMatchPlayingOrSpectating(player);

        if (match == null) {
            return;
        }

        if (match.getKitType().getId().equals("PearlFight")) return;
        if (match.getKitType().getId().equals("Void")) return;
        if (match.getKitType().getId().equals("Bridges")) return;

        Arena arena = match.getArena();
        Cuboid bounds = arena.getBounds();

        // pretend the vertical bounds of the arena are 2 blocks lower than they
        // are to avoid issues with players hitting their heads on the glass (Jon said to do this)
        // looks kind of funny but in a high frequency event this is by far the fastest
        if (!bounds.contains(to) || !bounds.contains(to.getBlockX(), to.getBlockY() + 2, to.getBlockZ())) {
            // spectators get a nice message, players just get cancelled
            if (match.isSpectator(player.getUniqueId())) {
                player.teleport(arena.getSpectatorSpawn());
            } else if (to.getBlockY() >= bounds.getUpperY() || to.getBlockY() <= bounds.getLowerY()) { // if left vertically

                if ((match.getKitType().getId().equals("Sumo") || match.getKitType().getId().equals("Spleef"))) {
                    if (to.getBlockY() <= bounds.getLowerY() && bounds.getLowerY() - to.getBlockY() <= 20) return; // let the player fall 10 blocks
                    match.markDead(player);
                    match.addSpectator(player, null, true);
                }

                player.teleport(arena.getSpectatorSpawn());
            } else {
                if (match.getKitType().getId().equals("Sumo") || match.getKitType().getId().equals("Spleef")) { // if they left horizontally
                    match.markDead(player);
                    match.addSpectator(player, null, true);
                    player.teleport(arena.getSpectatorSpawn());
                }

                event.setCancelled(true);
            }
        } else if (to.getBlockY() + 5 < arena.getSpectatorSpawn().getBlockY()) { // if the player is still in the arena bounds but fell down from the spawn point
            if (match.getKitType().getId().equals("Sumo")) {
                match.markDead(player);
                match.addSpectator(player, null, true);
                player.teleport(arena.getSpectatorSpawn());
            }
        }
    }

    /**
     * Prevents (non-fall) damage between ANY two players not on opposing {@link MatchTeam}s.
     * This includes cancelling damage from a player not in a match attacking a player in a match.
     */
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) {
            return;
        }

        // in the context of an EntityDamageByEntityEvent, DamageCause.FALL
        // is the 0 hearts of damage and knockback applied when hitting
        // another player with a thrown enderpearl. We allow this damage
        // in order to be consistent with HCTeams
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            return;
        }

        MatchHandler matchHandler = Mars.getInstance().getMatchHandler();
        Player victim = (Player) event.getEntity();
        Player damager = PlayerUtils.getDamageSource(event.getDamager());

        if (damager == null) {
            return;
        }

        Match match = matchHandler.getMatchPlaying(damager);
        boolean isSpleef = match != null && match.getKitType().getId().equals("Spleef");
        boolean isSumo = match != null && match.getKitType().getId().equals("Sumo");

        // we only specifically allow damage where both players are in a match together
        // and not on the same team, everything else is cancelled.
        if (match != null) {
            MatchTeam victimTeam = match.getTeam(victim.getUniqueId());
            MatchTeam damagerTeam = match.getTeam(damager.getUniqueId());

            if (isSpleef && event.getDamager() instanceof Snowball) return;

            if (isSumo && victimTeam != null && victimTeam != damagerTeam) {
                // Ugly hack because people actually lose health & hunger in sumo somehow
                event.setDamage(0);
                return;
            }

            if (victimTeam != null && victimTeam != damagerTeam && !isSpleef) {
                return;
            }
        }


        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        MatchHandler matchHandler = Mars.getInstance().getMatchHandler();
        Player player = event.getPlayer();

        if (!matchHandler.isPlayingMatch(player)) {
            return;
        }

        ItemStack itemStack = event.getItemDrop().getItemStack();
        Material itemType = itemStack.getType();
        String itemTypeName = itemType.name().toLowerCase();
        int heldSlot = player.getInventory().getHeldItemSlot();

        // don't let players drop swords, axes, and bows in the first slot
        if (heldSlot == 0 && (itemTypeName.contains("sword") || itemTypeName.contains("axe") || itemType == Material.BOW)) {
            player.sendMessage(ChatColor.RED + "You can't drop that while you're holding it in slot 1.");
            event.setCancelled(true);
        }

        // glass bottles and bowls are removed from inventories but
        // don't spawn items on the ground
        if (itemType == Material.GLASS_BOTTLE || itemType == Material.BOWL) {
            event.getItemDrop().remove();
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        MatchHandler matchHandler = Mars.getInstance().getMatchHandler();
        Player player = event.getPlayer();

        if (!matchHandler.isPlayingMatch(player)) {
            return;
        }

        Match match = matchHandler.getMatchPlaying(event.getPlayer());
        if (match == null) return;

        if (match.getState() == MatchState.ENDING || match.getState() == MatchState.TERMINATED) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onConsume(PlayerItemConsumeEvent event) {
        ItemStack stack = event.getItem();
        if (stack == null || stack.getType() != Material.POTION) return;

        Bukkit.getScheduler().runTaskLater(Mars.getInstance(), () -> {
            //event.getPlayer().setItemInHand(null);
        }, 1L);
    }
}