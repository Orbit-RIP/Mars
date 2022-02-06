package rip.orbit.mars.command;

import rip.orbit.mars.Mars;
import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SkullCommand {

    @Command(names = {"skull"}, permission = "op")
    public void skull(Player sender) {
        Mars.getInstance().getServer().dispatchCommand(sender,"give " + sender.getName() + " minecraft:skull 1 3 {SkullOwner:\"WhyLego\"}");
        sender.sendMessage(ChatColor.YELLOW + "You have been given WhyLego's Skull.");
        sender.sendMessage(ChatColor.YELLOW + "/give " + sender.getName() + " minecraft:skull 1 3 {SkullOwner:\"NAME\"");
    }
}
