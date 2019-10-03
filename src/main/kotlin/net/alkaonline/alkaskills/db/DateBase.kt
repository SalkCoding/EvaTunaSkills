package net.alkaonline.alkaskills.db

import com.google.gson.Gson
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import net.alkaonline.alkaskills.PlayerInfo
import net.alkaonline.alkaskills.alkaSkills
import net.alkaonline.alkaskills.getPlayerInfo
import org.bukkit.configuration.file.FileConfiguration
import java.lang.Exception
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*
import java.util.concurrent.TimeUnit

private var instance: DateBase? = null

class DateBase {

    var hikari: HikariDataSource? = null

    fun createDataTable() {
        var con: Connection? = null
        try {
            con = instance?.hikari?.connection
            val prestat = con!!.prepareStatement("CREATE TABLE IF NOT EXISTS alkaskills_playerinfo (playerUUID TEXT(40) NOT NULL DEFAULT '', info JSON NULL DEFAULT NULL,PRIMARY KEY (playerUUID(40)));")
            prestat.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            try {
                con!!.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }

        }
    }

    fun hasData(uuid: UUID): Boolean {
        var con: Connection? = null
        var isNotEmpty = true
        try {
            con = hikari?.connection
            val prestat = con!!.prepareStatement("SELECT playerUUID FROM alkaskills_playerinfo WHERE playerUUID='$uuid'")

            val rs = prestat.executeQuery()
            isNotEmpty = rs != null && rs.next()
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            try {
                con!!.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }

        }
        return isNotEmpty
    }

    fun addData(uuid: UUID, info: PlayerInfo) {
        val gson = Gson()

        var con: Connection? = null
        try {
            con = hikari?.connection

            val prestat = con!!.prepareStatement("INSERT INTO alkaskills_playerinfo (playerUUID, info) VALUES ('$uuid','${gson.toJson(info)}')")

            prestat.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            try {
                con!!.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }

        }
    }

    fun updateData(uuid: UUID, info: PlayerInfo) {
        val gson = Gson()

        var con: Connection? = null
        try {
            con = hikari?.connection

            val prestat = con!!.prepareStatement("UPDATE alkaskills_playerinfo SET info = '${gson.toJson(info)}' WHERE playerUUID = '$uuid';")

            prestat.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            try {
                con!!.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }

    fun loadData(uuid: UUID): PlayerInfo {
        val gson = Gson()

        var con: Connection? = null
        lateinit var resultInfo: PlayerInfo
        try {
            con = hikari?.connection

            val prestat = con!!.prepareStatement("SELECT info FROM alkaskills_playerinfo WHERE playerUUID='$uuid'")

            val resultSet = prestat.executeQuery()
            if (resultSet.next()) {
                //System.out.println(resultSet.getString(1))
                resultInfo = gson.fromJson(resultSet.getString("info"), PlayerInfo::class.java)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            try {
                con!!.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
        return resultInfo
    }

}

fun getInstance(dbConfig: FileConfiguration): DateBase? {
    if (instance != null)
        return instance
    instance = DateBase()

    val hikariConfig = HikariConfig()
    hikariConfig.driverClassName = "com.mysql.jdbc.Driver"
    hikariConfig.jdbcUrl = "jdbc:mysql://${dbConfig.getString("DBIP")}:${dbConfig.getInt("Port")}/${dbConfig.getString("DBName")}?useUnicode=yes&characterEncoding=${dbConfig.getString("Encoding")}"
    hikariConfig.username = dbConfig.getString("UserName")
    hikariConfig.password = dbConfig.getString("Password")
    try {
        instance!!.hikari = HikariDataSource(hikariConfig)
    } catch (e: Exception){
        alkaSkills!!.logger.warning(e.message)
    }finally {
        if (instance!!.hikari != null) {
            //Registering table
            instance?.createDataTable()
        } else {
            //Setting local file reading mode
            instance = null
        }
    }
    return instance
}

fun closeInstance() {
    if (instance == null)
        return
    if (instance!!.hikari == null)
        return
    instance!!.hikari!!.close()
}