package rip.orbit.mars.kittype.command;

import rip.orbit.mars.kittype.KitType;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KitSetDisplayNameCommand {

	@Command(names = { "kittype setdisplayname" }, permission = "op", description = "Sets a kit-type's display name")
	public static void execute(Player player, @Parameter(name = "kittype") KitType kitType, @Parameter(name = "displayName", wildcard = true) String displayName) {
		kitType.setDisplayName(displayName);
		kitType.saveAsync();

		player.sendMessage(ChatColor.GREEN + "You've updated this kit-type's display name.");
	}

}
