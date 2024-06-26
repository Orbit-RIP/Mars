package rip.orbit.mars.arena.menu.select;

import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import rip.orbit.mars.Mars;
import rip.orbit.mars.arena.ArenaSchematic;
import rip.orbit.mars.kittype.KitType;
import rip.orbit.mars.kittype.menu.select.SendDuelButton;
import rip.orbit.mars.kittype.menu.select.ToggleAllButton;
import rip.orbit.mars.match.MatchHandler;
import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import cc.fyre.proton.util.Callback;

public class SelectArenaMenu extends Menu {
    
    private KitType kitType;
    private Callback<Set<String>> mapsCallback;
    Set<String> allMaps;
    Set<String> enabledSchematics = Sets.newHashSet();
    
    public SelectArenaMenu(KitType kitType, Callback<Set<String>> mapsCallback, String title) {
        super(ChatColor.BLUE.toString() + ChatColor.BOLD + title);

        this.kitType = kitType;
        this.mapsCallback = mapsCallback;
        
        for (ArenaSchematic schematic : Mars.getInstance().getArenaHandler().getSchematics()) {
            if (MatchHandler.canUseSchematic(this.kitType, schematic)) {
                enabledSchematics.add(schematic.getName());
            }
        }
        
        this.allMaps = ImmutableSet.copyOf(enabledSchematics);
    }

    @Override
    public Map<Integer, Button> getButtons(Player arg0) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        int i = 0;
        for (String mapName : allMaps) {
            buttons.put(i++, new ArenaButton(mapName, enabledSchematics));
        }
        
        int bottomRight = 8;
        while (buttons.get(bottomRight) != null) {
            bottomRight += 9;
        }
        
        bottomRight += 9;
        
        buttons.put(bottomRight, new ToggleAllButton(allMaps, enabledSchematics));
        buttons.put(bottomRight - 8, new SendDuelButton(enabledSchematics, mapsCallback));
        
        return buttons;
    }
    
}
