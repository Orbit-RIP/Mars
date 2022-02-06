package rip.orbit.mars.hologram;

import com.gmail.filoghost.holographicdisplays.disk.HologramDatabase;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;
import com.gmail.filoghost.holographicdisplays.object.NamedHologramManager;
import rip.orbit.mars.Mars;
import rip.orbit.mars.elo.EloHandler;
import rip.orbit.mars.kittype.KitType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class HologramRunnable extends BukkitRunnable {

    private static final String NO_DEBUFF_NAME = "NoDebuff";
    private static final String SUMO_NAME = "Sumo";
    private static final String BUILD_UHC_NAME = "BuildUHC";
    private static final String SPLEEF_NAME = "Spleef";
    private static final String GLOBAL_NAME = "Global";

    private static EloHandler eloHandler = Mars.getInstance().getEloHandler();


    public HologramRunnable() {
        runTaskTimer(Mars.getInstance(), 0L, (20 * 60) * 10);
    }


    @Override
    public void run() {
        System.out.println("Refreshing all holograms");

        setLocations();

        setup();
    }


    private static void setupHologram(String name, Location location) {

        NamedHologram hologram = new NamedHologram(location, name.toLowerCase());

        if (!NamedHologramManager.isExistingHologram(name.toLowerCase())) {

            System.out.println(name + " does not exist. Creating");
            createHologram(hologram, name);

            hologram.refreshAll();

        } else if (NamedHologramManager.isExistingHologram(name.toLowerCase())) {

            NamedHologramManager.getHologram(name.toLowerCase()).delete();
            hologram.delete();

            NamedHologram newHologram = new NamedHologram(location, name.toLowerCase());
            createHologram(newHologram, name);

            System.out.println(name + " hologram refreshed");
        }
    }

    private static void namedHolomanager(NamedHologram hologram) {
        NamedHologramManager.addHologram(hologram);
        HologramDatabase.saveHologram(hologram);
        HologramDatabase.trySaveToDisk();
    }

    private static void createHologram(NamedHologram hologram, String elo) {

        hologram.clearLines();


        hologram.appendTextLine(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------------");
        hologram.appendTextLine(ChatColor.GOLD + "" + ChatColor.BOLD + "Top Elo " + ChatColor.GRAY + "(" + elo + ")");
        hologram.appendTextLine("");

        int counter = 1;
        for (Map.Entry<String, Integer> entry : eloHandler.topElo(KitType.byId(elo)).entrySet()) {
            // counter, entry getkey, entry getvalue
            if (counter > 5) {
                hologram.appendTextLine(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------------");
                namedHolomanager(hologram);
                return;
            }
            hologram.appendTextLine(ChatColor.GOLD + "" + ChatColor.BOLD + "#" + counter + " " + ChatColor.WHITE + entry.getKey() + " [" + entry.getValue() + "]");
            counter++;
        }
        while(counter <= 5) {
            hologram.appendTextLine(ChatColor.GOLD + "" + ChatColor.BOLD + "#" + counter + " " + ChatColor.GRAY + "" + ChatColor.ITALIC + "Loading...");
            counter++;
        }

        hologram.appendTextLine(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------------");
        namedHolomanager(hologram);
    }

    private static void setLocations() {
        HologramHandler hologramHandler = Mars.getInstance().getHologramHandler();

        try {
            hologramHandler.setTopOverallElo(NamedHologramManager.getHologram(GLOBAL_NAME.toLowerCase()).getLocation());
            hologramHandler.setTopNoDebuffElo(NamedHologramManager.getHologram(NO_DEBUFF_NAME.toLowerCase()).getLocation());
            hologramHandler.setTopSumoElo(NamedHologramManager.getHologram(SUMO_NAME.toLowerCase()).getLocation());
            hologramHandler.setTopBuildUHCElo(NamedHologramManager.getHologram(BUILD_UHC_NAME.toLowerCase()).getLocation());
            hologramHandler.setTopSpleefElo(NamedHologramManager.getHologram(SPLEEF_NAME.toLowerCase()).getLocation());
        } catch (Exception e) {
            System.out.println("No holograms made. Please make them to stop this message from showing");
        }
    }

    private static void setup() {
        HologramHandler hologramHandler = Mars.getInstance().getHologramHandler();

        try {

            setupHologram(GLOBAL_NAME, hologramHandler.getTopOverallElo());
            setupHologram(NO_DEBUFF_NAME, hologramHandler.getTopNoDebuffElo());
            setupHologram(SUMO_NAME, hologramHandler.getTopSumoElo());
            setupHologram(BUILD_UHC_NAME, hologramHandler.getTopBuildUHCElo());
            setupHologram(SPLEEF_NAME, hologramHandler.getTopSpleefElo());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
