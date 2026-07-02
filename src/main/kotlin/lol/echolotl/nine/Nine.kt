package lol.echolotl.nine

import lol.echolotl.nine.motd.NineMOTD
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import org.slf4j.LoggerFactory
import net.fabricmc.loader.api.FabricLoader
import java.nio.file.Path

class Nine : ModInitializer {

    companion object {
        const val MOD_ID = "nine"
        val LOGGER: org.slf4j.Logger = LoggerFactory.getLogger(MOD_ID)
        val CONFIG_PATH: Path = FabricLoader.getInstance().configDir.resolve(MOD_ID)
    }

    override fun onInitialize() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            lol.echolotl.nine.commands.NineCommand().register(dispatcher)
        }
        ServerLifecycleEvents.SERVER_STARTED.register { server ->
            NineMOTD.init(server)
        }
        ServerLifecycleEvents.SERVER_STOPPED.register {
            NineMOTD.destroy()
        }
        LOGGER.info("kid named ready")
    }
}


