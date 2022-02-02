package rip.bridge.practice.duel;

import rip.bridge.practice.kittype.KitType;
import rip.bridge.practice.party.Party;

public final class PartyDuelInvite extends DuelInvite<Party> {

    public PartyDuelInvite(Party sender, Party target, KitType kitTypes) {
        super(sender, target, kitTypes);
    }

}