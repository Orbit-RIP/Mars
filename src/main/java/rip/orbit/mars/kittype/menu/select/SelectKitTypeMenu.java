package rip.orbit.mars.kittype.menu.select;

import com.google.common.base.Preconditions;

import rip.orbit.mars.Mars;
import rip.orbit.mars.kittype.KitType;
import rip.orbit.mars.party.Party;
import rip.orbit.mars.util.InventoryUtils;
import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import cc.fyre.proton.util.Callback;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public final class SelectKitTypeMenu extends Menu {

    private final boolean reset;
    private final Callback<KitType> callback;

    public SelectKitTypeMenu(Callback<KitType> callback, String title) {
        this(callback, true, title);
    }

    public SelectKitTypeMenu(Callback<KitType> callback, boolean reset, String title) {
        this.callback = Preconditions.checkNotNull(callback, "callback");
        this.reset = reset;
    }

    @Override
    public void onClose(Player player) {
        if (reset) {
            InventoryUtils.resetInventoryDelayed(player);
        }
    }

    @Override
    public String getTitle(Player player) {
        return (ChatColor.BLUE.toString() + ChatColor.BOLD + "editing");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        int index = 0;

        for (KitType kitType : KitType.getAllTypes()) {
            if (!player.isOp() && kitType.isHidden()) {
                continue;
            }

            buttons.put(index++, new KitTypeButton(kitType, callback));
        }

        Party party = Mars.getInstance().getPartyHandler().getParty(player);
        if (party != null) {
            buttons.put(8, new KitTypeButton(KitType.teamFight, callback));
        }

        return buttons;
    }

}