package rip.orbit.mars.command;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import cc.fyre.proton.util.TimeUtils;
import cc.fyre.proton.util.UUIDUtils;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.orbit.mars.Mars;
import rip.orbit.mars.ability.Ability;
import rip.orbit.mars.persist.maps.PlaytimeMap;
import rip.orbit.mars.statistics.StatisticsHandler;
import rip.orbit.nebula.Nebula;
import rip.orbit.nebula.profile.Profile;
import rip.orbit.nebula.profile.attributes.wrapped.IWrapped;
import rip.orbit.nebula.profile.attributes.wrapped.WrappedType;
import rip.orbit.nebula.util.CC;

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

		PlaytimeMap playtime = Mars.getInstance().getPlaytimeMap();

		for (UUID uuid : toReturn) {
			Profile profile = Nebula.getInstance().getProfileHandler().fromUuid(uuid, true);

			if (profile != null) {
				IWrapped wrapped = new IWrapped(mapName, WrappedType.PRACTICE);

				int playtimeTime = (int) playtime.getPlaytime(uuid);
				Player bukkitPlayer = Mars.getInstance().getServer().getPlayer(uuid);

				if (bukkitPlayer != null) {
					playtimeTime += playtime.getCurrentSession(bukkitPlayer.getUniqueId()) / 1000;
				}

				wrapped.setPlayTime(TimeUtils.formatIntoDetailedString(playtimeTime));
				wrapped.setKills((int) Mars.getInstance().getStatisticsHandler().getStat(uuid, StatisticsHandler.Statistic.WINS, "GLOBAL"));
				wrapped.setDeaths((int) Mars.getInstance().getStatisticsHandler().getStat(uuid, StatisticsHandler.Statistic.LOSSES, "GLOBAL"));
				wrapped.setHighestKillStreak((int) Mars.getInstance().getHighestWinstreakMap().get(uuid));
				wrapped.setUniqueLogins((int) Mars.getInstance().getStatisticsHandler().getStat(uuid, StatisticsHandler.Statistic.UNIQUE_LOGINS, "GLOBAL"));

				for (Ability ability : Mars.getInstance().getAbilityHandler().getOrbitAbilities()) {
					wrapped.getPartnerItemUsedMap().put(ability.displayName(), ability.get(uuid));
				}

				profile.getWraps().add(wrapped);

				profile.save();

				++i;
			}

		}

		sender.sendMessage(CC.translate("&aSent out the practice wrap to " + i + " profiles."));


	}
	@Command(names={ "Playtime", "PTime" }, permission="")
	public static void playtime(Player sender, @Parameter(name="player", defaultValue="self") UUID player) {
		PlaytimeMap playtime = Mars.getInstance().getPlaytimeMap();
		int playtimeTime = (int) playtime.getPlaytime(player);
		Player bukkitPlayer = Mars.getInstance().getServer().getPlayer(player);

		if (bukkitPlayer != null && sender.canSee(bukkitPlayer)) {
			playtimeTime += playtime.getCurrentSession(bukkitPlayer.getUniqueId()) / 1000;
		}

		sender.sendMessage(ChatColor.LIGHT_PURPLE + UUIDUtils.name(player) + ChatColor.YELLOW + "'s total playtime is " + ChatColor.LIGHT_PURPLE + TimeUtils.formatIntoDetailedString(playtimeTime) + ChatColor.YELLOW + ".");
	}
}
