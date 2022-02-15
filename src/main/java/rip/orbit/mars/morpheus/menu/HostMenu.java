package rip.orbit.mars.morpheus.menu;

import com.qrakn.morpheus.game.event.GameEvent;
import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class HostMenu extends Menu {

	@Override
	public String getTitle(Player player) {
		return (ChatColor.YELLOW + "Host an event");
	}

	private int[] SLOTS = {
			11,12,13,14,15
	};

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> toReturn = new HashMap<>();

		for (int i = 0; i < 27; i++) {
			toReturn.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte) (i % 2 <= 0 ? 5 : 15), " "));
		}

		int i = 0;
		for (GameEvent event : GameEvent.getEvents()) {
			toReturn.put(SLOTS[i++], new HostEventButton(event));
		}

		if (i < 4) {
			toReturn.put(i, Button.fromItem(new ItemStack(Material.WOOL, 1, (short) 14)));
		}

		return toReturn;
	}

}
