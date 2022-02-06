package rip.orbit.mars.match.event;

import rip.orbit.mars.match.Match;

import org.bukkit.event.HandlerList;

import lombok.Getter;
import rip.orbit.mars.match.MatchState;

/**
 * Called when a match's countdown ends (when its {@link MatchState} changes
 * to {@link MatchState#IN_PROGRESS})
 * @see MatchState#IN_PROGRESS
 */
public final class MatchStartEvent extends MatchEvent {

    @Getter private static HandlerList handlerList = new HandlerList();

    public MatchStartEvent(Match match) {
        super(match);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}