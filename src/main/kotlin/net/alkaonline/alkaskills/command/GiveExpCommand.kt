package net.alkaonline.alkaskills.command

import net.alkaonline.alkaskills.giveAlkaExp
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.max
import kotlin.math.min

class GiveExpCommand : CommandExecutor {

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
            val exp = args[0].toDoubleOrNull() ?: return false

            sender.giveAlkaExp(exp, true)
            return true
        } else if (args.size == 2) {
            val exp = args[1].toDoubleOrNull() ?: return false

            Bukkit.getPlayer(args[0])?.giveAlkaExp(exp , true)
            return true
        } else if (args.size == 3) {
            val num1 = args[1].toDoubleOrNull() ?: return false
            val num2 = args[2].toDoubleOrNull() ?: return false

            Bukkit.getPlayer(args[0])?.giveAlkaExp(ThreadLocalRandom.current().nextDouble(max(num1, num2) - min(num1, num2), num1 + num2), true)
            return true
        } else {
            return false
        }
    }

}
