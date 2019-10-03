package net.alkaonline.alkaskills.command

/*class AddOrePopulatorCommand : CommandExecutor {

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): Boolean {
        if (!sender.isOp) {
            sender.sendMessage("관리자여야 합니다.")
            return true
        }

        when (args.size) {
            0 -> {
                if (sender !is Player) {
                    sender.sendMessage("Specify a world name!")
                    return true
                }

                val world = sender.world
                val populators = world.populators
                for (populator in populators) {
                    if (populator is OreBlockPopulator) {
                        sender.sendMessage("Already has OreBlockPopulator")
                        return true
                    }
                }
                world.populators.add(OreBlockPopulator())
                sender.sendMessage("Done!")
                return true
            }
            1 -> {
                val world = Bukkit.getWorld(args[0])
                if (world == null) {
                    sender.sendMessage("Unknown world name")
                    return true
                }
                world.populators.add(OreBlockPopulator())
                sender.sendMessage("Done!")
                return true
            }
            else -> {
                return false
            }
        }
    }

}
*/