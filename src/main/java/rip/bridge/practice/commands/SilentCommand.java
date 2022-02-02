package rip.bridge.practice.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import rip.bridge.practice.Practice;
import rip.bridge.practice.util.VisibilityUtils;
import rip.bridge.qlib.command.Command;

public final class SilentCommand {

    @Command(names = {"silent"}, permission = "basic.staff")
    public static void silent(Player sender) {
        if (sender.hasMetadata("ModMode")) {
            sender.removeMetadata("ModMode", Practice.getInstance());
            sender.removeMetadata("invisible", Practice.getInstance());
            sender.sendMessage(ChatColor.RED + "Silent mode disabled.");
        } else {
            sender.setMetadata("ModMode", new FixedMetadataValue(Practice.getInstance(), true));
            sender.setMetadata("invisible", new FixedMetadataValue(Practice.getInstance(), true));
            sender.sendMessage(ChatColor.GREEN + "Silent mode enabled.");
        }

        VisibilityUtils.updateVisibility(sender);
    }

}