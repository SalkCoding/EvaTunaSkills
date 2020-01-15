package net.alkaonline.alkaskills.util

import net.alkaonline.alkaskills.getInfo
import net.alkaonline.alkaskills.getSkillPoint
import net.alkaonline.alkaskills.skilltree.combat.bodyM
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.attribute.Attribute
import org.bukkit.block.Block
import org.bukkit.block.data.Ageable
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

val CheckSameFarmingSeconds = 900
val CheckSameLoggingSeconds = 600

fun String.infoFormat(): String {
    return "${ChatColor.GRAY}[${ChatColor.GREEN} ! ${ChatColor.GRAY}] " + this
}

fun String.warnFormat(): String {
    return "${ChatColor.GRAY}[${ChatColor.YELLOW} ! ${ChatColor.GRAY}] " + this
}

fun String.errorFormat(): String {
    return "${ChatColor.GRAY}[${ChatColor.RED} ! ${ChatColor.GRAY}] " + this
}

fun Player.giveOrDrop(item: ItemStack) {
    val left = this.inventory.addItem(item)
    for (entry in left) {
        this.eyeLocation.world.dropItem(eyeLocation, entry.value)
    }
}

fun broadcastExcept(player: Player, message: String) {
    Bukkit.getOnlinePlayers()
            .filter { it.uniqueId != player.uniqueId }
            .forEach { it.sendMessage(message) }
}

fun Block.getCropsAge(): Int {
    val ageable: Ageable = this.blockData as Ageable
    return ageable.age
}

fun Block.getMaxCropsAge(): Int {
    val ageable: Ageable = this.blockData as Ageable
    return ageable.maximumAge
}

fun Player.refreshLevelOnPlayerList() {
    if (this.isOp) return;
    val level = this.getInfo().level
    val color = when (level) {
        in 0..9 -> ChatColor.YELLOW
        in 10..19 -> ChatColor.GOLD
        in 20..29 -> ChatColor.AQUA
        in 30..39 -> ChatColor.DARK_AQUA
        in 40..49 -> ChatColor.BLUE
        else -> ChatColor.RED
    }
    val splitName = playerListName.split(" ")
    val regex = Regex("\\[[0-9]+\\]")
    val builder = StringBuilder()
    for (string in splitName) {
        if (regex.matches(ChatColor.stripColor(string) + "")) continue
        builder.append(ChatColor.translateAlternateColorCodes('&', string)).append(" ")
    }
    this.setPlayerListName("$color[${if (level < 10) "0$level" else level}]${ChatColor.RESET} $builder")
    /*val tag = this.world.spawnEntity(this.location, EntityType.ARMOR_STAND) as ArmorStand
    tag.isMarker = true
    tag.isVisible = false
    tag.customName = "$color[$level]${ChatColor.RESET} $builder"
    tag.isCustomNameVisible = true
    this.addPassenger(tag)*/
}

fun Player.refreshMaxHealth() {
    val attribute = this.getAttribute(Attribute.GENERIC_MAX_HEALTH)
    val maxHealth = (20 + (this.getSkillPoint(bodyM) * 4).toDouble())
    attribute!!.baseValue = maxHealth
    this.healthScale = maxHealth
    this.isHealthScaled = true
}

fun Block.isPlacedByPlayer(player: Player, second: Int): Boolean {
    val result = this.getMetadata("Place ${player.uniqueId}")
    if (result.size > 0) {
        for (v in result) {
            val placedTime = Calendar.getInstance()
            placedTime.timeInMillis = v.asLong()
            placedTime.add(Calendar.SECOND, second)
            if (placedTime.after(Calendar.getInstance()))
                return true
        }
    }
    return false
}