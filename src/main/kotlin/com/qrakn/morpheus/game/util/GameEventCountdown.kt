package com.qrakn.morpheus.game.util

import com.qrakn.morpheus.game.util.team.GameTeam
import rip.orbit.mars.Mars
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.scheduler.BukkitRunnable

class GameEventCountdown(var duration: Int, val runnable: Runnable, vararg val participants: GameTeam) {

    init {
        Countdown().runTaskTimerAsynchronously(Mars.getInstance(), 0L, 20L)
    }

    inner class Countdown: BukkitRunnable() {
        override fun run() {
            if (duration == -1) {
                cancel()
                return
            }

            for (participant in participants) {
                for (player in participant.players) {
                    if (duration > 0) {
                        player.sendMessage(ChatColor.YELLOW.toString() + duration + "...")
                        player.playSound(player.location, Sound.NOTE_PLING, 1F, 1F)
                    } else {
                        player.sendMessage(ChatColor.GREEN.toString() + "Match started.")
                        player.playSound(player.location, Sound.NOTE_PLING, 1F, 2F)
                    }
                }
            }

            if (duration == 0) {
                runnable.run()
            }

            duration--
        }

    }

}