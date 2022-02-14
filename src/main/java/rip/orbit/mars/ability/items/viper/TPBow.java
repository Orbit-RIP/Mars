package rip.orbit.mars.ability.items.viper;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import rip.orbit.mars.Mars;
import rip.orbit.mars.ability.Ability;
import rip.orbit.mars.util.Symbols;
import rip.orbit.mars.util.cooldown.Cooldowns;
import rip.orbit.nebula.util.CC;

import java.util.*;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 12/02/2022 / 9:58 PM
 * Mars / rip.orbit.mars.ability.items.viper
 */
public class TPBow extends Ability {

	public Cooldowns cd = new Cooldowns();

	public TPBow() {
		super("TPBow");
	}

	@Override
	public Cooldowns cooldown() {
		return cd;
	}

	@Override
	public List<String> lore() {
		return CC.translate(Arrays.asList(
				"&7Shoot & hit a player to teleport to",
				"&7them after 3 seconds."
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
		return CC.translate("&6&lTPBow &7(Viper)");
	}

	@Override
	public String name() {
		return "viper-tpbow";
	}

	@Override
	public int data() {
		return 0;
	}

	@Override
	public Material mat() {
		return Material.BOW;
	}

	@Override
	public boolean glow() {
		return false;
	}

	@EventHandler
	public void onProjectileLaunch(EntityShootBowEvent event) {
		if (event.getEntity() instanceof Player) {
			Player shooter = (Player) event.getEntity();
			if (isSimilar(shooter.getInventory().getItemInHand())) {
				if (!canUse(shooter)) {
					event.setCancelled(true);
					return;
				}
				event.getProjectile().setMetadata("tpbow", new FixedMetadataValue(Mars.getInstance(), true));
			}
		}
	}

	@EventHandler
	public void onEntityAttack(EntityDamageByEntityEvent event) {

		if (!event.getDamager().hasMetadata("tpbow")) return;

		if (!(event.getDamager() instanceof Projectile)) return;

		Projectile projectile = (Projectile) event.getDamager();

		if (!checkInstancePlayer(projectile.getShooter())) return;
		if (!checkInstancePlayer(event.getEntity())) return;

		Player attacker = (Player) projectile.getShooter();

		if (attacker == null) return;

		Player damaged = (Player) event.getEntity();

		if (!canAttack(attacker, damaged)) {
			event.setCancelled(true);
			cooldown().applyCooldown(attacker, 5);
			return;
		}


		attacker.sendMessage(CC.translate(" "));
		attacker.sendMessage(CC.translate(Symbols.LARROW + "&6You have successfully hit " + damaged.getName() + "&6!"));
		attacker.sendMessage(CC.translate(Symbols.LARROW + "&6Now on cooldown for 2 minutes"));
		attacker.sendMessage(CC.translate(" "));

		damaged.sendMessage(CC.translate(" "));
		damaged.sendMessage(CC.translate(Symbols.LARROW + "&cYou have been hit with &FTeleportation Bow&C!"));
		damaged.sendMessage(CC.translate(Symbols.LARROW + "&cThe shooter will be teleported to you in &f3 seconds&c."));
		damaged.sendMessage(CC.translate(" "));

		cd.applyCooldown(attacker, 120);

		new BukkitRunnable() {
			int i = 3;
			@Override
			public void run() {
				if (i == 0) {
					cancel();
					attacker.teleport(damaged.getLocation());
					return;
				}
				if (i == 1) {
					attacker.sendMessage(CC.translate("&eTeleporting in &c" + i + " &esecond..."));
				} else {
					attacker.sendMessage(CC.translate("&eTeleporting in &c" + i + " &eseconds..."));
				}
				--i;
			}
		}.runTaskTimer(Mars.getInstance(), 20, 20);

	}

}
