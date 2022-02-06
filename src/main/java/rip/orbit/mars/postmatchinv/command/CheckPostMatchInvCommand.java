package rip.orbit.mars.postmatchinv.command;

import rip.orbit.mars.Mars;
import rip.orbit.mars.postmatchinv.PostMatchInvHandler;
import rip.orbit.mars.postmatchinv.PostMatchPlayer;
import rip.orbit.mars.postmatchinv.menu.PostMatchMenu;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import cc.fyre.proton.util.UUIDUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public final class CheckPostMatchInvCommand {

    @Command(names = { "checkPostMatchInv", "_" }, permission = "")
    public static void checkPostMatchInv(Player sender, @Parameter(name = "target") UUID target) {
        PostMatchInvHandler postMatchInvHandler = Mars.getInstance().getPostMatchInvHandler();
        Map<UUID, PostMatchPlayer> players = postMatchInvHandler.getPostMatchData(sender.getUniqueId());

        if (players.containsKey(target)) {
            new PostMatchMenu(players.get(target)).openMenu(sender);
        } else {
            sender.sendMessage(ChatColor.RED + "Data for " + UUIDUtils.name(target) + " not found.");
        }
    }

}