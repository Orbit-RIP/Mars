//package rip.bridge.practice.tournament.commands;
//
//import rip.bridge.practice.Practice;
//import rip.bridge.practice.lobby.menu.TournamentSpectateMenu;
//import rip.bridge.qlib.command.Command;
//import org.bukkit.ChatColor;
//import org.bukkit.entity.Player;
//
//public class TournamentSpectateCommand {
//
//    @Command(names = {"tournament spectate", "tourn spectate", "tournament spec", "tourn spec", "tournament list", "tourn list"}, permission = "")
//    public static void host(Player sender) {
//        if (Practice.getInstance().getTournamentHandler().getTournament() == null) {
//            sender.sendMessage(ChatColor.RED + "No tournament is currently active.");
//        } else {
//            new TournamentSpectateMenu().openMenu(sender);
//        }
//    }
//}
//
