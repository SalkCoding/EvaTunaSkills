package net.alkaonline.alkaskills.listener

import net.alkaonline.alkaskills.getSkillPoint
import net.alkaonline.alkaskills.giveAlkaExp
import net.alkaonline.alkaskills.skilltree.farming.*
import net.alkaonline.alkaskills.util.CheckSameFarmingSeconds
import net.alkaonline.alkaskills.util.getCropsAge
import net.alkaonline.alkaskills.util.getMaxCropsAge
import net.alkaonline.alkaskills.util.isPlacedByPlayer
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import java.util.concurrent.ThreadLocalRandom


class FarmingListener : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onInteract(event: PlayerInteractEvent) {
        if (event.isCancelled) return

        if (event.action == Action.RIGHT_CLICK_BLOCK) {
            val player = event.player
            val block: Block? = event.clickedBlock
            when (event.clickedBlock?.type) {
                Material.SWEET_BERRY_BUSH -> {
                    if (player.isSneaking) return
                    if (block!!.getMaxCropsAge() != block.getCropsAge()) return
                    val skillPoint = player.getSkillPoint(t1SweetBerry)
                    val boundAddition = 0.01
                    if (skillPoint > 0) {
                        val giveAlkaExp = ThreadLocalRandom.current().nextDouble(0.01 + ((skillPoint - 1) * boundAddition), 0.02 + ((skillPoint - 1) * boundAddition))
                        player.giveAlkaExp(giveAlkaExp, true)
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onBreak(event: BlockBreakEvent) {
        if (event.isCancelled) return

        event.expToDrop = 0
        val player = event.player
        val block: Block = event.block

        if (block.isPlacedByPlayer(player, CheckSameFarmingSeconds)) return

        val boundAddition = 0.02
        when (event.block.type) {
            Material.WHEAT -> {
                if (block.getMaxCropsAge() != block.getCropsAge()) return
                val skillPoint = player.getSkillPoint(t1Wheat)
                if (skillPoint > 0) {
                    val giveAlkaExp = ThreadLocalRandom.current().nextDouble(0.02 + ((skillPoint - 1) * boundAddition), 0.04 + ((skillPoint - 1) * boundAddition))
                    player.giveAlkaExp(giveAlkaExp, true)
                }
            }
            Material.SWEET_BERRY_BUSH -> {
                if (block.getMaxCropsAge() != block.getCropsAge()) return
                val skillPoint = player.getSkillPoint(t1SweetBerry)
                if (skillPoint > 0) {
                    val giveAlkaExp = ThreadLocalRandom.current().nextDouble(0.01 + ((skillPoint - 1) * 0.01), 0.02 + ((skillPoint - 1) * 0.01))
                    player.giveAlkaExp(giveAlkaExp, true)
                }
            }
            Material.POTATOES -> {
                if (block.getMaxCropsAge() != block.getCropsAge()) return
                val skillPoint = player.getSkillPoint(t2Potato)
                if (skillPoint > 0) {
                    val giveAlkaExp = ThreadLocalRandom.current().nextDouble(0.03 + ((skillPoint - 1) * boundAddition), 0.05 + ((skillPoint - 1) * boundAddition))
                    player.giveAlkaExp(giveAlkaExp, true)
                }
            }
            Material.CARROTS -> {
                if (block.getMaxCropsAge() != block.getCropsAge()) return
                val skillPoint = player.getSkillPoint(t2Carrot)
                if (skillPoint > 0) {
                    val giveAlkaExp = ThreadLocalRandom.current().nextDouble(0.03 + ((skillPoint - 1) * boundAddition), 0.05 + ((skillPoint - 1) * boundAddition))
                    player.giveAlkaExp(giveAlkaExp, true)
                }
            }
            Material.NETHER_WART -> {
                if (block.getMaxCropsAge() != block.getCropsAge()) return
                val skillPoint = player.getSkillPoint(t3NetherWart)
                if (skillPoint > 0) {
                    val giveAlkaExp = ThreadLocalRandom.current().nextDouble(0.05 + ((skillPoint - 1) * boundAddition), 0.07 + ((skillPoint - 1) * boundAddition))
                    player.giveAlkaExp(giveAlkaExp, true)
                }
            }
            Material.MELON -> {
                if (event.block.getRelative(-1, 0, 0).type != Material.ATTACHED_MELON_STEM
                        && event.block.getRelative(1, 0, 0).type != Material.ATTACHED_MELON_STEM
                        && event.block.getRelative(0, 0, -1).type != Material.ATTACHED_MELON_STEM
                        && event.block.getRelative(0, 0, 1).type != Material.ATTACHED_MELON_STEM) return
                val skillPoint = player.getSkillPoint(t3Melon)
                if (skillPoint > 0) {
                    val giveAlkaExp = ThreadLocalRandom.current().nextDouble(0.04 + ((skillPoint - 1) * boundAddition), 0.06 + ((skillPoint - 1) * boundAddition))
                    player.giveAlkaExp(giveAlkaExp, true)
                }
            }
            Material.PUMPKIN -> {
                if (event.block.getRelative(-1, 0, 0).type != Material.ATTACHED_PUMPKIN_STEM
                        && event.block.getRelative(1, 0, 0).type != Material.ATTACHED_PUMPKIN_STEM
                        && event.block.getRelative(0, 0, -1).type != Material.ATTACHED_PUMPKIN_STEM
                        && event.block.getRelative(0, 0, 1).type != Material.ATTACHED_PUMPKIN_STEM) return
                val skillPoint = player.getSkillPoint(t3Pumpkin)
                if (skillPoint > 0) {
                    val giveAlkaExp = ThreadLocalRandom.current().nextDouble(0.04 + ((skillPoint - 1) * boundAddition), 0.06 + ((skillPoint - 1) * boundAddition))
                    player.giveAlkaExp(giveAlkaExp, true)
                }
            }
            Material.CACTUS -> {
                if (isAbnormalCrop(block)) return
                val skillPoint = player.getSkillPoint(t4Cactus)
                if (skillPoint > 0) {
                    val giveAlkaExp = ThreadLocalRandom.current().nextDouble(0.04 + ((skillPoint - 1) * boundAddition), 0.06 + ((skillPoint - 1) * boundAddition)) * getCropsLengthFromBlock(block)
                    player.giveAlkaExp(giveAlkaExp, true)
                }
            }
            Material.SUGAR_CANE -> {
                if (isAbnormalCrop(block)) return
                val skillPoint = player.getSkillPoint(t4SugarCane)
                if (skillPoint > 0) {
                    val giveAlkaExp = ThreadLocalRandom.current().nextDouble(0.04 + ((skillPoint - 1) * boundAddition), 0.06 + ((skillPoint - 1) * boundAddition)) * getCropsLengthFromBlock(block)
                    player.giveAlkaExp(giveAlkaExp, true)
                }
            }
            Material.BEETROOTS -> {
                if (block.getMaxCropsAge() != block.getCropsAge()) return
                val skillPoint = player.getSkillPoint(t5Beetroot)
                if (skillPoint > 0) {
                    val giveAlkaExp = ThreadLocalRandom.current().nextDouble(0.06 + ((skillPoint - 1) * boundAddition), 0.08 + ((skillPoint - 1) * boundAddition))
                    player.giveAlkaExp(giveAlkaExp, true)
                }
            }
            Material.COCOA -> {
                if (block.getMaxCropsAge() != block.getCropsAge()) return
                val skillPoint = player.getSkillPoint(t5Cocoa)
                if (skillPoint > 0) {
                    val giveAlkaExp = ThreadLocalRandom.current().nextDouble(0.05 + ((skillPoint - 1) * boundAddition), 0.07 + ((skillPoint - 1) * boundAddition))
                    player.giveAlkaExp(giveAlkaExp, true)
                }
            }
        }
    }
}

fun isAbnormalCrop(block: Block): Boolean {
    var count = 1
    for (i in 1..3)
        if (block.getRelative(0, -i, 0).type == block.type)
            count++
        else break
    if (count > 3)
        return true
    for (i in 1..3)
        if (block.getRelative(0, i, 0).type == block.type)
            count++
        else break
    if (count > 3)
        return true
    return false
}

fun getCropsLocationList(block: Block): List<Location> {
    val list = ArrayList<Location>()
    list.add(block.location)
    for (i in 1..2) {
        val anotherBlock = block.getRelative(0, i, 0)
        if (anotherBlock.type == block.type)
            list.add(anotherBlock.location)
    }
    return list
}

fun getCropsLengthFromBlock(block: Block): Int {
    var result = 1
    while (block.getRelative(0, result, 0).type == block.type) result++
    return result
}

/*@EventHandler(ignoreCancelled = true)
fun onBoneMeal(event: PlayerInteractEvent) {
    if (!event.hasBlock() || event.action != Action.RIGHT_CLICK_BLOCK || !event.hasItem() || event.item?.type != Material.INK_SAC || (event.item?.data as Dye).color != DyeColor.WHITE) return

    val block = event.clickedBlock
    if (block != null) {
        when (block.type) {
            Material.SUGAR_CANE -> {
                // Check one block down.
                when (block.getRelative(0, -1, 0).type) {
                    Material.SUGAR_CANE -> {
                        // one block down is not land.
                        val toBlockDownType = block.getRelative(0, -2, 0).type
                        if (toBlockDownType == Material.DIRT || toBlockDownType == Material.GRASS || toBlockDownType == Material.SAND) {
                            // two block down is land.
                            val oneBlockUp = block.getRelative(0, 1, 0)
                            if (oneBlockUp.type == Material.AIR) {
                                oneBlockUp.type = Material.SUGAR_CANE
                                takeBoneMeal(event.player.inventory)
                                oneBlockUp.location.world.spawnParticle(Particle.VILLAGER_HAPPY, 10.0, 1.0, 1.0, 1)
                            }
                        }
                    }
                    Material.DIRT, Material.GRASS, Material.SAND -> {
                        // one block down is land.
                        val oneBlockUp = block.getRelative(0, 1, 0)
                        when (oneBlockUp.type) {
                            Material.AIR -> {
                                oneBlockUp.type = Material.SUGAR_CANE
                                takeBoneMeal(event.player.inventory)
                                oneBlockUp.location.world.spawnParticle(Particle.VILLAGER_HAPPY, 10.0, 1.0, 1.0, 1)
                            }
                            Material.SUGAR_CANE -> {
                                val twoBlockUp = block.getRelative(0, 2, 0)
                                if (twoBlockUp.type == Material.AIR) {
                                    twoBlockUp.type = Material.SUGAR_CANE
                                    takeBoneMeal(event.player.inventory)
                                    twoBlockUp.location.world.spawnParticle(Particle.VILLAGER_HAPPY, 10.0, 1.0, 1.0, 1)
                                }
                            }
                        }
                    }
                }
            }
            Material.CACTUS -> {
                when (block.getRelative(0, -1, 0).type) {
                    Material.CACTUS -> {
                        // one block down is not land.
                        if (block.getRelative(0, -2, 0).type == Material.SAND) {
                            // two block down is land.
                            val oneBlockUp = block?.getRelative(0, 1, 0)
                            if (oneBlockUp != null) {
                                if (oneBlockUp.type == Material.AIR) {
                                    oneBlockUp.type = Material.CACTUS
                                    takeBoneMeal(event.player.inventory)
                                    oneBlockUp.location.world.spawnParticle(Particle.VILLAGER_HAPPY, 10.0, 1.0, 1.0, 1)
                                }
                            }
                        }
                    }
                    Material.SAND -> {
                        // one block down is land.
                        val oneBlockUp = block?.getRelative(0, 1, 0)
                        if (oneBlockUp != null) {
                            when (oneBlockUp.type) {
                                Material.AIR -> {
                                    oneBlockUp.type = Material.CACTUS
                                    takeBoneMeal(event.player.inventory)
                                    oneBlockUp.location.world.spawnParticle(Particle.VILLAGER_HAPPY, 10.0, 1.0, 1.0, 1)
                                }
                                Material.CACTUS -> {
                                    val twoBlockUp = block.getRelative(0, 2, 0)
                                    if (twoBlockUp.type == Material.AIR) {
                                        twoBlockUp.type = Material.CACTUS
                                        takeBoneMeal(event.player.inventory)
                                        twoBlockUp.location.world.spawnParticle(Particle.VILLAGER_HAPPY, 10.0, 1.0, 1.0, 1)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Material.NETHER_WART -> {
                val blockState = block.state
                val warts = (blockState.data as NetherWarts)
                if (warts.state != NetherWartsState.RIPE) {
                    takeBoneMeal(event.player.inventory)
                    block.location.world.spawnParticle(Particle.VILLAGER_HAPPY, 10.0, 1.0, 1.0, 1)
                    warts.state = NetherWartsState.RIPE
                    blockState.data = warts
                    blockState.update()
                }
            }
        }
    }
}

fun takeBoneMeal(inventory: Inventory) {
    for (i in 0 until inventory.size) {
        val item = inventory.getItem(i)
        if (item == null) continue
        if (item.type == Material.INK_SAC && (item.data as Dye).color == DyeColor.WHITE) {
            item.amount--
            inventory.setItem(i, item)
            return
        }
    }
}

@EventHandler(ignoreCancelled = true)
fun onBlockFromTo(event: BlockFromToEvent) {
    if (event.block.type == Material.WATER) {
        when (event.toBlock.type) {
            Material.WHEAT, Material.POTATOES, Material.BEETROOTS, Material.NETHER_WART, Material.CARROTS -> {
                event.isCancelled = true
            }
        }
    }
}

@EventHandler(ignoreCancelled = true)
fun blockWaterCrop(event: PlayerBucketEmptyEvent) {
    val block = event.blockClicked.getRelative(event.blockFace)
    if (block.type == Material.WHEAT
        || block.type == Material.POTATOES
        || block.type == Material.BEETROOTS
        || block.type == Material.NETHER_WART
        || block.type == Material.CARROTS
        || event.blockClicked.type == Material.WHEAT
        || event.blockClicked.type == Material.POTATOES
        || event.blockClicked.type == Material.BEETROOTS
        || event.blockClicked.type == Material.NETHER_WART
        || event.blockClicked.type == Material.CARROTS
    ) {
        event.isCancelled = true
    }
}

@EventHandler(ignoreCancelled = true)
fun blockCanPlace(event: BlockPlaceEvent) {
    if (event.itemInHand.type == Material.SUGAR_CANE
        && event.blockPlaced.getRelative(0, -1, 0).type == Material.SUGAR_CANE) {
        event.isCancelled = true
    } else if (event.itemInHand.type == Material.CACTUS
        && event.blockPlaced.getRelative(0, -1, 0).type == Material.CACTUS) {
        event.isCancelled = true
    } else if (event.itemInHand.type == Material.PUMPKIN
        && (event.blockPlaced.getRelative(-1, 0, 0).type == Material.PUMPKIN_STEM
                || event.blockPlaced.getRelative(1, 0, 0).type == Material.PUMPKIN_STEM
                || event.blockPlaced.getRelative(0, 0, -1).type == Material.PUMPKIN_STEM
                || event.blockPlaced.getRelative(0, 0, 1).type == Material.PUMPKIN_STEM)) {
        event.isCancelled = true
    }
}

@EventHandler(ignoreCancelled = true)
fun onPlayerInteract(event: PlayerInteractEvent) {
    if (event.action == Action.PHYSICAL) {
        val block = event.clickedBlock ?: return
        if (block.type === Material.FARMLAND) {
            event.isCancelled = true
        }
    }
}

@EventHandler(ignoreCancelled = true)
fun onPistonExtend(event: BlockPistonExtendEvent) {
    for (block in event.blocks) {
        if (block.type == Material.MELON || block.type == Material.SUGAR_CANE || block.type == Material.CACTUS) {
            event.isCancelled = true
            return
        }
    }
}

@EventHandler(ignoreCancelled = true)
fun onPistonRetract(event: BlockPistonRetractEvent) {
    for (block in event.blocks) {
        if (block.type == Material.MELON || block.type == Material.SUGAR_CANE || block.type == Material.CACTUS) {
            event.isCancelled = true
            return
        }
    }
}

@EventHandler(ignoreCancelled = true)
fun onBlockPhysics(event: BlockPhysicsEvent) {
    if (event.changedType == Material.CACTUS || event.changedType == Material.SUGAR_CANE) {
        event.isCancelled = true
    }
}

@EventHandler(ignoreCancelled = true)
fun onBlockGrow(event: BlockGrowEvent) {
    if (event.block.type != Material.CACTUS) {
        return
    }

    val northMat = event.block.getRelative(BlockFace.NORTH).type
    if (northMat.isSolid || northMat == Material.LAVA) {
        event.isCancelled = true
        return
    }
    val southMat = event.block.getRelative(BlockFace.SOUTH).type
    if (southMat.isSolid || southMat == Material.LAVA) {
        event.isCancelled = true
        return
    }
    val eastMat = event.block.getRelative(BlockFace.EAST).type
    if (eastMat.isSolid || eastMat == Material.LAVA) {
        event.isCancelled = true
        return
    }
    val westMat = event.block.getRelative(BlockFace.WEST).type
    if (westMat.isSolid || westMat == Material.LAVA) {
        event.isCancelled = true
        return
    }
}

}*/
