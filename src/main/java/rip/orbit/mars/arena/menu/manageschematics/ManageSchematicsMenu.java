package rip.orbit.mars.arena.menu.manageschematics;

import cc.fyre.proton.menu.buttons.BackButton;
import lombok.AllArgsConstructor;
import rip.orbit.mars.Mars;
import rip.orbit.mars.arena.ArenaHandler;
import rip.orbit.mars.arena.ArenaSchematic;
import rip.orbit.mars.command.ManageCommand;
import rip.orbit.mars.util.menu.MenuBackButton;
import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public final class ManageSchematicsMenu extends Menu {

    private List<ArenaSchematic> schematics;

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        ArenaHandler arenaHandler = Mars.getInstance().getArenaHandler();
        Map<Integer, Button> buttons = new HashMap<>();
        int index = 0;

        buttons.put(index++, new BackButton(new ManageSchematicSelectionMenu()));

        for (ArenaSchematic schematic : schematics) {
            buttons.put(index++, new ManageSchematicButton(schematic, schematics));
        }

        return buttons;
    }

    @Override
    public String getTitle(Player player) {
        return "Manage Schematics";
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }
}