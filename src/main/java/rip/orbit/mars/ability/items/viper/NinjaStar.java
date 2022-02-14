package rip.orbit.mars.ability.items.viper;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import rip.orbit.mars.Mars;
import rip.orbit.mars.ability.Ability;
import rip.orbit.mars.ability.profile.AbilityProfile;
import rip.orbit.mars.util.Symbols;
import rip.orbit.mars.util.cooldown.Cooldowns;
import rip.orbit.nebula.util.CC;
import rip.orbit.nebula.util.JavaUtils;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 31/07/2021 / 5:23 PM
 * HCTeams / rip.orbit.mars.ability.items
 */
public class NinjaStar extends Ability {

	public Cooldowns cd = new Cooldowns();

	public NinjaStar() {
		super("NinjaStar");
	}

	@Override
	public Cooldowns cooldown() {
		return cd;
	}

	@Override
	public String name() {
		return "viper-ninjastar";
	}

	@Override
	public String displayName() {
		return CC.translate("&6&lNinja Star &7(Viper)");
	}

	@Override
	public int data() {
		return 0;
	}

	@Override
	public Material mat() {
		return Material.NETHER_STAR;
	}

	@Override
	public boolean glow() {
		return false;
	}

	@Override
	public List<String> lore() {
		return CC.translate(
				Arrays.asList(
						"&7Right click this to teleport to start a 3 second sequence",
						"&7where if you're hit within the last 15 seconds, whoever",
						"&7hit you last will be the person you teleport to after the",
						"&73 seconds is up."
				)
		);
	}

	@Override
	public List<String> foundInfo() {
		return CC.translate(Arrays.asList(
				"Autumn Crates",
				"Star Shop (/starshop)"
		));
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

			AbilityProfile profile = AbilityProfile.byUUID(player.getUniqueId());

			if (profile.getLastHitTime() + 15_0000L < System.currentTimeMillis()) {
				player.sendMessage(CC.translate("&6&lNinjaStar &7» &fThere is no last hit"));
				return;
			}

			Player target = Bukkit.getPlayer(profile.getLastDamagerName());
			if (target == null) {
				player.sendMessage(CC.translate("&6&lNinjaStar &7» &fThere is no last hit"));
				return;
			}

			addCooldown(player, 150);
			event.setCancelled(true);

			List<String> hitMsg = Arrays.asList(
					"",
					"&6You" + " &fhave just used a " + displayName(),
					Symbols.STAR + "&fYou will be teleported to &6%player%&f in &63 seconds&f.",
					"");

			hitMsg.forEach(s -> {
				player.sendMessage(CC.translate(s.replaceAll("%player%", target.getName())));
			});

			new BukkitRunnable() {
				int i = 3;
				@Override
				public void run() {
					if (i == 0) {
						cancel();
						target.sendMessage(CC.translate("&6&lNinjaStar &7» &6" + player.getName() + " &fhas been teleported to you."));
						player.sendMessage(CC.translate("&6&lNinjaStar &7» &6You &fhave been teleported to &6" + target.getName()));
						player.teleport(target);
						return;
					}
					if (i == 1) {
						target.sendMessage(CC.translate("&6&lNinjaStar &7» &6" + player.getName() + " &fwill be teleported to you in &6" + i + " second"));
						player.sendMessage(CC.translate("&6&lNinjaStar &7» &6You &fwill be teleported to &6" + target.getName() + " &fin &6" + i + " second"));
					} else {
						target.sendMessage(CC.translate("&6&lNinjaStar &7» &6" + player.getName() + " &fwill be teleported to you in &6" + i + " seconds"));
						player.sendMessage(CC.translate("&6&lNinjaStar &7» &6You &fwill be teleported to &6" + target.getName() + " &fin &6" + i + " seconds"));
					}
					--i;
				}
			}.runTaskTimer(Mars.getInstance(), 20, 20);
		}
	}

}
