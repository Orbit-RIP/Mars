package rip.orbit.mars.kit.menu.editkit;

import com.google.common.base.Preconditions;

import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import rip.orbit.mars.Mars;
import cc.fyre.proton.menu.Button;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.orbit.mars.ability.Ability;
import rip.orbit.nebula.util.CC;

import java.util.List;

final class TakeItemButton extends Button {

    private final ItemStack item;

    TakeItemButton(ItemStack item) {
        this.item = Preconditions.checkNotNull(item, "item");
    }

    // We just override this whole method, as we need to keep enchants/potion data/etc
    @Override
    public ItemStack getButtonItem(Player player) {
        return item;
    }

    // We pass through the item given to us with some lore so all these
    // are unused. The fact we have to do this does represent a gap in
    // the menu api's functionality, but we can save that for another day.
    @Override public String getName(Player player) { return null; }
    @Override public List<String> getDescription(Player player) { return null; }
    @Override public Material getMaterial(Player player) { return null; }

    @Override
    public void clicked(final Player player, final int slot, ClickType clickType) {
        // make the item show up again

        Bukkit.getScheduler().runTaskLater(Mars.getInstance(), () -> {
            player.getOpenInventory().getTopInventory().setItem(slot, item);
        }, 4L);
    }

    @Override
    public boolean shouldCancel(Player player, int slot, ClickType clickType) {
        for (ItemStack stack : player.getInventory().getContents()) {
            if (stack == null || stack.getType() == Material.AIR)
                continue;
            ItemStack item = this.getButtonItem(player);
            if (stack.getAmount() >= 5 && isSimilar(stack, item)) {
                player.sendMessage(CC.translate("&cThe max stack size is 5 for ability items."));
                return true;
            }
        }
        return false;
    }
    public boolean isSimilar(ItemStack item, ItemStack stack) {
        if (item == null) return false;
        if (item.getItemMeta() == null) return false;
        if (!item.getItemMeta().hasDisplayName()) return false;
        if (stack == null) return false;
        if (stack.getItemMeta() == null) return false;
        if (!stack.getItemMeta().hasDisplayName()) return false;
        return ChatColor.stripColor(CC.translate(item.getItemMeta().getDisplayName())).equalsIgnoreCase(ChatColor.stripColor(CC.translate(stack.getItemMeta().getDisplayName())));

    }

}