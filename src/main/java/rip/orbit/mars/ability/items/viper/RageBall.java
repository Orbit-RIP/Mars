package rip.orbit.mars.ability.items.viper;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import rip.orbit.mars.Mars;
import rip.orbit.mars.ability.Ability;
import rip.orbit.mars.util.cooldown.Cooldowns;
import rip.orbit.nebula.util.CC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 05/12/2021 / 1:30 AM
 * SteelHCF-main / com.steelpvp.hcf.ability.types
 */
public class RageBall extends Ability {

	private final Cooldowns cooldown = new Cooldowns();

	public Cooldowns cd = new Cooldowns();

	public RageBall() {
		super("RageBall");
	}

	@Override
	public Cooldowns cooldown() {
		return cd;
	}

	@Override
	public List<String> lore() {
		return CC.translate(Arrays.asList(
				"&7Effects Distance: 15 blocks",
				"&7Throw at the ground and any player",
				"&7within 15 blocks receives Wither II",
				"&7for 8 seconds. You receive Strength II",
				"&7& Resistance III for 8 seconds."
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

	@Override
	public String displayName() {
		return CC.translate("&6&lRage Ball &7(Viper)");
	}

	@Override
	public String name() {
		return "viper-rageball";
	}

	@Override
	public int data() {
		return 0;
	}

	@Override
	public Material mat() {
		return Material.EGG;
	}

	@Override
	public boolean glow() {
		return false;
	}

	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		if (event.getEntity().getShooter() instanceof Player) {
			Player shooter = (Player) event.getEntity().getShooter();
			if (isSimilar(shooter.getInventory().getItemInHand())) {
				if (!canUse(shooter)) {
					event.setCancelled(true);
					return;
				}
				event.getEntity().setMetadata("rageball", new FixedMetadataValue(Mars.getInstance(), true));
				cooldown().applyCooldown(shooter, 90);
			}
		}
	}

	@EventHandler
	public void onEntityAttack(ProjectileHitEvent event) {

		Projectile projectile = event.getEntity();

		if (!checkInstancePlayer(projectile.getShooter())) {
			return;
		}

		Player attacker = (Player) projectile.getShooter();

		if (attacker == null) return;

		if (!projectile.hasMetadata("rageball")) {
			return;
		}

		List<String> strings = new ArrayList<>();

		for (Entity entity : attacker.getNearbyEntities(15, 15, 15)) {

			if (!checkInstancePlayer(entity)) continue;

			Player damaged = (Player) entity;

			if (!canAttack(attacker, damaged)) continue;

			damaged.sendMessage(CC.translate("&eYou have been hit by Rage Ball"));

			damaged.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * 8, 1));

			strings.add(damaged.getName());
		}

		attacker.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * (8 + strings.size()), 1));
		attacker.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * (8 + strings.size()), 2));

	}

}
