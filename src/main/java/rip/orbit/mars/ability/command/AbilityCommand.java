package rip.orbit.mars.ability.command;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import org.bukkit.Material;
import rip.orbit.mars.Mars;
import rip.orbit.mars.ability.Ability;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import rip.orbit.nebula.util.CC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 01/07/2021 / 11:35 AM
 * HCTeams / rip.orbit.mars.ability.command
 */
public class AbilityCommand {

	@Command(names = "ability give", permission = "foxtrot.ability")
	public static void give(CommandSender sender, @Parameter(name = "player") Player target, @Parameter(name = "ability") Ability ability, @Parameter(name = "amount") int amount) {
		ItemStack item = ability.getStack();
		item.setAmount(amount);

		target.getInventory().addItem(item);
	}

	@Command(names = "ability list", permission = "foxtrot.ability")
	public static void list(Player sender) {
		List<String> names = new ArrayList<>();
		Mars.getInstance().getAbilityHandler().getOrbitAbilities().forEach(ability -> names.add(ability.name()));
		sender.sendMessage(CC.translate("&6&lAbility List&f: " + StringUtils.join(names, ", ")));
	}

	@Command(names = {"ability preview"}, permission = "")
	public static void showcase(Player sender, @Parameter(name = "type", defaultValue = "orbit") String type) {

		Menu menu = new Menu() {

			@Override
			public boolean isPlaceholder() {
				return true;
			}

			@Override
			public int size(Player player) {
				return 27;
			}

			@Override
			public String getTitle(Player player) {
				return "Ability Items";
			}

			@Override
			public Map<Integer, Button> getButtons(Player player) {
				Map<Integer, Button> buttons = new HashMap<>();
				int i = 0;

				List<Ability> abilities = Mars.getInstance().getAbilityHandler().getOrbitAbilities();

				if (type.toLowerCase().equals("orbit")) {
					abilities = Mars.getInstance().getAbilityHandler().getOrbitAbilities();
				} else if (type.toLowerCase().equals("viper")) {
					abilities = Mars.getInstance().getAbilityHandler().getViperAbilities();
				} else if (type.toLowerCase().equals("cave")) {
					abilities = Mars.getInstance().getAbilityHandler().getCaveAbilities();
				} else {
					abilities = Mars.getInstance().getAbilityHandler().getOrbitAbilities();
				}

				for (Ability currentAbility : abilities) {
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
							ItemStack stack = currentAbility.getStack().clone();
							ItemMeta meta = stack.getItemMeta();

							List<String> toLore = meta.getLore();
//							toLore.add(CC.translate("&7&lFound In:"));
//							for (String s : currentAbility.foundInfo()) {
//								toLore.add(CC.translate("&6&l┃ &7" + s));
//							}
//							meta.setLore(toLore);
							stack.setItemMeta(meta);
							stack.setAmount(1);

							return stack;
						}

						@Override
						public void clicked(Player player, int slot, ClickType type) {
							if (player.isOp()) {
								player.getInventory().addItem(currentAbility.getStack());
							}
						}
					});
					++i;
				}

				return buttons;
			}
		};
		menu.openMenu(sender);

	}

}
