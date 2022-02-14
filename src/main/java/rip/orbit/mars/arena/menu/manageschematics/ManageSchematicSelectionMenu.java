package rip.orbit.mars.arena.menu.manageschematics;

import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import cc.fyre.proton.menu.buttons.BackButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import rip.orbit.mars.Mars;
import rip.orbit.mars.arena.ArenaHandler;
import rip.orbit.mars.arena.ArenaSchematic;
import rip.orbit.mars.command.ManageCommand;
import rip.orbit.mars.kittype.KitType;
import rip.orbit.mars.match.MatchHandler;
import rip.orbit.mars.util.menu.MenuBackButton;
import rip.orbit.nebula.util.CC;

import java.util.*;
import java.util.stream.Collectors;

public final class ManageSchematicSelectionMenu extends Menu {

    public ManageSchematicSelectionMenu() {
        super("Manage schematics");

        setAutoUpdate(true);
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        ArenaHandler arenaHandler = Mars.getInstance().getArenaHandler();

        int i = 0;
        List<ArenaSchematic> schematics = arenaHandler.getSchematics().stream().filter(schematic -> schematic.isSumoOnly())
                .collect(Collectors.toList());

        buttons.put(i++, new CategoryButton(CC.translate("&6Sumo Schematics"), new MaterialData(Material.LEASH), schematics));

        schematics = arenaHandler.getSchematics().stream().filter(schematic -> MatchHandler.canUseSchematic(KitType.byId("BaseRaiding"), schematic))
                .collect(Collectors.toList());

        buttons.put(i++, new CategoryButton(CC.translate("&6Base Raiding Schematics"), new MaterialData(Material.DIAMOND_PICKAXE), schematics));

        schematics = arenaHandler.getSchematics().stream().filter(schematic -> MatchHandler.canUseSchematic(KitType.byId("BuildUHC"), schematic))
                .collect(Collectors.toList());

        buttons.put(i++, new CategoryButton(CC.translate("&6BuildUHC Schematics"), new MaterialData(Material.GOLDEN_APPLE), schematics));

        schematics = arenaHandler.getSchematics().stream().filter(schematic -> MatchHandler.canUseSchematic(KitType.byId("HCF"), schematic))
                .collect(Collectors.toList());

        buttons.put(i++, new CategoryButton(CC.translate("&6HCF Schematics"), new MaterialData(Material.DIAMOND_SWORD), schematics));

        schematics = arenaHandler.getSchematics().stream().filter(schematic -> MatchHandler.canUseSchematic(KitType.byId("PearlFight"), schematic))
                .collect(Collectors.toList());

        buttons.put(i++, new CategoryButton(CC.translate("&6Pearl Fight Schematics"), new MaterialData(Material.ENDER_PEARL), schematics));

        schematics = arenaHandler.getSchematics().stream().filter(schematic -> MatchHandler.canUseSchematic(KitType.byId("Bridges"), schematic))
                .collect(Collectors.toList());

        buttons.put(i++, new CategoryButton(CC.translate("&6Bridges Schematics"), new MaterialData(Material.BOW), schematics));

        schematics = arenaHandler.getSchematics().stream().filter(schematic ->
            !schematic.isBaseRaidingOnly() ||
            !schematic.isTeamFightsOnly() ||
            !schematic.isBridgesOnly() ||
            !schematic.isArcherOnly() ||
            !schematic.isPearlFightOnly() ||
            !schematic.isSpleefOnly() ||
            !schematic.isBuildUHCOnly() ||
            !schematic.isSumoOnly() ||
            !schematic.isHCFOnly()
        ).collect(Collectors.toList());

        buttons.put(i + 2, new CategoryButton(CC.translate("&6ALL Normal Schematics"), new MaterialData(Material.ANVIL), schematics));

        schematics = arenaHandler.getSchematics().stream().filter(schematic ->
                !schematic.isBaseRaidingOnly() ||
                        !schematic.isTeamFightsOnly() ||
                        !schematic.isBridgesOnly() ||
                        !schematic.isArcherOnly() ||
                        !schematic.isPearlFightOnly() ||
                        !schematic.isSpleefOnly() ||
                        !schematic.isBuildUHCOnly() ||
                        !schematic.isSumoOnly() ||
                        !schematic.isHCFOnly() ||
                        schematic.isSupportsRanked()
        ).collect(Collectors.toList());

        buttons.put(i + 3, new CategoryButton(CC.translate("&6ALL Ranked Schematics"), new MaterialData(Material.DIAMOND), schematics));

        schematics = new ArrayList<>(arenaHandler.getSchematics());

        buttons.put(i + 4, new CategoryButton(CC.translate("&6ALL Schematics"), new MaterialData(Material.GRASS), schematics));

        buttons.put(i + 5, new BackButton(new ManageCommand.ManageMenu()));

        return buttons;
    }

}