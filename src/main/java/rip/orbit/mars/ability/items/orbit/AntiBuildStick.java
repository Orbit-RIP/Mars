package rip.orbit.mars.ability.items.orbit;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import rip.orbit.mars.Mars;
import rip.orbit.mars.ability.Ability;
import rip.orbit.mars.ability.items.AbilityType;
import rip.orbit.mars.ability.profile.AbilityProfile;
import rip.orbit.mars.util.cooldown.Cooldowns;
import rip.orbit.nebula.util.CC;
import rip.orbit.nebula.util.JavaUtils;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 18/07/2021 / 1:46 AM
 * HCTeams / rip.orbit.mars.ability.items
 */
public class AntiBuildStick extends Ability {

	public Cooldowns cd = new Cooldowns();
	public static Cooldowns buildTime = new Cooldowns();
	public ConcurrentHashMap<UUID, Integer> hits = new ConcurrentHashMap<>();

	public static List<String> blockedTypes = Arrays.asList(
			"DOOR",
			"PLATE",
			"CHEST",
			"GATE"
	);

	public static List<String> blockedTypesPretty = Arrays.asList(
			"Fence Gates",
			"Pressure Plates",
			"Trap Doors",
			"Chest",
			"Doors",
			"Buttons & Levers"
	);

	public AntiBuildStick() {
		super("Antibuildstikc");
	}

	@Override
	public Cooldowns cooldown() {
		return cd;
	}

	@Override
	public String name() {
		return "antibuildstick";
	}

	@Override
	public String displayName() {
		return CC.translate("&6&lAntiBuildStick");
	}

	@Override
	public int data() {
		return 0;
	}

	@Override
	public Material mat() {
		return Material.STICK;
	}

	@Override
	public boolean glow() {
		return true;
	}

	@Override
	public List<String> lore() {
		return CC.translate(
				Arrays.asList(
						" ",
						"&7Hit a player 3 times to commence a sequence where they",
						"&7cannot build/interact with a select few materials within",
						"&7the game.",
						" ",
						"&c&lNOTE&7: They will not be able to use/interact with any of the following:",
						"&7" + StringUtils.join(blockedTypesPretty, ", "),
						" "
				)
		);
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
		if (!checkInstancePlayer(event.getEntity()))
			return;
		if (!checkInstancePlayer(event.getDamager()))
			return;

		Player damager = (Player) event.getDamager();
		AbilityProfile profile = AbilityProfile.byUUID(damager.getUniqueId());

		if (!isSimilar(damager.getItemInHand()))
			return;

		Player damaged = (Player) event.getEntity();

		if (!canUse(damager)) {
			return;
		}
		if (!canAttack(damager, damaged))
			return;

		if (!hits.isEmpty() && hits.get(damager.getUniqueId()) != null && hits.get(damager.getUniqueId()) >= 3) {

			List<String> beenHitMsg = Arrays.asList(
					"",
					"&6&lYOU HAVE BEEN HIT!",
					" ",
					"&6" + damager.getName() + " &fhas just hit you with",
					"&fan &6AntiBuildStick&f.",
					" ",
					"&7┃ &fYou cannot use/interact with the following",
					"&7┃ &ffor &615 seconds&f.",
					"&7┃ &f" + StringUtils.join(blockedTypesPretty, ", "),
					"");

			List<String> hitMsg = Arrays.asList(
					"",
					"&6&lYOU HAVE HIT SOMEONE!",
					" ",
					"&6You" + " &fhave just hit &6" + damaged.getName(),
					"&fwith an &6AntiBuildStick&f.",
					" ",
					"&7┃ &fThey cannot use/interact with the following",
					"&7┃ &ffor &615 seconds&f.",
					"&7┃ &f" + StringUtils.join(blockedTypesPretty, ", "),
					"");

			hitMsg.forEach(s -> damager.sendMessage(CC.translate(s)));

			beenHitMsg.forEach(s -> damaged.sendMessage(CC.translate(s)));

			Mars.getInstance().getAbilityHandler().getAbilityEffect().applyCooldown(damaged, 15);
			buildTime.applyCooldown(damaged, 15);
			addCooldown(damager, 90);
			takeItem(damager);

			CompletableFuture.runAsync(() -> hits.remove(damager.getUniqueId()));
			return;
		}

		if (profile.canHit(profile.getAntibuildHitTime())) {
			CompletableFuture.runAsync(() -> hits.remove(damager.getUniqueId()));
		}

		addHits(damager, hits);

		profile.setAntibuildHitTime(System.currentTimeMillis() + JavaUtils.parse("15s"));
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (buildTime.onCooldown(event.getPlayer())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(CC.translate("&cYou cannot do this for &l" + buildTime.getRemaining(event.getPlayer())));
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (buildTime.onCooldown(event.getPlayer())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(CC.translate("&cYou cannot do this for &l" + buildTime.getRemaining(event.getPlayer())));
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (buildTime.onCooldown(event.getPlayer())) {
			if (event.getClickedBlock() == null)
				return;
			if (event.getAction() == Action.PHYSICAL) {
				event.setUseInteractedBlock(Event.Result.DENY);
				event.getPlayer().sendMessage(CC.translate("&cYou cannot do this for &l" + buildTime.getRemaining(event.getPlayer())));
				return;
			}
			for (String blockedType : blockedTypes) {
				if (event.getClickedBlock().getType().name().contains(blockedType)) {
					event.setUseInteractedBlock(Event.Result.DENY);
					event.getPlayer().sendMessage(CC.translate("&cYou cannot do this for &l" + buildTime.getRemaining(event.getPlayer())));
				}
			}
		}
	}

}
