package rip.bridge.practice.setting.commands;

import rip.bridge.practice.Practice;
import rip.bridge.practice.setting.Setting;
import rip.bridge.practice.setting.SettingHandler;
import rip.bridge.qlib.command.Command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * /night commands, allows players to toggle {@link Setting#NIGHT_MODE} setting
 */
public final class NightCommand {

    @Command(names = { "night", "nightMode" }, permission = "")
    public static void night(Player sender) {
        if (!Setting.NIGHT_MODE.canUpdate(sender)) {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return;
        }

        SettingHandler settingHandler = Practice.getInstance().getSettingHandler();
        boolean enabled = !settingHandler.getSetting(sender, Setting.NIGHT_MODE);

        settingHandler.updateSetting(sender, Setting.NIGHT_MODE, enabled);

        if (enabled) {
            sender.sendMessage(ChatColor.GREEN + "Night mode on.");
        } else {
            sender.sendMessage(ChatColor.RED + "Night mode off.");
        }
    }

}