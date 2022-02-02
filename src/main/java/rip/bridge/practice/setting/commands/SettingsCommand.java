package rip.bridge.practice.setting.commands;

import rip.bridge.practice.setting.menu.SettingsMenu;
import rip.bridge.qlib.command.Command;

import org.bukkit.entity.Player;

/**
 * /settings, accessible by all users, opens a {@link SettingsMenu}
 */
public final class SettingsCommand {

    @Command(names = {"settings", "preferences", "prefs", "options"}, permission = "")
    public static void settings(Player sender) {
        new SettingsMenu().openMenu(sender);
    }

}