package rip.orbit.mars.ability.profile;

import lombok.Data;
import rip.orbit.mars.ability.profile.task.TaskType;
import org.bukkit.scheduler.BukkitTask;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 18/07/2021 / 2:07 AM
 * HCTeams / rip.orbit.hcteams.profile
 */

@Data
public class Profile {

	public static Map<UUID, Profile> profileMap = new HashMap<>();

	private final UUID uuid;
	private String lastHitName = "";
	private String lastDamagerName = "";
	private BukkitTask ninjaTask;
	private ConcurrentHashMap<TaskType, BukkitTask> tasks;

	public Profile(UUID uuid) {
		this.uuid = uuid;

		tasks = new ConcurrentHashMap<>();

		Profile.profileMap.put(this.uuid, this);
	}

	public static Profile byUUID(UUID toSearch) {
		for (Profile value : profileMap.values()) {
			if (value.getUuid() == toSearch) {
				return value;
			}
		}
		return new Profile(toSearch);
	}

}
