package rip.orbit.mars.nametag.util;

import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.server.v1_7_R4.ScoreboardServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.scheduler.CraftScheduler;
import org.bukkit.craftbukkit.v1_7_R4.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Scoreboard;
import rip.orbit.mars.nametag.PlayerScoreboard;
import rip.orbit.mars.nametag.nms.PlayerScoreboard_1_7;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Executor;

public class NmsUtils_1_7 extends NmsUtils {

    @Override
    public PlayerScoreboard getNewPlayerScoreboard(Player player) {
        return new PlayerScoreboard_1_7(player);
    }

    @Override
    public Thread getMainThread() {
        return MinecraftServer.getServer().primaryThread;
    }

    @Override
    public Scoreboard getPlayerScoreboard(Player player) {
        return player.getScoreboard() == Bukkit.getScoreboardManager()
        .getMainScoreboard() ? newScoreboard() : player.getScoreboard();
    }

    private Scoreboard newScoreboard() {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();

        try {
            Constructor<?> constructor = CraftScoreboard.class.getDeclaredConstructor(net.minecraft.server.v1_7_R4.Scoreboard.class);
            constructor.setAccessible(true);

            return (Scoreboard) constructor.newInstance(new ScoreboardServer(server));
        } catch(NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return Bukkit.getScoreboardManager().getMainScoreboard();
    }

}
