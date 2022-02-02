package rip.bridge.practice.commands;

import rip.bridge.practice.Practice;
import rip.bridge.qlib.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ConfigReloadCommand {

    @Command(names = {"practice reload"}, permission = "op", hidden = true)
    public static void config(CommandSender sender) {

        Practice.getInstance().reloadConfig();
        Practice.getInstance().saveConfig();
        sender.sendMessage(ChatColor.GREEN + "Practice Configuration Reloaded.");
    }
}