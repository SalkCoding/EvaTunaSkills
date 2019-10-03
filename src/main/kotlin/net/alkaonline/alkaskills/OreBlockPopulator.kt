package net.alkaonline.alkaskills

/*class OreBlockPopulator : BlockPopulator() {

    override fun populate(world: World, random: Random, source: Chunk) {
        val r = random.nextInt(1000)
        if (r < 256) {
            val x = r / 16
            val z = r % 16

            val heights = getSafeHeights(source, x, z)
            if (heights.isEmpty()) {
                return
            }
            val height = heights[random.nextInt(heights.size)]
            source.getBlock(x, height - 1, z).type = getOreType(random)
        }
    }

}

private fun getSafeHeights(source: Chunk, x: Int, z: Int): List<Int> {
    val list = mutableListOf<Int>()
    for (y in 2..125) {
        val b = source.getBlock(x, y, z)
        if (b.type == Material.AIR && b.getRelative(BlockFace.UP).type == Material.AIR) {
            if (b.getRelative(BlockFace.DOWN).type == Material.NETHERRACK) {
                list.add(y)
            }
        }
    }
    // Couldn't find a safe place
    return list
}

private fun getOreType(random: Random): Material {
    return when (random.nextInt(100)) {
        in 0..29 -> Material.COAL_ORE
        in 30..49 -> Material.IRON_ORE
        in 50..69 -> Material.GOLD_ORE
        in 70..84 -> Material.BONE_BLOCK
        in 85..90 -> Material.LAPIS_ORE
        in 91..95 -> Material.REDSTONE_ORE
        else -> Material.DIAMOND_ORE
    }
}
*/