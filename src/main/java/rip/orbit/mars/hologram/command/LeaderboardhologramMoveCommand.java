package rip.orbit.mars.hologram.command;

import cc.fyre.proton.command.param.Parameter;
import rip.orbit.mars.Mars;
import cc.fyre.proton.command.Command;
import org.bukkit.entity.Player;
import rip.orbit.mars.hologram.HologramHandler;
import rip.orbit.mars.hologram.HologramRunnable;
import rip.orbit.mars.kittype.KitType;

public class LeaderboardhologramMoveCommand {

    @Command(names = {"leaderboardhologram move", "lh move"}, permission = "op")
    public static void leaderboardMove(Player sender, @Parameter(name="name")KitType type) {

        Mars.getInstance().getHologramHandler().getHologramLocs().put(type, sender.getLocation());
        HologramRunnable.update(HologramRunnable.getHolograms().get(type), type);

        Mars.getInstance().getHologramHandler().save();
    }
}
