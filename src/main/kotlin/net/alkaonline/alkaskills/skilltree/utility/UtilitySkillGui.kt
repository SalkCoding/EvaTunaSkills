package net.alkaonline.alkaskills.skilltree.utility

import me.finalchild.kotlinbukkit.util.plus
import me.finalchild.kotlinbukkit.util.set
import net.alkaonline.alkaskills.Gui
import net.alkaonline.alkaskills.alkaSkills
import net.alkaonline.alkaskills.getSkillPoint
import net.alkaonline.alkaskills.setSwitch
import net.alkaonline.alkaskills.skilltree.*
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import java.util.*

class UtilitySkillGui(val playerId: UUID) : Gui {

    fun render(view: Inventory) {
        view[0, 7] = backButton
        view[0, 8] = makePlayerInfoButton(playerId)

        view[2, 4] = doubleJump.makeButton(playerId)

        view[3, 3] = switchOnButton
        view[3, 4] = makeSwitchStatusButton(doubleJump, playerId)
        view[3, 5] = switchOffButton

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
        val id = player.uniqueId

        when (event.rawSlot) {
            7 -> {
                Bukkit.getScheduler().runTask(alkaSkills!!, Runnable {
                    player.openSkillGui()
                })
            }
            22 -> {
                doubleJump.requestAssignPoint(id)
                render(event.inventory)
                player.updateInventory()
            }
            30 -> {
                if (player.getSkillPoint(doubleJump) > 0) {
                    player.setSwitch(doubleJump, true)
                    render(event.inventory)
                    player.updateInventory()
                }
            }
            32 -> {
                if (player.getSkillPoint(doubleJump) > 0) {
                    player.setSwitch(doubleJump, false)
                    render(event.inventory)
                    player.updateInventory()
                }
            }
        }

    }

}

fun Player.openUtilitySkillGui() {
    val inventory = Bukkit.createInventory(null, 45, ChatColor.DARK_GRAY + ChatColor.BOLD + "유틸리티 스킬")
    val gui = UtilitySkillGui(this.uniqueId)
    gui.render(inventory)

    val view = this.openInventory(inventory)
    alkaSkills!!.openGuis[view] = gui
}