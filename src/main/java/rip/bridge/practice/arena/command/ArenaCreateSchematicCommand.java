package rip.bridge.practice.arena.command;

import rip.bridge.practice.Practice;
import rip.bridge.practice.arena.ArenaHandler;
import rip.bridge.practice.arena.ArenaSchematic;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;

public final class ArenaCreateSchematicCommand {

    @Command(names = { "arena createSchematic" }, permission = "op")
    public static void arenaCreateSchematic(Player sender, @Param(name="schematic") String schematicName) {
        ArenaHandler arenaHandler = Practice.getInstance().getArenaHandler();

        if (arenaHandler.getSchematic(schematicName) != null) {
            sender.sendMessage(ChatColor.RED + "Schematic " + schematicName + " already exists");
            return;
        }

        ArenaSchematic schematic = new ArenaSchematic(schematicName);
        File schemFile = schematic.getSchematicFile();

        if (!schemFile.exists()) {
            sender.sendMessage(ChatColor.RED + "No file for " + schematicName + " found. (" + schemFile.getPath() + ")");
            return;
        }

        arenaHandler.registerSchematic(schematic);

        try {
            schematic.pasteModelArena();
            arenaHandler.saveSchematics();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        sender.sendMessage(ChatColor.GREEN + "Schematic created.");
    }

}