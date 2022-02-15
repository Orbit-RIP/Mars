package rip.orbit.mars.kit.menu.editkit;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import rip.orbit.mars.Mars;
import rip.orbit.mars.kit.Kit;
import rip.orbit.mars.kit.menu.kits.KitsMenu;
import rip.orbit.mars.kit.menu.kits.baseraiding.BaseRaidingMenu;
import rip.orbit.mars.util.InventoryUtils;
import rip.orbit.mars.util.ItemUtils;
import cc.fyre.proton.menu.Button;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

final class SaveButton extends Button {

    private final Kit kit;

    SaveButton(Kit kit) {
        this.kit = Preconditions.checkNotNull(kit, "kit");
    }

    @Override
    public String getName(Player player) {
        return ChatColor.GREEN.toString() + ChatColor.BOLD + "Save";
    }

    @Override
    public List<String> getDescription(Player player) {
        return ImmutableList.of(
            "",
            ChatColor.YELLOW + "Click this to save your kit."
        );
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.WOOL;
    }

    @Override
    public byte getDamageValue(Player player) {
        return DyeColor.LIME.getWoolData();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        kit.setInventoryContents(player.getInventory().getContents());
        Mars.getInstance().getKitHandler().saveKitsAsync(player);

        player.setItemOnCursor(new ItemStack(Material.AIR));

        player.closeInventory();
        InventoryUtils.resetInventoryDelayed(player);

        if (kit.getType().getId().contains("BaseRaiding") || kit.getType().getId().contains("-Trapper")) {
            new BaseRaidingMenu((kit.getType().getId().contains("-Trapper") ? BaseRaidingMenu.getRaiderEqual(kit.getType()) : kit.getType())).openMenu(player);
        } else {
            new KitsMenu(kit.getType()).openMenu(player);
        }

        ItemStack[] defaultInventory = kit.getType().getDefaultInventory();
        int foodInDefault = ItemUtils.countStacksMatching(defaultInventory, v -> v.getType().isEdible());
        int pearlsInDefault = ItemUtils.countStacksMatching(defaultInventory, v -> v.getType() == Material.ENDER_PEARL);

        if (foodInDefault > 0 && kit.countFood() == 0) {
            player.sendMessage(ChatColor.RED + "Your saved kit is missing food.");
        }

        if (pearlsInDefault > 0 && kit.countPearls() == 0) {
            player.sendMessage(ChatColor.RED + "Your saved kit is missing enderpearls.");
        }
    }

}