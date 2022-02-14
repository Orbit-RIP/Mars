package rip.orbit.mars.match.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.spigotmc.SpigotConfig;
import rip.orbit.mars.match.Match;
import rip.orbit.mars.match.event.MatchEndEvent;
import rip.orbit.mars.match.event.MatchStartEvent;

import java.util.Objects;

public class MatchVoidListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onStart(MatchStartEvent event) {
        Match match = event.getMatch();

        int noDamageTicks = match.getKitType().getId().contains("Void") ? 3 : 19;
        match.getTeams().forEach(team -> {
            team.getAliveMembers().stream().map(Bukkit::getPlayer).filter(Objects::nonNull).forEach(p -> {
                p.setMaximumNoDamageTicks(noDamageTicks);
                if (match.getKitType().getId().contains("Void")) {
                    p.setKbProfile(SpigotConfig.getKbProfileByName("Void"));
                } else {
                    p.setKbProfile(SpigotConfig.getKbProfileByName("Default"));

                }
            });
        });
    }

}
