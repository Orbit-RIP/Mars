package rip.orbit.mars.match.listener;

import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.object.TitleType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import rip.orbit.mars.Mars;
import rip.orbit.mars.ability.Ability;
import rip.orbit.mars.match.Match;
import rip.orbit.mars.match.MatchHandler;
import rip.orbit.mars.match.MatchTeam;
import rip.orbit.mars.match.event.MatchEndEvent;
import rip.orbit.mars.match.event.MatchStartEvent;
import rip.orbit.nebula.util.CC;

import java.time.Duration;
import java.util.UUID;

public final class MatchBaseRaidingListener implements Listener {

	@EventHandler
	public void onMatchStart(MatchStartEvent event) {
		if (event.getMatch().getKitType().getId().equals("BaseRaiding")) {

			MatchTeam trapperTeam = event.getMatch().getTeams().get(0);

			for (UUID uuid : trapperTeam.getAllMembers()) {
				Player player = Bukkit.getPlayer(uuid);
				if (player != null) {
					player.setMetadata(Match.TRAPPER_METADATA, new FixedMetadataValue(Mars.getInstance(), true));
				}
			}

			for (MatchTeam team : event.getMatch().getTeams()) {
				for (UUID uuid : team.getAliveMembers()) {
					Player player = Bukkit.getPlayer(uuid);
					if (player != null) {

						player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
						player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
						player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));

						if (player.hasMetadata(Match.TRAPPER_METADATA)) {
							player.sendMessage(CC.translate("&7 "));
							player.sendMessage(CC.translate("&6&lYOU ARE A TRAPPER!"));
							player.sendMessage(CC.translate("&7 "));

							LunarClientAPI.getInstance().sendTitle(player, TitleType.TITLE, CC.translate("&6&lBASE RAIDING"), Duration.ofSeconds(2));
							LunarClientAPI.getInstance().sendTitle(player, TitleType.SUBTITLE, CC.translate("&6Role: Trapper"), Duration.ofSeconds(2));
						} else {
							player.sendMessage(CC.translate("&7 "));
							player.sendMessage(CC.translate("&6&lYOU ARE A BASE RAIDER!"));
							player.sendMessage(CC.translate("&7 "));

							LunarClientAPI.getInstance().sendTitle(player, TitleType.TITLE, CC.translate("&6&lBASE RAIDING"), Duration.ofSeconds(2));
							LunarClientAPI.getInstance().sendTitle(player, TitleType.SUBTITLE, CC.translate("&6Role: Raider"), Duration.ofSeconds(2));
						}
					}
				}
			}

		}
	}

	@EventHandler
	public void onEnd(MatchEndEvent event) {
		if (event.getMatch().getKitType().getId().equals("BaseRaiding")) {
			for (UUID uuid : event.getMatch().getAllPlayers()) {
				Player player = Bukkit.getPlayer(uuid);
				if (player != null) {
					player.removeMetadata(Match.TRAPPER_METADATA, Mars.getInstance());
				}
			}
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
			if (match.getKitType().getId().equals("BaseRaiding")) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		if (event.getPlayer().hasMetadata(Match.TRAPPER_METADATA)) {
			event.getPlayer().removeMetadata(Match.TRAPPER_METADATA, Mars.getInstance());
		}
	}

	@EventHandler
	public void interact(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Ability dome = Mars.getInstance().getAbilityHandler().byName("dome");
		if (dome.isSimilar(event.getItem()))
			return;
		MatchHandler matchHandler = Mars.getInstance().getMatchHandler();
		Match match = matchHandler.getMatchPlayingOrSpectating(player);

		if (match != null) {

			if (match.getSpectators().contains(player.getUniqueId())) {
				event.setUseInteractedBlock(Event.Result.DENY);
				return;
			}

			if (matchHandler.isPlayingMatch(player)) {

				if (match.getKitType().getId().equals("BaseRaiding")) {

					if (!player.hasMetadata(Match.TRAPPER_METADATA)) {
						if (player.getItemInHand().getType() == Material.POTION || player.getItemInHand().getType() == Material.ENDER_PEARL || player.getItemInHand().getType() == Material.BOW || player.getItemInHand().getType() == Material.COOKED_BEEF || player.getItemInHand().getType() == Material.GOLDEN_APPLE) {
							if (event.getClickedBlock() == null)
								return;
							if (event.getClickedBlock().getType() == Material.FENCE_GATE || event.getClickedBlock().getType().name().contains("CHEST")) {
								if (player.isSneaking())
									return;
								event.setCancelled(true);
								return;
							}
							return;
						}
						event.setCancelled(true);
					}
				}
			}
		}
	}

}