package rip.orbit.mars.arena.event;

import rip.orbit.mars.arena.Arena;

import org.bukkit.event.HandlerList;

import lombok.Getter;
import rip.orbit.mars.match.Match;

/**
 * Called when an {@link Arena} is done being used by a
 * {@link Match}
 */
public final class ArenaReleasedEvent extends ArenaEvent {

    @Getter private static HandlerList handlerList = new HandlerList();

    public ArenaReleasedEvent(Arena arena) {
        super(arena);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}