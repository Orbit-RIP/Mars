package rip.bridge.practice.commands;

import rip.bridge.practice.Practice;
import org.bukkit.OfflinePlayer;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class EloReset {

    @Command(names = "eloreset", permission = "practice.admin", description = "Manually reset the player's elo")
    public static void eloreset(Player sender, @Param(name="target") OfflinePlayer target) {
        Practice.getInstance().getEloHandler().resetElo(target.getUniqueId());
        sender.sendMessage(ChatColor.GREEN + "Resetting elo of " + target.getName() + ChatColor.GREEN + ".");
    }
}
