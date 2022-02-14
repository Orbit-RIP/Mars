package rip.orbit.mars.arena.menu.manageschematics;

import com.google.common.base.Preconditions;

import rip.orbit.mars.Mars;
import rip.orbit.mars.arena.Arena;
import rip.orbit.mars.arena.ArenaHandler;
import rip.orbit.mars.arena.ArenaSchematic;
import rip.orbit.mars.arena.menu.manageschematic.ManageSchematicMenu;
import cc.fyre.proton.menu.Button;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

final class ManageSchematicButton extends Button {

    private final ArenaSchematic schematic;
    private final List<ArenaSchematic> schematics;

    ManageSchematicButton(ArenaSchematic schematic, List<ArenaSchematic> schematics) {
        this.schematic = Preconditions.checkNotNull(schematic, "schematic");
        this.schematics = schematics;
    }

    @Override
    public String getName(Player player) {
        return ChatColor.YELLOW + "Manage " + schematic.getName();
    }

    @Override
    public List<String> getDescription(Player player) {
        ArenaHandler arenaHandler = Mars.getInstance().getArenaHandler();
        int totalCopies = 0;
        int inUseCopies = 0;

        for (Arena arena : arenaHandler.getArenas(schematic)) {
            totalCopies++;

            if (arena.isInUse()) {
                inUseCopies++;
            }
        }

        List<String> description = new ArrayList<>();

        description.add("");
        description.add(ChatColor.GREEN + "Enabled: " + ChatColor.WHITE + (schematic.isEnabled() ? "Yes" : "No"));
        description.add(ChatColor.GREEN + "Copies: " + ChatColor.WHITE + totalCopies);
        description.add(ChatColor.GREEN + "Copies in use: " + ChatColor.WHITE + inUseCopies);

        return description;
    }

    @Override
    public int getAmount(Player player) {
        ArenaHandler arenaHandler = Mars.getInstance().getArenaHandler();
        return arenaHandler.getArenas(schematic).size();
    }

    @Override
    public Material getMaterial(Player player) {
        return schematic.isEnabled() ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        player.closeInventory();
        new ManageSchematicMenu(schematic, schematics).openMenu(player);
    }

}