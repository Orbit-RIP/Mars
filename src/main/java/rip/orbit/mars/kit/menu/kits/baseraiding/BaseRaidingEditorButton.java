package rip.orbit.mars.kit.menu.kits.baseraiding;

import cc.fyre.proton.menu.Button;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import jdk.internal.dynalink.DefaultBootstrapper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.orbit.mars.Mars;
import rip.orbit.mars.kit.Kit;
import rip.orbit.mars.kit.KitHandler;
import rip.orbit.mars.kit.menu.editkit.EditKitMenu;
import rip.orbit.mars.kittype.KitType;
import rip.orbit.nebula.util.CC;

import java.util.List;
import java.util.Optional;

public final class BaseRaidingEditorButton extends Button {

    private final Optional<Kit> kitOpt;
    private final KitType kitType;
    private final boolean raider;

    public BaseRaidingEditorButton(Optional<Kit> kitOpt, KitType kitType, boolean raider) {
        this.kitOpt = Preconditions.checkNotNull(kitOpt, "kitOpt");
        this.kitType = Preconditions.checkNotNull(kitType, "kitType");
        this.raider = raider;
    }

    @Override
    public String getName(Player player) {
        return CC.translate(raider ? "&dRaider Kit" : "&bTrapper Kit");
    }

    @Override
    public List<String> getDescription(Player player) {
        return CC.translate(kitOpt.map(kit -> ImmutableList.of(
                "",
                "&7Click to edit the " + (raider ? "raider" : "trapper") + " kit",
                ""
        )).orElse(ImmutableList.of()));
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.INK_SACK;
    }

    @Override
    public byte getDamageValue(Player player) {
        return (byte) (raider ? 13 : 12);
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        Kit resolvedKit = kitOpt.orElseGet(() -> {
            KitHandler kitHandler = Mars.getInstance().getKitHandler();
            return kitHandler.saveDefaultKit(player, kitType, 1);
        });

        new EditKitMenu(resolvedKit).openMenu(player);
    }

}