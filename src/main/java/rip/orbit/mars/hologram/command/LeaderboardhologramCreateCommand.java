package rip.orbit.mars.hologram.command;

import rip.orbit.mars.Mars;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LeaderboardhologramCreateCommand {

    @Command(names = {"leaderboardhologram create", "lh create"}, permission = "op")
    public static void leaderboardHologram(Player sender, @Parameter(name="name") String name) {

        sender.sendMessage(ChatColor.YELLOW + "Setting up hologram " + name + "...");

        switch(name.toLowerCase()) {
            case "nodebuff":
                Mars.getInstance().getHologramHandler().setTopNoDebuffElo(sender.getLocation());
                Mars.getInstance().getHologramHandler().getHologramRunnable().run();
                break;
            case "sumo":
                Mars.getInstance().getHologramHandler().setTopSumoElo(sender.getLocation());
                Mars.getInstance().getHologramHandler().getHologramRunnable().run();
                break;
            case "spleef":
                Mars.getInstance().getHologramHandler().setTopSpleefElo(sender.getLocation());
                Mars.getInstance().getHologramHandler().getHologramRunnable().run();
                break;
            case "builduhc":
                Mars.getInstance().getHologramHandler().setTopBuildUHCElo(sender.getLocation());
                Mars.getInstance().getHologramHandler().getHologramRunnable().run();
                break;
            case "global":
                Mars.getInstance().getHologramHandler().setTopOverallElo(sender.getLocation());
                Mars.getInstance().getHologramHandler().getHologramRunnable().run();
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Invalid elo name.");
        }



    }
}
