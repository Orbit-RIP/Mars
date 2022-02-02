package rip.bridge.practice.setting.commands;

import rip.bridge.practice.Practice;
import rip.bridge.practice.setting.Setting;
import rip.bridge.practice.setting.SettingHandler;
import rip.bridge.qlib.command.Command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * /toggleduels commands, allows players to toggle {@link Setting#RECEIVE_DUELS} setting
 */
public final class ToggleDuelCommand {

    @Command(names = { "toggleduels", "td", "tduels" }, permission = "")
    public static void toggleDuel(Player sender) {
        if (!Setting.RECEIVE_DUELS.canUpdate(sender)) {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return;
        }

        SettingHandler settingHandler = Practice.getInstance().getSettingHandler();
        boolean enabled = !settingHandler.getSetting(sender, Setting.RECEIVE_DUELS);

        settingHandler.updateSetting(sender, Setting.RECEIVE_DUELS, enabled);

        if (enabled) {
            sender.sendMessage(ChatColor.GREEN + "Toggled duel requests on.");
        } else {
            sender.sendMessage(ChatColor.RED + "Toggled duel requests off.");
        }
    }

}