package net.alkaonline.alkaskills.listener

/*class ChunkListener : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onChunkPopulate(event: ChunkPopulateEvent) {
        for (populator in event.world.populators) {
            if (populator is OreBlockPopulator) {
                val chunk = event.chunk
                for (x in 0..15) for (y in 0..255) (0..15)
                    .asSequence()
                    .map { z -> chunk.getBlock(x, y, z) }
                    .filter { it.type == Material.NETHER_QUARTZ_ORE }
                    .forEach { it.type = Material.NETHERRACK }
                return
            }
        }
    }
}*/
