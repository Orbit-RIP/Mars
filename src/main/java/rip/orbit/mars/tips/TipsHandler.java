package rip.orbit.mars.tips;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Bukkit;
import rip.orbit.mars.Mars;
import rip.orbit.nebula.util.CC;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 13/02/2022 / 11:53 AM
 * Mars / rip.orbit.mars.tips
 */

@Data
public class TipsHandler {

	private Tips lastSentTip;

	private Map<Tips, Integer> tipMap = new HashMap<>();

	public TipsHandler() {

		int i = 0;
		for (Tips tip : Tips.values()) {
			tipMap.put(tip, ++i);
		}

		Bukkit.getScheduler().runTaskTimerAsynchronously(Mars.getInstance(), () -> {

			Tips next = next();
			Bukkit.broadcastMessage(" ");
			Bukkit.broadcastMessage(CC.translate("&e&l[TIP] " + next.getTitle()));
			Bukkit.broadcastMessage(" ");
			for (String s : next.getMessage()) {
				Bukkit.broadcastMessage(CC.translate("&6&l┃ " + s));
			}
			Bukkit.broadcastMessage(" ");

		}, 20 * 85, 20 * 85);

	}

	private Tips next() {

		Tips newTip = Tips.EDIT_KIT;

		if (this.tipMap.entrySet().size() == 1) {
			return Tips.values()[0];
		}

		for (Map.Entry<Tips, Integer> entry : this.tipMap.entrySet()) {
			if (entry.getValue() < this.tipMap.get(this.lastSentTip)) continue;

			newTip = entry.getKey();
			break;
		}

		return newTip;
	}

}
