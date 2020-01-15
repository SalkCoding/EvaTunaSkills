package net.alkaonline.alkaskills

import com.google.gson.Gson
import net.alkaonline.alkaskills.skillswitch.SwitchManager
import net.alkaonline.alkaskills.skillswitch.childswitch.ChopTreeSwitchManager
import net.alkaonline.alkaskills.skillswitch.childswitch.DoubleJumpSwitchManager
import net.alkaonline.alkaskills.skilltree.logging.chopTree
import net.alkaonline.alkaskills.skilltree.utility.doubleJump
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.*


class PlayerInfoManager(val playerInfoDirectory: Path) {

    private val playerInfoes = mutableMapOf<UUID, PlayerInfo>()
    private val gson = Gson()

    private val switchManagerMap = mutableMapOf<Skill, SwitchManager>()

    init {
        val chopTreeDirectory: Path = File(alkaSkills!!.dataFolder, "choptree").toPath()
        val doubleJumpDirectory: Path = File(alkaSkills!!.dataFolder, "doublejump").toPath()

        Files.createDirectories(chopTreeDirectory)
        Files.createDirectories(doubleJumpDirectory)

        switchManagerMap[chopTree] = ChopTreeSwitchManager(chopTreeDirectory)
        switchManagerMap[doubleJump] = DoubleJumpSwitchManager(doubleJumpDirectory)
    }

    fun loadInfo(id: UUID) {
        if (playerInfoes.containsKey(id)) {
            return
        }

        //Searching DB and loading info, if failed searching files
        /*val dataBase = dataBase
        if (dataBase != null) {
            if (dataBase.hasData(id)) {
                playerInfoes[id] = dataBase.loadData(id)
                return
            }
        }*/

        val playerInfoFile = playerInfoDirectory.resolve("$id.json")
        if (Files.exists(playerInfoFile)) {
            Files.newBufferedReader(playerInfoFile).use { bufferedReader ->
                playerInfoes[id] = gson.fromJson(bufferedReader, PlayerInfo::class.java)
            }
        } else {
            playerInfoes[id] = PlayerInfo()
        }

        for (switchManager in switchManagerMap) {
            switchManager.value.loadData(id)
        }
    }

    fun saveAndUnloadInfo(id: UUID) {
        if (!playerInfoes.containsKey(id)) {
            return
        }

        /*val dataBase = dataBase
        if (dataBase != null) {
            if (dataBase.hasData(id)) {
                dataBase.updateData(id, playerInfoes[id]!!)
            } else {
                dataBase.addData(id, playerInfoes[id]!!)
            }
            playerInfoes.remove(id)
            return
        }*/

        for (switchManager in switchManagerMap) {
            switchManager.value.saveAndUnloadData(id)
        }

        val playerInfoFile = playerInfoDirectory.resolve("$id.json")
        Files.newBufferedWriter(playerInfoFile).use { bufferedWriter ->
            gson.toJson(playerInfoes[id], bufferedWriter)
        }

        playerInfoes.remove(id)
    }

    fun saveInfo(id: UUID) {
        if (!playerInfoes.containsKey(id)) {
            return
        }

        for (switchManager in switchManagerMap) {
            switchManager.value.saveData(id)
        }

        /*val dataBase = dataBase
        if (dataBase != null) {
            if (dataBase.hasData(id)) {
                dataBase.updateData(id, playerInfoes[id]!!)
            } else {
                dataBase.addData(id, playerInfoes[id]!!)
            }
            return
        }*/

        val playerInfoFile = playerInfoDirectory.resolve("$id.json")
        Files.newBufferedWriter(playerInfoFile).use { bufferedWriter ->
            gson.toJson(playerInfoes[id], bufferedWriter)
        }
    }

    fun loadOnlinePlayersInfo() {
        Bukkit.getOnlinePlayers().forEach { onlinePlayer ->
            loadInfo(onlinePlayer.uniqueId)
            loadSwitchData(chopTree, onlinePlayer.uniqueId)
            loadSwitchData(doubleJump, onlinePlayer.uniqueId)
        }
    }

    fun saveAndUnloadAllInfo() {
        for (id in playerInfoes.keys.toList()) {
            for (switchManager in switchManagerMap) {
                switchManager.value.saveAndUnloadData(id)
            }
            saveAndUnloadInfo(id)
        }
    }

    fun saveAllInfo() {
        for (id in playerInfoes.keys.toList()) {
            for (switchManager in switchManagerMap) {
                switchManager.value.saveData(id)
            }
            saveInfo(id)
        }
    }

    fun getPlayerInfo(id: UUID): PlayerInfo {
        if (!playerInfoes.containsKey(id)) {
            loadInfo(id)
        }

        return playerInfoes[id]!!
    }

    fun setSwitch(skill: Skill, player: Player, set: Boolean) {
        switchManagerMap[skill]?.setSwitch(player, set)
    }

    fun isSwitchOn(skill: Skill, player: Player): Boolean {
        return switchManagerMap[skill]?.isSwitchOn(player)!!
    }

    fun saveSwitchData(skill: Skill, id: UUID) {
        switchManagerMap[skill]?.saveData(id)
    }

    fun loadSwitchData(skill: Skill, id: UUID) {
        switchManagerMap[skill]?.loadData(id)
    }

}

fun Player.getInfo(): PlayerInfo {
    return alkaSkills!!.playerInfoManager.getPlayerInfo(this.uniqueId)
}

fun UUID.getPlayerInfo(): PlayerInfo {
    return alkaSkills!!.playerInfoManager.getPlayerInfo(this)
}

fun Player.setSwitch(skill: Skill, set: Boolean) {
    this.playSound(this.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
    alkaSkills!!.playerInfoManager.setSwitch(skill, this, set)
}

fun Player.isSwitchOn(skill: Skill): Boolean {
    return alkaSkills!!.playerInfoManager.isSwitchOn(skill, this)
}