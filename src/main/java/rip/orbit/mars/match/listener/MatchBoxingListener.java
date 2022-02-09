package rip.orbit.mars.match.listener;

import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.object.TitleType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import rip.orbit.mars.Mars;
import rip.orbit.mars.match.Match;
import rip.orbit.mars.match.MatchTeam;
import rip.orbit.mars.match.event.MatchEndEvent;
import rip.orbit.mars.match.event.MatchStartEvent;
import rip.orbit.nebula.util.CC;

import java.time.Duration;
import java.util.UUID;

public final class MatchBoxingListener implements Listener {

	@EventHandler
	public void onMatchStart(MatchStartEvent event) {
		if (event.getMatch().getKitType().getId().equals("Boxing")) {

			MatchTeam trapperTeam = event.getMatch().getTeams().get(0);

			for (MatchTeam team : event.getMatch().getTeams()) {
				for (UUID uuid : team.getAliveMembers()) {
					Player player = Bukkit.getPlayer(uuid);
					if (player != null) {

						player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 10));
						player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 10));
						player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
						player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
					}
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
			if (match.getKitType().getId().equals("Boxing")) {
				event.setCancelled(true);
			}
		}
	}

}