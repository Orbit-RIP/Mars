package rip.bridge.practice.listener;

import rip.bridge.practice.Practice;
import rip.bridge.practice.setting.Setting;
import rip.bridge.practice.setting.SettingHandler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public final class ChatToggleListener implements Listener {

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        // players always see messages sent by ops
        if (event.getPlayer().isOp()) {
            return;
        }

        SettingHandler settingHandler = Practice.getInstance().getSettingHandler();
        event.getRecipients().removeIf(p -> !settingHandler.getSetting(p, Setting.ENABLE_GLOBAL_CHAT));
    }

}