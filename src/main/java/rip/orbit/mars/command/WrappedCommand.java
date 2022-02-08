package rip.orbit.mars.command;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import cc.fyre.proton.util.TimeUtils;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import rip.orbit.mars.Mars;
import rip.orbit.mars.statistics.StatisticsHandler;
import rip.orbit.nebula.Nebula;
import rip.orbit.nebula.profile.Profile;
import rip.orbit.nebula.profile.attributes.wrapped.IWrapped;
import rip.orbit.nebula.profile.attributes.wrapped.WrappedType;
import rip.orbit.nebula.util.CC;

import javax.swing.text.PlainDocument;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/02/2022 / 9:51 PM
 * Mars / rip.orbit.mars.command
 */
public class WrappedCommand {

	@Command(names = {"sendpracticewrapped"}, permission = "op", async = true)
	public static void sendpracticewrapped(CommandSender sender, @Parameter(name = "name") String mapName) {


		List<UUID> toReturn = new ArrayList<>();

		for (Document document : Nebula.getInstance().getProfileHandler().getCollection().find()) {

			if (document == null) continue;

			UUID uuid = UUID.fromString(document.getString("uuid"));
			OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

			if (player.hasPlayedBefore()) {
				toReturn.add(uuid);
			}
		}

		int i = 0;
		for (UUID uuid : toReturn) {
			Profile profile = Nebula.getInstance().getProfileHandler().fromUuid(uuid, true);

			if (profile != null) {
				IWrapped wrapped = new IWrapped(mapName, WrappedType.PRACTICE);

				wrapped.setPlayTime(TimeUtils.formatIntoDetailedString((int) Mars.getInstance().getStatisticsHandler().getStat(uuid, StatisticsHandler.Statistic.PLAY_TIME, "GLOBAL")));
				wrapped.setKills((int) Mars.getInstance().getStatisticsHandler().getStat(uuid, StatisticsHandler.Statistic.WINS, "GLOBAL"));
				wrapped.setDeaths((int) Mars.getInstance().getStatisticsHandler().getStat(uuid, StatisticsHandler.Statistic.LOSSES, "GLOBAL"));
				wrapped.setHighestKillStreak((int) Mars.getInstance().getStatisticsHandler().getStat(uuid, StatisticsHandler.Statistic.HIGHEST_WINSTREAK, "GLOBAL"));
				wrapped.setUniqueLogins((int) Mars.getInstance().getStatisticsHandler().getStat(uuid, StatisticsHandler.Statistic.UNIQUE_LOGINS, "GLOBAL"));
				profile.getWraps().add(wrapped);

				profile.save();
			}

		}

		sender.sendMessage(CC.translate("&aSent out the wrap to " + i + " profiles."));


	}

}
