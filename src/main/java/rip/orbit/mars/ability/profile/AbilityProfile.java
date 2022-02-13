package rip.orbit.mars.ability.profile;

import lombok.Data;
import org.bukkit.scheduler.BukkitTask;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 18/07/2021 / 2:07 AM
 * HCTeams / rip.orbit.mars.profile
 */

@Data
public class AbilityProfile {

	public static Map<UUID, AbilityProfile> profileMap = new ConcurrentHashMap<>();

	private final UUID uuid;
	private String lastHitName = "";
	private String lastDamagerName = "";
	private BukkitTask ninjaTask;

	private long ninjaLastHitTime;
	private long antibuildHitTime;
	private long abilityInspectorHitTime;
	private long curseHitTime;
	private long peekABooHitTime;
	private long pearlThrownTime;

	public AbilityProfile(UUID uuid) {
		this.uuid = uuid;

		profileMap.put(this.uuid, this);
	}

	public boolean canHit(long time) {
		return time - System.currentTimeMillis() <= 0;
	}

	public static AbilityProfile byUUID(UUID toSearch) {
		for (AbilityProfile value : profileMap.values()) {
			if (value.getUuid() == toSearch) {
				return value;
			}
		}
		return new AbilityProfile(toSearch);
	}

}
