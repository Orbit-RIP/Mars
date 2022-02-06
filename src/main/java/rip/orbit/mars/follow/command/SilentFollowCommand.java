package rip.orbit.mars.follow.command;

import com.qrakn.morpheus.game.Game;
import com.qrakn.morpheus.game.GameQueue;
import rip.orbit.mars.Mars;
import rip.orbit.mars.match.command.LeaveCommand;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class SilentFollowCommand {

    @Command(names = "silentfollow", permission = "potpvp.silent")
    public static void silentfollow(Player sender, @Parameter(name = "target") Player target) {
        sender.setMetadata("ModMode", new FixedMetadataValue(Mars.getInstance(), true));
        sender.setMetadata("invisible", new FixedMetadataValue(Mars.getInstance(), true));

        if (Mars.getInstance().getPartyHandler().hasParty(sender)) {
            LeaveCommand.leave(sender);
        }

        Game game = GameQueue.INSTANCE.getCurrentGame(target);
        if (game != null && game.getPlayers().contains(target)) {
            sender.sendMessage(ChatColor.RED + target.getName() + " is playing an event!");
            return;
        }

        FollowCommand.follow(sender, target);
    }

}
