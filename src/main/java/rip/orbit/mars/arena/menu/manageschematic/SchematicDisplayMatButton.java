package rip.orbit.mars.arena.menu.manageschematic;

import cc.fyre.proton.menu.Button;
import com.google.common.base.Preconditions;
import lombok.SneakyThrows;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.orbit.mars.Mars;
import rip.orbit.mars.arena.Arena;
import rip.orbit.mars.arena.ArenaHandler;
import rip.orbit.mars.arena.ArenaSchematic;
import rip.orbit.nebula.util.CC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class SchematicDisplayMatButton extends Button {

    private final ArenaSchematic schematic;

    SchematicDisplayMatButton(ArenaSchematic schematic) {
        this.schematic = Preconditions.checkNotNull(schematic, "schematic");
    }

    @Override
    public String getName(Player player) {
        return CC.translate("&eChange Display Material");
    }

    @Override
    public List<String> getDescription(Player player) {
        return CC.translate(Arrays.asList(
                "&7&m--------------",
                "&aClick to set the display",
                "&amaterial as your held",
                "&aitem.",
                "&7&m--------------"
        ));
    }

    @Override
    public int getAmount(Player player) {
        return 1;
    }

    @Override
    public Material getMaterial(Player player) {
        return schematic.getDisplayMaterial();
    }

    @SneakyThrows
    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        schematic.setDisplayMaterial(player.getItemInHand().getType());
        Mars.getInstance().getArenaHandler().saveSchematics();
    }
}