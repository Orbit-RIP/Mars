package rip.orbit.mars.ability.items;

import rip.orbit.mars.Mars;
import rip.orbit.mars.ability.Ability;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import rip.orbit.mars.pvpclasses.PvPClass;
import rip.orbit.nebula.util.CC;
import rip.orbit.mars.util.cooldown.Cooldowns;

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
public class Warrior extends Ability {

	public Cooldowns cd = new Cooldowns();
	public static Cooldowns warrior = new Cooldowns();
	public ConcurrentHashMap<UUID, Integer> hits = new ConcurrentHashMap<>();

	@Override
	public Cooldowns cooldown() {
		return cd;
	}

	@Override
	public String name() {
		return "warrior";
	}

	@Override
	public String displayName() {
		return CC.translate("&c&lWarrior Ability");
	}

	@Override
	public int data() {
		return 1;
	}

	@Override
	public Material mat() {
		return Material.INK_SACK;
	}

	@Override
	public boolean glow() {
		return true;
	}

	@Override
	public List<String> lore() {
		return CC.translate(Arrays.asList(
				" ",
				"&7Click this to start a 10 second sequence",
				"&7where as many hits that you get for the next",
				"&710 seconds is how many seconds you will receive",
				"&7Strength II, Resistance III, Regeneration III,",
				"&7, and Speed III",
				" ",
				"&c&lNOTE&7: You can only get up to 8 seconds of these effects.",
				" "
		));
	}

	@Override
	public List<String> foundInfo() {
		return CC.translate(Arrays.asList(
				"Ability Packages",
				"Partner Crates"
		));
	}

	@EventHandler
	public void onHit(EntityDamageByEntityEvent event) {
		if (checkInstancePlayer(event.getEntity())) {
			if (checkInstancePlayer(event.getDamager())) {
				Player damager = (Player) event.getDamager();
				if (canAttack(damager, (Player) event.getEntity())) {
					if (warrior.onCooldown(damager)) {
						CompletableFuture.runAsync(() -> {
							if (hits.get(damager.getUniqueId()) < 8) {
								hits.put(damager.getUniqueId(), hits.get(damager.getUniqueId()) + 1);
								damager.sendMessage(CC.translate("&c&lWarrior Ability &7» &fYou currently have &c" + hits.get(damager.getUniqueId()) + " hits&f."));
							}
						});
					}
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
						"",
						"&cYou" + " &fhave just used a " + displayName(),
						" ",
						"&6&l┃ &fYou have 10 seconds to get as many",
						"&6&l┃ &fhits as you can. After 10 seconds you",
						"&6&l┃ &fwill receive &cStrength II, Resistance III,",
						"&6&l┃ &cRegeneration III, and Speed III&f for the amount",
						"&6&l┃ &fof hits you have in these 10 seconds.",
						"");

				hitMsg.forEach(s -> player.sendMessage(CC.translate(s)));

				List<PotionEffect> effects = new ArrayList<>(player.getActivePotionEffects());

				new BukkitRunnable() {
					@Override
					public void run() {
						player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * hits.get(player.getUniqueId()), 2));
						player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * hits.get(player.getUniqueId()), 2));
						player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * hits.get(player.getUniqueId()), 2));
						player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * hits.get(player.getUniqueId()), 1));

						new BukkitRunnable() {
							@Override
							public void run() {
								player.addPotionEffects(effects);
							}
						}.runTaskLater(Mars.getInstance(), 20 * hits.get(player.getUniqueId()));

						CompletableFuture.runAsync(() -> hits.remove(player.getUniqueId()));
					}
				}.runTaskLater(Mars.getInstance(), 20 * 10);

			}
		}
	}

}
