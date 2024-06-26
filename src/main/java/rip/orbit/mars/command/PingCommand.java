package rip.orbit.mars.command;

import rip.orbit.mars.Mars;
import rip.orbit.mars.match.Match;
import rip.orbit.mars.match.MatchTeam;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import cc.fyre.proton.util.PlayerUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PingCommand {

    @Command(names = "ping", permission = "")
    public static void ping(Player sender, @Parameter(name = "target", defaultValue = "self") Player target) {
        int ping = PlayerUtils.getPing(target);

        sender.sendMessage(target.getDisplayName() + ChatColor.YELLOW + "'s Ping: " + ChatColor.GREEN + ping + "ms");

        if (sender.getName().equalsIgnoreCase(target.getName())) {
            Match match = Mars.getInstance().getMatchHandler().getMatchPlaying(sender);
            if (match != null) {
                for (MatchTeam team : match.getTeams()) {
                    for (UUID other : team.getAllMembers()) {
                        Player otherPlayer = Bukkit.getPlayer(other);

                        if (otherPlayer != null && !otherPlayer.equals(sender)) {
                            int otherPing = PlayerUtils.getPing(otherPlayer);
                            sender.sendMessage(otherPlayer.getDisplayName() + ChatColor.YELLOW + "'s Ping: " + ChatColor.GREEN + otherPing + "ms");
                        }
                    }
                }
            }
        }
    }

}
