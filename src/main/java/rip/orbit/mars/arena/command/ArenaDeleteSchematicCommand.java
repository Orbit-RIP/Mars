package rip.orbit.mars.arena.command;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.mars.Mars;
import rip.orbit.mars.arena.ArenaHandler;
import rip.orbit.mars.arena.ArenaSchematic;

import java.io.File;

public final class ArenaDeleteSchematicCommand {

    @Command(names = { "arena deleteschem" }, permission = "op")
    public static void deleteschem(Player sender, @Parameter(name="schematic") String schematicName) {
        ArenaHandler arenaHandler = Mars.getInstance().getArenaHandler();

        if (arenaHandler.getSchematic(schematicName) == null) {
            sender.sendMessage(ChatColor.RED + "Schematic " + schematicName + " doesn't exist");
            return;
        }

        ArenaSchematic schematic = new ArenaSchematic(schematicName);

        arenaHandler.unregisterSchematic(schematic);

        try {
            schematic.removeModelArena();
            arenaHandler.saveSchematics();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        sender.sendMessage(ChatColor.GREEN + "Schematic deleted.");
    }

}