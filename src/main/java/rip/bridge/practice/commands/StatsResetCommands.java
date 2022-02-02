package rip.bridge.practice.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.base.Objects;

import rip.bridge.practice.Practice;
import rip.bridge.qlib.qLib;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;
import rip.bridge.qlib.menu.menus.ConfirmMenu;
import rip.bridge.qlib.redis.RedisCommand;
import rip.bridge.qlib.util.Callback;
import rip.bridge.qlib.util.UUIDUtils;
import redis.clients.jedis.Jedis;

public class StatsResetCommands {

    private static String REDIS_PREFIX = "Practice:statsResetToken:";

    @Command(names = { "statsreset addtoken" }, permission = "op", async = true)
    public static void addToken(CommandSender sender, @Param(name = "player") String playerName, @Param(name = "amount") int amount) {
        UUID uuid = UUIDUtils.uuid(playerName);

        if (uuid == null) {
            sender.sendMessage(ChatColor.RED + "Unable to locate '" + playerName + "'.");
            return;
        }

        addTokens(uuid, amount);
        sender.sendMessage(ChatColor.GREEN + "Added " + amount + " token" + (amount == 1 ? "" : "s") + " to " + UUIDUtils.name(uuid) + ".");
    }

    @Command(names = {"statsreset"}, permission = "", async = true)
    public static void reset(Player sender) {
        int tokens = getTokens(sender.getUniqueId());
        if (tokens <= 0) {
            sender.sendMessage(ChatColor.RED + "You need at least one token to reset your stats.");
            return;
        }

        Bukkit.getScheduler().runTask(Practice.getInstance(), () -> {
            new ConfirmMenu("Stats reset", new Callback<Boolean>() {

                @Override
                public void callback(Boolean reset) {
                    if (!reset) {
                        sender.sendMessage(ChatColor.RED + "Stats reset aborted.");
                        return;
                    }

                    Bukkit.getScheduler().runTaskAsynchronously(Practice.getInstance(), () -> {
                        Practice.getInstance().getEloHandler().resetElo(sender.getUniqueId());
                        removeTokens(sender.getUniqueId(), 1);
                        int tokens = getTokens(sender.getUniqueId());
                        sender.sendMessage(ChatColor.GREEN + "Reset your stats! Used one token. " + tokens + " token" + (tokens == 1 ? "" : "s") + " left.");
                    });
                }
                
            }).openMenu(sender); 
        });
    }

    private static int getTokens(UUID player) {
        return qLib.getInstance().runBackboneRedisCommand(new RedisCommand<Integer>() {

            @Override
            public Integer execute(Jedis redis) {
                return Integer.valueOf(Objects.firstNonNull(redis.get(REDIS_PREFIX + player.toString()), "0"));
            }

        });
    }

    private static void addTokens(UUID player, int amountBy) {
        qLib.getInstance().runBackboneRedisCommand(new RedisCommand<Object>() {

            @Override
            public Object execute(Jedis redis) {
                redis.incrBy(REDIS_PREFIX + player.toString(), amountBy);
                return null;
            }
            
        });
    }

    public static void removeTokens(UUID player, int amountBy) {
        qLib.getInstance().runBackboneRedisCommand(new RedisCommand<Object>() {

            @Override
            public Object execute(Jedis redis) {
                redis.decrBy(REDIS_PREFIX + player.toString(), amountBy);
                return null;
            }
            
        });
    }
}
