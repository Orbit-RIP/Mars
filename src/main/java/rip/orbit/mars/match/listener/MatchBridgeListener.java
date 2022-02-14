//package rip.orbit.mars.match.listener;
//
//import cc.fyre.proton.cuboid.Cuboid;
//import org.bukkit.*;
//import org.bukkit.block.Block;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.block.BlockBreakEvent;
//import org.bukkit.event.block.BlockFormEvent;
//import org.bukkit.event.block.BlockPlaceEvent;
//import org.bukkit.event.player.PlayerBucketEmptyEvent;
//import org.bukkit.event.player.PlayerMoveEvent;
//import org.bukkit.event.player.PlayerQuitEvent;
//import org.bukkit.metadata.FixedMetadataValue;
//import org.bukkit.scheduler.BukkitRunnable;
//import rip.orbit.mars.Mars;
//import rip.orbit.mars.arena.Arena;
//import rip.orbit.mars.match.Match;
//import rip.orbit.mars.match.MatchHandler;
//import rip.orbit.mars.match.MatchState;
//import rip.orbit.mars.match.MatchTeam;
//import rip.orbit.nebula.util.CC;
//
//import java.util.UUID;
//
//public final class MatchBridgeListener implements Listener {
//
//	@EventHandler
//	public void onPlayerMove(PlayerMoveEvent event) {
//		Player player = event.getPlayer();
//		Location from = event.getFrom();
//		Location to = event.getTo();
//
//		if (
//				from.getBlockX() == to.getBlockX() &&
//						from.getBlockY() == to.getBlockY() &&
//						from.getBlockZ() == to.getBlockZ()
//		) {
//			return;
//		}
//
//		MatchHandler matchHandler = Mars.getInstance().getMatchHandler();
//		Match match = matchHandler.getMatchPlayingOrSpectating(player);
//
//		if (match == null) {
//			return;
//		}
//
//		if (!match.getKitType().getId().equals("Bridges")) return;
//
//		Arena arena = match.getArena();
//
//		if (to.getBlockY() + 8 < arena.getSpectatorSpawn().getBlockY()) { // if the player is still in the arena bounds but fell down from the spawn point
//			if (match.getKitType().getId().equals("Bridges")) {
//
//				match.getUsedKit().get(player.getUniqueId()).apply(player);
//
//				MatchTeam team = match.getTeam(player.getUniqueId());
//				if (team != null) {
//					player.setNoDamageTicks(20);
//					player.teleport(match.getSpawns().get(team));
//
//					match.markDead(player);
//				}
//			}
//		}
//
//		if (to.getBlock().getType().name().contains("WATER")) {
//
//			event.setTo(event.getFrom().add(0, 2, 0));
//
//			match.getUsedKit().get(player.getUniqueId()).apply(player);
//
//			MatchTeam team = match.getTeam(player.getUniqueId());
//			if (team != null) {
//				if (team == match.getTeams().get(1)) {
//					player.teleport(match.getSpawns().get(team));
//					player.sendMessage(CC.translate("&cYou cannot enter your own pool."));
//					return;
//				}
//				player.setNoDamageTicks(20);
//
//				player.teleport(match.getSpawns().get(team));
//
//				match.setState(MatchState.PAUSED);
//				team.setWins(team.getWins() + 1);
//
//				for (MatchTeam other : match.getTeams()) {
//					for (UUID uuid : other.getAliveMembers()) {
//						Player target = Bukkit.getPlayer(uuid);
//						if (target != null) {
//							target.teleport(match.getSpawns().get(other));
//
//							match.getUsedKit().get(target.getUniqueId()).apply(target);
//						}
//					}
//				}
//
//				if (team.getWins() >= 3) {
//					for (MatchTeam other : match.getTeams()) {
//						if (other != team) {
//							for (UUID uuid : other.getAliveMembers()) {
//								other.markDead(uuid);
//							}
//						}
//					}
//					match.checkEnded();
//				} else {
//					startCountdown(match);
//				}
//			}
//			return;
//		}
//
//		if (to.getBlock().getType().name().contains("LAVA")) {
//
//			event.setTo(event.getFrom().add(0, 2, 0));
//			player.setFireTicks(0);
//
//			match.getUsedKit().get(player.getUniqueId()).apply(player);
//
//			MatchTeam team = match.getTeam(player.getUniqueId());
//			if (team != null) {
//				if (team == match.getTeams().get(0)) {
//					player.teleport(match.getSpawns().get(team));
//					player.sendMessage(CC.translate("&cYou cannot enter your own pool."));
//					return;
//				}
//				player.setNoDamageTicks(20);
//
//				match.setState(MatchState.PAUSED);
//				team.setWins(team.getWins() + 1);
//
//				for (MatchTeam other : match.getTeams()) {
//					for (UUID uuid : other.getAliveMembers()) {
//						Player target = Bukkit.getPlayer(uuid);
//						if (target != null) {
//							target.teleport(match.getSpawns().get(other));
//
//							match.getUsedKit().get(target.getUniqueId()).apply(target);
//						}
//					}
//				}
//
//				if (team.getWins() >= 3) {
//					for (MatchTeam other : match.getTeams()) {
//						if (other != team) {
//							for (UUID uuid : other.getAliveMembers()) {
//								other.setLives(0);
//								other.markDead(uuid);
//							}
//						}
//					}
//					match.checkEnded();
//				} else {
//					startCountdown(match);
//				}
//
//			}
//		}
//	}
//
//	public void startCountdown(Match match) {
//		final int[] i = {3};
//		new BukkitRunnable() {
//			@Override
//			public void run() {
//				if (match.getState() == MatchState.ENDING || match.getState() == MatchState.TERMINATED) {
//					cancel();
//					return;
//				}
//				if (i[0] > 0) {
//					match.messageAll(CC.translate("&aYou will respawn in " + i[0] + " seconds"));
//				} else {
//
//					for (UUID uuid : match.getAllPlayers()) {
//						Player target = Bukkit.getPlayer(uuid);
//						if (target != null) {
//							target.setNoDamageTicks(10);
//						}
//					}
//
//					match.setState(MatchState.IN_PROGRESS);
//
//					cancel();
//					return;
//				}
//				--i[0];
//			}
//		}.runTaskTimer(Mars.getInstance(), 0, 20);
//	}
//
//	@EventHandler
//	public void onQuit(PlayerQuitEvent event) {
//
//		Player player = event.getPlayer();
//		Match match = Mars.getInstance().getMatchHandler().getMatchPlaying(player);
//		if (match != null) {
//			MatchTeam team = match.getTeam(player.getUniqueId());
//			if (team != null) {
//				team.setLives(0);
//				team.markDead(player.getUniqueId());
//
//				match.checkEnded();
//			}
//		}
//
//	}
//
//}