package rip.orbit.mars.scoreboard;

import cc.fyre.proton.Proton;
import cc.fyre.proton.scoreboard.construct.TitleGetter;
import lombok.Getter;
import org.bukkit.Bukkit;
import rip.orbit.mars.Mars;

import java.util.Arrays;
import java.util.List;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 13/02/2022 / 4:38 PM
 * Mars / rip.orbit.mars.util
 */

public class AnimationHandler {

	@Getter private String title, footer;

	private int currentTitle = 0;
	private int currentFooter = 0;

	public AnimationHandler() {
		List<String> titles = Arrays.asList(
				"&6&lPRACTICE",
				"&f&lP&6&lRACTICE",
				"&f&lPR&6&lACTICE",
				"&f&lPRA&6&lCTICE",
				"&f&lPRAC&6&lTICE",
				"&f&lPRACT&6&lICE",
				"&f&lPRACTI&6&lCE",
				"&f&lPRACTIC&6&lE",
				"&f&lPRACTICE&6&l",
				"&f&lPRACTIC&e&lE",
				"&f&lPRACTI&e&lCE",
				"&f&lPRACT&e&lICE",
				"&f&lPRAC&e&lTICE",
				"&f&lPRA&e&lCTICE",
				"&f&lPR&e&lACTICE",
				"&f&lP&e&lRACTICE",
				"&e&lPRACTICE",
				"&e&lPRACTICE",
				"&7&lPRACTICE",
				"&7&lPRACTICE",
				"&6&lPRACTICE",
				"&6&lPRACTICE",
				"&6&lPRACTICE"
		);

		Bukkit.getScheduler().runTaskTimerAsynchronously(Mars.getInstance(), () -> {
			if (currentTitle == titles.size()) currentTitle = 0;

			String newTitle = titles.get(currentTitle++);

			Proton.getInstance().getScoreboardHandler().getConfiguration().setTitleGetter(new TitleGetter(newTitle));
			this.title = newTitle;
		}, 40L, (long) (0.3 * 20L));

		List<String> footers = Arrays.asList(
				"&7&odonate.orbit.rip",
				"&7&oorbit.rip",
				"&7&oforums.orbit.rip"
		);

		Bukkit.getScheduler().runTaskTimerAsynchronously(Mars.getInstance(), () -> {
			if (currentFooter == footers.size()) currentFooter = 0;

			footer = footers.get(currentFooter++);
		}, 0L, 5 * 20L);

	}

}
