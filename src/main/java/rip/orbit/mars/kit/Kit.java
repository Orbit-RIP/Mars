package rip.orbit.mars.kit;

import org.bukkit.GameMode;
import rip.orbit.mars.Mars;
import rip.orbit.mars.kittype.KitType;
import rip.orbit.mars.util.ItemUtils;
import rip.orbit.mars.util.PatchedPlayerUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.Getter;
import lombok.Setter;

public final class Kit {

    @Getter @Setter private String name;
    @Getter @Setter private int slot; // starts at 1, not 0
    @Getter @Setter private String type;
    @Getter @Setter private ItemStack[] inventoryContents;

    public static Kit ofDefaultKitCustomName(KitType kitType, String name) {
        return ofDefaultKit(kitType, name, 0);
    }

    public static Kit ofDefaultKit(KitType kitType) {
        return ofDefaultKit(kitType, "Default Kit", 0);
    }

    public static Kit ofDefaultKit(KitType kitType, String name, int slot) {
        Kit kit = new Kit();

        kit.setName(name);
        kit.setType(kitType.getId());
        kit.setSlot(slot);
        kit.setInventoryContents(kitType.getDefaultInventory());

        return kit;
    }

    public void apply(Player player) {
        PatchedPlayerUtils.resetInventory(player, GameMode.SURVIVAL, false, true);

        // we don't let players actually customize their armor, we just apply default
        player.getInventory().setArmorContents(getKitType().getDefaultArmor());
        player.getInventory().setContents(inventoryContents);

        Bukkit.getScheduler().runTaskLater(Mars.getInstance(), player::updateInventory, 1L);
    }

    public int countHeals() {
        return ItemUtils.countStacksMatching(inventoryContents, ItemUtils.INSTANT_HEAL_POTION_PREDICATE);
    }

    public int countDebuffs() {
        return ItemUtils.countStacksMatching(inventoryContents, ItemUtils.DEBUFF_POTION_PREDICATE);
    }

    public int countFood() {
        return ItemUtils.countStacksMatching(inventoryContents, ItemUtils.EDIBLE_PREDICATE);
    }

    public int countPearls() {
        return ItemUtils.countStacksMatching(inventoryContents, v -> v.getType() == Material.ENDER_PEARL);
    }

    // we use this method instead of .toSelectableBook().isSimilar()
    // to avoid the slight performance overhead of constructing
    // that itemstack every time
    public boolean isSelectionItem(ItemStack itemStack) {
        if (itemStack.getType() != Material.ENCHANTED_BOOK) {
            return false;
        }

        ItemMeta meta = itemStack.getItemMeta();
        return meta.hasDisplayName() && meta.getDisplayName().equals(ChatColor.YELLOW.toString() + ChatColor.BOLD + name);
    }

    public ItemStack createSelectionItem() {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + name);

        item.setItemMeta(itemMeta);
        return item;
    }

    public KitType getKitType() {
        return KitType.byId(type);
    }

    public KitType getType() {
        return KitType.byId(type);
    }

}