package rip.orbit.mars.command.starshop.menu;

import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import cc.fyre.proton.util.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.orbit.mars.Mars;
import rip.orbit.mars.command.starshop.command.StarsCommand;
import rip.orbit.mars.command.starshop.menu.type.StarShopItem;
import rip.orbit.nebula.util.CC;

import java.util.*;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 14/08/2021 / 3:17 PM
 * HCTeams / rip.orbit.mars.stars.menu
 */
public class StarShopMenu extends Menu {

	@Override
	public String getTitle(Player player) {
		return CC.translate("&6Star Shop");
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();

		buttons.put(13, new StarsButton());

		int i = 27;
		for (StarShopItem value : StarShopItem.values()) {
			buttons.put(i, new ItemButton(value));
			++i;
		}

		return buttons;
	}

	@Override
	public boolean isPlaceholder() {
		return true;
	}

	public static class StarsButton extends Button {

		@Override
		public String getName(Player player) {
			return CC.translate("&fYour Stars&7: &6" + Mars.getInstance().getStarsMap().get(player.getUniqueId()) + "✧");
		}

		@Override
		public List<String> getDescription(Player player) {
			return Collections.emptyList();
		}

		@Override
		public Material getMaterial(Player player) {
			return Material.NETHER_STAR;
		}
	}

	@AllArgsConstructor
	public static class ItemButton extends Button {

		private final StarShopItem item;

		@Override
		public ItemStack getButtonItem(Player player) {
			return ItemBuilder.of(item.getMaterial())
					.amount(item.getAmount())
					.name(CC.translate(item.getDisplayName()))
					.data((short) item.getData())
					.setLore(CC.translate(Arrays.asList(
							"&7┃ &fPrice&7: &6" + item.getPrice() + "✧",
							"&7┃ &fClick to purchase this item"
					)))
					.build();
		}

		@Override
		public String getName(Player player) {
			return null;
		}

		@Override
		public List<String> getDescription(Player player) {
			return null;
		}

		@Override
		public Material getMaterial(Player player) {
			return null;
		}

		@Override
		public void clicked(Player player, int slot, ClickType clickType) {
			int stars = Mars.getInstance().getStarsMap().get(player.getUniqueId());
			if (stars < item.getPrice()) {
				player.sendMessage(CC.translate("&cInsufficient Funds."));
				return;
			}
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), item.getCommand().replaceAll("%player%", player.getName()));

			player.sendMessage(CC.translate("&fYou have just purchased " + item.getDisplayName() + " &ffor &6" + item.getPrice() + "✧"));

			StarsCommand.setStars(Bukkit.getConsoleSender(), player.getUniqueId(), stars - item.getPrice());
		}
	}
}
