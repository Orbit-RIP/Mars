package rip.orbit.mars.ability.items.viper;

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
import rip.orbit.mars.ability.profile.AbilityProfile;
import rip.orbit.mars.util.Symbols;
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
public class ExoticBone extends Ability {

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

	public ExoticBone() {
		super("exoticbone");
	}

	@Override
	public Cooldowns cooldown() {
		return cd;
	}

	@Override
	public String name() {
		return "viper-exoticbone";
	}

	@Override
	public String displayName() {
		return CC.translate("&6&lExotic Bone &7(Viper)");
	}

	@Override
	public int data() {
		return 0;
	}

	@Override
	public Material mat() {
		return Material.BONE;
	}

	@Override
	public boolean glow() {
		return false;
	}

	@Override
	public List<String> lore() {
		return CC.translate(
				Arrays.asList(
						"&7Hit a player 3 times to commence a sequence where they",
						"&7cannot build/interact with a select few materials"
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
					" ",
					Symbols.LARROW + "&cYou have been hit with &fExotic Bone&c!",
					Symbols.LARROW + "&CYou cannot break or interact with openables for &f15 seconds&c.",
					" ");

			List<String> hitMsg = Arrays.asList(
					" ",
					Symbols.LARROW + "&6You have successfully hit &f" + damaged.getName() + "&6!",
					Symbols.LARROW + "&6Now on cooldown for &f2 minutes",
					" ");

			hitMsg.forEach(s -> damager.sendMessage(CC.translate(s)));

			beenHitMsg.forEach(s -> damaged.sendMessage(CC.translate(s)));

			Mars.getInstance().getAbilityHandler().getAbilityEffect().applyCooldown(damaged, 15);
			buildTime.applyCooldown(damaged, 15);
			addCooldown(damager, 120);
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
