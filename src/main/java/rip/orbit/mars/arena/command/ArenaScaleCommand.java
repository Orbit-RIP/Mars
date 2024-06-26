package rip.orbit.mars.arena.command;

import rip.orbit.mars.Mars;
import rip.orbit.mars.arena.Arena;
import rip.orbit.mars.arena.ArenaHandler;
import rip.orbit.mars.arena.ArenaSchematic;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.mars.arena.menu.select.SelectArenaMenu;
import rip.orbit.mars.kittype.KitType;
import rip.orbit.mars.match.MatchHandler;

import java.util.stream.Collectors;

public final class ArenaScaleCommand {

    @Command(names = { "arena scale" }, permission = "op")
    public static void arenaScale(Player sender, @Parameter(name="schematic") String schematicName, @Parameter(name="count") int count) {
        ArenaHandler arenaHandler = Mars.getInstance().getArenaHandler();
        ArenaSchematic schematic = arenaHandler.getSchematic(schematicName);

        if (schematic == null) {
            sender.sendMessage(ChatColor.RED + "Schematic " + schematicName + " not found.");
            sender.sendMessage(ChatColor.RED + "List all schematics with /arena listSchematics");
            return;
        }

        sender.sendMessage(ChatColor.GREEN + "Starting...");

        arenaHandler.getGrid().scaleCopies(schematic, count, () -> {
            sender.sendMessage(ChatColor.GREEN + "Scaled " + schematic.getName() + " to " + count + " copies.");
        });
    }

    @Command(names = "arena rescaleall", permission = "op")
    public static void arenaRescaleAll(Player sender) {
        Mars.getInstance().getArenaHandler().getSchematics().forEach(schematic -> {
            ArenaHandler arenaHandler = Mars.getInstance().getArenaHandler();
            int totalCopies = 0;
            int inUseCopies = 0;

            for (Arena arena : arenaHandler.getArenas(schematic)) {
                totalCopies++;
            }

            arenaScale(sender, schematic.getName(), 0);
            arenaScale(sender, schematic.getName(), totalCopies);
        });
    }

    @Command(names = "arena test", permission = "op")
    public static void test(Player sender, @Parameter(name = "type") KitType type) {
        ArenaHandler arenaHandler = Mars.getInstance().getArenaHandler();

        new SelectArenaMenu(type,
                (maps) -> {
                    arenaHandler.getSchematics().stream().filter(schematic -> !MatchHandler.canUseSchematic(type, schematic)).map(ArenaSchematic::getName).collect(Collectors.toList());
                },
                "Test"
        ).openMenu(sender);
    }

}