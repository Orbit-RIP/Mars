package rip.orbit.mars.ability.items.viper;

import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import rip.orbit.mars.Mars;
import rip.orbit.mars.ability.Ability;
import rip.orbit.mars.util.cooldown.Cooldowns;
import rip.orbit.nebula.util.CC;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 31/07/2021 / 5:57 PM
 * HCTeams / rip.orbit.mars.ability.items
 */
public class PocketBard extends Ability {

	public Cooldowns cd = new Cooldowns();

	public PocketBard() {
		super("PocketBard");
	}

	@Override
	public Cooldowns cooldown() {
		return cd;
	}

	@Override
	public List<String> lore() {
		return CC.translate(Arrays.asList(
				"&7Right click to reveal a menu to choose",
				"&7a portable bard effect of your choice."
		));
	}

	@Override
	public List<String> foundInfo() {
		return CC.translate(Arrays.asList(
				"Ability Packages",
				"Partner Crates",
				"Star Shop (/starshop)"
		));
	}

	@Override
	public String displayName() {
		return CC.translate("&6&lPocketBard &7(Viper)");
	}

	@Override
	public String name() {
		return "pocketbard";
	}

	@Override
	public int data() {
		return 14;
	}

	@Override
	public Material mat() {
		return Material.INK_SACK;
	}

	@Override
	public boolean glow() {
		return false;
	}

	@EventHandler
	public void onInteractNinjaStar(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		if (isSimilar(event.getItem())) {
			if (!isClick(event, "RIGHT")) {
				event.setUseItemInHand(Event.Result.DENY);
				return;
			}
			if (!canUse(player)) {
				event.setUseItemInHand(Event.Result.DENY);
				return;
			}

			event.setCancelled(true);
			takeItem(player);

			new Menu() {

				@Override
				public String getTitle(Player player) {
					return "PocketBard";
				}

				@Override
				public Map<Integer, Button> getButtons(Player player) {
					Map<Integer, Button> buttons = new HashMap<>();

					int i = 0;
					for (Ability pb : Mars.getInstance().getAbilityHandler().getOrbitPocketBards()) {
						buttons.put(i, new Button() {
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
							public ItemStack getButtonItem(Player player) {
								ItemStack clone = pb.getStack().clone();
								clone.setAmount(5);
								return clone;
							}

							@Override
							public void clicked(Player player, int slot, ClickType clickType) {
								player.getInventory().addItem(getButtonItem(player));
								player.closeInventory();
							}
						});
						++i;
					}

					return buttons;
				}
			}.openMenu(player);

		}
	}

}
