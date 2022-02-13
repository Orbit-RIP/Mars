package rip.orbit.mars.ability.items.cave;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import rip.orbit.mars.Mars;
import rip.orbit.mars.ability.Ability;
import rip.orbit.mars.util.cooldown.Cooldowns;
import rip.orbit.nebula.util.CC;

import java.util.Arrays;
import java.util.List;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 01/07/2021 / 12:48 AM
 * HCTeams / rip.orbit.mars.ability.items
 */
public class Switcher extends Ability {

	public Cooldowns cd = new Cooldowns();

	public Switcher() {
		super("Eggport");
	}

	@Override
	public Cooldowns cooldown() {
		return cd;
	}

	@Override
	public String name() {
		return "eggport";
	}

	@Override
	public String displayName() {
		return CC.translate("&d&lEggport");
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

	@Override
	public List<String> lore() {
		return CC.translate(Arrays.asList(
				" ",
				"&7Throw this and if it hits a player your",
				"&7locations will be swapped.",
				" ",
				"&c&lNOTE&7: This can only be done if they're at least 10 blocks away.",
				" "
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
	public void onInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (isSimilar(event)) {
			if (!isClick(event, "RIGHT")) {
				event.setUseItemInHand(Event.Result.DENY);
				return;
			}
			if (!canUse(p)) {
				event.setUseItemInHand(Event.Result.DENY);
				return;
			}
			event.setUseItemInHand(Event.Result.DENY);

			addCooldown(p, 10);
			Snowball snowball = p.launchProjectile(Snowball.class);
			snowball.setMetadata("eggport", new FixedMetadataValue(Mars.getInstance(), true));
			takeItem(p);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onSwitcherHit(EntityDamageByEntityEvent event) {
		if (checkInstancePlayer(event.getEntity())) {
			if (checkInstanceEgg(event.getDamager())) {
				if (event.getDamager().hasMetadata("eggport")) {
					if (checkInstancePlayer(((Egg) event.getDamager()).getShooter())) {
						Player shooter = (Player) ((Egg) event.getDamager()).getShooter();
						Player damaged = (Player) event.getEntity();
						if (!canAttack(shooter, damaged))
							return;
						if (!isInDistance(shooter, damaged, 10)) {
							shooter.sendMessage(CC.translate("&cThat player was out of range."));
							return;
						}

						List<String> hitMsg = Arrays.asList(
								"&cYou have used the &d&lEggport &cand have been put on cooldown",
								"&cfor 10 seconds.");
						hitMsg.forEach(s -> shooter.sendMessage(CC.translate(s)));

						shooter.sendMessage(CC.translate("&aPoof! You have hit " + damaged.getName() + " with your Eggport."));
						damaged.teleport(shooter.getLocation());
						shooter.teleport(damaged.getLocation());
					}
				}
			}
		}
	}

}
