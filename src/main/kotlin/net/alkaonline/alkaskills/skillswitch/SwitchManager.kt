package net.alkaonline.alkaskills.skillswitch

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.bukkit.entity.Player
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

open class SwitchManager(private val directory: Path) {
    private val switchMap = mutableSetOf<UUID>()
    private val gson = Gson()

    fun setSwitch(player: Player, set: Boolean) {
        if (set) {
            switchMap.add(player.uniqueId)
        } else {
            switchMap.remove(player.uniqueId)
        }
    }

    fun isSwitchOn(player: Player): Boolean {
        return switchMap.contains(player.uniqueId)
    }

    open fun saveAndUnloadData(id: UUID) {
        val file = directory.resolve("$id.json")
        Files.newBufferedWriter(file).use { bufferedWriter ->
            val json = JsonObject()
            json.addProperty("isActive", switchMap.contains(id))
            gson.toJson(json, bufferedWriter)
        }
        switchMap.remove(id)
    }

    open fun saveData(id: UUID) {
        val file = directory.resolve("$id.json")
        Files.newBufferedWriter(file).use { bufferedWriter ->
            val json = JsonObject()
            json.addProperty("isActive", switchMap.contains(id))
            gson.toJson(json, bufferedWriter)
        }
    }

    open fun loadData(id: UUID) {
        val file = directory.resolve("$id.json")
        if (Files.exists(file)) {
            Files.newBufferedReader(file).use { bufferedReader ->

                val isActive = JsonParser().parse(bufferedReader).asJsonObject["isActive"].asBoolean
                if (isActive) switchMap.add(id)
            }
        }
    }

}