package rip.orbit.mars.ability.items.viper.pocketbard;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import rip.orbit.mars.Mars;
import rip.orbit.mars.ability.Ability;
import rip.orbit.mars.match.Match;
import rip.orbit.mars.match.MatchTeam;
import rip.orbit.mars.util.Symbols;
import rip.orbit.mars.util.cooldown.Cooldowns;
import rip.orbit.nebula.util.CC;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 31/07/2021 / 6:03 PM
 * HCTeams / rip.orbit.mars.ability.items.pocketbard
 */
public class Strength extends Ability {

	public Cooldowns cd = new Cooldowns();

	public Strength() {
		super("Strength");
	}

	@Override
	public Cooldowns cooldown() {
		return cd;
	}

	@Override
	public String name() {
		return "strengthpocketbard";
	}

	@Override
	public String displayName() {
		return CC.translate("&c&lStrength II &7(Viper)");
	}

	@Override
	public int data() {
		return 0;
	}

	@Override
	public Material mat() {
		return Material.BLAZE_POWDER;
	}

	@Override
	public boolean glow() {
		return false;
	}

	@Override
	public List<String> lore() {
		return CC.translate(Arrays.asList(
				"&7Right click to receive strength 2 for",
				"&75 seconds.",
				"&a&lTEAMMATES"
		));
	}

	@Override
	public List<String> foundInfo() {
		return Collections.emptyList();
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

			PotionEffect effect = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 30 * 5, 1);
			player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
			player.addPotionEffect(effect);

			List<String> hitMsg = Arrays.asList(
					"",
					Symbols.LARROW + "&6You have received INCREASE_DAMAGE II!",
					Symbols.LARROW + "&6Use the effects without using Bard Armor!",
					"");

			Match match = Mars.getInstance().getMatchHandler().getMatchPlaying(player);
			if (match != null) {
				MatchTeam team = match.getTeam(player.getUniqueId());
				if (team != null) {
					player.getNearbyEntities(15, 15, 15).forEach(entity -> {
						if (entity instanceof Player) {
							Player p = (Player) entity;
							if (team.getAliveMembers().contains(p.getUniqueId())) {
								p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
								p.addPotionEffect(effect);

								hitMsg.forEach(s -> p.sendMessage(CC.translate(s)));
							}
						}
					});
				}
			}

			hitMsg.forEach(s -> player.sendMessage(CC.translate(s)));

		}
	}

}
