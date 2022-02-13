package rip.orbit.mars.hologram;

import cc.fyre.proton.Proton;
import cc.fyre.proton.hologram.builder.HologramBuilder;
import cc.fyre.proton.hologram.construct.Hologram;
import com.gmail.filoghost.holographicdisplays.object.NamedHologramManager;
import com.sun.org.apache.bcel.internal.generic.RET;
import lombok.Getter;
import rip.orbit.mars.Mars;
import rip.orbit.mars.elo.EloHandler;
import rip.orbit.mars.kittype.KitType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import rip.orbit.nebula.util.CC;

import java.util.*;
import java.util.stream.Collectors;

public class HologramRunnable extends BukkitRunnable {

    @Getter private static Map<KitType, Hologram> holograms = new HashMap<>();

    private static EloHandler eloHandler = Mars.getInstance().getEloHandler();

    public HologramRunnable() {
        runTaskTimer(Mars.getInstance(), 0L, (20 * 60) * 10);
    }

    @Override
    public void run() {
        System.out.println("Refreshing all holograms");

        setup();

        for (Map.Entry<KitType, Hologram> entry : holograms.entrySet()) {
            Proton.getInstance().getHologramHandler().unRegister(entry.getValue());
        }
    }


    public static void setupHologram(KitType type, Location location) {

        boolean found = false;
        for (Map.Entry<KitType, Hologram> entry : holograms.entrySet()) {
            if (entry.getKey().getId().equals(type.getId())) {
                if (entry.getValue() == null) {
                    HologramBuilder builder = Proton.getInstance().getHologramHandler().createHologram();

                    holograms.put(type, createHologram(builder, type));

                    System.out.println(type.getId() + " hologram refreshed");

                } else {
                    update(entry.getValue(), entry.getKey());
                }
                found = true;
            }
        }
        if (!found) {
            HologramBuilder builder = Proton.getInstance().getHologramHandler().createHologram();

            holograms.put(type, createHologram(builder, type));

            System.out.println(type.getId() + " hologram refreshed");
        }

    }

    public static Hologram namedHolomanager(HologramBuilder builder) {
        Hologram hologram = builder.build();
        hologram.send();
        return hologram;
    }

    public static Hologram update(Hologram hologram, KitType type) {

        hologram.destroy();
        hologram.move(Mars.getInstance().getHologramHandler().getHologramLocs().get(type));

        hologram.setLine(0, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------------");
        hologram.setLine(1, ChatColor.GOLD + "" + ChatColor.BOLD + "Top Elo " + ChatColor.GRAY + "(" + type.getDisplayName() + ")");
        hologram.setLine(2, " ");

        List<Map.Entry<String, Integer>> entry = new ArrayList<>(eloHandler.topElo(type).entrySet());

        if (entry.isEmpty()) {
            hologram.setLine(3, CC.translate("&6&l#1 &fLoading..."));
        } else {
            String first = entry.get(0).getKey();
            hologram.setLine(3, CC.translate("&6&l#1 &f" + first + ": &6" + entry.get(0).getValue()));
        }

        if (entry.size() < 2) {
            hologram.setLine(4, CC.translate("&6&l#2 &fLoading..."));
        } else {
            String second = entry.get(1).getKey();
            hologram.setLine(4, CC.translate("&6&l#2 &f" + second + ": &6" + entry.get(1).getValue()));
        }

        if (entry.size() < 3) {
            hologram.setLine(5, CC.translate("&6&l#3 &fLoading..."));
        } else {
            String third = entry.get(2).getKey();
            hologram.setLine(5, CC.translate("&6&l#3 &f" + third + ": &6" + entry.get(2).getValue()));
        }

        if (entry.size() < 4) {
            hologram.setLine(6, CC.translate("&6&l#4 &fLoading..."));
        } else {
            String fourth = entry.get(3).getKey();
            hologram.setLine(6, CC.translate("&6&l#4 &f" + fourth + ": &6" + entry.get(3).getValue()));
        }

        if (entry.size() < 5) {
            hologram.setLine(7, CC.translate("&6&l#5 &fLoading..."));
        } else {
            String fifth = entry.get(4).getKey();
            hologram.setLine(7, CC.translate("&6&l#5 &f" + fifth + ": &6" + entry.get(4).getValue()));
        }

        hologram.setLine(8, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------------");
        return hologram;
    }

    public static Hologram createHologram(HologramBuilder builder, KitType type) {

        builder.at(Mars.getInstance().getHologramHandler().getHologramLocs().get(type));

        builder.addLines(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------------");
        builder.addLines(ChatColor.GOLD + "" + ChatColor.BOLD + "Top Elo " + ChatColor.GRAY + "(" + type.getDisplayName() + ")");
        builder.addLines(" ");

        List<Map.Entry<String, Integer>> entry = new ArrayList<>(eloHandler.topElo(type).entrySet());

        if (entry.isEmpty()) {
            builder.addLines(CC.translate("&6&l#1 &fLoading..."));
        } else {
            String first = entry.get(0).getKey();
            builder.addLines(CC.translate("&6&l#1 &f" + first + ": &6" + entry.get(0).getValue()));
        }

        if (entry.size() < 2) {
            builder.addLines(CC.translate("&6&l#2 &fLoading..."));
        } else {
            String second = entry.get(1).getKey();
            builder.addLines(CC.translate("&6&l#2 &f" + second + ": &6" + entry.get(1).getValue()));
        }

        if (entry.size() < 3) {
            builder.addLines(CC.translate("&6&l#3 &fLoading..."));
        } else {
            String third = entry.get(2).getKey();
            builder.addLines(CC.translate("&6&l#3 &f" + third + ": &6" + entry.get(2).getValue()));
        }

        if (entry.size() < 4) {
            builder.addLines(CC.translate("&6&l#4 &fLoading..."));
        } else {
            String fourth = entry.get(3).getKey();
            builder.addLines(CC.translate("&6&l#4 &f" + fourth + ": &6" + entry.get(3).getValue()));
        }

        if (entry.size() < 5) {
            builder.addLines(CC.translate("&6&l#5 &fLoading..."));
        } else {
            String fifth = entry.get(4).getKey();
            builder.addLines(CC.translate("&6&l#5 &f" + fifth + ": &6" + entry.get(4).getValue()));
        }

        builder.addLines(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------------");
        return namedHolomanager(builder);
    }

    public static void setup() {
        HologramHandler hologramHandler = Mars.getInstance().getHologramHandler();

        try {

            for (Map.Entry<KitType, Location> entry : hologramHandler.getHologramLocs().entrySet()) {
                setupHologram(entry.getKey(), entry.getValue());

                System.out.println("Setting up the " + entry.getKey().getDisplayName() + " ladder.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
