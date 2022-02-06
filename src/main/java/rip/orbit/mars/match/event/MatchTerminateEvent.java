package rip.orbit.mars.match.event;

import rip.orbit.mars.match.Match;

import org.bukkit.event.HandlerList;

import lombok.Getter;
import rip.orbit.mars.match.MatchState;

/**
 * Called when a match is terminated (when its {@link MatchState} changes
 * to {@link MatchState#TERMINATED})
 * @see MatchState#TERMINATED
 */
public final class MatchTerminateEvent extends MatchEvent {

    @Getter private static HandlerList handlerList = new HandlerList();


    public MatchTerminateEvent(Match match) {
        super(match);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}