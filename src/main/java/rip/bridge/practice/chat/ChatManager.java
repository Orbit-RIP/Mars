package rip.bridge.practice.chat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scheduler.BukkitRunnable;
import rip.bridge.practice.Practice;
import rip.bridge.practice.chat.listener.ChatListener;
import rip.bridge.practice.party.Party;
import rip.bridge.practice.chat.modes.PartyChatMode;
import rip.bridge.qlib.chat.ChatHandler;
import rip.bridge.qlib.chat.ChatPlayer;
import rip.bridge.qlib.chat.ChatPopulator;
import rip.bridge.qlib.util.TaskUtil;

import java.util.ArrayList;
import java.util.List;

public class ChatManager implements Listener {

    public static List<ChatPopulator> modes;

    public ChatManager() {
        modes = new ArrayList<>();
        Practice.getInstance().getServer().getPluginManager().registerEvents(new ChatListener(), Practice.getInstance());
    }

    public static ChatPopulator findByName(String name) {
        for (ChatPopulator populator : modes) {
            if (populator.getName() == name) {
                return populator;
            }
        }
        return null;
    }

    public static void refresh(Player player) {
        refresh(player, false);
        TaskUtil.runLater(() -> refresh(player, false), 42L);

    }

    public static void refresh(Player player, boolean s) {
        ChatPlayer chatPlayer = ChatHandler.getChatPlayer(player.getUniqueId());
        Party team = Practice.getInstance().getPartyHandler().getParty(player.getUniqueId());
        chatPlayer.registerProvider(new ChatPopulator.PublicChatProvider());
//        if (team != null) {
//            if (team.getMembers().contains(player.getUniqueId())) {
//                chatPlayer.registerProvider(new PartyChatMode());
//            }
//        } else {
//            chatPlayer.setSelectedPopulator(new ChatPopulator.PublicChatProvider());
//        }
        ChatPopulator defaultPubFormat = new ChatPopulator.PublicChatProvider();
        if (chatPlayer.getRegisteredPopulators().contains(defaultPubFormat)) {
            chatPlayer.removeProvider(defaultPubFormat);
        }
    }

    public static void update(Player player, ChatPopulator populator) {
        Practice.getInstance().getChatModeMap().setPopulator(player.getUniqueId(), populator);
    }

}