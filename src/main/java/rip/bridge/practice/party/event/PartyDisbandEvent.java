package rip.bridge.practice.party.event;

import rip.bridge.practice.party.Party;

import org.bukkit.event.HandlerList;

import lombok.Getter;

/**
 * Called when a {@link Party} is disbanded.
 * @see rip.bridge.practice.party.commands.PartyDisbandCommand
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