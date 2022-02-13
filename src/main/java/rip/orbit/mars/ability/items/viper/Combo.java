package rip.orbit.mars.ability.items.viper;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import rip.orbit.mars.Mars;
import rip.orbit.mars.ability.Ability;
import rip.orbit.mars.pvpclasses.PvPClass;
import rip.orbit.mars.util.cooldown.Cooldowns;
import rip.orbit.nebula.util.CC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 22/07/2021 / 9:25 AM
 * HCTeams / rip.orbit.mars.ability.items
 */
public class Combo extends Ability {

	public Cooldowns cd = new Cooldowns();
	public Cooldowns warrior = new Cooldowns();
	public ConcurrentHashMap<UUID, Integer> hits = new ConcurrentHashMap<>();

	public Combo() {
		super("Combo");
	}

	@Override
	public Cooldowns cooldown() {
		return cd;
	}

	@Override
	public String name() {
		return "combo";
	}

	@Override
	public String displayName() {
		return CC.translate("&6&lCombo Ability");
	}

	@Override
	public int data() {
		return 3;
	}

	@Override
	public Material mat() {
		return Material.RAW_FISH;
	}

	@Override
	public boolean glow() {
		return false;
	}

	@Override
	public List<String> lore() {
		return CC.translate(Arrays.asList(
				"&7Click this to start a 10 second sequence",
				"&7where as many hits that you receive for the next",
				"&710 seconds is how many seconds you will receive",
				"&7Strength II"
		));
	}

	@Override
	public List<String> foundInfo() {
		return CC.translate(Arrays.asList(
				"Ability Packages",
				"Partner Crates",
				"Star Shop (/starshop)"
		));
	}

	@EventHandler
	public void onHit(EntityDamageByEntityEvent event) {
		if (checkInstancePlayer(event.getEntity())) {
			if (checkInstancePlayer(event.getDamager())) {
				Player damaged = (Player) event.getEntity();
				if (warrior.onCooldown(damaged)) {
					CompletableFuture.runAsync(() -> {
						if (hits.get(damaged.getUniqueId()) < 12) {
							hits.put(damaged.getUniqueId(), hits.get(damaged.getUniqueId()) + 1);
							damaged.sendMessage(CC.translate("&6&lCombo Ability &7» &fYou currently have &6" + hits.get(damaged.getUniqueId()) + " hits&f."));
						}
					});
				}
			}
		}
	}

	@EventHandler
	public void onInteractWarrior(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getItem() == null)
			return;
		if (event.getAction().name().contains("RIGHT")) {
			if (isSimilar(event.getItem())) {
				if (!canUse(player)) {
					return;
				}
				hits.putIfAbsent(player.getUniqueId(), 0);
				warrior.applyCooldown(player, 10);
				addCooldown(player, 90);
				event.setCancelled(true);
				takeItem(player);

				List<String> hitMsg = Arrays.asList(
						" ",
						CC.GRAY + CC.UNICODE_ARROW_RIGHT + " &6You" + " &fhave just used a " + displayName(),
						" ");

				hitMsg.forEach(s -> player.sendMessage(CC.translate(s)));

				new BukkitRunnable() {
					@Override
					public void run() {
						PvPClass.setRestoreEffect(player, new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * hits.get(player.getUniqueId()), 1));

						CompletableFuture.runAsync(() -> hits.remove(player.getUniqueId()));
					}
				}.runTaskLater(Mars.getInstance(), 20 * 10);

			}
		}
	}

}
