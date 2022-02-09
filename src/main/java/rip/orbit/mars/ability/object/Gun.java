package rip.orbit.mars.ability.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 02/07/2021 / 1:43 AM
 * HCTeams / rip.orbit.mars.ability.object
 */

@AllArgsConstructor
@Getter
@Setter
public class Gun {

	private UUID owner;
	private int hits;
	private Location location;
	private BukkitTask task;

}
