package net.alkaonline.alkaskills.listener

import com.sk89q.worldedit.bukkit.BukkitWorld
import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.protection.flags.Flags
import com.sk89q.worldguard.protection.flags.StateFlag
import com.sk89q.worldguard.protection.regions.ProtectedRegion
import me.finalchild.kotlinbukkit.util.name
import me.finalchild.kotlinbukkit.util.plus
import net.alkaonline.alkaskills.alkaSkills
import net.alkaonline.alkaskills.getSkillPoint
import net.alkaonline.alkaskills.skilltree.combat.axeM
import net.alkaonline.alkaskills.skilltree.combat.bowM
import net.alkaonline.alkaskills.skilltree.combat.swordM
import net.alkaonline.alkaskills.util.AdditionalAttack
import net.alkaonline.alkaskills.util.refreshMaxHealth
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.max
import kotlin.math.min


class CombatListener : Listener {

    val barraging = mutableSetOf<Player>()

    var isUsingWorldGuard = false

    var region: ProtectedRegion? = null

    fun worldGuardRegister() {
        try {
            if (alkaSkills!!.server.pluginManager.isPluginEnabled("WorldGuard")) {

                val container = WorldGuard.getInstance().platform.regionContainer
                val manager = container.get(BukkitWorld(Bukkit.getWorld("world")))
                region = manager?.getRegion("global")
                isUsingWorldGuard = true
            }
        } catch (e: Exception) {
            isUsingWorldGuard = false
            alkaSkills!!.logger.warning("WorldGuard API Exception")
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if (event.cause == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
            event.isCancelled = true
            return
        }

        val damager = event.damager as? Player ?: return
        val entity = event.entity as? LivingEntity ?: return

        val item = damager.inventory.itemInMainHand
        if (item.name == null) return

        if (item.name!!.startsWith(ChatColor.GRAY + "", false) ||
                item.name!!.startsWith(ChatColor.AQUA + "", false)) {
            /*if (item.name!!.contains("" + ChatColor.RED + ChatColor.YELLOW + ChatColor.GREEN + ChatColor.BLACK + ChatColor.DARK_BLUE + ChatColor.RESET, true) ||
                    item.name!!.contains("" + ChatColor.RED + ChatColor.YELLOW + ChatColor.GREEN + ChatColor.BLACK + ChatColor.DARK_BLUE, true)) {*/

            when (item.type) {
                Material.WOODEN_SWORD, Material.GOLDEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.DIAMOND_SWORD -> {
                    Bukkit.getScheduler().runTask(
                            alkaSkills!!,
                            Runnable {
                                //val originalNoDamageTicks = entity.noDamageTicks
                                entity.noDamageTicks = 24
                                /*val newVelocity = entity.velocity
                            newVelocity.multiply(
                                when (damager.getSkillPoint(swordM)) {
                                    1 -> 0.5
                                    2 -> 0.3
                                    3 -> 0.2
                                    else -> 0.7
                                }
                            )
                            newVelocity.y = min(newVelocity.y, 0.0)

                            entity.velocity = newVelocity*/
                            })
                    if (isUsingWorldGuard && region != null) {
                        if (region!!.contains(damager.location.blockX, damager.location.blockY, damager.location.blockZ)) {
                            val isPVPAllow = region!!.getFlag(Flags.PVP) == StateFlag.State.ALLOW
                            if (!isPVPAllow && entity is Player) {
                                return
                            }
                        }
                    }
                    val addition = when (damager.getSkillPoint(swordM)) {
                        1 -> 2
                        2 -> 3
                        3 -> 5
                        else -> 0
                    }
                    entity.damage(addition * 0.5)
                    val attack = AdditionalAttack(entity, addition)
                    attack.setTask(Bukkit.getScheduler().runTaskTimer(alkaSkills!!, attack, 0, 2))
                }
                Material.WOODEN_AXE,
                Material.GOLDEN_AXE,
                Material.STONE_AXE,
                Material.IRON_AXE,
                Material.DIAMOND_AXE -> {
                    var isPVPAllow = false
                    if (isUsingWorldGuard && region != null) {
                        if (region!!.contains(damager.location.blockX, damager.location.blockY, damager.location.blockZ)) {
                            isPVPAllow = isUsingWorldGuard && region!!.getFlag(Flags.PVP) == StateFlag.State.ALLOW
                            if (!isPVPAllow && entity is Player)
                                return
                        } else isPVPAllow = true
                    }
                    val active = when (damager.getSkillPoint(axeM)) {
                        1 -> ThreadLocalRandom.current().nextDouble() <= 0.05
                        2 -> ThreadLocalRandom.current().nextDouble() <= 0.1
                        3 -> ThreadLocalRandom.current().nextDouble() <= 0.2
                        4 -> ThreadLocalRandom.current().nextDouble() <= 0.25
                        else -> false
                    }
                    if (active) {
                        val particleLocation = entity.location
                        particleLocation.y += 1.5
                        particleLocation.world.createExplosion(particleLocation, 0f)
                        particleLocation.world.spawnParticle(Particle.SWEEP_ATTACK, particleLocation, 5)
                        for (e: Entity in entity.getNearbyEntities(2.5, 2.5, 2.5)) {
                            if (damager.uniqueId == e.uniqueId)
                                continue
                            val armor = entity.getAttribute(Attribute.GENERIC_ARMOR)!!.value
                            val toughness = entity.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS)!!.value
                            val computedDamage = event.damage * (1 - min(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value, max(armor / 5, armor - entity.lastDamage / (toughness / 4 + 2))) / 25)
                            if (isPVPAllow) {
                                if (e is LivingEntity)
                                    e.damage(computedDamage)
                            } else {
                                if (e !is Player && e is LivingEntity)
                                    e.damage(computedDamage)
                            }
                        }
                        entity.addPotionEffect(
                                PotionEffect(
                                        PotionEffectType.SLOW,
                                        40,
                                        damager.getSkillPoint(axeM),
                                        true,
                                        true
                                )
                        )
                    }
                }
                else -> return
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onLaunchArrow(event: ProjectileLaunchEvent) {
        if (event.isCancelled) return
        if (event.entityType != EntityType.ARROW) return

        val shooter = (event.entity as Arrow).shooter as? Player ?: return
        if (shooter.inventory.itemInMainHand.type == Material.CROSSBOW || shooter.inventory.itemInOffHand.type == Material.CROSSBOW) return
        if (shooter.inventory.itemInMainHand.containsEnchantment(Enchantment.ARROW_INFINITE) || shooter.inventory.itemInOffHand.containsEnchantment(Enchantment.ARROW_INFINITE)) {
            if (ThreadLocalRandom.current().nextDouble() > 0.3)
                return
        }
        if (shooter in barraging || !(event.entity as Arrow).isCritical) return
        val howMany = when (shooter.getSkillPoint(bowM)) {
            1 -> 2
            2 -> 5
            3 -> 7
            else -> 0
        }

        val velocity = event.entity.velocity

        barraging.add(shooter)
        for (i in 0 until howMany) {
            val arrow = shooter.launchProjectile(
                    Arrow::class.java,
                    velocity.add(Vector.getRandom().add(Vector(-0.5, -0.5, -0.5)).multiply(0.3))
            )
            arrow.isCritical = false
            arrow.pickupStatus = AbstractArrow.PickupStatus.CREATIVE_ONLY
            arrow.damage = when (shooter.getSkillPoint(bowM)) {
                1 -> 1.0
                2 -> 1.5
                3 -> 2.0
                else -> 0.0
            }
        }
        barraging.remove(shooter)
    }

    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        event.player.refreshMaxHealth()
    }

/*@EventHandler(ignoreCancelled = true)
fun onFall(event: EntityDamageEvent) {
    if (event.entityType != EntityType.PLAYER || event.cause != EntityDamageEvent.DamageCause.FALL) {
        return
    }
    if (event.entity.world.name == "ASkyBlock") {
        event.isCancelled = true
    }
}*/

/*@EventHandler(ignoreCancelled = true)
fun onDamage(event: EntityDamageByEntityEvent) {
    if (event.entityType != EntityType.PLAYER || event.damage < 3) {
        return
    }

    val bodyMasteryPoint = (event.entity as Player).getSkillPoint(bodyM)
    if (event.damage <= bodyMasteryPoint) {
        event.damage = 0.0
    } else {
        event.damage -= bodyMasteryPoint
    }
}*/

}