package rip.orbit.mars.ability.profile;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import rip.orbit.mars.ability.Ability;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 18/07/2021 / 2:08 AM
 * HCTeams / rip.orbit.mars.profile
 */
public class ProfileListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if (AbilityProfile.profileMap.containsKey(event.getPlayer().getUniqueId()))
			return;

		new AbilityProfile(event.getPlayer().getUniqueId());

	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onHit(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Projectile)
			return;
		if (event.getDamager() instanceof Player) {
			if (event.getEntity() instanceof Player) {
				if (Ability.canAttack((Player) event.getDamager(), (Player) event.getEntity())) {
					AbilityProfile p = AbilityProfile.byUUID(event.getDamager().getUniqueId());
					AbilityProfile damaged = AbilityProfile.byUUID(event.getEntity().getUniqueId());
					if (!p.getLastHitName().equals(((Player) event.getEntity()).getName())) {
						p.setLastHitName(((Player) event.getEntity()).getName());
					}
					if (!damaged.getLastDamagerName().equals(((Player) event.getDamager()).getName())) {
						damaged.setLastDamagerName(((Player) event.getDamager()).getName());
					}
				}
			}
		}
	}

}
