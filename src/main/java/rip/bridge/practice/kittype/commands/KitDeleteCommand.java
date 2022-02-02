package rip.bridge.practice.kittype.commands;

import rip.bridge.practice.kittype.KitType;
import rip.bridge.practice.Practice;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KitDeleteCommand {

	@Command(names = { "kittype delete" }, permission = "op", description = "Deletes an existing kit-type")
	public static void execute(Player player, @Param(name = "kittype") KitType kitType) {
		kitType.deleteAsync();
		KitType.getAllTypes().remove(kitType);
		Practice.getInstance().getQueueHandler().removeQueues(kitType);

		player.sendMessage(ChatColor.GREEN + "You've deleted the kit-type by the ID \"" + kitType.id + "\".");
	}

}
