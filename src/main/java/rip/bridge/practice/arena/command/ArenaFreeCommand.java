package rip.bridge.practice.arena.command;

import rip.bridge.practice.Practice;
import rip.bridge.qlib.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class ArenaFreeCommand {

    @Command(names = { "arena free" }, permission = "op")
    public static void arenaFree(Player sender) {
        Practice.getInstance().getArenaHandler().getGrid().free();
        sender.sendMessage(ChatColor.GREEN + "Arena grid has been freed.");
    }

}