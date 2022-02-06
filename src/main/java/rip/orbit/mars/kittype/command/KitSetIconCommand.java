package rip.orbit.mars.kittype.command;

import rip.orbit.mars.kittype.KitType;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KitSetIconCommand {

	@Command(names = { "kittype seticon" }, permission = "op", description = "Sets a kit-type's icon")
	public static void execute(Player player, @Parameter(name = "kittype") KitType kitType) {
		if (player.getItemInHand() == null) {
			player.sendMessage(ChatColor.RED + "Please hold an item in your hand.");
			return;
		}

		kitType.setIcon(player.getItemInHand().getData());
		kitType.saveAsync();

		player.sendMessage(ChatColor.GREEN + "You've updated this kit-type's icon.");
	}

}
