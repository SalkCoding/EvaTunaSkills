package net.alkaonline.alkaskills.skilltree.fishing

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

class FishingSkillGUI(val playerId: UUID) : Gui {

    fun render(view: Inventory) {
        view[0, 7] = backButton
        view[0, 8] = makePlayerInfoButton(playerId)

        view[1, 2] = codAlkaExp.makeButton(playerId)
        view[1, 6] = tropicalFishAlkaExp.makeButton(playerId)
        view[3, 2] = salmonAlkaExp.makeButton(playerId)
        view[3, 6] = pufferFishAlkaExp.makeButton(playerId)

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
            11 -> {
                codAlkaExp.requestAssignPoint(playerId)
                render(event.inventory)
                player.updateInventory()
            }
            15 -> {
                tropicalFishAlkaExp.requestAssignPoint(playerId)
                render(event.inventory)
                player.updateInventory()
            }
            29 -> {
                salmonAlkaExp.requestAssignPoint(playerId)
                render(event.inventory)
                player.updateInventory()
            }
            33 -> {
                pufferFishAlkaExp.requestAssignPoint(playerId)
                render(event.inventory)
                player.updateInventory()
            }
        }

    }

}

fun Player.openFishingGui() {
    val inventory = Bukkit.createInventory(null, 45, ChatColor.DARK_GRAY + ChatColor.BOLD + "낚시 스킬")
    val gui = FishingSkillGUI(this.uniqueId)
    gui.render(inventory)

    val view = this.openInventory(inventory)
    alkaSkills!!.openGuis[view] = gui
}
