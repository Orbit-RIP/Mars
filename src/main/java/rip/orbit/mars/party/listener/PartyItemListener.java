package rip.orbit.mars.party.listener;

import rip.orbit.mars.Mars;
import rip.orbit.mars.party.Party;
import rip.orbit.mars.party.PartyHandler;
import rip.orbit.mars.party.PartyItems;
import rip.orbit.mars.party.command.PartyFfaCommand;
import rip.orbit.mars.party.command.PartyInfoCommand;
import rip.orbit.mars.party.command.PartyLeaveCommand;
import rip.orbit.mars.party.command.PartyTeamSplitCommand;
import rip.orbit.mars.party.menu.RosterMenu;
import rip.orbit.mars.party.menu.otherparties.OtherPartiesMenu;
import rip.orbit.mars.util.ItemListener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public final class PartyItemListener extends ItemListener {

    public PartyItemListener(PartyHandler partyHandler) {
        addHandler(PartyItems.LEAVE_PARTY_ITEM, PartyLeaveCommand::partyLeave);
        addHandler(PartyItems.START_TEAM_SPLIT_ITEM, PartyTeamSplitCommand::partyTeamSplit);
        addHandler(PartyItems.START_FFA_ITEM, PartyFfaCommand::partyFfa);
        addHandler(PartyItems.OTHER_PARTIES_ITEM, p -> new OtherPartiesMenu().openMenu(p));
        addHandler(PartyItems.ASSIGN_CLASSES, p -> new RosterMenu(partyHandler.getParty(p)).openMenu(p));
    }

    // this item changes based on who your party leader is,
    // so we have to manually implement this one.
    @EventHandler
    public void fastPartyIcon(PlayerInteractEvent event) {
        if (event.getPlayer().hasMetadata("Build")) return;

        if (!event.hasItem() || !event.getAction().name().contains("RIGHT_")) {
            return;
        }

        if (event.getItem().getType() != PartyItems.ICON_TYPE) {
            return;
        }

        boolean permitted = canUseButton.getOrDefault(event.getPlayer().getUniqueId(), 0L) < System.currentTimeMillis();

        if (permitted) {
            Player player = event.getPlayer();
            Party party = Mars.getInstance().getPartyHandler().getParty(player);

            if (party != null && PartyItems.icon(party).isSimilar(event.getItem())) {
                event.setCancelled(true);
                PartyInfoCommand.partyInfo(player, player);
            }

            canUseButton.put(player.getUniqueId(), System.currentTimeMillis() + 500);
        }
    }

}