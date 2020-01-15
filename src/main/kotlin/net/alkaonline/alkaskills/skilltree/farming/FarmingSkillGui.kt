package net.alkaonline.alkaskills.skilltree.farming

import me.finalchild.kotlinbukkit.util.plus
import me.finalchild.kotlinbukkit.util.set
import net.alkaonline.alkaskills.Gui
import net.alkaonline.alkaskills.alkaSkills
import net.alkaonline.alkaskills.skilltree.backButton
import net.alkaonline.alkaskills.skilltree.grayGlass
import net.alkaonline.alkaskills.skilltree.makePlayerInfoButton
import net.alkaonline.alkaskills.skilltree.openSkillGui
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import java.util.*
import kotlin.collections.set

class FarmingSkillGui(val playerId: UUID) : Gui {

    fun render(view: Inventory) {
        view[0, 7] = backButton
        view[0, 8] = makePlayerInfoButton(playerId)

        view[1, 1] = t1Wheat.makeButton(playerId)
        view[3, 1] = t1SweetBerry.makeButton(playerId)
        view[1, 2] = t2Potato.makeButton(playerId)
        view[3, 2] = t2Carrot.makeButton(playerId)
        view[2, 3] = t3Pumpkin.makeButton(playerId)
        view[2, 4] = t3NetherWart.makeButton(playerId)
        view[2, 5] = t3Melon.makeButton(playerId)
        view[1, 6] = t4SugarCane.makeButton(playerId)
        view[3, 6] = t4Cactus.makeButton(playerId)
        view[1, 7] = t5Beetroot.makeButton(playerId)
        view[3, 7] = t5Cocoa.makeButton(playerId)
        view[0, 4] = t6GoldFinger.makeButton(playerId)

        for (i in 0..6) {
            if (i == 4) continue
            view[0, i] = grayGlass
        }
        for (i in 0..8) {
            view[4, i] = grayGlass
        }
        for (i in 1..3) {
            view[i, 0] = grayGlass
            view[i, 8] = grayGlass
        }
    }

    override fun onClick(event: InventoryClickEvent) {
        if (event.action != InventoryAction.PICKUP_ALL
                || event.click != ClickType.LEFT) {
            return
        }

        val player = event.whoClicked as Player
        val id = player.uniqueId

        when (event.rawSlot) {
            7 -> {
                Bukkit.getScheduler().runTask(alkaSkills!!, Runnable {
                    player.openSkillGui()
                })
            }
            10 -> {
                t1Wheat.requestAssignPoint(id)
                render(event.inventory)
                player.updateInventory()
            }
            28 -> {
                t1SweetBerry.requestAssignPoint(id)
                render(event.inventory)
                player.updateInventory()
            }
            11 -> {
                t2Potato.requestAssignPoint(id)
                render(event.inventory)
                player.updateInventory()
            }
            29 -> {
                t2Carrot.requestAssignPoint(id)
                render(event.inventory)
                player.updateInventory()
            }
            21 -> {
                t3Pumpkin.requestAssignPoint(id)
                render(event.inventory)
                player.updateInventory()
            }
            22 -> {
                t3NetherWart.requestAssignPoint(id)
                render(event.inventory)
                player.updateInventory()
            }
            23 -> {
                t3Melon.requestAssignPoint(id)
                render(event.inventory)
                player.updateInventory()
            }
            15 -> {
                t4SugarCane.requestAssignPoint(id)
                render(event.inventory)
                player.updateInventory()
            }
            33 -> {
                t4Cactus.requestAssignPoint(id)
                render(event.inventory)
                player.updateInventory()
            }
            16 -> {
                t5Beetroot.requestAssignPoint(id)
                render(event.inventory)
                player.updateInventory()
            }
            34 -> {
                t5Cocoa.requestAssignPoint(id)
                render(event.inventory)
                player.updateInventory()
            }
            4->{
                t6GoldFinger.requestAssignPoint(id)
                render(event.inventory)
                player.updateInventory()
            }

        }

    }

}

fun Player.openFarmingSkillGui() {
    val inventory = Bukkit.createInventory(null, 45, ChatColor.DARK_GREEN + ChatColor.BOLD + "농사 스킬")
    val gui = FarmingSkillGui(this.uniqueId)
    gui.render(inventory)

    val view = this.openInventory(inventory)
    alkaSkills!!.openGuis[view] = gui
}
