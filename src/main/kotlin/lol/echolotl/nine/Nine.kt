package lol.echolotl.nine

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import org.slf4j.LoggerFactory

class Nine : ModInitializer {

    companion object {
        const val MOD_ID = "nine"
        val LOGGER = LoggerFactory.getLogger(MOD_ID)
    }

    override fun onInitialize() {
        LOGGER.info("ninemod ready!")

        CommandRegistrationCallback.EVENT.register { dispatcher, buildContext, selection ->
            lol.echolotl.nine.commands.NineCommand().register(dispatcher)
        }
    }
}
