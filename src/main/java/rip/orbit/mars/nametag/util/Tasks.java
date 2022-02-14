package rip.orbit.mars.nametag.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import rip.orbit.mars.Mars;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;

public class Tasks {

    public static ThreadFactory newThreadFactory(String name) {
        return new ThreadFactoryBuilder().setNameFormat(name).build();
    }

    public static ThreadFactory newThreadFactory(String name, UncaughtExceptionHandler handler) {
        return new ThreadFactoryBuilder().setNameFormat(name).setUncaughtExceptionHandler(handler).build();
    }

    public static void sync(Callable callable) {
        Bukkit.getScheduler().runTask(Mars.getInstance(), callable::call);
    }

    public static BukkitTask syncLater(Callable callable, long delay) {
        return Bukkit.getScheduler().runTaskLater(Mars.getInstance(), callable::call, delay);
    }

    public static BukkitTask syncTimer(Callable callable, long delay, long value) {
        return Bukkit.getScheduler().runTaskTimer(Mars.getInstance(), callable::call, delay, value);
    }

    public static void async(Callable callable) {
        Bukkit.getScheduler().runTaskAsynchronously(Mars.getInstance(), callable::call);
    }

    public static BukkitTask asyncLater(Callable callable, long delay) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(Mars.getInstance(), callable::call, delay);
    }

    public static BukkitTask asyncTimer(Callable callable, long delay, long value) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(Mars.getInstance(), callable::call, delay, value);
    }

    public interface Callable {
        void call();
    }
}
