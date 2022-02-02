package rip.bridge.practice.commands;

import rip.bridge.practice.Practice;
import rip.bridge.qlib.command.Command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public final class BuildCommand {

    @Command(names = {"build"}, permission = "op")
    public static void silent(Player sender) {
        if (sender.hasMetadata("Build")) {
            sender.removeMetadata("Build", Practice.getInstance());
            sender.sendMessage(ChatColor.RED + "Build mode disabled.");
        } else {
            sender.setMetadata("Build", new FixedMetadataValue(Practice.getInstance(), true));
            sender.sendMessage(ChatColor.GREEN + "Build mode enabled.");
        }
    }

}