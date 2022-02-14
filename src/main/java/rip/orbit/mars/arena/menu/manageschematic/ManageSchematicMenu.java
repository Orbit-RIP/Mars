package rip.orbit.mars.arena.menu.manageschematic;

import cc.fyre.proton.menu.buttons.BackButton;
import rip.orbit.mars.Mars;
import rip.orbit.mars.arena.Arena;
import rip.orbit.mars.arena.ArenaSchematic;
import rip.orbit.mars.arena.menu.manageschematics.ManageSchematicsMenu;
import rip.orbit.mars.util.menu.MenuBackButton;
import rip.orbit.mars.util.menu.BooleanTraitButton;
import rip.orbit.mars.util.menu.IntegerTraitButton;
import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class ManageSchematicMenu extends Menu {

    private final ArenaSchematic schematic;
    private final List<ArenaSchematic> schematics;

    public ManageSchematicMenu(ArenaSchematic schematic, List<ArenaSchematic> schematics) {
        setAutoUpdate(true);

        this.schematic = schematic;
        this.schematics = schematics;
    }

    @Override
    public String getTitle(Player player) {
        return ("Manage " + schematic.getName());
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new SchematicStatusButton(schematic));
        buttons.put(1, new ToggleEnabledButton(schematic));

        buttons.put(3, new TeleportToModelButton(schematic));
        buttons.put(4, new SaveModelButton(schematic));
        buttons.put(5, new SchematicDisplayMatButton(schematic));

        if (Mars.getInstance().getArenaHandler().getGrid().isBusy()) {
            Button busyButton = Button.placeholder(Material.WOOL, DyeColor.SILVER.getWoolData(), ChatColor.GRAY.toString() + ChatColor.BOLD + "Grid is busy");

            buttons.put(7, busyButton);
            buttons.put(8, busyButton);
        } else {
            buttons.put(7, new CreateCopiesButton(schematic));
            buttons.put(8, new RemoveCopiesButton(schematic));
        }

        buttons.put(9, new BackButton(new ManageSchematicsMenu(schematics)));

        Consumer<ArenaSchematic> save = schematic -> {
            try {
                Mars.getInstance().getArenaHandler().saveSchematics();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };

        int i = 27;
        buttons.put(18, new IntegerTraitButton<>(schematic, "Max Player Count", ArenaSchematic::setMaxPlayerCount, ArenaSchematic::getMaxPlayerCount, save));
        buttons.put(19, new IntegerTraitButton<>(schematic, "Min Player Count", ArenaSchematic::setMinPlayerCount, ArenaSchematic::getMinPlayerCount, save));
        buttons.put(20, new BooleanTraitButton<>(schematic, "Supports Ranked", ArenaSchematic::setSupportsRanked, ArenaSchematic::isSupportsRanked, save));
        buttons.put(21, new BooleanTraitButton<>(schematic, "Archer Only", ArenaSchematic::setArcherOnly, ArenaSchematic::isArcherOnly, save));
        buttons.put(22, new BooleanTraitButton<>(schematic, "Sumo Only", ArenaSchematic::setSumoOnly, ArenaSchematic::isSumoOnly, save));
        buttons.put(23, new BooleanTraitButton<>(schematic, "Spleef Only", ArenaSchematic::setSpleefOnly, ArenaSchematic::isSpleefOnly, save));
        buttons.put(24, new BooleanTraitButton<>(schematic, "BuildUHC Only", ArenaSchematic::setBuildUHCOnly, ArenaSchematic::isBuildUHCOnly, save));
        buttons.put(25, new BooleanTraitButton<>(schematic, "HCF Only", ArenaSchematic::setHCFOnly, ArenaSchematic::isHCFOnly, save));
        buttons.put(26, new BooleanTraitButton<>(schematic, "Team Fights Only", ArenaSchematic::setTeamFightsOnly, ArenaSchematic::isTeamFightsOnly, save));
        buttons.put(i++, new BooleanTraitButton<>(schematic, "Base Raiding Only", ArenaSchematic::setBaseRaidingOnly, ArenaSchematic::isBaseRaidingOnly, save));
        buttons.put(i++, new BooleanTraitButton<>(schematic, "Pearl Fight Only", ArenaSchematic::setPearlFightOnly, ArenaSchematic::isPearlFightOnly, save));
        buttons.put(i++, new BooleanTraitButton<>(schematic, "Bridges Only", ArenaSchematic::setBridgesOnly, ArenaSchematic::isBridgesOnly, save));
        buttons.put(i, new Button() {
            @Override
            public String getName(Player player) {
                return ChatColor.GREEN + "Qrakn Game Events";
            }

            @Override
            public List<String> getDescription(Player player) {
                return Collections.singletonList(ChatColor.GRAY + "Manage which events can utilize this arena.");
            }

            @Override
            public Material getMaterial(Player player) {
                return Material.EMERALD;
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType) {
                new ManageEventsMenu(schematic).openMenu(player);
            }
        });

        return buttons;
    }

}