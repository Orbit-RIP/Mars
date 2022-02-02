package rip.bridge.practice.kittype.commands;

import rip.bridge.practice.kittype.KitType;

import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class KitLoadDefaultCommand {

    @Command(names = "kit loadDefault", permission = "op")
    public static void kitLoadDefault(Player sender, @Param(name="kit type") KitType kitType) {
        sender.getInventory().setArmorContents(kitType.getDefaultArmor());
        sender.getInventory().setContents(kitType.getDefaultInventory());
        sender.updateInventory();

        sender.sendMessage(ChatColor.YELLOW + "Loaded default armor/inventory for " + kitType + ".");
    }

}