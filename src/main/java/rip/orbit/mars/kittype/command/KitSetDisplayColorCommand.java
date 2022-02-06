package rip.orbit.mars.kittype.command;

import rip.orbit.mars.kittype.KitType;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KitSetDisplayColorCommand {

	@Command(names = { "kittype setdisplaycolor" }, permission = "op", description = "Sets a kit-type's display color")
	public static void execute(Player player, @Parameter(name = "kittype") KitType kitType, @Parameter(name = "displayColor", wildcard = true) String color) {
		kitType.setDisplayColor(ChatColor.valueOf(color.toUpperCase().replace(" ", "_")));
		kitType.saveAsync();

		player.sendMessage(ChatColor.GREEN + "You've updated this kit-type's display color.");
	}

}
