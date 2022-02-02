package rip.bridge.practice.party.event;

import com.google.common.base.Preconditions;

import rip.bridge.practice.party.Party;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import lombok.Getter;

/**
 * Called when a player leaves a {@link Party}.
 * This event will not be called if a player 'leaves' their party by
 * having it be disbanded.
 */
public final class PartyMemberLeaveEvent extends PartyEvent {

    @Getter private static HandlerList handlerList = new HandlerList();

    @Getter private final Player member;

    public PartyMemberLeaveEvent(Player member, Party party) {
        super(party);

        this.member = Preconditions.checkNotNull(member, "member");
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}