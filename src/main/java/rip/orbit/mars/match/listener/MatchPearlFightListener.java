package rip.orbit.mars.match.listener;

import cc.fyre.proton.util.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import rip.orbit.mars.Mars;
import rip.orbit.mars.arena.Arena;
import rip.orbit.mars.match.Match;
import rip.orbit.mars.match.MatchHandler;
import rip.orbit.mars.match.MatchState;
import rip.orbit.mars.match.MatchTeam;
import rip.orbit.mars.match.event.MatchCountdownStartEvent;
import rip.orbit.mars.match.event.MatchEndEvent;
import rip.orbit.mars.match.event.MatchStartEvent;
import rip.orbit.nebula.util.CC;
import rip.orbit.nebula.util.JavaUtils;

import java.util.UUID;

public final class MatchPearlFightListener implements Listener {

	@EventHandler
	public void onMatchStart(MatchCountdownStartEvent event) {
		if (event.getMatch().getKitType().getId().equals("PearlFight")) {

			for (MatchTeam team : event.getMatch().getTeams()) {
				for (UUID uuid : team.getAliveMembers()) {
					Player player = Bukkit.getPlayer(uuid);
					if (player != null) {
						player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 10));
						player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 10));
						player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
						player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
					}
					team.setLives(3);
				}
			}

		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location from = event.getFrom();
		Location to = event.getTo();

		if (from.getBlockX() == to.getBlockX() &&
						from.getBlockY() == to.getBlockY() &&
						from.getBlockZ() == to.getBlockZ()) {
			return;
		}

		MatchHandler matchHandler = Mars.getInstance().getMatchHandler();
		Match match = matchHandler.getMatchPlayingOrSpectating(player);

		if (match == null) {
			return;
		}

		if (!match.getKitType().getId().equals("PearlFight")) return;

		Arena arena = match.getArena();

		if (to.getBlockY() < arena.getTeam1Spawn().getBlockY() - 10) { // if the player is still in the arena bounds but fell down from the spawn point
			if (match.getKitType().getId().equals("PearlFight")) {

				event.setCancelled(true);

				for (UUID uuid : match.getAllPlayers()) {
					if (uuid == player.getUniqueId()) continue;
					Player target = Bukkit.getPlayer(uuid);
					if (target != null) {
						target.hidePlayer(player);
					}
				}

				match.markDead(player);
				player.setGameMode(GameMode.CREATIVE);
				player.teleport(match.getArena().getSpectatorSpawn());
				player.setMetadata("nomove", new FixedMetadataValue(Mars.getInstance(), true));
				player.getInventory().clear();

				final int[] i = {3};
				new BukkitRunnable() {
					@Override
					public void run() {
						if (match.getState() == MatchState.ENDING || match.getState() == MatchState.TERMINATED) {

							player.teleport(player.getWorld().getSpawnLocation());
							player.setNoDamageTicks(10);
							player.setGameMode(GameMode.SURVIVAL);
							player.removeMetadata("nomove", Mars.getInstance());

							for (UUID uuid : match.getAllPlayers()) {
								if (uuid == player.getUniqueId()) continue;
								Player target = Bukkit.getPlayer(uuid);
								if (target != null) {
									target.showPlayer(player);
								}
							}

							cancel();
							return;
						}
						if (i[0] > 0) {
							player.sendMessage(CC.translate("&aYou will respawn in " + i[0] + " seconds"));
						} else {

							for (UUID uuid : match.getAllPlayers()) {
								if (uuid == player.getUniqueId()) continue;
								Player target = Bukkit.getPlayer(uuid);
								if (target != null) {
									target.showPlayer(player);
								}
							}

							player.teleport(match.getSpawns().get(match.getTeam(player.getUniqueId())));
							player.setNoDamageTicks(10);
							player.setGameMode(GameMode.SURVIVAL);
							player.removeMetadata("nomove", Mars.getInstance());

							match.getUsedKit().get(player.getUniqueId()).apply(player);
							cancel();
							return;
						}
						--i[0];
					}
				}.runTaskTimer(Mars.getInstance(), 0, 20);
			}
		}
	}


	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Location from = event.getFrom();
		Location to = event.getTo();

		if (from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ()) return;

		if (event.getPlayer().hasMetadata("nomove")) {
			event.setTo(from);
		}
	}

	@EventHandler
	public void onSaturationChange(FoodLevelChangeEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			Match match = Mars.getInstance().getMatchHandler().getMatchPlaying(player);
			if (match == null) {
				return;
			}
			if (match.getKitType().getId().equals("PearlFight")) {
				event.setCancelled(true);
			}
		}
	}

}