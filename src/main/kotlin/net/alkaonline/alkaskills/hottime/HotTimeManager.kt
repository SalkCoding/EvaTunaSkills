package net.alkaonline.alkaskills.hottime

import com.google.gson.Gson
import net.alkaonline.alkaskills.alkaSkills
import java.nio.file.Files
import java.nio.file.Path

var isHotTime = false
var hotTimeValue = 1.0

private val hotTimeDirectory: Path = alkaSkills!!.dataFolder.toPath()
private val hotTimeFile = hotTimeDirectory.resolve("HotTimeData.json")

class HotTimeManager {

    fun saveData() {
        val gson = Gson()

        Files.createDirectories(hotTimeDirectory)

        Files.newBufferedWriter(hotTimeFile).use { bufferedWriter ->
            gson.toJson(hotTimeValue, bufferedWriter)
        }
    }

    fun loadData() {
        val gson = Gson()

        Files.createDirectories(hotTimeDirectory)

        if (Files.exists(hotTimeFile)) {
            Files.newBufferedReader(hotTimeFile).use { bufferedReader ->
                hotTimeValue = gson.fromJson(bufferedReader, Double::class.java)
                if (hotTimeValue > 1)
                    isHotTime = true
            }
        } else {
            hotTimeValue = 1.0
            isHotTime = false
        }
    }
}