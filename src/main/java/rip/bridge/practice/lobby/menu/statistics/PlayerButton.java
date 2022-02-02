package rip.bridge.practice.lobby.menu.statistics;

import com.google.common.collect.Lists;
import rip.bridge.bridge.BridgeGlobal;
import rip.bridge.bridge.global.profile.Profile;
import rip.bridge.practice.Practice;
import rip.bridge.practice.elo.EloHandler;
import rip.bridge.practice.kittype.KitType;
import rip.bridge.qlib.menu.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public class PlayerButton extends Button {

    private static EloHandler eloHandler = Practice.getInstance().getEloHandler();

    @Override
    public String getName(Player player) {
        return getColoredName(player) + ChatColor.WHITE + ChatColor.BOLD + " \u2503 "  + ChatColor.WHITE + "Statistics";
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> description = Lists.newArrayList();

        description.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------");

        for (KitType kitType : KitType.getAllTypes()) {
            if (kitType.isSupportsRanked()) {
                description.add(ChatColor.GOLD + kitType.getDisplayName() + ChatColor.WHITE + ": " + eloHandler.getElo(player, kitType));
            }
        }

        description.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------");
        description.add(ChatColor.GOLD + "Global" + ChatColor.WHITE + ": " + eloHandler.getGlobalElo(player.getUniqueId()));
        description.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------");

        return description;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.SKULL_ITEM;
    }

    @Override
    public byte getDamageValue(Player player) {
        return (byte) 3;
    }

    private String getColoredName(Player player) {
        Optional<Profile> profileOptional = Optional.ofNullable(BridgeGlobal.getProfileHandler().getProfileByUUID(player.getUniqueId()));
        if (profileOptional.isPresent()) {
            return profileOptional.get().getCurrentGrant().getRank().getColor() + player.getDisplayName();
        }

        return player.getDisplayName();
    }
}