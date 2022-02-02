package rip.bridge.practice.kittype.commands;

import rip.bridge.practice.kittype.KitType;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KitInfoCommand {

    @Command(names = { "kittype info" }, permission = "op", description = "Creates a new kit-type")
    public static void execute(Player player, @Param(name = "name")KitType kitType) {

        player.sendMessage(ChatColor.RED + "Name:  " + ChatColor.GRAY + kitType.getColoredDisplayName());
        player.sendMessage(ChatColor.RED + "KB Profile: " + ChatColor.GRAY + kitType.getKnockbackName());

    }

}
