package net.alkaonline.alkaskills.skilltree.combat

import me.finalchild.kotlinbukkit.util.plus
import me.finalchild.kotlinbukkit.util.set
import net.alkaonline.alkaskills.Gui
import net.alkaonline.alkaskills.alkaSkills
import net.alkaonline.alkaskills.getInfo
import net.alkaonline.alkaskills.getSkillPoint
import net.alkaonline.alkaskills.skilltree.backButton
import net.alkaonline.alkaskills.skilltree.grayGlass
import net.alkaonline.alkaskills.skilltree.makePlayerInfoButton
import net.alkaonline.alkaskills.skilltree.openSkillGui
import net.alkaonline.alkaskills.util.refreshMaxHealth
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import java.util.*

class CombatSkillGui(val playerId: UUID) : Gui {

    fun render(view: Inventory) {
        view[0, 7] = backButton
        view[0, 8] = makePlayerInfoButton(playerId)

        view[2, 1] = swordM.makeButton(playerId)
        view[2, 3] = axeM.makeButton(playerId)
        view[2, 5] = bowM.makeButton(playerId)
        view[2, 7] = bodyM.makeButton(playerId)

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
                Bukkit.getScheduler().runTask(alkaSkills!!, Runnable() {
                    player.openSkillGui()
                })
            }
            19 -> {
                swordM.requestAssignPoint(id)
                render(event.inventory)
                player.updateInventory()
            }
            21 -> {
                axeM.requestAssignPoint(id)
                render(event.inventory)
                player.updateInventory()
            }
            23 -> {
                bowM.requestAssignPoint(id)
                render(event.inventory)
                player.updateInventory()
            }
            25 -> {
                bodyM.requestAssignPoint(id)
                render(event.inventory)
                player.updateInventory()
                player.refreshMaxHealth()
            }
        }

    }

}

fun Player.openCombatSkillGui() {
    val inventory = Bukkit.createInventory(null, 45, ChatColor.GOLD + ChatColor.BOLD + "전투 스킬")
    val gui = CombatSkillGui(this.uniqueId)
    gui.render(inventory)

    val view = this.openInventory(inventory)
    alkaSkills!!.openGuis[view] = gui
}
