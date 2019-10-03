package net.alkaonline.alkaskills

import com.google.gson.Gson
import com.google.gson.JsonArray
import org.bukkit.entity.Player
import java.io.File
import java.nio.file.Files
import java.util.*

private val file = File(alkaSkills!!.dataFolder, "ChopTree.json")

class ChopTreeSwitchManager {
    private val switchMap = mutableSetOf<UUID>()
    private val gson = Gson()

    fun setChopTree(player: Player, set: Boolean) {
        if (set) {
            if (!isChopTreeOn(player))
                switchMap.add(player.uniqueId)
        } else {
            switchMap.remove(player.uniqueId)
        }
    }

    fun isChopTreeOn(player: Player): Boolean {
        return switchMap.contains(player.uniqueId)
    }

    fun saveChopTreeData() {
        if (!file.exists())
            file.createNewFile()

        val jsonArray = JsonArray()
        for (uuid in switchMap) jsonArray.add(uuid.toString())

        Files.newBufferedWriter(file.toPath()).use { bufferedWriter ->
            gson.toJson(jsonArray, bufferedWriter)
        }
    }

    fun loadChopTreeData() {
        if (!file.exists())
            return

        var jsonArray = JsonArray()
        Files.newBufferedReader(file.toPath()).use { bufferedReader ->
            jsonArray = gson.fromJson(bufferedReader, JsonArray::class.java)
        }
        for (element in jsonArray) {
            switchMap.add(UUID.fromString(element.asString))
        }
    }
}