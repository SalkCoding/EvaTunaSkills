package net.alkaonline.alkaskills.command

import net.alkaonline.alkaskills.bossbar.refreshPlayerBar
import net.alkaonline.alkaskills.getInfo
import net.alkaonline.alkaskills.util.refreshLevelOnPlayerList
import net.alkaonline.alkaskills.util.refreshMaxHealth
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SetLevelCommand : CommandExecutor {

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

        if (args.size == 2) {
            val target: Player? = Bukkit.getPlayer(args[0])
            val level = args[1].toIntOrNull() ?: return false

            if (target == null) return false
            else {
                target.getInfo().level = level
                target.getInfo().exp = 0.0

                target.run { getInfo().skills.clear() }
                target.healthScale = 20.0
                target.isHealthScaled = true
                target.getInfo().getNeededExpToLevelUp()
                refreshPlayerBar(target)
                target.refreshLevelOnPlayerList()
                target.refreshMaxHealth()
            }
            return true
        }
        return false
    }

}