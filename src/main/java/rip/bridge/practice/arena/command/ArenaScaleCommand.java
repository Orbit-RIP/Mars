package rip.bridge.practice.arena.command;

import rip.bridge.practice.Practice;
import rip.bridge.practice.arena.Arena;
import rip.bridge.practice.arena.ArenaHandler;
import rip.bridge.practice.arena.ArenaSchematic;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class ArenaScaleCommand {

    @Command(names = { "arena scale" }, permission = "op")
    public static void arenaScale(Player sender, @Param(name="schematic") String schematicName, @Param(name="count") int count) {
        ArenaHandler arenaHandler = Practice.getInstance().getArenaHandler();
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
        Practice.getInstance().getArenaHandler().getSchematics().forEach(schematic -> {
            ArenaHandler arenaHandler = Practice.getInstance().getArenaHandler();
            int totalCopies = 0;
            int inUseCopies = 0;

            for (Arena arena : arenaHandler.getArenas(schematic)) {
                totalCopies++;
            }

            arenaScale(sender, schematic.getName(), 0);
            arenaScale(sender, schematic.getName(), totalCopies);
        });
    }

}