package rip.orbit.mars.ability.items.viper;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import rip.orbit.mars.ability.Ability;
import rip.orbit.mars.ability.profile.AbilityProfile;
import rip.orbit.mars.pvpclasses.pvpclasses.ArcherClass;
import rip.orbit.mars.util.Symbols;
import rip.orbit.mars.util.cooldown.Cooldowns;
import rip.orbit.nebula.util.CC;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 12/02/2022 / 10:00 PM
 * Mars / rip.orbit.mars.ability.items.viper
 */
public class FocusMode extends Ability {

	public HashSet<UUID> gAngel = new HashSet<>();

	public Cooldowns cd = new Cooldowns();

	public FocusMode() {
		super("FocusMode");
	}

	@Override
	public Cooldowns cooldown() {
		return cd;
	}

	@Override
	public List<String> lore() {
		return CC.translate(Arrays.asList(
				"&7After clicking this you will give 10 seconds of",
				"&725% more damage to the person who last hit you."
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
		return CC.translate("&6&lFocus Mode &7(Viper)");
	}

	@Override
	public String name() {
		return "focusmode";
	}

	@Override
	public int data() {
		return 0;
	}

	@Override
	public Material mat() {
		return Material.GOLD_NUGGET;
	}

	@Override
	public boolean glow() {
		return false;
	}

	@EventHandler
	public void onFocusMode(PlayerInteractEvent event) {
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

			event.setCancelled(true);
			takeItem(player);

			addCooldown(player, 90);

			Player target = Bukkit.getPlayer(AbilityProfile.byUUID(player.getUniqueId()).getLastDamagerName());
			if (target != null) {
				ArcherClass.getFocusModedPlayers().put(target.getName(), System.currentTimeMillis() + (10 * 1000));

				List<String> hitMsg = Arrays.asList(
						"",
						"&6You" + " &fhave just used a " + displayName(),
						"&6Dealing 25% more damage to &e" + target.getName() + " &6for the next 10",
						"&6seconds.",
						"");

				hitMsg.forEach(s -> player.sendMessage(CC.translate(s)));
			}

		}
	}
}
