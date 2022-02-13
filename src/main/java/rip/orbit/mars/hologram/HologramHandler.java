package rip.orbit.mars.hologram;

import cc.fyre.proton.hologram.construct.Hologram;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import rip.orbit.mars.Mars;
import org.bukkit.Location;
import rip.orbit.mars.kittype.KitType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class HologramHandler {


    private HologramRunnable hologramRunnable = new HologramRunnable();

    private Map<KitType, Location> hologramLocs = new HashMap<>();

   public HologramHandler() {
       for (String s : Mars.getInstance().getConfig().getStringList("holograms")) {
           String[] args = s.split(":");
           double x = Double.parseDouble(args[1]);
           double y = Double.parseDouble(args[2]);
           double z = Double.parseDouble(args[3]);
           String world = args[4];
           hologramLocs.put(KitType.byId(args[0]), new Location(Bukkit.getWorld(world), x, y, z));
       }

       Bukkit.getScheduler().runTaskLater(Mars.getInstance(), () -> {
           hologramRunnable.run();
       }, 20);


       Bukkit.getScheduler().runTaskTimer(Mars.getInstance(), () -> {
           for (Map.Entry<KitType, Hologram> entry : HologramRunnable.getHolograms().entrySet()) {
               HologramRunnable.update(entry.getValue(), entry.getKey());
           }
       }, 20, 20 * 60);
   }

   public void save() {
       List<String> strings = new ArrayList<>();

       for (Map.Entry<KitType, Location> entry : hologramLocs.entrySet()) {
           Location l = entry.getValue();
           strings.add(entry.getKey().getId() + ":" + l.getX() + ":" + l.getY() + ":" + l.getZ() + ":" + l.getWorld().getName());
       }
       Mars.getInstance().getConfig().set("holograms", strings);
       Mars.getInstance().saveConfig();
   }


}
