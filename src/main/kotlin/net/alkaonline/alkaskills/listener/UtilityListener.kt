package net.alkaonline.alkaskills.listener

import net.alkaonline.alkaskills.alkaSkills
import net.alkaonline.alkaskills.getSkillPoint
import net.alkaonline.alkaskills.isSwitchOn
import net.alkaonline.alkaskills.skilltree.utility.doubleJump
import net.alkaonline.alkaskills.util.errorFormat
import net.alkaonline.alkaskills.util.infoFormat
import org.bukkit.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.util.Vector
import java.lang.Math.toRadians
import java.util.*

class UtilityListener : Listener {

    val cooldownSet = mutableSetOf<UUID>()

    @EventHandler
    fun onSneak(event: PlayerToggleSneakEvent) {
        if (!event.isSneaking) return

        val player = event.player
        if (player.gameMode != GameMode.SURVIVAL)
            return

        val skillPoint = player.getSkillPoint(doubleJump)
        if (skillPoint <= 0) return

        if (!player.isSwitchOn(doubleJump)) return

        if (player.isFlying or player.isGliding or player.isInsideVehicle or player.isDead) return

        val playerUUID = player.uniqueId
        if (!cooldownSet.contains(playerUUID)) {
            if (!player.isOnGround) {
                val currentVelocity = player.velocity
                player.velocity = Vector(currentVelocity.x * 1.5, 0.6, currentVelocity.z * 1.5)
                player.fallDistance -= 1.2f

                val radius = 1
                for (angle in 0..360 step 15) {
                    val x = kotlin.math.cos(toRadians(angle.toDouble()))
                    val z = kotlin.math.sin(toRadians(angle.toDouble()))
                    player.world.spawnParticle(Particle.FALLING_DUST, Location(player.world,
                            player.location.x + (radius * x),
                            player.location.y,
                            player.location.z + (radius * z)), 1, 0.0, 0.0, 0.0, Material.WHITE_CONCRETE.createBlockData())

                }
                player.world.playSound(player.location, Sound.ENTITY_ENDER_DRAGON_FLAP, 0.2f, 1.5f)

                player.sendMessage("이중 도약을 사용하였습니다!".infoFormat())
                cooldownSet.add(playerUUID)

                Bukkit.getScheduler().runTaskLater(alkaSkills!!,
                        Runnable {
                            cooldownSet.remove(playerUUID)
                            player.sendMessage("이중 도약을 다시 사용할 수 있습니다!".infoFormat())
                            player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.2f, 1f)
                        },
                        when (skillPoint) {
                            1 -> {
                                2400L
                            }
                            2 -> {
                                1200L
                            }
                            3 -> {
                                600L
                            }
                            else -> {
                                100L
                            }
                        }
                )
            }
        } else {
            player.sendMessage("아직 사용할 수 없습니다!".errorFormat())
        }
    }

}