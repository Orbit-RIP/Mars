package rip.bridge.practice.kittype.commands;

import rip.bridge.practice.kittype.KitType;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.spigotmc.SpigotConfig;

public class KitSetKnockbackProfileCommand {

    @Command(names = { "kittype setkb", "kittype setknockback" }, permission = "op", description = "Sets a Kit-Types Knockback profile")
    public static void execute(Player player, @Param(name = "kittype")KitType kitType, @Param(name = "profile") String profile) {
        if (SpigotConfig.getKnockbackByName(profile) == null) {
            player.sendMessage(ChatColor.RED + "No knockback profile with the name " + profile + " found.");
        }
        kitType.setKnockbackName(profile);
        kitType.saveAsync();
        player.sendMessage(ChatColor.YELLOW + "Updated the Knockback Profile of " + kitType.getDisplayName() + " to " + ChatColor.LIGHT_PURPLE + profile + ".");


    }
}
