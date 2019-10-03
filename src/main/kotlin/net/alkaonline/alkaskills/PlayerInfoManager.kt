package net.alkaonline.alkaskills

import com.google.gson.Gson
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.nio.file.Files
import java.nio.file.Path
import java.util.*


class PlayerInfoManager(val playerInfoDirectory: Path) {

    private val playerInfoes = mutableMapOf<UUID, PlayerInfo>()
    private val gson = Gson()
    private val switchManager = ChopTreeSwitchManager()

    fun loadInfo(id: UUID) {
        if (playerInfoes.containsKey(id)) {
            return
        }

        //Searching DB and loading info, if failed searching files
        val dataBase = dataBase
        if (dataBase != null) {
            if (dataBase.hasData(id)) {
                playerInfoes[id] = dataBase.loadData(id)
                return
            }
        }

        val playerInfoFile = playerInfoDirectory.resolve("$id.json")
        if (Files.exists(playerInfoFile)) {
            Files.newBufferedReader(playerInfoFile).use { bufferedReader ->
                playerInfoes[id] = gson.fromJson(bufferedReader, PlayerInfo::class.java)
            }
        } else {
            playerInfoes[id] = PlayerInfo()
        }
    }

    fun saveAndUnloadInfo(id: UUID) {

        if (!playerInfoes.containsKey(id)) {
            return
        }

        val dataBase = dataBase
        if (dataBase != null) {
            if (dataBase.hasData(id)) {
                dataBase.updateData(id, playerInfoes[id]!!)
            } else {
                dataBase.addData(id, playerInfoes[id]!!)
            }
            playerInfoes.remove(id)
            return
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

        val dataBase = dataBase
        if (dataBase != null) {
            if (dataBase.hasData(id)) {
                dataBase.updateData(id, playerInfoes[id]!!)
            } else {
                dataBase.addData(id, playerInfoes[id]!!)
            }
            return
        }

        val playerInfoFile = playerInfoDirectory.resolve("$id.json")
        Files.newBufferedWriter(playerInfoFile).use { bufferedWriter ->
            gson.toJson(playerInfoes[id], bufferedWriter)
        }
    }

    fun loadOnlinePlayersInfo() {
        Bukkit.getOnlinePlayers().forEach { onlinePlayer ->
            loadInfo(onlinePlayer.uniqueId)
        }
    }

    fun saveAndUnloadAllInfo() {
        for (id in playerInfoes.keys.toList()) {
            saveAndUnloadInfo(id)
        }
    }

    fun getPlayerInfo(id: UUID): PlayerInfo {
        if (!playerInfoes.containsKey(id)) {
            loadInfo(id)
        }

        return playerInfoes[id]!!
    }

    fun setChopTree(player: Player, set: Boolean) {
        switchManager.setChopTree(player, set)
    }

    fun isChopTreeOn(player: Player): Boolean {
        return switchManager.isChopTreeOn(player)
    }

    fun saveChopTreeData() {
        switchManager.saveChopTreeData()
    }

    fun loadChopTreeData() {
        switchManager.loadChopTreeData()
    }

}

fun Player.getInfo(): PlayerInfo {
    return alkaSkills!!.playerInfoManager.getPlayerInfo(this.uniqueId)
}

fun UUID.getPlayerInfo(): PlayerInfo {
    return alkaSkills!!.playerInfoManager.getPlayerInfo(this)
}

fun Player.setChopTree(set: Boolean) {
    alkaSkills!!.playerInfoManager.setChopTree(this, set)
}

fun Player.isChopTreeOn(): Boolean {
    return alkaSkills!!.playerInfoManager.isChopTreeOn(this)
}