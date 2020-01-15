package net.alkaonline.alkaskills.listener

import net.alkaonline.alkaskills.getSkillPoint
import net.alkaonline.alkaskills.giveAlkaExp
import net.alkaonline.alkaskills.skilltree.fishing.*
import net.alkaonline.alkaskills.skilltree.logging.loggingFortune
import net.alkaonline.alkaskills.util.giveOrDrop
import net.alkaonline.alkaskills.util.infoFormat
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Item
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerFishEvent
import java.util.concurrent.ThreadLocalRandom

class FishingListener : Listener {

    @EventHandler
    fun onFishing(event: PlayerFishEvent) {
        if (event.isCancelled) return

        if (event.state != PlayerFishEvent.State.CAUGHT_FISH) return

        val caught = event.caught ?: return

        val player = event.player

        if (caught.customName != null) return

        if (caught.type != EntityType.DROPPED_ITEM) return

        val isFortuneActive: Boolean = when (player.getSkillPoint(fishingFortune)) {
            1 -> ThreadLocalRandom.current().nextDouble() <= 0.05
            2 -> ThreadLocalRandom.current().nextDouble() <= 0.07
            3 -> ThreadLocalRandom.current().nextDouble() <= 0.10
            else -> false
        }
        if (isFortuneActive) {
            val droppedItem = caught as Item
            player.giveOrDrop(droppedItem.itemStack)
            player.sendMessage("낚시 행운이 발동하였습니다!".infoFormat())
        }
        //probability code
        when ((caught as Item).itemStack.type) {
            Material.COD -> {
                val boundAddition = 0.2
                val skillPoint = player.getSkillPoint(codAlkaExp)
                if (skillPoint > 0) {
                    val giveAlkaExp = ThreadLocalRandom.current().nextDouble(0.1 + ((skillPoint - 1) * boundAddition), 0.3 + ((skillPoint - 1) * boundAddition))
                    player.giveAlkaExp(giveAlkaExp, true)
                }
            }
            Material.TROPICAL_FISH -> {
                val boundAddition = 2
                val skillPoint = player.getSkillPoint(tropicalFishAlkaExp)
                if (skillPoint > 0) {
                    val giveAlkaExp = ThreadLocalRandom.current().nextDouble(2.0 + ((skillPoint - 1) * boundAddition), 4.0 + ((skillPoint - 1) * boundAddition))
                    player.giveAlkaExp(giveAlkaExp, true)
                }
            }
            Material.PUFFERFISH -> {
                val boundAddition = 0.2
                val skillPoint = player.getSkillPoint(pufferFishAlkaExp)
                if (skillPoint > 0) {
                    val giveAlkaExp = ThreadLocalRandom.current().nextDouble(1.3 + ((skillPoint - 1) * boundAddition), 1.5 + ((skillPoint - 1) * boundAddition))
                    player.giveAlkaExp(giveAlkaExp, true)
                }
            }
            Material.SALMON -> {
                val boundAddition = 0.2
                val skillPoint = player.getSkillPoint(salmonAlkaExp)
                if (skillPoint > 0) {
                    val giveAlkaExp = ThreadLocalRandom.current().nextDouble(0.5 + ((skillPoint - 1) * boundAddition), 0.7 + ((skillPoint - 1) * boundAddition))
                    player.giveAlkaExp(giveAlkaExp, true)
                }
            }
        }
    }

}