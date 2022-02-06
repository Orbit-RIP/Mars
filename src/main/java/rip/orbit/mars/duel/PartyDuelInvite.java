package rip.orbit.mars.duel;

import rip.orbit.mars.kittype.KitType;
import rip.orbit.mars.party.Party;

public final class PartyDuelInvite extends DuelInvite<Party> {

    public PartyDuelInvite(Party sender, Party target, KitType kitTypes) {
        super(sender, target, kitTypes);
    }

}