package rip.orbit.mars.match.listener;

import org.bukkit.event.player.PlayerItemDamageEvent;
import rip.orbit.mars.match.Match;
import rip.orbit.mars.match.event.MatchEndEvent;
import rip.orbit.mars.match.event.MatchStartEvent;
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

        int noDamageTicks = match.getKitType().getId().contains("Combo") ? 3 : 19;
        match.getTeams().forEach(team -> {
            team.getAliveMembers().stream().map(Bukkit::getPlayer).filter(Objects::nonNull).forEach(p -> {
                p.setMaximumNoDamageTicks(noDamageTicks);
                if (match.getKitType().getId().contains("Combo")) {
                    p.setKbProfile(SpigotConfig.getKnockbackByName("Combo"));
                } else {
                    p.setKbProfile(SpigotConfig.getKnockbackByName("Default"));

                }
            });
        });
    }

    @EventHandler
    public void onEnd(MatchEndEvent event) {

    }

    @EventHandler
    public void itemDmg(PlayerItemDamageEvent event) {
        if (!event.getItem().getType().name().contains("SWORD")) {
            if (event.getPlayer().getMaximumNoDamageTicks() == 3) {
                event.setDamage(event.getDamage() + 1);
            }
        }
    }
}
