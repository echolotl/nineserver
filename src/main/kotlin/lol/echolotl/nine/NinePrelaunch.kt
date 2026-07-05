package lol.echolotl.nine

import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint
import org.spongepowered.asm.mixin.Mixins

class NinePrelaunch : PreLaunchEntrypoint {
    override fun onPreLaunch() {
        if (FabricLoader.getInstance().isModLoaded("discord_mc_chat")) {
            Mixins.addConfiguration("nine.discordmcchat.mixins.json")
            Nine.LOGGER.info("DMCC is detected, enabling mixins!")
        }
    }
}