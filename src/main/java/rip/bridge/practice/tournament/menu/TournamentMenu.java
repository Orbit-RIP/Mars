package rip.bridge.practice.tournament.menu;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.practice.kittype.KitType;
import rip.bridge.practice.util.InventoryUtils;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.menu.Menu;
import rip.bridge.qlib.util.Callback;

import java.util.HashMap;
import java.util.Map;

//import rip.bridge.practice.tournament.menu.button.TournamentButton;

public class TournamentMenu extends Menu {
    private final boolean reset;
    private final Callback<KitType> callback;
    private Predicate<KitType> predicate;

    public TournamentMenu(Callback<KitType> callback, String title) {
        this(callback, true, title);
    }

    public TournamentMenu(Callback<KitType> callback, boolean reset, String title) {
        super(ChatColor.BLUE.toString() + ChatColor.BOLD + "Select a tournament kit");

        this.callback = Preconditions.checkNotNull(callback, "callback");
        this.reset = reset;
    }

    public TournamentMenu predicate(Predicate<KitType> predicate) {
        this.predicate = predicate;
        return this;
    }

    @Override
    public void onClose(Player player) {
        if (reset) {
            InventoryUtils.resetInventoryDelayed(player);
        }
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        int index = 0;

        for (KitType kitType : KitType.getAllTypes()) {
            if (!player.isOp() && kitType.isHidden()) {
                continue;
            }

            if (predicate != null) {
                if (!predicate.apply(kitType)) {
                    continue;
                }
            }

//            buttons.put(index++, new TournamentButton(kitType, callback));
        }

        return buttons;
    }

}