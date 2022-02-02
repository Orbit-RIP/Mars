package rip.bridge.practice.kit.menu.kits;

import rip.bridge.practice.Practice;
import rip.bridge.practice.kit.Kit;
import rip.bridge.practice.kit.KitHandler;
import rip.bridge.practice.kittype.KitType;
import rip.bridge.practice.kittype.menu.select.SelectKitTypeMenu;
import rip.bridge.practice.util.InventoryUtils;
import rip.bridge.practice.util.menu.MenuBackButton;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.menu.Menu;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class KitsMenu extends Menu {

    private final KitType kitType;

    public KitsMenu(KitType kitType) {
        super("Viewing " + kitType.getDisplayName() + " kits");

        setPlaceholder(true);
        setAutoUpdate(true);

        this.kitType = kitType;
    }

    @Override
    public void onClose(Player player) {
        InventoryUtils.resetInventoryDelayed(player);
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        KitHandler kitHandler = Practice.getInstance().getKitHandler();
        Map<Integer, Button> buttons = new HashMap<>();

        // kit slots are 1-indexed
        for (int kitSlot = 1; kitSlot <= KitHandler.KITS_PER_TYPE; kitSlot++) {
            Optional<Kit> kitOpt = kitHandler.getKit(player, kitType, kitSlot);
            int column = (kitSlot * 2) - 1; // - 1 to compensate for this being 0-indexed

            buttons.put(getSlot(column, 0), new KitIconButton(kitOpt, kitType, kitSlot));
            buttons.put(getSlot(column, 2), new KitEditButton(kitOpt, kitType, kitSlot));

            if (kitOpt.isPresent()) {
                buttons.put(getSlot(column, 3), new KitRenameButton(kitOpt.get()));
                buttons.put(getSlot(column, 4), new KitDeleteButton(kitType, kitSlot));
            } else {
                buttons.put(getSlot(column, 3), Button.placeholder(Material.STAINED_GLASS_PANE, DyeColor.GRAY.getWoolData(), ""));
                buttons.put(getSlot(column, 4), Button.placeholder(Material.STAINED_GLASS_PANE, DyeColor.GRAY.getWoolData(), ""));
            }
        }

        buttons.put(getSlot(0, 4), new MenuBackButton(p -> {
            new SelectKitTypeMenu(kitType -> {
                new KitsMenu(kitType).openMenu(p);
            }, "Select a kit type...").openMenu(p);
        }));

        return buttons;
    }

}