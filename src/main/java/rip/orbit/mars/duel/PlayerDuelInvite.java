package rip.orbit.mars.duel;

import java.util.UUID;

import lombok.Getter;
import org.bukkit.entity.Player;

import rip.orbit.mars.arena.ArenaSchematic;
import rip.orbit.mars.kittype.KitType;

@Getter
public final class PlayerDuelInvite extends DuelInvite<UUID> {

    private ArenaSchematic schematic;

    public PlayerDuelInvite(Player sender, Player target, KitType kitType) {
        super(sender.getUniqueId(), target.getUniqueId(), kitType);
    }

    public PlayerDuelInvite(Player sender, Player target, KitType kitType, ArenaSchematic schematic) {
        super(sender.getUniqueId(), target.getUniqueId(), kitType);
        this.schematic = schematic;
    }

}