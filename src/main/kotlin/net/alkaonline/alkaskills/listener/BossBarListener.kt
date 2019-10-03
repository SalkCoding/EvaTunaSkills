package net.alkaonline.alkaskills.listener

import net.alkaonline.alkaskills.bossbar.addPlayerBar
import net.alkaonline.alkaskills.bossbar.deletePlayerBar
import net.alkaonline.alkaskills.bossbar.refreshPlayerBar
import net.alkaonline.alkaskills.event.SkillExpChangedEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class BossBarListener : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onJoin(event: PlayerJoinEvent) {
        if (!loadingSet.contains(event.player.uniqueId)) {
            addPlayerBar(event.player)
        }
    }

    @EventHandler
    fun onExpChange(event: SkillExpChangedEvent) {
        refreshPlayerBar(event.player)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        if (!loadingSet.contains(event.player.uniqueId)) {
            deletePlayerBar(event.player)
        }
    }

}