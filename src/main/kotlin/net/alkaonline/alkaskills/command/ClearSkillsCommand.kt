package net.alkaonline.alkaskills.command;

import net.alkaonline.alkaskills.getInfo
import net.alkaonline.alkaskills.util.refreshLevelOnPlayerList
import net.alkaonline.alkaskills.util.refreshMaxHealth
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ClearSkillsCommand : CommandExecutor {

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
            val target: Player? = Bukkit.getPlayer(args[0])
            if (target == null) return false
            else {
                target.sendTitle("", "${ChatColor.GREEN}스킬이 초기화되었습니다.", 10, 40, 10)
                target.run { getInfo().skills.clear() }
                target.refreshMaxHealth()
            }
            return true
        }
        return false
    }

}
