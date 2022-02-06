package rip.orbit.mars.hologram.command;

import cc.fyre.proton.command.param.Parameter;
import rip.orbit.mars.Mars;
import cc.fyre.proton.command.Command;
import org.bukkit.entity.Player;

public class LeaderboardhologramMoveCommand {

    @Command(names = {"leaderboardhologram move", "lh move"}, permission = "op")
    public static void leaderboardMove(Player sender, @Parameter(name="name") String name) {
        Mars.getInstance().getServer().dispatchCommand(sender, "lh create " + name);
    }
}
