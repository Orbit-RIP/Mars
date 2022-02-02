//package rip.bridge.practice.tournament.commands;
//
//import rip.bridge.practice.Practice;
//import rip.bridge.practice.tournament.TournamentHandler;
//import rip.bridge.qlib.command.Command;
//import org.bukkit.Bukkit;
//import org.bukkit.ChatColor;
//import org.bukkit.command.CommandSender;
//
//public class TournamentCancelCommand {
//
//    @Command(names = { "tournament cancel", "tcancel", "tourn cancel"},  permission = "practice.admin")
//    public static void tournamentCancel(CommandSender sender) {
//        if (Practice.getInstance().getTournamentHandler().getTournament() == null) {
//            sender.sendMessage(ChatColor.RED + "There is no running tournament to cancel.");
//            return;
//        }
//
//        Bukkit.broadcastMessage("");
//        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&cTournament cancelled."));
//        Bukkit.broadcastMessage("");
//        Practice.getInstance().getTournamentHandler().setTournament(null);
//    }
//}
