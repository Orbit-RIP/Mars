package rip.bridge.practice.tournament.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.practice.tournament.menu.TournamentMenu;
import rip.bridge.qlib.command.Command;

public class TournamentStartCommand {

    @Command(names = { "tournament start", "tourn start"}, permission = "practice.tournament.start")
    public static void host(Player sender) {
        new TournamentMenu(kitType -> kitType.getColoredDisplayName(), ChatColor.BLUE.toString() + ChatColor.BOLD + "Select a tournament kit").openMenu(sender);
        }
    }
