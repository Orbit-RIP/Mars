package rip.orbit.mars.morpheus.command;

import rip.orbit.mars.morpheus.menu.HostMenu;
import cc.fyre.proton.command.Command;
import org.bukkit.entity.Player;

public class HostCommand {

    @Command(names = { "host"}, permission = "vexor.host")
    public static void host(Player sender) {
        new HostMenu().openMenu(sender);
    }

}
