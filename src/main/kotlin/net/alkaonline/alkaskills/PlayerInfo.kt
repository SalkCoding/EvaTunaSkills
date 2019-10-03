package net.alkaonline.alkaskills

import net.alkaonline.alkaskills.event.SkillExpChangedEvent
import net.alkaonline.alkaskills.hottime.hotTimeValue
import net.alkaonline.alkaskills.hottime.isHotTime
import net.alkaonline.alkaskills.util.refreshLevelOnPlayerList
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.text.DecimalFormat
import java.util.*
import kotlin.math.pow
import kotlin.math.roundToInt

val decimalFormat = DecimalFormat("#.##");

data class PlayerInfo internal constructor(
        var level: Int = 1,
        var exp: Double = 0.0,
        val skills: MutableMap<String, Int> = mutableMapOf()
) {

    fun getSumOfPoints(): Int {
        return skills.values.sum()
    }

    fun getPointsLeft(): Int {
        return level - getSumOfPoints()
    }

    fun getNeededExpToLevelUp(): Int {
        return if (level in 0..20) {
            (1.25.pow(level.toDouble()) * 50).roundToInt()
        } else {
            return (4770 * 1.1.pow(level - 21)).roundToInt()
        }
    }

    fun giveExp(exp: Double): Boolean {
        this.exp += exp

        var result = false
        while (this.exp >= getNeededExpToLevelUp()) {
            this.exp -= getNeededExpToLevelUp()
            level++
            result = true
        }
        return result
    }

}

fun Player.giveAlkaExp(exp: Double, titleNotice: Boolean) {
    val result = this.getInfo().giveExp(exp * if (isHotTime) hotTimeValue else 1.0)
    alkaSkills!!.server.pluginManager.callEvent(SkillExpChangedEvent(this))
    if (titleNotice)
        sendTitle(
                "",
                "${ChatColor.GREEN}${decimalFormat.format(
                        exp * if (isHotTime) hotTimeValue else 1.0
                )} ${ChatColor.YELLOW}경험치를 얻었습니다.", 10, 20, 10
        )
    if (result) {
        sendTitle("", "${ChatColor.GREEN}축하합니다! 레벨 ${this.getInfo().level}이 되었습니다!", 10, 20, 10)
        this.refreshLevelOnPlayerList()
    }
}

fun UUID.givePlayerAlkaExp(exp: Double, titleNotice: Boolean) {
    val result = this.getPlayerInfo().giveExp(exp * if (isHotTime) hotTimeValue else 1.0)
    val player = Bukkit.getPlayer(this)
    alkaSkills!!.server.pluginManager.callEvent(SkillExpChangedEvent(player))
    if (player == null) return
    if (titleNotice)
        player.sendTitle(
                "",
                "${ChatColor.GREEN}${decimalFormat.format(
                        exp
                )} ${ChatColor.YELLOW}경험치를 얻었습니다.", 10, 20, 10
        )

    if (result) {
        player.sendTitle("", "${ChatColor.GREEN}축하합니다! 레벨 ${this.getPlayerInfo().level}이 되었습니다!", 10, 20, 10)
        player.refreshLevelOnPlayerList()
    }
}

fun Player.getSkillPoint(skill: Skill): Int {
    return this.getInfo().skills.getOrDefault(skill.name, 0)
}

fun Player.getSkillPoint(skill: String): Int {
    return this.getInfo().skills.getOrDefault(skill, 0)
}

fun UUID.getPlayerSkillPoint(skill: Skill): Int {
    return this.getPlayerInfo().skills.getOrDefault(skill.name, 0)
}

fun UUID.getPlayerSkillPoint(skill: String): Int {
    return this.getPlayerInfo().skills.getOrDefault(skill, 0)
}

fun Player.setSkillPoint(skill: Skill, level: Int) {
    this.getInfo().skills[skill.name] = level
}

fun Player.setSkillPoint(skill: String, level: Int) {
    this.getInfo().skills[skill] = level
}

fun UUID.setPlayerSkillPoint(skill: Skill, level: Int) {
    this.getPlayerInfo().skills[skill.name] = level
}

fun UUID.setPlayerSkillPoint(skill: String, level: Int) {
    this.getPlayerInfo().skills[skill] = level
}
