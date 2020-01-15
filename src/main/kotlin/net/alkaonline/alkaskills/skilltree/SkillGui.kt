package net.alkaonline.alkaskills.skilltree

import com.mojang.authlib.GameProfile
import me.finalchild.kotlinbukkit.util.*
import net.alkaonline.alkaskills.*
import net.alkaonline.alkaskills.skilltree.combat.openCombatSkillGui
import net.alkaonline.alkaskills.skilltree.farming.openFarmingSkillGui
import net.alkaonline.alkaskills.skilltree.fishing.openFishingGui
import net.alkaonline.alkaskills.skilltree.logging.chopTree
import net.alkaonline.alkaskills.skilltree.logging.openLoggingSkillGui
import net.alkaonline.alkaskills.skilltree.mining.openMiningSkillGui
import net.alkaonline.alkaskills.skilltree.utility.doubleJump
import net.alkaonline.alkaskills.skilltree.utility.openUtilitySkillGui
import net.alkaonline.alkaskills.util.warnFormat
import org.bukkit.*
import org.bukkit.craftbukkit.v1_15_R1.CraftServer
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.*
import kotlin.math.roundToInt

class SkillGui(val playerId: UUID) : Gui {

    val combatButton = (Material.GOLDEN_SWORD * 1).also {
        it.name = "${ChatColor.GOLD}${ChatColor.BOLD}전투 스킬"
        it.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_UNBREAKABLE)
    }

    val miningButton = (Material.IRON_PICKAXE * 1).also {
        it.name = "${ChatColor.GRAY}${ChatColor.BOLD}채광 스킬"
        it.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_UNBREAKABLE)
    }

    val utilityButton = (Material.TOTEM_OF_UNDYING * 1).also {
        it.name = "${ChatColor.LIGHT_PURPLE}${ChatColor.BOLD}유틸리티 스킬"
        it.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_UNBREAKABLE)
    }

    val farmingButton = (Material.WHEAT * 1).also {
        it.name = "${ChatColor.DARK_GREEN}${ChatColor.BOLD}농사 스킬"
        it.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_UNBREAKABLE)
    }

    val fishingButton = (Material.FISHING_ROD * 1).also {
        it.name = "${ChatColor.BLUE}${ChatColor.BOLD}낚시 스킬"
        it.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_UNBREAKABLE)
    }

    val loggingButton = (Material.OAK_WOOD * 1).also {
        it.name = "${ChatColor.GREEN}${ChatColor.BOLD}벌목 스킬"
        it.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_UNBREAKABLE)
    }

    fun render(view: Inventory) {
        view[0, 8] = makePlayerInfoButton(playerId)

        view[1, 2] = combatButton
        view[1, 4] = miningButton
        view[1, 6] = utilityButton

        view[3, 2] = farmingButton
        view[3, 4] = fishingButton
        view[3, 6] = loggingButton



        for (i in 0..7) {
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
            11 -> {
                Bukkit.getScheduler().runTask(alkaSkills!!, Runnable {
                    player.openCombatSkillGui()
                })
            }
            13 -> {
                Bukkit.getScheduler().runTask(alkaSkills!!, Runnable {
                    player.openMiningSkillGui()
                })
            }
            15 -> {
                if (player.getInfo().level > 15) {
                    Bukkit.getScheduler().runTask(alkaSkills!!, Runnable {
                        player.openUtilitySkillGui()
                    })
                } else {
                    player.sendMessage("유틸리티 스킬은 35레벨 이후부터 사용 가능합니다.".warnFormat())
                    player.playSound(player.location, Sound.ENTITY_VILLAGER_NO, 1f, 1f)
                }
            }
            29 -> {
                Bukkit.getScheduler().runTask(alkaSkills!!, Runnable {
                    player.openFarmingSkillGui()
                })
            }
            31 -> {
                Bukkit.getScheduler().runTask(alkaSkills!!, Runnable {
                    player.openFishingGui()
                })
            }
            33 -> {
                Bukkit.getScheduler().runTask(alkaSkills!!, Runnable {
                    player.openLoggingSkillGui()
                })
            }
        }

    }

}

fun Player.openSkillGui() {
    val inventory = Bukkit.createInventory(
            null,
            45,
            ChatColor.BLUE + ChatColor.BOLD + "        TUNA " + ChatColor.DARK_AQUA + ChatColor.BOLD + "SKILLS"
    )
    val gui = SkillGui(this.uniqueId)
    gui.render(inventory)

    val view = this.openInventory(inventory)
    alkaSkills!!.openGuis[view] = gui
}

fun makePlayerInfoButton(playerId: UUID): ItemStack {
    return ItemStack(Material.PLAYER_HEAD).also {
        val meta = it.itemMeta as SkullMeta
        meta.owningPlayer = Bukkit.getPlayer(playerId)
        meta.setDisplayName("${ChatColor.BOLD}${ChatColor.GRAY}[${ChatColor.GREEN}내 정보${ChatColor.GRAY}]")
        meta.lore = listOf(
                "${ChatColor.LIGHT_PURPLE}레벨: ${ChatColor.GRAY}${playerId.getPlayerInfo().level}",
                "${ChatColor.LIGHT_PURPLE}경험치: ${ChatColor.GRAY}${playerId.getPlayerInfo().exp.roundToInt()}/${playerId.getPlayerInfo().getNeededExpToLevelUp()}",
                "${ChatColor.LIGHT_PURPLE}남은 스킬 포인트: ${ChatColor.GRAY}${playerId.getPlayerInfo().getPointsLeft()}"
        )
        it.itemMeta = meta
    }

}

fun makeSwitchStatusButton(skill: Skill, playerID: UUID): ItemStack {
    return ItemStack(Material.RED_STAINED_GLASS_PANE).also {
        val player = Bukkit.getPlayer(playerID) as Player
        if (player.getSkillPoint(skill) > 0) {
            when {
                player.isSwitchOn(skill) -> {
                    it.type = Material.GREEN_STAINED_GLASS_PANE
                    it.displayName = "${ChatColor.GREEN}${ChatColor.BOLD}활성화"
                }
                else -> {
                    it.displayName = "${ChatColor.RED}${ChatColor.BOLD}비활성화"
                }
            }
        } else {
            it.displayName = "${ChatColor.RED}${ChatColor.BOLD}비활성화"
        }
    }
}

val switchOnButton = ItemStack(Material.GREEN_WOOL).also {
    it.displayName = "${ChatColor.GREEN}${ChatColor.BOLD}활성화"
}

val switchOffButton = ItemStack(Material.RED_WOOL).also {
    it.displayName = "${ChatColor.RED}${ChatColor.BOLD}비활성화"
}

val backButton = ItemStack(Material.PLAYER_HEAD).also {
    val meta = it.itemMeta as SkullMeta
    meta.owningPlayer = (Bukkit.getServer() as CraftServer).getOfflinePlayer(
            GameProfile(UUID.fromString("a68f0b64-8d14-4000-a95f-4b9ba14f8df9"), "MHF_ArrowLeft")
    )
    meta.setDisplayName("${ChatColor.YELLOW}${ChatColor.BOLD}뒤로 가기")
    it.itemMeta = meta
}

val grayGlass = ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1).also {
    it.displayName = " "
}
