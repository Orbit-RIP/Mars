package rip.orbit.mars.lobby.menu.statistics;

import com.google.common.collect.Lists;
import rip.orbit.mars.Mars;
import rip.orbit.mars.elo.EloHandler;
import rip.orbit.mars.kittype.KitType;
import cc.fyre.proton.menu.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerButton extends Button {

    private static EloHandler eloHandler = Mars.getInstance().getEloHandler();

    @Override
    public String getName(Player player) {
        return getColoredName(player) + ChatColor.WHITE + ChatColor.BOLD + " | " + ChatColor.WHITE + "Statistics";
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> description = Lists.newArrayList();

        description.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------");

        for (KitType kitType : KitType.getAllTypes()) {
            if (kitType.isSupportsRanked()) {
                description.add(ChatColor.DARK_PURPLE + kitType.getDisplayName() + ChatColor.GRAY + ": " + eloHandler.getElo(player, kitType));
            }
        }

        description.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------");
        description.add(ChatColor.DARK_PURPLE + "Global" + ChatColor.GRAY + ": " + eloHandler.getGlobalElo(player.getUniqueId()));
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

        return player.getName();
    }
}
