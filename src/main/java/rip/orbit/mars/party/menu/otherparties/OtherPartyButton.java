package rip.orbit.mars.party.menu.otherparties;

import com.google.common.base.Preconditions;

import rip.orbit.mars.Mars;
import rip.orbit.mars.duel.command.DuelCommand;
import rip.orbit.mars.party.Party;
import cc.fyre.proton.menu.Button;
import cc.fyre.proton.util.UUIDUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

final class OtherPartyButton extends Button {

    private final Party party;

    OtherPartyButton(Party party) {
        this.party = Preconditions.checkNotNull(party, "party");
    }

    @Override
    public String getName(Player player) {
        return ChatColor.GOLD + UUIDUtils.name(party.getLeader());
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> description = new ArrayList<>();

        description.add("");

        for (UUID member : party.getMembers()) {
            ChatColor color = party.isLeader(member) ? ChatColor.GOLD : ChatColor.YELLOW;
            description.add(color + UUIDUtils.name(member));
        }

        description.add("");
        description.add(ChatColor.GREEN + "» Click to duel «");

        return description;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.SKULL_ITEM;
    }

    @Override
    public byte getDamageValue(Player player) {
        return (byte) 3; // player head
    }

    @Override
    public int getAmount(Player player) {
        return party.getMembers().size();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        Party senderParty = Mars.getInstance().getPartyHandler().getParty(player);

        if (senderParty == null) {
            return;
        }

        if (senderParty.isLeader(player.getUniqueId())) {
            DuelCommand.duel(player, Bukkit.getPlayer(party.getLeader()));
        } else {
            player.sendMessage(ChatColor.RED + "Only the leader can duel other parties!");
        }
    }

}