package rip.bridge.practice.kit.listener;

import rip.bridge.practice.Practice;
import rip.bridge.practice.kit.KitItems;
import rip.bridge.practice.kit.menu.kits.KitsMenu;
import rip.bridge.practice.kittype.menu.select.SelectKitTypeMenu;
import rip.bridge.practice.lobby.LobbyHandler;
import rip.bridge.practice.util.ItemListener;

public final class KitItemListener extends ItemListener {

    public KitItemListener() {
        addHandler(KitItems.OPEN_EDITOR_ITEM, player -> {
            LobbyHandler LobbyHandler = Practice.getInstance().getLobbyHandler();

            if (LobbyHandler.isInLobby(player)) {
                new SelectKitTypeMenu(kitType -> {
                    new KitsMenu(kitType).openMenu(player);
                }, "Select a kit to edit...").openMenu(player);
            }
        });
    }

}