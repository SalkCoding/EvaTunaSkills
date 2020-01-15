package net.alkaonline.alkaskills.listener

import me.finalchild.kotlinbukkit.util.hasLore
import me.finalchild.kotlinbukkit.util.plus
import me.finalchild.kotlinbukkit.util.times
import net.alkaonline.alkaskills.getInfo
import net.alkaonline.alkaskills.getPlayerSkillPoint
import net.alkaonline.alkaskills.giveAlkaExp
import net.alkaonline.alkaskills.skilltree.mining.betterDiamond
import net.alkaonline.alkaskills.skilltree.mining.betterIron
import net.alkaonline.alkaskills.skilltree.mining.fasterMin
import net.alkaonline.alkaskills.skilltree.mining.moreMin
import net.alkaonline.alkaskills.util.broadcastExcept
import net.alkaonline.alkaskills.util.giveOrDrop
import net.alkaonline.alkaskills.util.infoFormat
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.ComponentBuilder
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.ItemMeta
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class MiningListener : Listener {

    val miningStatuses = mutableMapOf<UUID, LinkedList<MiningStatus>>()

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        miningStatuses[event.player.uniqueId]?.clear()
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onBreak(event: BlockBreakEvent) {
        if (event.isCancelled) return

        val uuid = event.player.uniqueId

        if (event.player.isOp && event.player.isSneaking) {
            miningStatuses[uuid]?.clear()
            return
        }

        val health = getBlockHealth(event.block.type)
        if (health < 0) {
            val list = miningStatuses[uuid]
            if (list != null) {
                for (status in list.iterator()) {
                    if (status.blockLocation == event.block.location) {
                        list.remove(status)
                        break
                    }
                }
            }
            if (event.block.type == Material.STONE && event.player.getInfo().level <= 3)
                event.player.giveAlkaExp(0.1, false)
            return
        }

        val itemOnHand = event.player.inventory.itemInMainHand
        if (itemOnHand.itemMeta != null && itemOnHand.type != Material.AIR && event.player.gameMode != GameMode.CREATIVE) {
            if (itemOnHand.itemMeta is Damageable) {
                val meta = itemOnHand.itemMeta as Damageable

                if (itemOnHand.enchantments.containsKey(Enchantment.DURABILITY)) {
                    if ((1.0 / (itemOnHand.enchantments[Enchantment.DURABILITY]!! + 1)) < ThreadLocalRandom.current().nextDouble())
                        meta.damage += 1
                } else meta.damage += 1
                itemOnHand.itemMeta = meta as ItemMeta

                if (itemOnHand.type.maxDurability <= meta.damage) {
                    event.player.inventory.setItemInMainHand(null)
                    event.player.playSound(event.player.location, Sound.ENTITY_ITEM_BREAK, 1f, 1f)
                }
            }
        }

        event.isCancelled = true

        if (miningStatuses.containsKey(uuid)) {
            val list = miningStatuses[uuid]
            var found = false
            for (status in list!!.iterator()) {
                if (status.blockLocation == event.block.location) {
                    status.damage += getBlockDamagePerBreak(uuid, event.player.inventory.itemInMainHand)
                    found = true
                    break
                }
            }
            if (!found) {
                list.add(MiningStatus(
                        event.block.location,
                        getBlockDamagePerBreak(event.player.uniqueId, event.player.inventory.itemInMainHand)
                ))
                if (list.size > 3) {
                    list.removeFirst()
                }
            }

            /*if (miningStatuses[uuid]?.blockLocation == event.block.location) {
                miningStatuses[uuid]!!.damage += getBlockDamagePerBreak(
                uuid,
                event.player.inventory.itemInMainHand
                    )
            } else {
                miningStatuses[uuid] = MiningStatus(
                event.block.location,
                getBlockDamagePerBreak(event.player.uniqueId, event.player.inventory.itemInMainHand)
                )
            }*/
            for (status in list.iterator()) {
                if (status.blockLocation == event.block.location) {
                    if (status.damage >= health) {
                        list.remove(status)
                        dropRewards(event)
                        event.isCancelled = false
                        event.isDropItems = false
                        break
                    } else {
                        /*if (event.block.getDrops(event.player.inventory.itemInMainHand).isEmpty() && event.player.isSneaking && event.player.world.name == "ASkyBlock") {
                        miningStatuses.remove(event.player.uniqueId)
                        event.isCancelled = false
                        return
                    }*/

                        val filledCount = (status.damage * 20 / health).roundToInt()
                        /*event.player.sendMessage(
                                ChatMessageType.ACTION_BAR,
                                *ComponentBuilder(ChatColor.BOLD + "채광 진행도: ").color(ChatColor.GREEN)
                                        .append("■".repeat(min(20, filledCount))).color(ChatColor.BLUE)
                                        .append("■".repeat(max(0, 20 - filledCount))).color(ChatColor.BLACK).create()
                        )*/
                        event.player.sendTitle("",
                                ChatColor.BOLD + ChatColor.GREEN + "채광 진행도 : " + ChatColor.BLUE + "■".repeat(min(20, filledCount)) + ChatColor.BLACK + "■".repeat(max(0, 20 - filledCount)),
                                0, 20, 10)
                    }
                }
            }
        } else {
            val list = LinkedList<MiningStatus>()
            list.add(MiningStatus(
                    event.block.location,
                    getBlockDamagePerBreak(uuid, event.player.inventory.itemInMainHand)
            ))
            miningStatuses[uuid] = list
        }
    }

    private fun dropRewards(event: BlockBreakEvent) {
        val type = event.block.type

        val player = event.player
        val playerId = player.uniqueId
        player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f)

        when (type) {
            Material.IRON_ORE -> {
                val ranks = mutableMapOf<MinRank, Int>().toSortedMap()

                for (i in 1..getHowMany(playerId)) {
                    val rank = getIronRank(playerId.getPlayerSkillPoint(betterIron))
                    ranks[rank] = ranks.getOrDefault(rank, 0) + 1
                }

                val joiner = StringJoiner(", ")
                val publicJoiner = StringJoiner(", ")
                for ((rank, count) in ranks) {
                    player.giveOrDrop(getRankedMin(Material.IRON_INGOT, rank, count))
                    if (rank.displayName.contains("S") || rank.displayName.contains("A")) {
                        publicJoiner.add("${rank.displayName} 등급 ${count}개")
                    }
                    joiner.add("${rank.displayName} 등급 ${count}개")
                }

                player.sendMessage("${ChatColor.GRAY}철 $joiner${ChatColor.WHITE}가 나왔습니다!".infoFormat())
                /*if (publicJoiner.length() > 0)
                    broadcastExcept(
                            player,
                            "${ChatColor.GRAY}[ ${ChatColor.GREEN}!${ChatColor.GRAY} ] ${ChatColor.GRAY}${player.displayName}${ChatColor.WHITE}님이 ${ChatColor.GRAY}철 $publicJoiner${ChatColor.WHITE}를 캤습니다!"
                    )
                */
                player.giveAlkaExp(1.5, true)
            }
            Material.DIAMOND_ORE -> {
                val ranks = mutableMapOf<MinRank, Int>()

                for (i in 1..getHowMany(playerId)) {
                    val rank = getDiamondRank(playerId.getPlayerSkillPoint(betterDiamond))
                    ranks[rank] = ranks.getOrDefault(rank, 0) + 1
                }

                val joiner = StringJoiner(", ")
                val publicJoiner = StringJoiner(", ")
                for ((rank, count) in ranks) {
                    player.giveOrDrop(getRankedMin(Material.DIAMOND, rank, count))
                    if (rank.displayName.contains("S") || rank.displayName.contains("A")) {
                        publicJoiner.add("${rank.displayName} 등급 ${count}개")
                    }
                    joiner.add("${rank.displayName} 등급 ${count}개")
                }

                player.sendMessage("${ChatColor.AQUA}다이아몬드 $joiner${ChatColor.WHITE}가 나왔습니다!".infoFormat())
                if (publicJoiner.length() > 0)
                    broadcastExcept(
                            player,
                            "${ChatColor.GRAY}${player.displayName}${ChatColor.WHITE}님이 ${ChatColor.AQUA}다이아몬드 $publicJoiner${ChatColor.WHITE}를 캤습니다!".infoFormat()
                    )
                player.giveAlkaExp(20.0, true)
            }
            /*Material.BONE_BLOCK -> {
                val count = (1..3).random()
                player.giveOrDrop(Material.BONE * count)
                player.sendMessage("${ChatColor.GRAY}[ ${ChatColor.GREEN}!${ChatColor.GRAY} ] 뼈 ${count}개가 나왔습니다!")
                // broadcastExcept(
                //     player,
                //     "${ChatColor.GRAY}[ ${ChatColor.GREEN}!${ChatColor.GRAY} ] ${ChatColor.AQUA}${player.displayName}${ChatColor.WHITE}님이 뼈 ${count}개를 캤습니다!"
                // )
            }*/
            Material.COAL_ORE -> {
                val count = (1..5).random()
                player.giveOrDrop(Material.COAL * count)
                player.giveAlkaExp(0.3, true)
                player.sendMessage("석탄 ${count}개가 나왔습니다!".infoFormat())
                // broadcastExcept(
                //     player,
                //     "${ChatColor.GRAY}[ ${ChatColor.GREEN}!${ChatColor.GRAY} ] ${ChatColor.AQUA}${player.displayName}${ChatColor.WHITE}님이 석탄 ${count}개를 캤습니다!"
                // )
            }
            Material.GOLD_ORE -> {
                val count = (2..5).random()
                player.giveOrDrop(Material.GOLD_INGOT * count)
                player.giveAlkaExp(10.0, true)
                player.sendMessage("금 ${count}개가 나왔습니다!".infoFormat())
                // broadcastExcept(
                //     player,
                //     "${ChatColor.GRAY}[ ${ChatColor.GREEN}!${ChatColor.GRAY} ] ${ChatColor.AQUA}${player.displayName}${ChatColor.WHITE}님이 금 ${count}개를 캤습니다!"
                // )
            }
            Material.REDSTONE_ORE -> {
                val count = (1..3).random()
                player.giveOrDrop(Material.REDSTONE * count)
                player.giveAlkaExp(6.0, true)
                player.sendMessage("레드스톤 ${count}개가 나왔습니다!".infoFormat())
                // broadcastExcept(
                //     player,
                //     "${ChatColor.GRAY}[ ${ChatColor.GREEN}!${ChatColor.GRAY} ] ${ChatColor.AQUA}${player.displayName}${ChatColor.WHITE}님이 레드스톤 ${count}개를 캤습니다!"
                // )
            }
            Material.LAPIS_ORE -> {
                val count = (1..3).random()
                player.giveOrDrop(ItemStack(Material.LAPIS_LAZULI, count))
                player.giveAlkaExp(6.0, true)
                player.sendMessage("청금석 ${count}개가 나왔습니다!".infoFormat())
                // if (!player.isOp) {
                //     broadcastExcept(
                //         player,
                //         "${ChatColor.GRAY}[ ${ChatColor.GREEN}!${ChatColor.GRAY} ] ${ChatColor.AQUA}${player.displayName}${ChatColor.WHITE}님이 청금석 ${count}개를 캤습니다!"
                //     )
                // }
            }
            Material.EMERALD_ORE -> {
                val count = (1..3).random()
                player.giveOrDrop(ItemStack(Material.EMERALD, count))
                player.giveAlkaExp(45.0, true)
                player.sendMessage("에메랄드 ${count}개가 나왔습니다!".infoFormat())
                // if (!player.isOp) {
                //     broadcastExcept(
                //         player,
                //         "${ChatColor.GRAY}[ ${ChatColor.GREEN}!${ChatColor.GRAY} ] ${ChatColor.AQUA}${player.displayName}${ChatColor.WHITE}님이 청금석 ${count}개를 캤습니다!"
                //     )
                // }
            }
            else -> {
                return
            }
        }
    }

}

fun getIronRank(level: Int): MinRank {
    return when (level) {
        0 -> when (ThreadLocalRandom.current().nextInt(100)) {
            in 0..86 -> MinRank.C
            in 87..96 -> MinRank.B
            else -> MinRank.A
        }
        1 -> when (ThreadLocalRandom.current().nextInt(100)) {
            in 0..75 -> MinRank.C
            in 76..90 -> MinRank.B
            else -> MinRank.A
        }
        2 -> when (ThreadLocalRandom.current().nextInt(100)) {
            in 0..48 -> MinRank.C
            in 49..86 -> MinRank.B
            in 87..96 -> MinRank.A
            else -> MinRank.S
        }
        else -> when (ThreadLocalRandom.current().nextInt(100)) {
            in 0..31 -> MinRank.C
            in 32..84 -> MinRank.B
            in 85..92 -> MinRank.A
            else -> MinRank.S
        }
    }
}

fun getDiamondRank(level: Int): MinRank {
    return when (level) {
        0 -> when (ThreadLocalRandom.current().nextInt(100)) {
            in 0..89 -> MinRank.C
            else -> MinRank.B
        }
        1 -> when (ThreadLocalRandom.current().nextInt(100)) {
            in 0..86 -> MinRank.C
            in 87..96 -> MinRank.B
            else -> MinRank.A
        }
        2 -> when (ThreadLocalRandom.current().nextInt(100)) {
            in 0..75 -> MinRank.C
            in 76..90 -> MinRank.B
            else -> MinRank.A
        }
        else -> when (ThreadLocalRandom.current().nextInt(100)) {
            in 0..48 -> MinRank.C
            in 49..86 -> MinRank.B
            in 87..96 -> MinRank.A
            else -> MinRank.S
        }
    }
}

fun getBlockDamagePerBreak(playerId: UUID, stack: ItemStack?): Double {
    return when (playerId.getPlayerSkillPoint(fasterMin)) {
        0 -> 1.0
        1 -> 1.4
        2 -> 1.8
        3 -> 2.2
        4 -> 2.6
        5 -> 3.0
        else -> 0.0
    } + if (stack != null) readBlockDamageFromItem(stack) else 0.0
}

fun getBlockHealth(type: Material): Double {
    return when (type) {
        Material.COAL_ORE -> 10.0
        Material.IRON_ORE -> 25.0
        Material.GOLD_ORE -> 40.0
        Material.REDSTONE_ORE -> 40.0
        Material.LAPIS_ORE -> 40.0
        Material.DIAMOND_ORE -> 90.0
        Material.EMERALD_ORE -> 90.0
        else -> -1.0
    }
}

fun getHowMany(playerId: UUID): Int {
    return when (playerId.getPlayerSkillPoint(moreMin)) {
        1 -> (1..2).random()
        2 -> (1..3).random()
        3 -> (1..4).random()
        else -> 1
    }
}

fun getRankedMin(mat: Material, rank: MinRank, amount: Int = 1): ItemStack {
    return (mat * amount).also {
        it.lore = listOf(ChatColor.BOLD + ChatColor.BLUE + "등급: " + rank.displayName)
    }
}

data class MiningStatus(val blockLocation: Location, var damage: Double)

enum class MinRank(val displayName: String) {
    S("${ChatColor.LIGHT_PURPLE}S"),
    A("${ChatColor.GREEN}A"),
    B("${ChatColor.GOLD}B"),
    C("${ChatColor.YELLOW}C")
}

fun readBlockDamageFromItem(stack: ItemStack): Double {
    if (!stack.hasLore()) return 0.0
    val lore = stack.lore
    return lore
            ?.firstOrNull { it.startsWith("§9채굴 속도 +") }
            ?.let { it.substring(9).toDoubleOrNull() ?: 0.0 }
            ?: 0.0
}

fun ClosedRange<Int>.random() =
        Random().nextInt(endInclusive - start + 1) + start
