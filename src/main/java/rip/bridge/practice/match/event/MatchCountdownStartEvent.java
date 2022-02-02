package rip.bridge.practice.match.event;

import rip.bridge.practice.match.Match;

import org.bukkit.event.HandlerList;

import lombok.Getter;
import rip.bridge.practice.match.MatchState;

/**
 * Called when a match's countdown starts (when its {@link MatchState} changes
 * to {@link MatchState#COUNTDOWN})
 * @see MatchState#COUNTDOWN
 */
public final class MatchCountdownStartEvent extends MatchEvent {

    @Getter private static HandlerList handlerList = new HandlerList();

    public MatchCountdownStartEvent(Match match) {
        super(match);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}