package rip.orbit.mars.tab;

import cc.fyre.proton.tab.construct.TabLayout;
import rip.orbit.mars.Mars;
import cc.fyre.proton.util.PlayerUtils;
import cc.fyre.proton.util.UUIDUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.BiConsumer;

public class AlternateOnlinePlayersLayoutProvider implements Listener, BiConsumer<Player, TabLayout> {

    private Map<UUID, String> playersMap = generateNewTreeMap();

    public AlternateOnlinePlayersLayoutProvider() {
        Bukkit.getPluginManager().registerEvents(this, Mars.getInstance());
        Bukkit.getScheduler().runTaskTimerAsynchronously(Mars.getInstance(), this::rebuildCache, 0, 1 * 60 * 20);
    }

    @Override
    public void accept(Player player, TabLayout tabLayout) {
        tabLayout.set(1, 0, "&6&lOrbit Network");
        tabLayout.set(0, 18, "", 1);
        tabLayout.set(1, 18, "", 1);
        tabLayout.set(2, 18, "", 1);
    }


    @EventHandler(ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        playersMap.put(event.getPlayer().getUniqueId(), getName(event.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        playersMap.remove(event.getPlayer().getUniqueId());
    }

    private void rebuildCache() {
        TreeMap<UUID, String> newTreeMap = generateNewTreeMap();

        Bukkit.getOnlinePlayers().forEach(player -> {
            newTreeMap.put(player.getUniqueId(), getName(player.getUniqueId()));
        });

        this.playersMap = newTreeMap;
    }

    private String getName(UUID uuid) {
        return UUIDUtils.name(uuid);
    }

    public int getPing(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        return player == null ? -1 : Math.max(((PlayerUtils.getPing(player) + 5) / 10) * 10, 1);
    }

    private TreeMap<UUID, String> generateNewTreeMap() {
        return new TreeMap<UUID, String>(new Comparator<UUID>() {

            @Override
            public int compare(UUID first, UUID second) {

                return tieBreaker(first, second);
            }

        });
    }

    private int tieBreaker(UUID first, UUID second) {
        String firstName = UUIDUtils.name(first);
        String secondName = UUIDUtils.name(second);

        return firstName.compareTo(secondName);
    }

}
