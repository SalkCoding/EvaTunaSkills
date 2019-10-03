package net.alkaonline.alkaskills.listener

import net.alkaonline.alkaskills.alkaSkills
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.player.PlayerQuitEvent

class GuiListener : Listener {

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (alkaSkills!!.openGuis.containsKey(event.view)) {
            event.isCancelled = true
            alkaSkills!!.openGuis[event.view]!!.onClick(event)
        }
    }

    @EventHandler
    fun onInventoryDrag(event: InventoryDragEvent) {
        if (alkaSkills!!.openGuis.containsKey(event.view)) {
            event.isCancelled = true
            alkaSkills!!.openGuis[event.view]!!.onDrag(event)
        }
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        alkaSkills!!.openGuis.remove(event.view)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        alkaSkills!!.openGuis.remove(event.player.openInventory)
    }

}
