package net.alkaonline.alkaskills.command

import net.alkaonline.alkaskills.listener.loadingSet
import net.alkaonline.alkaskills.skilltree.openSkillGui
import net.alkaonline.alkaskills.util.warnFormat
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SkillCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("플레이어만 사용할 수 있습니다.")
            return true
        }
        if(loadingSet.contains(sender.uniqueId)){
            sender.sendMessage("데이터 로딩중입니다!".warnFormat())
            return true
        }
        sender.openSkillGui()
        return true
    }

}
