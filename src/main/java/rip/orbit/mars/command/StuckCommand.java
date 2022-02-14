package rip.orbit.mars.command;

import cc.fyre.proton.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import rip.orbit.mars.Mars;
import rip.orbit.mars.command.task.StuckTask;
import rip.orbit.mars.match.Match;
import rip.orbit.mars.util.cooldown.Cooldowns;
import rip.orbit.nebula.util.CC;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 26/06/2021 / 9:27 AM
 * potpvp-si / net.frozenorb.potpvp.command
 */
public class StuckCommand {

	public static Cooldowns stuckTime = new Cooldowns();

	@Command(names = "stuck", permission = "")
	public static void stuck(Player sender) {
		Match match = Mars.getInstance().getMatchHandler().getMatchPlaying(sender);
		if (match == null) {
			sender.sendMessage(CC.translate("&cYou cannot do this whilst you're not in a match."));
			return;
		}
		if (!match.getKitType().getId().contains("BaseRaiding")) {
			sender.sendMessage(CC.translate("&cYou can only do this if you're playing the Base Raiding simulation."));
			return;
		}
		sender.sendMessage(CC.translate("&cYou will be teleported to your spawn point in 15 seconds, don't get hit or move a block!"));
		stuckTime.applyCooldown(sender, 15);
		BukkitTask task = new BukkitRunnable() {
			@Override
			public void run() {
				sender.teleport(match.getArena().getSpectatorSpawn().add(0, 2, 0));
			}
		}.runTaskLater(Mars.getInstance(), 20 * 15);
		BukkitRunnable runnable = new StuckTask(sender, sender.getLocation(), task, sender.getHealth());
		runnable.runTaskTimer(Mars.getInstance(), 2, 2);


	}

}
