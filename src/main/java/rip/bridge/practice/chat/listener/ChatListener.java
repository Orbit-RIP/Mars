package rip.bridge.practice.chat.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import rip.bridge.practice.Practice;
import rip.bridge.practice.chat.ChatManager;
import rip.bridge.qlib.chat.ChatHandler;
import rip.bridge.qlib.chat.ChatPlayer;
import rip.bridge.qlib.util.TaskUtil;

public class ChatListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        TaskUtil.run(() -> {
            ChatManager.refresh(player);
        } );

    }

}
