package rip.orbit.mars.command.task;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import rip.orbit.mars.command.StuckCommand;
import rip.orbit.nebula.util.CC;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 26/06/2021 / 9:30 AM
 * potpvp-si / net.frozenorb.potpvp.command.task
 */
public class StuckTask extends BukkitRunnable {

	@Getter private final Player user;
	@Getter private final Location startingLocation;
	@Getter private final BukkitTask bukkitTask;
	@Getter private final double startingHealth;

	public StuckTask(Player user, Location startingLocation, BukkitTask bukkitTask, double startingHealth) {
		this.user = user;
		this.startingLocation = startingLocation;
		this.bukkitTask = bukkitTask;
		this.startingHealth = startingHealth;
	}

	@Override
	public void run() {
		int startingX = startingLocation.getBlockX();
		int startingZ = startingLocation.getBlockZ();
		int startingY = startingLocation.getBlockY();

		int x = user.getLocation().getBlockX();
		int y = user.getLocation().getBlockY();
		int z = user.getLocation().getBlockZ();
		if (startingX != x || startingY != y || startingZ != z) {
			this.cancel();
			bukkitTask.cancel();
			user.sendMessage(CC.translate("&cYou moved a block! Cancelling your teleport!"));
			StuckCommand.stuckTime.removeCooldown(user);
		} else if (startingHealth > user.getHealth()) {
			this.cancel();
			bukkitTask.cancel();
			user.sendMessage(CC.translate("&cYou took damage! Cancelling your teleport!"));
			StuckCommand.stuckTime.removeCooldown(user);
		}
	}
}
