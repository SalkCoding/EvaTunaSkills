package net.alkaonline.alkaskills

import com.google.common.collect.HashMultimap
import me.finalchild.kotlinbukkit.util.displayName
import net.alkaonline.alkaskills.skilltree.combat.axeM
import net.alkaonline.alkaskills.skilltree.combat.bodyM
import net.alkaonline.alkaskills.skilltree.fishing.codAlkaExp
import net.alkaonline.alkaskills.skilltree.fishing.pufferFishAlkaExp
import net.alkaonline.alkaskills.skilltree.fishing.salmonAlkaExp
import net.alkaonline.alkaskills.skilltree.fishing.tropicalFishAlkaExp
import net.alkaonline.alkaskills.skilltree.logging.chopTree
import net.alkaonline.alkaskills.skilltree.mining.moreMin
import net.alkaonline.alkaskills.skilltree.utility.doubleJump
import net.alkaonline.alkaskills.util.warnFormat
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import java.util.*

val skills = HashMultimap.create<SkillCategory, Skill>()!!

class Skill(
        val name: String,
        val displayName: String,
        val category: SkillCategory,
        val description: List<String>,
        val maxLevel: Int,
        val icon: ItemStack,
        vararg val requirements: (playerId: UUID) -> Boolean
) {

    constructor(
            displayName: String,
            category: SkillCategory,
            description: List<String>,
            maxLevel: Int,
            icon: ItemStack,
            vararg requirements: (playerId: UUID) -> Boolean
    ) : this(ChatColor.stripColor(displayName)!!, displayName, category, description, maxLevel, icon, *requirements)

    init {
        skills.put(category, this)
    }

    fun makeButton(level: Int): ItemStack {
        return icon.clone().also {
            it.displayName = "${ChatColor.BOLD}$displayName"
            it.lore = listOf(
                    "${ChatColor.GRAY}스킬 레벨: ${ChatColor.WHITE}$level / $maxLevel",
                    //"${ChatColor.GRAY}---------------------------------------------",
                    *description.map { "${ChatColor.LIGHT_PURPLE}$it" }.toTypedArray()
            )
            it.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS)
            if (level > 0)
                it.addUnsafeEnchantment(Enchantment.LUCK, 0)
        }
    }

    fun makeButton(playerId: UUID): ItemStack {
        return makeButton(playerId.getPlayerSkillPoint(this))
    }

    fun requestAssignPoint(playerId: UUID): Boolean {
        val player: Player = Bukkit.getPlayer(playerId)!!

        if (player.getInfo().getPointsLeft() <= 0 || player.getSkillPoint(this) >= maxLevel) {
            player.playSound(player.location, Sound.ENTITY_VILLAGER_NO, 1f, 1f)
            return false
        }
        for (requirement in requirements) {
            if (!requirement(playerId)) {
                player.playSound(player.location, Sound.ENTITY_VILLAGER_NO, 1f, 1f)
                return false
            }
        }

        when (this.name) {
            moreMin.name -> {
                val level: Int = player.getInfo().level
                when (player.getSkillPoint(this)) {
                    0 -> {//0->1
                        if (level <= 10) {
                            sendFailMessage(player, 10)
                            player.playSound(player.location, Sound.ENTITY_VILLAGER_NO, 1f, 1f)
                            return false
                        }
                    }
                    1 -> {//1->2
                        if (level <= 20) {
                            sendFailMessage(player, 20)
                            player.playSound(player.location, Sound.ENTITY_VILLAGER_NO, 1f, 1f)
                            return false
                        }
                    }
                    2 -> {//2->3
                        if (level <= 30) {
                            sendFailMessage(player, 30)
                            player.playSound(player.location, Sound.ENTITY_VILLAGER_NO, 1f, 1f)
                            return false
                        }
                    }
                }
            }
            bodyM.name -> {
                val level: Int = player.getInfo().level
                when (player.getSkillPoint(this)) {
                    0 -> {//0->1
                        if (level <= 5) {
                            sendFailMessage(player, 5)
                            player.playSound(player.location, Sound.ENTITY_VILLAGER_NO, 1f, 1f)
                            return false
                        }
                    }
                    1 -> {//1->2
                        if (level <= 10) {
                            sendFailMessage(player, 10)
                            player.playSound(player.location, Sound.ENTITY_VILLAGER_NO, 1f, 1f)
                            return false
                        }
                    }
                    2 -> {//2->3
                        if (level <= 15) {
                            sendFailMessage(player, 15)
                            player.playSound(player.location, Sound.ENTITY_VILLAGER_NO, 1f, 1f)
                            return false
                        }
                    }
                    3 -> {//3->4
                        if (level <= 20) {
                            sendFailMessage(player, 20)
                            player.playSound(player.location, Sound.ENTITY_VILLAGER_NO, 1f, 1f)
                            return false
                        }
                    }
                    4 -> {//4->5
                        if (level <= 25) {
                            sendFailMessage(player, 25)
                            player.playSound(player.location, Sound.ENTITY_VILLAGER_NO, 1f, 1f)
                            return false
                        }
                    }
                }
            }
            doubleJump.name -> {
                val level: Int = player.getInfo().level
                if (level <= 35) {
                    sendFailMessage(player, 35)
                    player.playSound(player.location, Sound.ENTITY_VILLAGER_NO, 1f, 1f)
                    return false
                }
            }
            axeM.name,

            chopTree.name -> {
                val level: Int = player.getInfo().level
                when (player.getSkillPoint(this)) {
                    3 -> {//3->4
                        if (level <= 35) {
                            sendFailMessage(player, 35)
                            player.playSound(player.location, Sound.ENTITY_VILLAGER_NO, 1f, 1f)
                            return false
                        }
                    }
                }
            }
            codAlkaExp.name,
            tropicalFishAlkaExp.name,
            pufferFishAlkaExp.name,
            salmonAlkaExp.name -> {
                val level: Int = player.getInfo().level
                when (player.getSkillPoint(this)) {
                    3 -> {//3->4
                        if (level <= 30) {
                            sendFailMessage(player, 30)
                            player.playSound(player.location, Sound.ENTITY_VILLAGER_NO, 1f, 1f)
                            return false
                        }
                    }
                }
            }
        }

        player.setSkillPoint(this, player.getSkillPoint(this) + 1)
        player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_CHIME, 1f, 1f)
        return true
    }

    private fun sendFailMessage(player: Player, requireLevel: Int) {
        player.sendMessage("해당 스킬은 참치 레벨 ${requireLevel}레벨 이후부터 올릴 수 있습니다.".warnFormat())
        /*player.spigot().sendMessage(
                ChatMessageType.ACTION_BAR,
                *ComponentBuilder("레벨이 부족합니다!").color(net.md_5.bungee.api.ChatColor.RED).create())*/
    }

}

class SkillRequirement(val skill: Skill) : ((UUID) -> Boolean) {
    override fun invoke(playerId: UUID): Boolean {
        return playerId.getPlayerSkillPoint(skill) > 0
    }
}

class ExclusiveRequirement(val skill: String) : ((UUID) -> Boolean) {

    constructor(skill: Skill) : this(skill.name)

    override fun invoke(playerId: UUID): Boolean {
        return playerId.getPlayerSkillPoint(skill) == 0
    }
}

class SkillOrRequirement(vararg val skills: Skill) : ((UUID) -> Boolean) {
    override fun invoke(playerId: UUID): Boolean {
        return skills.any { playerId.getPlayerSkillPoint(it) > 0 }
    }
}

enum class SkillCategory {
    COMBAT,
    MINING,
    FARMING,
    LOGGING,
    FISHING,
    UTILITY
}
