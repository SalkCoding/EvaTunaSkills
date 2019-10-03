package net.alkaonline.alkaskills.command;

import net.alkaonline.alkaskills.bossbar.refreshPlayerBar
import net.alkaonline.alkaskills.hottime.hotTimeValue
import net.alkaonline.alkaskills.hottime.isHotTime
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class HotTimeCommand : CommandExecutor {

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

        if (args.size == 1) {
            isHotTime = true
            hotTimeValue = args[0].toDouble()
            if(hotTimeValue <= 1) isHotTime = false
            else sender.sendMessage("$hotTimeValue 배 핫타임이 설정되었습니다.")
            for(p in Bukkit.getOnlinePlayers())
                refreshPlayerBar(p)
            return true
        }
        return false
    }
}
