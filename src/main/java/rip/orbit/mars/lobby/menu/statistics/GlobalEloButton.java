package rip.orbit.mars.lobby.menu.statistics;

import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import rip.orbit.mars.Mars;
import rip.orbit.mars.elo.EloHandler;
import cc.fyre.proton.menu.Button;
import rip.orbit.nebula.util.CC;

public class GlobalEloButton extends Button {

    private static EloHandler eloHandler = Mars.getInstance().getEloHandler();

    @Override
    public String getName(Player player) {
        return ChatColor.GOLD + "Global" + ChatColor.GRAY.toString() + ChatColor.BOLD + " | " + ChatColor.WHITE + "Top 10";
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> description = Lists.newArrayList();

        description.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------");

        int counter = 1;

        for (Entry<String, Integer> entry : eloHandler.topElo(null).entrySet()) {
            String color = (ChatColor.GOLD).toString();
            description.add(color + counter + ") " + CC.WHITE + entry.getKey() + ": " + ChatColor.GOLD + entry.getValue());

            counter++;
        }

        description.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------");

        return description;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.NETHER_STAR;
    }
}
