package rip.orbit.mars.arena.menu.manageschematics;

import cc.fyre.proton.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import rip.orbit.mars.arena.ArenaSchematic;
import rip.orbit.mars.kittype.KitType;
import rip.orbit.nebula.util.CC;

import java.util.List;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 13/02/2022 / 3:43 PM
 * Mars / rip.orbit.mars.arena.menu.manageschematic
 */

@AllArgsConstructor
public class CategoryButton extends Button {

	private String filterName;
	private MaterialData filterData;
	private List<ArenaSchematic> schematics;

	@Override
	public String getName(Player player) {
		return CC.translate(filterName);
	}

	@Override
	public List<String> getDescription(Player player) {
		return null;
	}

	@Override
	public Material getMaterial(Player player) {
		return filterData.getItemType();
	}

	@Override
	public byte getDamageValue(Player player) {
		return filterData.getData();
	}

	@Override
	public void clicked(Player player, int slot, ClickType clickType) {
		new ManageSchematicsMenu(schematics).openMenu(player);
	}
}
