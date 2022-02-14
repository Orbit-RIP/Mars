package rip.orbit.mars.ability.items.cave;

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
import rip.orbit.mars.util.cooldown.Cooldowns;
import rip.orbit.nebula.util.CC;

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
	public static final ConcurrentHashMap<UUID, UUID> lastHitMap = new ConcurrentHashMap<>();

	public NinjaStar() {
		super("CaveNinjaStar");
	}

	@Override
	public Cooldowns cooldown() {
		return cd;
	}

	@Override
	public String name() {
		return "CaveNinjaStar";
	}

	@Override
	public String displayName() {
		return CC.translate("&a&lNinja Star");
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
				player.sendMessage(CC.translate("&cYou may not use an &lNinja Star &csince no last hit was found!"));
				return;
			}

			Player target = Bukkit.getPlayer(profile.getLastDamagerName());
			if (target == null) {
				player.sendMessage(CC.translate("&cYou may not use an &lNinja Star &csince no last hit was found!"));
				return;
			}

			takeItem(player);

			addCooldown(player, 150);
			event.setCancelled(true);

			new BukkitRunnable() {
				int i = 3;
				@Override
				public void run() {
					if (i == 0) {
						cancel();
						player.teleport(target);
						return;
					}
					if (i == 1) {
						target.sendMessage(CC.translate("&5" + player.getName() + "&cwill teleport to you in &F" + i + "second"));
						player.sendMessage(CC.translate("&cYou will be teleported to &5" + target.getName() + " &cin &f" + i + " second"));
					} else {
						target.sendMessage(CC.translate("&5" + player.getName() + "&cwill teleport to you in &F" + i + "seconds"));
						player.sendMessage(CC.translate("&cYou will be teleported to &5" + target.getName() + " &cin &f" + i + " seconds"));
					}
					--i;
				}
			}.runTaskTimer(Mars.getInstance(), 20, 20);
		}
	}

}
