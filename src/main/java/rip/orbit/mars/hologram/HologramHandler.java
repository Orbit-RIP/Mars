package rip.orbit.mars.hologram;

import lombok.Getter;
import lombok.Setter;
import rip.orbit.mars.Mars;
import org.bukkit.Location;

public class HologramHandler {


    @Getter private HologramRunnable hologramRunnable = new HologramRunnable();

    /**
     * Holograms we are going to have.
     * - NoDebuff
     * - Sumo
     * - BuildUHC
     * - Spleef
     * - Overall
     */

    @Getter @Setter private Location topNoDebuffElo = Mars.getInstance().getServer().getWorld("world").getSpawnLocation().add(0, -200, 0);
    @Getter @Setter private Location topSumoElo = Mars.getInstance().getServer().getWorld("world").getSpawnLocation().add(0, -200, 0);
    @Getter @Setter private Location topBuildUHCElo = Mars.getInstance().getServer().getWorld("world").getSpawnLocation().add(0, -200, 0);
    @Getter @Setter private Location topSpleefElo = Mars.getInstance().getServer().getWorld("world").getSpawnLocation().add(0, -200, 0);
    @Getter @Setter private Location topOverallElo = Mars.getInstance().getServer().getWorld("world").getSpawnLocation().add(0, -200, 0);




}
