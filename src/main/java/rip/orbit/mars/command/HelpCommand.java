package rip.orbit.mars.command;

import com.google.common.collect.ImmutableList;

import rip.orbit.mars.MarsLang;
import rip.orbit.mars.Mars;
import rip.orbit.mars.match.MatchHandler;
import cc.fyre.proton.command.Command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Generic /help command, changes message sent based on if sender is playing in
 * or spectating a match.
 */
public final class HelpCommand {

    private static final List<String> HELP_MESSAGE_HEADER = ImmutableList.of(
        ChatColor.DARK_GRAY + MarsLang.LONG_LINE,
        "§6§lPractice Help",
        ChatColor.DARK_GRAY + MarsLang.LONG_LINE
    );

    private static final List<String> HELP_MESSAGE_LOBBY = ImmutableList.of(
        "§6Common Commands:",
        "§e/duel <player> §7- Challenge a player to a duel",
        "§e/party invite <player> §7- Invite a player to a party",
        "",
        "§6Other Commands:",
        "§e/party help §7- Information on party commands",
        "§e/report <player> <reason> §7- Report a player for violating the rules",
        "§e/request <message> §7- Request assistance from a staff member"
    );

    private static final List<String> HELP_MESSAGE_MATCH = ImmutableList.of(
        "§6Common Commands:",
        "§e/spectate <player> §7- Spectate a player in a match",
        "§e/report <player> <reason> §7- Report a player for violating the rules",
        "§e/request <message> §7- Request assistance from a staff member"
    );

    private static final List<String> HELP_MESSAGE_FOOTER = ImmutableList.of(
        "",
        "§6Server Information:",
        Mars.getInstance().getDominantColor() == ChatColor.GOLD ? "§eOfficial Teamspeak §7- ts.orbit.rip" : "§eOfficial Teamspeak §7- ts.orbit.rip",
//        Mars.getInstance().getDominantColor() == ChatColor.RED ? "§eOfficial Rules §7- §dwww.veltpvp.com/rules" : "§eOfficial Rules §7- §dwww.veltpvp.com/rules",
        Mars.getInstance().getDominantColor() == ChatColor.GOLD ? "§eStore §7- donate.orbit.rip" : "§eStore §7- donate.orbit.rip",
     // "§ePractice Leaderboards §7- §dwww.minehq.com/stats/potpvp",
        ChatColor.DARK_GRAY + MarsLang.LONG_LINE
    );

    @Command(names = {"help", "?", "halp", "helpme"}, permission = "")
    public static void help(Player sender) {
        MatchHandler matchHandler = Mars.getInstance().getMatchHandler();

        HELP_MESSAGE_HEADER.forEach(sender::sendMessage);

        if (matchHandler.isPlayingOrSpectatingMatch(sender)) {
            HELP_MESSAGE_MATCH.forEach(sender::sendMessage);
        } else {
            HELP_MESSAGE_LOBBY.forEach(sender::sendMessage);
        }

        HELP_MESSAGE_FOOTER.forEach(sender::sendMessage);
    }

}
