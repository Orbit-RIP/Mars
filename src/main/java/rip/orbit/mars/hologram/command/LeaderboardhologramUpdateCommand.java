package rip.orbit.mars.hologram.command;

import rip.orbit.mars.Mars;
import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LeaderboardhologramUpdateCommand {

    @Command(names = {"leaderboardhologram update", "lh update"}, permission = "op")
    public static void leaderboardHologram(Player sender) {

        sender.sendMessage(ChatColor.YELLOW + "Updating all holograms...");
        Mars.getInstance().getHologramHandler().getHologramRunnable().run();
    }
}
