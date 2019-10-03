package net.alkaonline.alkaskills.placeholder

import codecrafter47.bungeetablistplus.api.bukkit.Variable
import net.alkaonline.alkaskills.getInfo
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class LevelPlaceHolder : Variable("tuna_level") {

    override fun getReplacement(player: Player?): String {
        val level = player?.getInfo()?.level
        val color = when (level) {
            in 0..9 -> ChatColor.YELLOW
            in 10..19 -> ChatColor.GOLD
            in 20..29 -> ChatColor.AQUA
            in 30..39 -> ChatColor.DARK_AQUA
            in 40..49 -> ChatColor.BLUE
            else -> ChatColor.RED
        }
        return "$color[$level]${ChatColor.RESET}"
    }

}