package net.alkaonline.alkaskills

import ch.njol.skript.Skript
import ch.njol.skript.SkriptAPIException
import codecrafter47.bungeetablistplus.api.bukkit.BungeeTabListPlusBukkitAPI
import net.alkaonline.alkaskills.bossbar.addPlayerBar
import net.alkaonline.alkaskills.bossbar.deletePlayerBar
import net.alkaonline.alkaskills.command.*
import net.alkaonline.alkaskills.db.DateBase
import net.alkaonline.alkaskills.db.closeInstance
import net.alkaonline.alkaskills.db.getInstance
import net.alkaonline.alkaskills.hottime.HotTimeManager
import net.alkaonline.alkaskills.listener.*
import net.alkaonline.alkaskills.placeholder.LevelPlaceHolder
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.InventoryView
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.*


var alkaSkills: AlkaSkills? = null
var dataBase: DateBase? = null

class AlkaSkills : JavaPlugin() {

    lateinit var playerInfoManager: PlayerInfoManager
    var hotTimeManager = HotTimeManager()

    val openGuis = WeakHashMap<InventoryView, Gui>()
    var task: BukkitTask? = null

    override fun onEnable() {
        alkaSkills = this

        config.options().copyDefaults(true)
        saveConfig()

        dataBase = getInstance(config)
        if (dataBase == null) {
            logger.warning("DB 연결 실패!")
            logger.warning("로컬 파일로 데이터를 읽기 쓰기합니다!")
        }

        /*server.worlds
                .filter { world -> world.environment == World.Environment.NETHER }
                .filter { world -> !world.populators.any { populator -> populator is OreBlockPopulator } }
                .forEach { world -> world.populators.add(OreBlockPopulator()) }*/

        val playerInfoDirectory: Path = dataFolder.toPath()
        Files.createDirectories(playerInfoDirectory)
        playerInfoManager = PlayerInfoManager(playerInfoDirectory)
        playerInfoManager.loadOnlinePlayersInfo()

        hotTimeManager.loadData()

        server.pluginManager.registerEvents(PlayerListener(), this)
        server.pluginManager.registerEvents(GuiListener(), this)
        server.pluginManager.registerEvents(FarmingListener(), this)
        server.pluginManager.registerEvents(MiningListener(), this)
        if (server.pluginManager.isPluginEnabled("WorldGuard")) {
            val combatListener = CombatListener()
            combatListener.worldGuardRegister()
            server.pluginManager.registerEvents(combatListener, this)
        } else
            logger.warning("Plugin can't found WorldGuard plugin")
        server.pluginManager.registerEvents(ChatListener(), this)
        //server.pluginManager.registerEvents(ChunkListener(), this)
        server.pluginManager.registerEvents(BossBarListener(), this)
        server.pluginManager.registerEvents(FishingListener(), this)
        server.pluginManager.registerEvents(LoggingListener(), this)


        getCommand("skills")?.setExecutor(SkillCommand())
        getCommand("giveexp")?.setExecutor(GiveExpCommand())
        //getCommand("addorepopulator")?.setExecutor(AddOrePopulatorCommand())
        getCommand("clearskills")?.setExecutor(ClearSkillsCommand())
        getCommand("setlevel")?.setExecutor(SetLevelCommand())
        getCommand("playerinfo")?.setExecutor(PlayerInfoCommand())
        getCommand("hottime")?.setExecutor(HotTimeCommand())
        getCommand("saveinfo")?.setExecutor(SaveInfoCommand())
        getCommand("givepercentageexp")?.setExecutor(GivePercentageExpCommand())

        if (server.pluginManager.isPluginEnabled("Skript")) {
            try {
                val addon = Skript.registerAddon(this)
                try {
                    //This will register all our syntax for us. Explained below
                    addon.loadClasses("net.alkaonline.alkaskills", "skript")
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } catch (e: SkriptAPIException) {
                logger.warning("SkriptAPIException : Skript expression register failed")
            }
        }

        //For plugin reload
        for (player: Player in Bukkit.getOnlinePlayers()) addPlayerBar(player)

        playerInfoManager.loadChopTreeData()

        task = Bukkit.getScheduler().runTaskTimer(
                this,
                Runnable {
                    logger.info("Player data auto saved")
                    for (player in Bukkit.getOnlinePlayers()) {
                        playerInfoManager.saveInfo(player.uniqueId)
                    }
                },
                360000,
                360000
        )

        BungeeTabListPlusBukkitAPI.registerVariable(this, LevelPlaceHolder())
    }

    override fun onDisable() {
        task?.cancel()
        playerInfoManager.saveAndUnloadAllInfo()
        playerInfoManager.saveChopTreeData()

        hotTimeManager.saveData()

        for (player: Player in Bukkit.getOnlinePlayers()) deletePlayerBar(player)

        BungeeTabListPlusBukkitAPI.unregisterVariables(this)

        //close DB
        closeInstance()

        if (alkaSkills == this) {
            alkaSkills = null
        }
    }
}
