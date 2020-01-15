package net.alkaonline.alkaskills.command

import net.alkaonline.alkaskills.getInfo
import net.alkaonline.alkaskills.util.errorFormat
import net.alkaonline.alkaskills.util.infoFormat
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import kotlin.math.pow
import kotlin.math.roundToInt

class PlayerInfoCommand : CommandExecutor {

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
            val target = Bukkit.getPlayer(args[0])
            if (target != null) {
                for (i in 1..50) {
                    /*if (i <= 20)
                        System.out.println("level $i " + (1.25.pow(i.toDouble()) * 60).toInt() + " | " + (1.25.pow(i.toDouble()) * 50).toInt())
                    else if (i <= 20)
                        System.out.println("level $i " + (1.25.pow(i.toDouble()) * 60).toInt() + " | " + (1.25.pow(i.toDouble()) * 20).toInt())
                    else if (i <= 35)
                        System.out.println("level $i " + (1.25.pow(i.toDouble()) * 60).toInt() + " | " + (1.25.pow(i.toDouble()) * 10).toInt())
                    else if (i <= 50)
                        System.out.println("level $i " + (1.25.pow(i.toDouble()) * 60).toInt() + " | " + (1.25.pow(i.toDouble()) * 5).toInt())
                    else {
                        twlevel *= 1.1
                        System.out.println("level $i " + (1.25.pow(i.toDouble()) * 60).toInt() + " | " + twlevel.toInt())*/
                }
                sender.sendMessage(target.name.infoFormat())
                sender.sendMessage(" - 레벨 : ${target.getInfo().level}".infoFormat())
                sender.sendMessage(" - 경험치 : ${target.getInfo().exp.roundToInt()} / ${target.getInfo().getNeededExpToLevelUp()}".infoFormat())
                sender.sendMessage(" - 남은 스킬 포인트 : ${target.getInfo().getPointsLeft()}".infoFormat())
                sender.sendMessage(" - 스킬 포인트 총합 : ${target.getInfo().getSumOfPoints()}".infoFormat())
            } else {
                sender.sendMessage("존재하지 않는 플레이어입니다.".errorFormat())
            }
            return true
        }
        return true
    }
}