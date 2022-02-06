package rip.orbit.mars.match.listener;

import rip.orbit.mars.match.Match;
import rip.orbit.mars.match.event.MatchStartEvent;
import net.syncpvp.cobalt.knockback.KnockbackProfile;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.spigotmc.SpigotConfig;

import java.util.Objects;

public class MatchComboListener implements Listener {
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onStart(MatchStartEvent event) {
        Match match = event.getMatch();

        KnockbackProfile knockbackProfile = SpigotConfig.getKbProfileByName(match.getKitType().getId());
        match.getTeams().forEach(team -> team.getAliveMembers().stream().map(Bukkit::getPlayer).filter(Objects::nonNull).forEach(p -> p.setKbProfile(knockbackProfile)));

        int noDamageTicks = match.getKitType().getId().contains("Combo") ? 3 : 20;
        match.getTeams().forEach(team -> team.getAliveMembers().stream().map(Bukkit::getPlayer).filter(Objects::nonNull).forEach(p -> p.setMaximumNoDamageTicks(noDamageTicks)));
    }
}
