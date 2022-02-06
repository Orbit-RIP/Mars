package rip.orbit.mars.party.event;

import rip.orbit.mars.party.Party;

import org.bukkit.event.HandlerList;

import lombok.Getter;
import rip.orbit.mars.party.command.PartyDisbandCommand;

/**
 * Called when a {@link Party} is disbanded.
 * @see PartyDisbandCommand
 * @see Party#disband()
 */
public final class PartyDisbandEvent extends PartyEvent {

    @Getter private static HandlerList handlerList = new HandlerList();

    public PartyDisbandEvent(Party party) {
        super(party);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}