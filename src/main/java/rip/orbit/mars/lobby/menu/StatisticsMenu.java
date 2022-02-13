package rip.orbit.mars.lobby.menu;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import rip.orbit.mars.kittype.KitType;
import rip.orbit.mars.lobby.menu.statistics.GlobalEloButton;
import rip.orbit.mars.lobby.menu.statistics.KitButton;
import rip.orbit.mars.lobby.menu.statistics.PlayerButton;
import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import cc.fyre.proton.util.ItemBuilder;

public final class StatisticsMenu extends Menu {

    public StatisticsMenu() {
        setAutoUpdate(true);
    }

    @Override
    public String getTitle(Player player) {
        return "Statistics";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(getSlot(3, 1), new PlayerButton());
        buttons.put(getSlot(5, 1), new GlobalEloButton());

        int y = 3;
        int x = 1;

        for (KitType kitType : KitType.getAllTypes()) {
            if (!kitType.isSupportsRanked()) continue;

            buttons.put(getSlot(x++, y), new KitButton(kitType));

            if (x == 8) {
                y++;
                x = 1;
            }
        }

        return buttons;
    }

    @Override
    public boolean isPlaceholder() {
        return true;
    }

    @Override
    public int size(Player player) {
        return 9 * 6;
    }

}