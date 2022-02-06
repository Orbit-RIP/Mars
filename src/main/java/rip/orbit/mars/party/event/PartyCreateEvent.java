package rip.orbit.mars.party.event;

import rip.orbit.mars.party.Party;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import rip.orbit.mars.party.PartyHandler;
import rip.orbit.mars.party.command.PartyCreateCommand;

/**
 * Called when a {@link Party} is created.
 * @see PartyCreateCommand
 * @see PartyHandler#getOrCreateParty(Player)
 */
public final class PartyCreateEvent extends PartyEvent {

    @Getter private static HandlerList handlerList = new HandlerList();

    public PartyCreateEvent(Party party) {
        super(party);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}