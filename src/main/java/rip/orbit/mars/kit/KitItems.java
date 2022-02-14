package rip.orbit.mars.kit;

import cc.fyre.proton.util.ItemUtils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lombok.experimental.UtilityClass;

import static org.bukkit.ChatColor.*;
import static rip.orbit.mars.MarsLang.LEFT_ARROW;
import static rip.orbit.mars.MarsLang.RIGHT_ARROW;

@UtilityClass
public final class KitItems {

    public static final ItemStack OPEN_EDITOR_ITEM = new ItemStack(Material.BOOK);

    static {
        ItemUtils.setDisplayName(OPEN_EDITOR_ITEM, LEFT_ARROW + GOLD + "Edit Kits" + RIGHT_ARROW);
    }

}