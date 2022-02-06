package rip.orbit.mars.hologram.command;

import rip.orbit.mars.Mars;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LeaderboardhologramDeleteCommand {
    @Command(names = {"leaderboardhologram delete", "lh delete"}, permission = "op")
    public static void leaderboardDelete(Player sender, @Parameter(name="name") String name) {

        sender.sendMessage(ChatColor.YELLOW + "Attempting to delete " + name + "...");

        switch(name.toLowerCase()) {
            case "nodebuff":
                Mars.getInstance().getHologramHandler().setTopNoDebuffElo(sender.getLocation().add(0,-200,0));
                break;
            case "sumo":
                Mars.getInstance().getHologramHandler().setTopSumoElo(sender.getLocation().add(0,-200,0));
                break;
            case "spleef":
                Mars.getInstance().getHologramHandler().setTopSpleefElo(sender.getLocation().add(0,-200,0));
                break;
            case "builduhc":
                Mars.getInstance().getHologramHandler().setTopBuildUHCElo(sender.getLocation().add(0,-200,0));
                break;
            case "global":
                Mars.getInstance().getHologramHandler().setTopOverallElo(sender.getLocation().add(0,-200,0));
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Invalid elo name.");
        }

        Mars.getInstance().getServer().dispatchCommand(sender, "lh create " + name);


    }
}
