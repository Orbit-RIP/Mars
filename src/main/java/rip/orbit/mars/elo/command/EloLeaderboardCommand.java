package rip.orbit.mars.elo.command;

import com.google.common.collect.Lists;
import rip.orbit.mars.Mars;
import rip.orbit.mars.elo.EloHandler;
import rip.orbit.mars.kittype.KitType;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;


public class EloLeaderboardCommand {

    private static EloHandler eloHandler = Mars.getInstance().getEloHandler();
    private static KitType kitType;

    public EloLeaderboardCommand(KitType kitType) {
        this.kitType = kitType;
    }


    @Command(names = {"leaderboard"}, permission = "")
    public static void leaderboard(Player sender, @Parameter(name="elo") String elo) {
        leaderboardMessage(sender, elo);
    }

    private static void leaderboardMessage(Player sender, String leaderboard) {
        List<String> description = Lists.newArrayList();

        description.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------------");
            description.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Top Elo " + ChatColor.GRAY + "(" + leaderboard + ")");
        description.add(" ");

        int counter = 1;

        for (Map.Entry<String, Integer> entry : eloHandler.topElo(KitType.byId(leaderboard)).entrySet()) {
            String color = (counter <= 3 ? ChatColor.GOLD + "" + ChatColor.BOLD : ChatColor.GRAY).toString();
            description.add(color + "#" + counter + " " + ChatColor.WHITE + entry.getKey() + " [" + entry.getValue() + "]");

            counter++;
        }

        description.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------------");

        description.forEach(sender::sendMessage);
    }
}
