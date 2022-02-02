package rip.bridge.practice.setting.commands;

import rip.bridge.practice.Practice;
import rip.bridge.practice.setting.Setting;
import rip.bridge.practice.setting.SettingHandler;
import rip.bridge.qlib.command.Command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * /toggleglobalchat commands, allows players to toggle {@link Setting#ENABLE_GLOBAL_CHAT} setting
 */
public final class ToggleGlobalChatCommand {

    @Command(names = {"toggleGlobalChat", "tgc", "togglechat"}, permission = "")
    public static void toggleGlobalChat(Player sender) {
        if (!Setting.ENABLE_GLOBAL_CHAT.canUpdate(sender)) {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return;
        }

        SettingHandler settingHandler = Practice.getInstance().getSettingHandler();
        boolean enabled = !settingHandler.getSetting(sender, Setting.ENABLE_GLOBAL_CHAT);

        settingHandler.updateSetting(sender, Setting.ENABLE_GLOBAL_CHAT, enabled);

        if (enabled) {
            sender.sendMessage(ChatColor.GREEN + "Toggled global chat on.");
        } else {
            sender.sendMessage(ChatColor.RED + "Toggled global chat off.");
        }
    }

}