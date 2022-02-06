package rip.orbit.mars.command;

import rip.orbit.mars.Mars;
import rip.orbit.mars.util.VisibilityUtils;
import cc.fyre.proton.command.Command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public final class SilentCommand {

    @Command(names = {"silent"}, permission = "potpvp.silent")
    public static void silent(Player sender) {
        if (sender.hasMetadata("ModMode")) {
            sender.removeMetadata("ModMode", Mars.getInstance());
            sender.removeMetadata("invisible", Mars.getInstance());

            sender.sendMessage(ChatColor.RED + "Silent mode disabled.");
        } else {
            sender.setMetadata("ModMode", new FixedMetadataValue(Mars.getInstance(), true));
            sender.setMetadata("invisible", new FixedMetadataValue(Mars.getInstance(), true));
            
            sender.sendMessage(ChatColor.GREEN + "Silent mode enabled.");
        }

        VisibilityUtils.updateVisibility(sender);
    }

}