package net.alkaonline.alkaskills.listener

import net.alkaonline.alkaskills.getSkillPoint
import net.alkaonline.alkaskills.giveAlkaExp
import net.alkaonline.alkaskills.isChopTreeOn
import net.alkaonline.alkaskills.skilltree.logging.canAlkaExp
import net.alkaonline.alkaskills.skilltree.logging.chopTree
import net.alkaonline.alkaskills.util.CheckSameLoggingSeconds
import net.alkaonline.alkaskills.util.isPlacedByPlayer
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.ItemMeta
import java.util.concurrent.ThreadLocalRandom


class LoggingListener : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onLogging(event: BlockBreakEvent) {
        if (event.isCancelled) return

        val block = event.block
        val player = event.player
        when (block.type) {
            Material.OAK_LOG, Material.ACACIA_LOG, Material.BIRCH_LOG, Material.DARK_OAK_LOG, Material.JUNGLE_LOG, Material.SPRUCE_LOG -> {
                if (block.isPlacedByPlayer(player, CheckSameLoggingSeconds)) return

                val boundAddition = 0.02
                val skillPoint = player.getSkillPoint(canAlkaExp)

                val chopTree = player.getSkillPoint(chopTree)
                val haveToChop = when (chopTree) {
                    1 -> {
                        ThreadLocalRandom.current().nextDouble() < 0.05
                    }
                    2 -> {
                        ThreadLocalRandom.current().nextDouble() < 0.1
                    }
                    3 -> {
                        ThreadLocalRandom.current().nextDouble() < 0.15
                    }
                    else -> false
                }

                val axe = player.inventory.itemInMainHand
                val hasAxe = axe.type.toString().endsWith("_AXE")

                var size = 1
                if (haveToChop && hasAxe && player.isChopTreeOn()) {
                    val blockList = chopTree(block)
                    size = blockList.size

                    if (player.gameMode != GameMode.CREATIVE) {
                        val damageable = axe.itemMeta as Damageable
                        if (axe.enchantments.containsKey(Enchantment.DURABILITY)) {
                            if ((1.0 / (axe.enchantments[Enchantment.DURABILITY]!! + 1)) < ThreadLocalRandom.current().nextDouble())
                                damageable.damage += 1
                        } else damageable.damage += 1
                        axe.itemMeta = damageable as ItemMeta
                    }

                    for (b in blockList) {
                        if (b.isPlacedByPlayer(player, CheckSameLoggingSeconds)) continue

                        b.breakNaturally(ItemStack(b.type))
                    }
                }

                if (skillPoint > 0) {
                    val giveAlkaExp = ThreadLocalRandom.current().nextDouble(0.0 + ((skillPoint - 1) * boundAddition), 0.04 + ((skillPoint - 1) * boundAddition)) * size
                    player.giveAlkaExp(giveAlkaExp, true)
                }
            }
        }
    }

    fun chopTree(start: Block): List<Block> {
        val blockList = ArrayList<Block>()

        var y = 0

        while (start.getRelative(0, y, 0).type == start.type) {
            blockList.add(start.getRelative(0, y, 0))
            y--
        }

        y = 1
        while (start.getRelative(0, y, 0).type == start.type) {
            blockList.add(start.getRelative(0, y, 0))
            y++
        }

        return blockList
    }

}