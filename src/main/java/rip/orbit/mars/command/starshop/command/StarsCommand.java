package rip.orbit.mars.command.starshop.command;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.orbit.mars.Mars;
import rip.orbit.mars.command.starshop.menu.StarShopMenu;
import rip.orbit.nebula.util.CC;

import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 14/08/2021 / 6:04 AM
 * HCTeams / rip.orbit.mars.stars.command
 */
public class StarsCommand {

	@Command(names = "starshop", permission = "")
	public static void starshop(Player sender) {
		new StarShopMenu().openMenu(sender);
	}

	@Command(names = {"stars set"}, permission = "op")
	public static void setStars(CommandSender sender, @Parameter(name = "target") UUID target, @Parameter(name = "amount") int amount) {
		Mars.getInstance().getStarsMap().set(target, amount);
		sender.sendMessage(CC.translate("&aSuccess, the players stars is now " + amount));
	}

	@Command(names = {"stars add"}, permission = "op")
	public static void addStars(CommandSender sender, @Parameter(name = "target") UUID target, @Parameter(name = "amount") int amount) {
		int toAdd = Mars.getInstance().getStarsMap().get(target) + amount;
		Mars.getInstance().getStarsMap().set(target, toAdd);
		sender.sendMessage(CC.translate("&aSuccess, the players stars is now " + toAdd));
	}

}
