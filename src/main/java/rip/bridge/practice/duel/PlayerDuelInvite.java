package rip.bridge.practice.duel;

import java.util.UUID;

import org.bukkit.entity.Player;

import rip.bridge.practice.kittype.KitType;

public final class PlayerDuelInvite extends DuelInvite<UUID> {

    public PlayerDuelInvite(Player sender, Player target, KitType kitType) {
        super(sender.getUniqueId(), target.getUniqueId(), kitType);
    }

}