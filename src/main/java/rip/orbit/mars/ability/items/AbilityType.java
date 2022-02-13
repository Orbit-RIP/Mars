package rip.orbit.mars.ability.items;

import cc.fyre.proton.util.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 12/02/2022 / 6:27 PM
 * Mars / rip.orbit.mars.ability.items
 */

@AllArgsConstructor
@Getter
public enum AbilityType {

	VIPER("&6&lViper", "vipermc.net", ItemBuilder.of(Material.INK_SACK).data((short) 14).build()),
	CAVE("&4&lCave", "cavepvp.org", ItemBuilder.of(Material.INK_SACK).data((short) 1).build()),
	ORBIT("&6&lOrbit", "orbit.rip", ItemBuilder.of(Material.INK_SACK).data((short) 14).build());

	private String displayName;
	private String ip;
	private ItemStack displayStack;

}
