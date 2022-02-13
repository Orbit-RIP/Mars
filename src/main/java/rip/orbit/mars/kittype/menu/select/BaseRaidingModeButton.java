package rip.orbit.mars.kittype.menu.select;

import cc.fyre.proton.menu.Button;
import cc.fyre.proton.util.Callback;
import cc.fyre.proton.util.ItemBuilder;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.orbit.mars.Mars;
import rip.orbit.mars.ability.items.AbilityType;
import rip.orbit.mars.kittype.KitType;
import rip.orbit.mars.match.MatchHandler;
import rip.orbit.mars.queue.QueueHandler;
import rip.orbit.nebula.util.CC;

import java.util.List;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 12/02/2022 / 7:33 PM
 * Mars / rip.orbit.mars.kittype.menu.select
 */

@AllArgsConstructor
public class BaseRaidingModeButton extends Button {

	private KitType kitType;
	private AbilityType abilityType;
	private Callback<KitType> callback;
	private boolean ranked;

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
		ItemBuilder builder = ItemBuilder.of(kitType.getIcon().getItemType());

		builder.data(kitType.getIcon().getData());
		builder.name(CC.translate(kitType.getColoredDisplayName()));

		QueueHandler queueHandler = Mars.getInstance().getQueueHandler();
		MatchHandler matchHandler = Mars.getInstance().getMatchHandler();

		int inFights = 0;
		int inQueue = 0;

		if (ranked) {
			inFights = matchHandler.countPlayersPlayingMatches(m -> m.getKitType() == kitType && kitType.isSupportsRanked());
			inQueue = queueHandler.countPlayersQueued(kitType, true);

		} else {
			inFights = matchHandler.countPlayersPlayingMatches(m -> m.getKitType() == kitType && !kitType.isSupportsRanked());
			inQueue = queueHandler.countPlayersQueued(kitType, false);

		}

		builder.addToLore(
				" ",
				kitType.getDisplayColor() + "&l┃ &fFighting" + ChatColor.GRAY + ": " + kitType.getDisplayColor() + inFights,
				kitType.getDisplayColor() + "&l┃ &fQueued" + ChatColor.GRAY + ": " + kitType.getDisplayColor() + inQueue,
				" ",
				org.bukkit.ChatColor.GRAY + "Click here to play " + kitType.getDisplayColor() + kitType.getDisplayName() + org.bukkit.ChatColor.GRAY + "."
		);

		return builder.build();
	}

	@Override
	public void clicked(Player player, int slot, ClickType clickType) {
		callback.callback(kitType);
	}
}
