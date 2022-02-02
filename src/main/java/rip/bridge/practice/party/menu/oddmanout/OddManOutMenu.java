package rip.bridge.practice.party.menu.oddmanout;

import com.google.common.base.Preconditions;

import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.menu.Menu;
import rip.bridge.qlib.util.Callback;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public final class OddManOutMenu extends Menu {

    private final Callback<Boolean> callback;

    public OddManOutMenu(Callback<Boolean> callback) {
        super("Continue with unbalanced teams?");

        this.callback = Preconditions.checkNotNull(callback, "callback");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(getSlot(2, 0), new OddManOutButton(true, callback));
        buttons.put(getSlot(6, 0), new OddManOutButton(false, callback));

        return buttons;
    }

}