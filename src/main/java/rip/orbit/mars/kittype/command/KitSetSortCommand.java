package rip.orbit.mars.kittype.command;

import rip.orbit.mars.kittype.KitType;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Comparator;

public class KitSetSortCommand {

	@Command(names = { "kittype setsort" }, permission = "op", description = "Sets a kit-type's sort")
	public static void execute(Player player, @Parameter(name = "kittype") KitType kitType, @Parameter(name = "sort") int sort) {
		kitType.setSort(sort);
		kitType.saveAsync();

		KitType.getAllTypes().sort(Comparator.comparing(KitType::getSort));

		player.sendMessage(ChatColor.GREEN + "You've updated this kit-type's sort.");
	}

}
