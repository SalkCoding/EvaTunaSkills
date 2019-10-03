package net.alkaonline.alkaskills.listener

import org.bukkit.event.Listener

class ChatListener : Listener {

    /*@EventHandler(priority = EventPriority.MONITOR)
    fun onChat(event: AsyncPlayerChatEvent) {
        if (event.player.isOp) {
            return
        }

        val level = event.player.getInfo().level
        val color = when (level) {
            in 0..9 -> ChatColor.YELLOW
            in 10..19 -> ChatColor.GOLD
            in 20..29 -> ChatColor.AQUA
            in 30..39 -> ChatColor.DARK_AQUA
            in 40..49 -> ChatColor.BLUE
            else -> ChatColor.RED
        }
        event.format = "$color[$level]${ChatColor.RESET}${event.format}"
    }*/

}
