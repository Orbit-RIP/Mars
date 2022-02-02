package rip.bridge.practice.kittype.commands;

import rip.bridge.practice.kittype.KitType;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KitSetDisplayNameCommand {

	@Command(names = { "kittype setdisplayname" }, permission = "op", description = "Sets a kit-type's display name")
	public static void execute(Player player, @Param(name = "kittype") KitType kitType, @Param(name = "displayName", wildcard = true) String displayName) {
		kitType.setDisplayName(displayName);
		kitType.saveAsync();

		player.sendMessage(ChatColor.GREEN + "You've updated this kit-type's display name.");
	}

}
