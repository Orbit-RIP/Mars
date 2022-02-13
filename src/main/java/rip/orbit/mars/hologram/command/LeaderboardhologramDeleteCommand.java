package rip.orbit.mars.hologram.command;

import rip.orbit.mars.Mars;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.mars.hologram.HologramRunnable;
import rip.orbit.mars.kittype.KitType;

public class LeaderboardhologramDeleteCommand {
    @Command(names = {"leaderboardhologram delete", "lh delete"}, permission = "op")
    public static void leaderboardDelete(Player sender, @Parameter(name="name") KitType type) {

        sender.sendMessage(ChatColor.YELLOW + "Attempting to delete " + type.getDisplayName() + "...");

        Mars.getInstance().getHologramHandler().getHologramLocs().remove(type);
        HologramRunnable.getHolograms().remove(type).delete();

        Mars.getInstance().getHologramHandler().save();

    }
}
