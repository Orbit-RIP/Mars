package rip.orbit.mars.party.command;

import com.google.common.collect.ImmutableList;

import rip.orbit.mars.MarsLang;
import cc.fyre.proton.command.Command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public final class PartyHelpCommand {

    private static final List<String> HELP_MESSAGE = ImmutableList.of(
        ChatColor.DARK_RED + MarsLang.LONG_LINE,
        "§c§lParty Help §7- §fInformation on how to use party commands",
        ChatColor.DARK_RED + MarsLang.LONG_LINE,
        "§cParty Commands:",
        "§e/party invite §7- Invite a player to join your party",
        "§e/party leave §7- Leave your current party",
        "§e/party accept [player] §7- Accept party invitation",
        "§e/party info [player] §7- View the roster of the party",
        "",
        "§cLeader Commands:",
        "§e/party kick <player> §7- Kick a player from your party",
        "§e/party leader <player> §7- Transfer party leadership",
        "§e/party disband §7 - Disbands party",
        "§e/party lock §7 - Lock party from others joining",
        "§e/party open §7 - Open party to others joining",
        "§e/party password <password> §7 - Sets party password",
        "",
        "§cOther Help:",
        "§eTo use §cparty chat§e, prefix your message with the §7'§c@§7' §esign.",
        ChatColor.DARK_RED + MarsLang.LONG_LINE
    );

    @Command(names = {"party", "p", "t", "team", "f", "party help", "p help", "t help", "team help", "f help"}, permission = "")
    public static void party(Player sender) {
        HELP_MESSAGE.forEach(sender::sendMessage);
    }

}