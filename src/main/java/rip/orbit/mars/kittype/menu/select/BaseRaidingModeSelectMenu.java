package rip.orbit.mars.kittype.menu.select;

import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import cc.fyre.proton.menu.buttons.BackButton;
import cc.fyre.proton.util.Callback;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import rip.orbit.mars.Mars;
import rip.orbit.mars.ability.items.AbilityType;
import rip.orbit.mars.kittype.KitType;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 12/02/2022 / 7:33 PM
 * Mars / rip.orbit.mars.kittype.menu.select
 */

@AllArgsConstructor
public class BaseRaidingModeSelectMenu extends Menu {

	private Callback<KitType> callback;
	private Menu back;
	private boolean ranked;

	@Override
	public String getTitle(Player player) {
		return "Base Raiding Modes...";
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();

		for (KitType type : KitType.getAllTypes()) {
			if (type.getId().equals(KitType.viperBaseRaiding.getId())) {
				buttons.put(10, new BaseRaidingModeButton(type, AbilityType.VIPER, callback, ranked));
			} else if (type.getId().equals(KitType.caveBaseRaiding.getId())) {
				buttons.put(13, new BaseRaidingModeButton(type, AbilityType.CAVE, callback, ranked));
			} else if (type.getId().equals(KitType.orbitBaseRaiding.getId())) {
				buttons.put(16, new BaseRaidingModeButton(type, AbilityType.ORBIT, callback, ranked));
			}

		}

		buttons.put(31, new BackButton(back));

		return buttons;
	}

	@Override
	public boolean isPlaceholder() {
		return true;
	}
}
