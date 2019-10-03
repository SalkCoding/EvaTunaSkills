package net.alkaonline.alkaskills.command

import net.alkaonline.alkaskills.getInfo
import net.alkaonline.alkaskills.giveAlkaExp
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GivePercentageExpCommand : CommandExecutor {

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

        if (args.size == 1 && sender is Player) {
            val percentage = args[0].toDoubleOrNull() ?: return false

            sender.giveAlkaExp(sender.getInfo().getNeededExpToLevelUp() * percentage / 100, true)
            return true
        } else if (args.size == 2) {
            val percentage = args[1].toDoubleOrNull() ?: return false
            val player = Bukkit.getPlayer(args[0])
            if (player == null || !player.isOnline) return false

            player.giveAlkaExp(player.getInfo().getNeededExpToLevelUp() * percentage / 100, true)
            return true
        } else {
            return false
        }
    }

}
