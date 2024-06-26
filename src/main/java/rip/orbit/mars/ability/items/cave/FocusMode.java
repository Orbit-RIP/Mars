package rip.orbit.mars.ability.items.cave;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import rip.orbit.mars.ability.Ability;
import rip.orbit.mars.ability.profile.AbilityProfile;
import rip.orbit.mars.pvpclasses.pvpclasses.ArcherClass;
import rip.orbit.mars.util.cooldown.Cooldowns;
import rip.orbit.nebula.util.CC;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 12/02/2022 / 10:00 PM
 * Mars / rip.orbit.mars.ability.items.viper
 */
public class FocusMode extends Ability {

    public HashSet<UUID> gAngel = new HashSet<>();
    public Cooldowns cd = new Cooldowns();

    public FocusMode() {
        super("FocusMode");
    }

    @Override
    public Cooldowns cooldown() {
        return cd;
    }

    @Override
    public List<String> lore() {
        return CC.translate(Arrays.asList(
                "&7After clicking this you will give 10 seconds of",
                "&725% more damage to the person who last hit you."
        ));
    }

    @Override
    public List<String> foundInfo() {
        return CC.translate(Arrays.asList(
                "Ability Packages",
                "Partner Crates",
                "Star Shop (/starshop)"
        ));
    }

    @Override
    public String displayName() {
        return CC.translate("&b&lFocus Mode &7(Cave)");
    }

    @Override
    public String name() {
        return "focusmode";
    }

    @Override
    public int data() {
        return 0;
    }

    @Override
    public Material mat() {
        return Material.LEVER;
    }

    @Override
    public boolean glow() {
        return false;
    }

    @EventHandler
    public void onFocusMode(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (isSimilar(event.getItem())) {
            if (!isClick(event, "RIGHT")) {
                event.setUseItemInHand(Event.Result.DENY);
                return;
            }
            if (!canUse(player)) {
                event.setUseItemInHand(Event.Result.DENY);
                return;
            }

            event.setCancelled(true);
            takeItem(player);

            addCooldown(player, 120);

            Player target = Bukkit.getPlayer(AbilityProfile.byUUID(player.getUniqueId()).getLastDamagerName());
            if (target != null) {
                ArcherClass.getFocusModedPlayers().put(target.getName(), System.currentTimeMillis() + (10 * 1000));

                List<String> beenHitMsg = Collections.singletonList(
                        "&cYou have been hit with the &b&lFocus Mode&C!");

                List<String> hitMsg = Arrays.asList(
                        "&6You have hit &f" + player.getName() + " &6with the &b&lFocus Mode&6.",
                        "&7For the next 10 seconds you now deal 20% more damage towards them!",
                        "",
                        "&cYou have used the &b&lFocus Mode &cand and have been put on",
                        "&ccooldown for 2 minutes");

                hitMsg.forEach(s -> player.sendMessage(CC.translate(s)));
                beenHitMsg.forEach(s -> target.sendMessage(CC.translate(s)));
            }

        }
    }
}
