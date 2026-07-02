package lol.echolotl.nine.motd

import lol.echolotl.nine.Nine
import lol.echolotl.nine.util.MiniMessageUtil
import net.minecraft.network.protocol.status.ServerStatus
import net.minecraft.server.MinecraftServer
import java.nio.file.Files
import java.util.Optional

object NineMOTD {

    private val configFile get() = Nine.CONFIG_PATH.resolve("motd.yml")
    private val iconsDir get() = Nine.CONFIG_PATH.resolve("icons")

    var server: MinecraftServer? = null
    private var config: NineMOTDConfig = NineMOTDConfig.DEFAULT
    private var favicons: Map<String, ServerStatus.Favicon> = emptyMap()

    private fun loadConfig() {
        try {
            if (Files.notExists(configFile)) {
                Files.createDirectories(Nine.CONFIG_PATH)
                Files.writeString(configFile, NineMOTDConfig.DEFAULT_CONFIG)
            }
            config = NineMOTDConfig.parse(Files.readString(configFile))
        } catch (ex: Exception) {
            Nine.LOGGER.error("Failed to load ${configFile.fileName}, falling back to defaults", ex)
            config = NineMOTDConfig.DEFAULT
        }
    }

    private fun loadFavicons() {
        val cache = HashMap<String, ServerStatus.Favicon>()
        val iconNames = config.motds.mapNotNull { it.icon }.distinct()

        for (iconName in iconNames) {
            val iconPath = iconsDir.resolve(iconName)
            if (Files.notExists(iconPath)) continue

            try {
                cache[iconName] = ServerStatus.Favicon(Files.readAllBytes(iconPath))
            } catch (ex: Exception) {
                Nine.LOGGER.error("Failed to load MOTD icon '$iconName'", ex)
            }
        }

        favicons = cache
    }

    fun init(server: MinecraftServer) {
        this.server = server
        reload()
    }

    fun destroy() {
        this.server = null
    }

    fun reload() {
        loadConfig()
        loadFavicons()
        Nine.LOGGER.info("MOTD module reloaded")
    }

    @JvmStatic
    fun server(): MinecraftServer? {
        return server
    }

    @JvmStatic
    fun applyToStatus(vanilla: ServerStatus): ServerStatus {
        if (!config.enabled) return vanilla

        val mutable = MutableServerStatus(vanilla)
        val motd = config.random()
        val (line1, line2) = config.render(motd)
        mutable.description = MiniMessageUtil.deserialize("$line1\n$line2")
        motd.icon?.let { favicons[it] }?.let { mutable.favicon = Optional.of(it) }
        return mutable.toServerStatus()
    }
}