package com.qrakn.morpheus.game.bukkit.event

import com.qrakn.morpheus.game.Game
import rip.orbit.mars.Mars
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class PlayerQuitGameEvent(val player: Player, val game: Game) : Event() {

    companion object {
        @JvmStatic val handlerList = HandlerList()
    }

    init {
        Bukkit.getScheduler().runTaskLater(Mars.getInstance(), {
            game.players.remove(player)
            game.spectators.remove(player)
        }, 2L)
    }

    override fun getHandlers(): HandlerList {
        return handlerList
    }

}