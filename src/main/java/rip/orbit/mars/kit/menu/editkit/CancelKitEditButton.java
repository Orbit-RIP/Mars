package rip.orbit.mars.kit.menu.editkit;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import rip.orbit.mars.kit.menu.kits.KitsMenu;
import rip.orbit.mars.kit.menu.kits.baseraiding.BaseRaidingMenu;
import rip.orbit.mars.kittype.KitType;
import rip.orbit.mars.util.InventoryUtils;
import cc.fyre.proton.menu.Button;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

final class CancelKitEditButton extends Button {

    private final KitType kitType;

    CancelKitEditButton(KitType kitType) {
        this.kitType = Preconditions.checkNotNull(kitType, "kitType");
    }

    @Override
    public String getName(Player player) {
        return ChatColor.RED.toString() + ChatColor.BOLD + "Cancel";
    }

    @Override
    public List<String> getDescription(Player player) {
        return ImmutableList.of(
            "",
            ChatColor.YELLOW + "Click this to abort editing your kit,",
            ChatColor.YELLOW + "and return to the kit menu."
        );
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.WOOL;
    }

    @Override
    public byte getDamageValue(Player player) {
        return DyeColor.RED.getWoolData();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        player.closeInventory();
        InventoryUtils.resetInventoryDelayed(player);

        if (kitType.getId().contains("BaseRaiding") || kitType.getId().contains("-Trapper")) {
            new BaseRaidingMenu((kitType.getId().contains("-Trapper") ? BaseRaidingMenu.getRaiderEqual(kitType) : kitType)).openMenu(player);
        } else {
            new KitsMenu(kitType).openMenu(player);
        }
    }

}