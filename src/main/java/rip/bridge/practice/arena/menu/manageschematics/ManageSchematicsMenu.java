package rip.bridge.practice.arena.menu.manageschematics;

import rip.bridge.practice.Practice;
import rip.bridge.practice.arena.ArenaHandler;
import rip.bridge.practice.arena.ArenaSchematic;
import rip.bridge.practice.commands.ManageCommand;
import rip.bridge.practice.util.menu.MenuBackButton;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public final class ManageSchematicsMenu extends Menu {

    public ManageSchematicsMenu() {
        super("Manage schematics");
        setAutoUpdate(true);
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        ArenaHandler arenaHandler = Practice.getInstance().getArenaHandler();
        Map<Integer, Button> buttons = new HashMap<>();
        int index = 0;

        buttons.put(index++, new MenuBackButton(p -> new ManageCommand.ManageMenu().openMenu(p)));

        for (ArenaSchematic schematic : arenaHandler.getSchematics()) {
            buttons.put(index++, new ManageSchematicButton(schematic));
        }

        return buttons;
    }

}