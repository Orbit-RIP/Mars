package rip.orbit.mars.hologram.command;

import rip.orbit.mars.Mars;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.mars.kittype.KitType;

public class LeaderboardhologramCreateCommand {

    @Command(names = {"leaderboardhologram create", "lh create"}, permission = "op")
    public static void leaderboardHologram(Player sender, @Parameter(name="kit") String name) {

        sender.sendMessage(ChatColor.YELLOW + "Setting up hologram " + name + "...");

        KitType type = KitType.byId(name);

        Mars.getInstance().getHologramHandler().getHologramLocs().put(type, sender.getLocation());
        Mars.getInstance().getHologramHandler().getHologramRunnable().run();

    }
}
