package rip.bridge.practice.kittype.menu.select;

import rip.bridge.practice.Practice;
import rip.bridge.practice.party.Party;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.util.HashMap;
import java.util.Map;

import rip.bridge.practice.kittype.KitType;
import rip.bridge.practice.util.InventoryUtils;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.menu.Menu;
import rip.bridge.qlib.util.Callback;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class SelectKitTypeMenu extends Menu {

	private final boolean reset;
	private final Callback<KitType> callback;
	private Predicate<KitType> predicate;

	public SelectKitTypeMenu(Callback<KitType> callback, String title) {
		this(callback, true, title);
	}

	public SelectKitTypeMenu(Callback<KitType> callback, boolean reset, String title) {
		super(ChatColor.BLUE.toString() + ChatColor.BOLD + title);

		this.callback = Preconditions.checkNotNull(callback, "callback");
		this.reset = reset;
	}

	public SelectKitTypeMenu predicate(Predicate<KitType> predicate) {
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

			buttons.put(index++, new KitTypeButton(kitType, callback));
		}

		return buttons;
	}

}