package rip.orbit.mars.match.event;

import rip.orbit.mars.match.Match;

import org.bukkit.event.HandlerList;

import lombok.Getter;
import rip.orbit.mars.match.MatchState;

/**
 * Called when a match is ended (when its {@link MatchState} changes
 * to {@link MatchState#ENDING})
 * @see MatchState#ENDING
 */
public final class MatchEndEvent extends MatchEvent {

    @Getter private static HandlerList handlerList = new HandlerList();

    public MatchEndEvent(Match match) {
        super(match);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}