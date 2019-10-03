package net.alkaonline.alkaskills.listener

import net.alkaonline.alkaskills.alkaSkills
import net.alkaonline.alkaskills.bossbar.addPlayerBar
import net.alkaonline.alkaskills.getInfo
import net.alkaonline.alkaskills.giveAlkaExp
import net.alkaonline.alkaskills.util.refreshLevelOnPlayerList
import net.alkaonline.alkaskills.util.refreshMaxHealth
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.*


val loadingSet = mutableSetOf<UUID>()

class PlayerListener : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player

        loadingSet.add(player.uniqueId)
        player.sendMessage("${ChatColor.GRAY}[ ${ChatColor.GREEN}!${ChatColor.GRAY} ] 데이터 로딩중...")
        Bukkit.getScheduler().runTaskLater(alkaSkills!!, Runnable {
            alkaSkills!!.playerInfoManager.loadInfo(player.uniqueId)

            val info = player.getInfo()
            if (info.exp > info.getNeededExpToLevelUp()) {
                info.exp = 0.0
                info.level++
            }

            player.refreshLevelOnPlayerList()
            player.refreshMaxHealth()
            addPlayerBar(player)
            loadingSet.remove(player.uniqueId)
            player.sendMessage("${ChatColor.GRAY}[ ${ChatColor.GREEN}!${ChatColor.GRAY} ] 데이터 로딩완료!")
        }, 40)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerLeave(event: PlayerQuitEvent) {
        alkaSkills!!.playerInfoManager.saveAndUnloadInfo(event.player.uniqueId)
    }

    /*@EventHandler(ignoreCancelled = true)
    fun blockEnchantment(event: InventoryOpenEvent) {
        if (event.inventory.type == InventoryType.ENCHANTING && !event.player.isOp) {
            event.isCancelled = true
        }
    }*/

    /*@EventHandler(ignoreCancelled = true)
    fun blockExp(event: PlayerExpChangeEvent) {
        if (!event.player.isOp) {
            event.amount = 0
            event.player.exp = 0.0F
            event.player.level = 0
        }
    }*/

    /*@EventHandler(priority = EventPriority.LOW)
    fun onRightClick(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_AIR || event.hand != EquipmentSlot.HAND) {
            return
        }

        val item = event.item
        val meta = item!!.itemMeta
        val lore = meta.lore
        if (lore == null || lore.isEmpty()) {
            return
        }
        val firstLore = ChatColor.stripColor(lore[0])
        if (firstLore != null) {
            if (firstLore.contains("스킬 포인트 초기화")) {
                if (event.player.getInfo().skills.isEmpty()) {
                    event.player.sendMessage(ChatColor.RED.toString() + "투자한 스킬 포인트가 없습니다!")
                    event.isCancelled = true
                    return
                }

                event.player.getInfo().skills.clear()
                event.player.sendMessage(ChatColor.GREEN.toString() + "스킬 포인트를 초기화했습니다!")
                event.isCancelled = true
                if (item.amount == 1) {
                    event.player.inventory.setItemInMainHand(ItemStack(Material.AIR))
                } else {
                    item.amount = item.amount - 1
                }
            } else if (firstLore.startsWith("포함된 경험치 : ")) {
                val xpAmount = try {
                    firstLore.substring(10).toDouble()
                } catch (e: NumberFormatException) {
                    return
                }

                event.player.giveAlkaExp(xpAmount, true)
                event.isCancelled = true
                if (item.amount == 1) {
                    event.player.inventory.setItemInMainHand(ItemStack(Material.AIR))
                } else {
                    item.amount = item.amount - 1
                }
            }
        }

    }*/

}
