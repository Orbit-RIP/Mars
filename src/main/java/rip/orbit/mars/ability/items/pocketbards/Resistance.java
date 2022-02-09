package rip.orbit.mars.ability.items.pocketbards;

import rip.orbit.mars.Mars;
import rip.orbit.mars.ability.Ability;
import rip.orbit.mars.match.Match;
import rip.orbit.mars.match.MatchTeam;
import rip.orbit.nebula.util.CC;
import rip.orbit.mars.util.cooldown.Cooldowns;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import java.util.Arrays;
import java.util.List;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 31/07/2021 / 6:03 PM
 * HCTeams / rip.orbit.hcteams.ability.items.pocketbard
 */
public class Resistance extends Ability {

	public Cooldowns cd = new Cooldowns();

	@Override
	public Cooldowns cooldown() {
		return cd;
	}

	@Override
	public String name() {
		return "respocketbard";
	}

	@Override
	public String displayName() {
		return "&b&lResistance III";
	}

	@Override
	public int data() {
		return 0;
	}

	@Override
	public Material mat() {
		return Material.IRON_INGOT;
	}

	@Override
	public boolean glow() {
		return true;
	}

	@Override
	public List<String> lore() {
		return CC.translate(Arrays.asList(
				"",
				"&7Right click to receive resistance 3 for",
				"&75 seconds.",
				"",
				"&c&lNOTE&7: Your teammates receive the effects as well.",
				""
		));
	}

	@Override
	public List<String> foundInfo() {
		return null;
	}

	@EventHandler
	public void onInteractNinjaStar(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		if (isSimilar(event.getItem())) {
			if (!isClick(event, "RIGHT")) {
				event.setUseItemInHand(Event.Result.DENY);
				return;
			}
			if (!canUse(player)) {
				event.setUseItemInHand(Event.Result.DENY);
				return;
			}

			addCooldown(player, 60);
			event.setCancelled(true);
			takeItem(player);

			PotionEffect effect = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 30 * 5, 2);
			player.addPotionEffect(effect);

			List<String> hitMsg = Arrays.asList(
					"",
					"&bYou &fhave just activated a " + displayName() + "&f.",
					" ",
					"&6&l┃ &fYou now have " + displayName() + " &ffor 5 seconds",
					"");


			player.getNearbyEntities(15, 15, 15).forEach(entity -> {
				if (entity instanceof Player) {
					Player p = (Player) entity;
					Match match = Mars.getInstance().getMatchHandler().getMatchPlaying(p);
					if (match != null) {
						MatchTeam team = match.getTeam(p.getUniqueId());
						if (team != null) {
							if (team.getAllMembers().contains(player.getUniqueId())) {
								p.addPotionEffect(effect);

								hitMsg.forEach(s -> p.sendMessage(CC.translate(s)));
							}
						}
					}
				}
			});

			hitMsg.forEach(s -> player.sendMessage(CC.translate(s)));
		}
	}

}
