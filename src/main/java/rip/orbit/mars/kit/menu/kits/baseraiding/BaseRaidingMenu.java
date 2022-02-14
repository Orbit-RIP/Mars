package rip.orbit.mars.kit.menu.kits.baseraiding;

import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.orbit.mars.Mars;
import rip.orbit.mars.kit.Kit;
import rip.orbit.mars.kit.KitHandler;
import rip.orbit.mars.kittype.KitType;
import rip.orbit.mars.kittype.menu.select.SelectKitTypeMenu;
import rip.orbit.mars.util.InventoryUtils;
import rip.orbit.mars.util.menu.MenuBackButton;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class BaseRaidingMenu extends Menu {

    private final KitType kitType;

    public BaseRaidingMenu(KitType kitType) {
        setPlaceholder(true);
        setAutoUpdate(true);

        this.kitType = kitType;
    }

    @Override
    public void onClose(Player player) {
        InventoryUtils.resetInventoryDelayed(player);
    }

    @Override
    public String getTitle(Player player) {
        return ("Editing: " + kitType.getDisplayName());
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        KitHandler kitHandler = Mars.getInstance().getKitHandler();
        Map<Integer, Button> buttons = new HashMap<>();

        Optional<Kit> kitOpt = kitHandler.getKit(player, (getTrapperEqual(kitType) == null ? getRaiderEqual(kitType) : getTrapperEqual(kitType)), 1);

        buttons.put(12, new BaseRaidingEditorButton(kitOpt, (getTrapperEqual(kitType) == null ? getRaiderEqual(kitType) : getTrapperEqual(kitType)), false));

        kitOpt = kitHandler.getKit(player, (getTrapperEqual(kitType) == null ? getRaiderEqual(kitType) : getTrapperEqual(kitType)), 1);

        buttons.put(14, new BaseRaidingEditorButton(kitOpt, (getTrapperEqual(kitType) == null ? getRaiderEqual(kitType) : getTrapperEqual(kitType)), true));

        buttons.put(0, new MenuBackButton(p -> {
            new SelectKitTypeMenu(kitType -> {
                new BaseRaidingMenu(kitType).openMenu(p);
            }, "Select a kit type...").openMenu(p);
        }));

        return buttons;
    }

    public static KitType getTrapperEqual(KitType type) {
        switch (type.getId()) {
            case "Viper-BaseRaiding":
                return KitType.byId("Viper-Trapper");
            case "Orbit-BaseRaiding":
                return KitType.byId("Orbit-Trapper");
            case "Cave-BaseRaiding":
                return KitType.byId("Cave-Trapper");
            case "BaseRaiding":
                return KitType.byId("Normal-Trapper");
        }
        return null;
    }

    public static KitType getRaiderEqual(KitType type) {
        switch (type.getId()) {
            case "Viper-Trapper":
                return KitType.byId("Viper-BaseRaiding");
            case "Orbit-Trapper":
                return KitType.byId("Orbit-BaseRaiding");
            case "Cave-Trapper":
                return KitType.byId("Cave-BaseRaiding");
            case "Normal-Trapper":
                return KitType.byId("BaseRaiding");
        }
        return null;
    }

    @Override
    public int size(Player player) {
        return 27;
    }
}