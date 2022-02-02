package rip.bridge.practice.match.event;

import rip.bridge.practice.match.Match;

import org.bukkit.event.HandlerList;

import lombok.Getter;

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