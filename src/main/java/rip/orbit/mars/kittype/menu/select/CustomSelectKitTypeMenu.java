package rip.orbit.mars.kittype.menu.select;

import com.google.common.base.Preconditions;

import org.bukkit.Material;
import rip.orbit.mars.kittype.KitType;
import rip.orbit.mars.util.InventoryUtils;
import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import cc.fyre.proton.util.Callback;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Similar to {@link SelectKitTypeMenu} but allows the user to set custom
 * descriptions/item counts for each KitType. For example, this is used by
 * the queue system to show the number of players in each queue prior to joining.
 */
public final class CustomSelectKitTypeMenu extends Menu {

    private final Callback<KitType> callback;
    private final Function<KitType, CustomKitTypeMeta> metaFunc;
    private final boolean ranked;

    public CustomSelectKitTypeMenu(Callback<KitType> callback, Function<KitType, CustomKitTypeMeta> metaFunc, String title, boolean ranked) {
        super(ChatColor.RED + title);

        setAutoUpdate(true);

        this.callback = Preconditions.checkNotNull(callback, "callback");
        this.metaFunc = Preconditions.checkNotNull(metaFunc, "metaFunc");
        this.ranked = ranked;
    }

    @Override
    public void onClose(Player player) {
        InventoryUtils.resetInventoryDelayed(player);
    }

    private int[] ORANGE_SLOTS = {9, 17, 36, 44};
    private int[] BLACK_SLOTS = {10, 16, 35, 37, 43, 18, 26, 27};
    private int[] KITS_NOT_FULL_SLOTS = {
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34
    };
    private int[] KITS_FULL_SLOTS = {
            11, 12, 13, 14, 15,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            38, 39, 40, 41, 42,
            };

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        int index = 0;

        for (int i = 0; i < 9; i++) {
            buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 1, " "));
        }

        for (int i = 44; i < 54; i++) {
            buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 1, " "));
        }

        for (int slot : ORANGE_SLOTS) {
            buttons.put(slot, Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 1, " "));
        }

        for (int slot : BLACK_SLOTS) {
            buttons.put(slot, Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 15, " "));
        }

        List<KitType> types = new ArrayList<>();

        for (KitType kitType : KitType.getAllTypes()) {
            if (!player.isOp() && kitType.isHidden()) {
                continue;
            }

            if (ranked && !kitType.isSupportsRanked()) {
                continue;
            }
            
            if (kitType.getId().equals(KitType.hcfdiamond.getId())) continue;
            if (kitType.getId().equals(KitType.hcfarcher.getId())) continue;
            if (kitType.getId().equals(KitType.hcfbard.getId())) continue;

            if (kitType.getId().contains("-BaseRaiding")) continue;

            if (kitType.getId().contains("-Trapper")) continue;

            types.add(kitType);

        }

        buttons.put(KITS_FULL_SLOTS[index++], new BaseRaidingModeSelectButton(callback, ranked, this));

        if (types.size() >= 14) {
            for (KitType type : types) {
                CustomKitTypeMeta meta = metaFunc.apply(type);
                buttons.put(KITS_FULL_SLOTS[index++], new KitTypeButton(type, callback, meta.getDescription(), meta.getQuantity()));
            }
        } else {
            for (KitType type : types) {
                CustomKitTypeMeta meta = metaFunc.apply(type);
                buttons.put(KITS_NOT_FULL_SLOTS[index++], new KitTypeButton(type, callback, meta.getDescription(), meta.getQuantity()));
            }
        }

        return buttons;
    }

    @AllArgsConstructor
    public static final class CustomKitTypeMeta {

        @Getter private int quantity;
        @Getter private List<String> description;

    }

}