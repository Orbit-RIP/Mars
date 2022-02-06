package rip.orbit.mars.arena.event;

import rip.orbit.mars.arena.Arena;

import org.bukkit.event.HandlerList;

import lombok.Getter;
import rip.orbit.mars.match.Match;

/**
 * Called when an {@link Arena} is allocated for use by a
 * {@link Match}
 */
public final class ArenaAllocatedEvent extends ArenaEvent {

    @Getter private static HandlerList handlerList = new HandlerList();

    public ArenaAllocatedEvent(Arena arena) {
        super(arena);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}