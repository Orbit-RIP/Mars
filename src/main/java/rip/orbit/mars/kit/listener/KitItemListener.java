package rip.orbit.mars.kit.listener;

import rip.orbit.mars.Mars;
import rip.orbit.mars.kit.KitItems;
import rip.orbit.mars.kit.menu.kits.KitsMenu;
import rip.orbit.mars.kittype.menu.select.SelectKitTypeMenu;
import rip.orbit.mars.lobby.LobbyHandler;
import rip.orbit.mars.util.ItemListener;

public final class KitItemListener extends ItemListener {

    public KitItemListener() {
        addHandler(KitItems.OPEN_EDITOR_ITEM, player -> {
            LobbyHandler lobbyHandler = Mars.getInstance().getLobbyHandler();

            if (lobbyHandler.isInLobby(player)) {
                new SelectKitTypeMenu(kitType -> {
                    new KitsMenu(kitType).openMenu(player);
                }, "Select a kit to edit...").openMenu(player);
            }
        });
    }

}