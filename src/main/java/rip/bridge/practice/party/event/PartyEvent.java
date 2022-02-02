package rip.bridge.practice.party.event;

import com.google.common.base.Preconditions;

import rip.bridge.practice.party.Party;

import org.bukkit.event.Event;

import lombok.Getter;

/**
 * Represents an event involving a {@link Party}
 */
abstract class PartyEvent extends Event {

    /**
     * The party involved in this event
     */
    @Getter private final Party party;

    PartyEvent(Party party) {
        this.party = Preconditions.checkNotNull(party, "party");
    }

}