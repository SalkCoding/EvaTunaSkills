package net.alkaonline.alkaskills.bossbar

import net.alkaonline.alkaskills.PlayerInfo
import net.alkaonline.alkaskills.alkaSkills
import net.alkaonline.alkaskills.getInfo
import net.alkaonline.alkaskills.hottime.hotTimeValue
import net.alkaonline.alkaskills.hottime.isHotTime
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.HashMap

private val barHashMap = HashMap<UUID, BossBar>()

fun addPlayerBar(player: Player) {
    val info: PlayerInfo = player.getInfo()
    val hotTime = if (isHotTime) "${ChatColor.RED} (핫타임 x${String.format("%.1f", hotTimeValue)}) " else ""
    val bar: BossBar = Bukkit.createBossBar(
            "$hotTime${ChatColor.GRAY}참치 레벨 : ${info.level} (${String.format(
                    "%.1f",
                    info.exp
            )}/${info.getNeededExpToLevelUp()})", BarColor.WHITE, BarStyle.SOLID
    )
    bar.addPlayer(player)
    barHashMap[player.uniqueId] = bar
    refreshPlayerBar(player)
}

//run on async
fun refreshPlayerBar(player: Player) {
    if (barHashMap.containsKey(player.uniqueId)) {
        Bukkit.getScheduler().runTaskAsynchronously(alkaSkills!!, Runnable {
            val bar: BossBar? = barHashMap[player.uniqueId]
            val info: PlayerInfo = player.getInfo()
            val hotTime = if (isHotTime) "${ChatColor.RED} (핫타임 x${String.format("%.1f", hotTimeValue)}) " else ""
            bar?.setTitle("$hotTime${ChatColor.GRAY}참치 레벨 : ${info.level} (${String.format(
                    "%.1f",
                    info.exp
            )}/${info.getNeededExpToLevelUp()})")
            bar!!.progress = player.getInfo().exp / player.getInfo().getNeededExpToLevelUp()
        })
    }
}

fun deletePlayerBar(player: Player) {
    if (barHashMap.containsKey(player.uniqueId)) {
        val bar: BossBar? = barHashMap[player.uniqueId]
        bar?.removePlayer(player)
        barHashMap.remove(player.uniqueId)
    }
}
