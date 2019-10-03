package net.alkaonline.alkaskills

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent

interface Gui {

    fun onClick(event: InventoryClickEvent) {}
    fun onDrag(event: InventoryDragEvent) {}

}
