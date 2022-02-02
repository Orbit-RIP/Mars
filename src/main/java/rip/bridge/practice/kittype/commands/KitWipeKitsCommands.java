package rip.bridge.practice.kittype.commands;

import rip.bridge.practice.Practice;
import rip.bridge.practice.kittype.KitType;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;
import rip.bridge.qlib.util.UUIDUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class KitWipeKitsCommands {

    @Command(names = "kit wipeKits Type", permission = "op")
    public static void kitWipeKitsType(Player sender, @Param(name="kit type") KitType kitType) {
        int modified = Practice.getInstance().getKitHandler().wipeKitsWithType(kitType);
        sender.sendMessage(ChatColor.YELLOW + "Wiped " + modified + " " + kitType.getDisplayName() + " kits.");
        sender.sendMessage(ChatColor.GRAY + "^ We would have a proper count here if we ran recent versions of MongoDB");
    }

    @Command(names = "kit wipeKits Player", permission = "op")
    public static void kitWipeKitsPlayer(Player sender, @Param(name="target") UUID target) {
        Practice.getInstance().getKitHandler().wipeKitsForPlayer(target);
        sender.sendMessage(ChatColor.YELLOW + "Wiped " + UUIDUtils.name(target) + "'s kits.");
    }

}