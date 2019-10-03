package net.alkaonline.alkaskills.command

import net.alkaonline.alkaskills.alkaSkills
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class SaveInfoCommand : CommandExecutor {

    override fun onCommand(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<String>
    ): Boolean {
        if (args.isEmpty()) {
            for (player in Bukkit.getOnlinePlayers()) {
                alkaSkills!!.playerInfoManager.saveInfo(player.uniqueId)
            }
            sender.sendMessage("Saved all of data")
            return true
        } else if (args.size == 1) {
            val player = Bukkit.getPlayer(args[0])
            if (player != null) {
                alkaSkills!!.playerInfoManager.saveInfo(player.uniqueId)
            }else sender.sendMessage("Please enter the correct name of  player")
            sender.sendMessage("Saved data")
            return true
        }
        return false
    }

}