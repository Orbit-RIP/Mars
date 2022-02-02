package rip.bridge.practice.follow.command;

import rip.bridge.practice.Practice;
import rip.bridge.practice.match.commands.LeaveCommand;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class SilentFollowCommand {

    @Command(names = {"silentfollow", "sf"}, permission = "basic.staff")
    public static void silentfollow(Player sender, @Param(name = "target") Player target) {
        sender.setMetadata("ModMode", new FixedMetadataValue(Practice.getInstance(), true));
        sender.setMetadata("invisible", new FixedMetadataValue(Practice.getInstance(), true));

        if (Practice.getInstance().getPartyHandler().hasParty(sender)) {
            LeaveCommand.leave(sender);
        }

        FollowCommand.follow(sender, target);
    }

}
