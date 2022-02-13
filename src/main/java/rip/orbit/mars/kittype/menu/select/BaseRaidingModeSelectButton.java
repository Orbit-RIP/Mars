package rip.orbit.mars.kittype.menu.select;

import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import cc.fyre.proton.util.Callback;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.orbit.mars.Mars;
import rip.orbit.mars.kittype.KitType;
import rip.orbit.mars.match.MatchHandler;
import rip.orbit.mars.queue.QueueHandler;
import rip.orbit.nebula.util.CC;

import java.util.Arrays;
import java.util.List;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 12/02/2022 / 7:33 PM
 * Mars / rip.orbit.mars.kittype.menu.select
 */

@AllArgsConstructor
public class BaseRaidingModeSelectButton extends Button {

	private Callback<KitType> callback;
	private boolean ranked;
	private Menu menu;

	@Override
	public String getName(Player player) {
		return CC.translate("&6Base Raiding");
	}

	@Override
	public List<String> getDescription(Player player) {

		if (ranked) {
			QueueHandler queueHandler = Mars.getInstance().getQueueHandler();
			MatchHandler matchHandler = Mars.getInstance().getMatchHandler();

			KitType orbitBaseRaiding = KitType.byId(KitType.orbitBaseRaiding.getId());
			KitType caveBaseRaiding = KitType.byId(KitType.caveBaseRaiding.getId());
			KitType viperBaseRaiding = KitType.byId(KitType.viperBaseRaiding.getId());
			
			int inFightsRankedOrbit = matchHandler.countPlayersPlayingMatches(m -> m.getKitType() == orbitBaseRaiding && orbitBaseRaiding.isSupportsRanked());
			int inQueueRankedOrbit = queueHandler.countPlayersQueued(KitType.orbitBaseRaiding, true);

			int inFightsRankedCave = matchHandler.countPlayersPlayingMatches(m -> m.getKitType() == caveBaseRaiding && caveBaseRaiding.isSupportsRanked());
			int inQueueRankedCave = queueHandler.countPlayersQueued(KitType.caveBaseRaiding, true);

			int inFightsRankedViper = matchHandler.countPlayersPlayingMatches(m -> m.getKitType() == viperBaseRaiding && viperBaseRaiding.isSupportsRanked());
			int inQueueRankedViper = queueHandler.countPlayersQueued(KitType.viperBaseRaiding, true);

			return CC.translate(Arrays.asList(
					"&7&m----------------------------",
					"&6&lBase Raiding Ranked",
					"&7&m----------------------------",
					"&6&l┃ &fViper Queueing: &6" + inQueueRankedViper,
					"&6&l┃ &fViper Fighting: &6" + inFightsRankedViper,
					"&7&m----------------------------",
					"&6&l┃ &fCave Queueing: &6" + inQueueRankedCave,
					"&6&l┃ &fCave Fighting: &6" + inFightsRankedCave,
					"&7&m----------------------------",
					"&6&l┃ &fOrbit Queueing: &6" + inQueueRankedOrbit,
					"&6&l┃ &fOrbit Fighting: &6" + inFightsRankedOrbit,
					"&7&m----------------------------",
					"&7&oClick to view all base raiding modes.",
					"&7&m----------------------------"
			));
		} else {
			QueueHandler queueHandler = Mars.getInstance().getQueueHandler();
			MatchHandler matchHandler = Mars.getInstance().getMatchHandler();

			KitType orbitBaseRaiding = KitType.byId(KitType.orbitBaseRaiding.getId());
			KitType caveBaseRaiding = KitType.byId(KitType.caveBaseRaiding.getId());
			KitType viperBaseRaiding = KitType.byId(KitType.viperBaseRaiding.getId());

			int inFightsUnrankedOrbit = matchHandler.countPlayersPlayingMatches(m -> m.getKitType() == orbitBaseRaiding && !orbitBaseRaiding.isSupportsRanked());
			int inQueueUnrankedOrbit = queueHandler.countPlayersQueued(orbitBaseRaiding, false);

			int inFightsUnrankedCave = matchHandler.countPlayersPlayingMatches(m -> m.getKitType() == caveBaseRaiding && !caveBaseRaiding.isSupportsRanked());
			int inQueueUnrankedCave = queueHandler.countPlayersQueued(caveBaseRaiding, false);

			int inFightsUnrankedViper = matchHandler.countPlayersPlayingMatches(m -> m.getKitType() == viperBaseRaiding && !viperBaseRaiding.isSupportsRanked());
			int inQueueUnrankedViper = queueHandler.countPlayersQueued(viperBaseRaiding, false);

			return CC.translate(Arrays.asList(
					"&7&m----------------------------",
					"&6&lBase Raiding Unranked",
					"&7&m----------------------------",
					"&6&l┃ &fViper Queueing: &6" + inQueueUnrankedViper,
					"&6&l┃ &fViper Fighting: &6" + inFightsUnrankedViper,
					"&7&m----------------------------",
					"&6&l┃ &fCave Queueing: &6" + inQueueUnrankedCave,
					"&6&l┃ &fCave Fighting: &6" + inFightsUnrankedCave,
					"&7&m----------------------------",
					"&6&l┃ &fOrbit Queueing: &6" + inQueueUnrankedOrbit,
					"&6&l┃ &fOrbit Fighting: &6" + inFightsUnrankedOrbit,
					"&7&m----------------------------",
					"&7&oClick to view all base raiding modes.",
					"&7&m----------------------------"
			));
		}

	}

	@Override
	public Material getMaterial(Player player) {
		return Material.DIAMOND_PICKAXE;
	}

	@Override
	public void clicked(Player player, int slot, ClickType clickType) {
		new BaseRaidingModeSelectMenu(callback, menu, ranked).openMenu(player);
	}
}
