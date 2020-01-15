package net.alkaonline.alkaskills.skilltree.logging

import me.finalchild.kotlinbukkit.util.plus
import me.finalchild.kotlinbukkit.util.set
import net.alkaonline.alkaskills.*
import net.alkaonline.alkaskills.skilltree.*
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import java.util.*

class LoggingSkillGUI(val playerId: UUID) : Gui {

    fun render(view: Inventory) {
        view[0, 7] = backButton
        view[0, 8] = makePlayerInfoButton(playerId)

        view[2, 2] = canAlkaExp.makeButton(playerId)
        view[2, 4] = loggingFortune.makeButton(playerId)
        view[2, 6] = chopTree.makeButton(playerId)

        view[3, 5] = switchOnButton
        view[3, 6] = makeSwitchStatusButton(chopTree, playerId)
        view[3, 7] = switchOffButton

        for (i in 0..6) {
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

        when (event.rawSlot) {
            7 -> {
                Bukkit.getScheduler().runTask(alkaSkills!!, Runnable {
                    player.openSkillGui()
                })
            }
            20 -> {
                canAlkaExp.requestAssignPoint(playerId)
                render(event.inventory)
                player.updateInventory()
            }
            22 -> {
                loggingFortune.requestAssignPoint(playerId)
                render(event.inventory)
                player.updateInventory()
            }
            24 -> {
                chopTree.requestAssignPoint(playerId)
                render(event.inventory)
                player.updateInventory()
            }
            32 -> {
                if (player.getSkillPoint(chopTree) > 0) {
                    player.setSwitch(chopTree, true)
                    render(event.inventory)
                    player.updateInventory()
                }
            }
            34 -> {
                if (player.getSkillPoint(chopTree) > 0) {
                    player.setSwitch(chopTree, false)
                    render(event.inventory)
                    player.updateInventory()
                }
            }
        }

    }

}

fun Player.openLoggingSkillGui() {
    val inventory = Bukkit.createInventory(null, 45, ChatColor.DARK_GRAY + ChatColor.BOLD + "벌목 스킬")
    val gui = LoggingSkillGUI(this.uniqueId)
    gui.render(inventory)

    val view = this.openInventory(inventory)
    alkaSkills!!.openGuis[view] = gui
}
