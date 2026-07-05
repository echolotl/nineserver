package lol.echolotl.nine.compat

import net.fabricmc.loader.api.FabricLoader
import org.objectweb.asm.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo

class CompatMixins : IMixinConfigPlugin {
    override fun onLoad(mixinPackage: String?) {
        TODO("Not yet implemented")
    }

    override fun getRefMapperConfig(): String? {
        TODO("Not yet implemented")
    }

    override fun shouldApplyMixin(targetClassName: String?, mixinClassName: String?): Boolean {
        return when (mixinClassName) {
            "lol.echolotl.nine.compat.discordmcchat.mixin.MixinChatMessage" -> {
                FabricLoader.getInstance().isModLoaded("discord_mc_chat")
            }

            else -> true
        }
    }

    override fun acceptTargets(
        myTargets: Set<String?>?,
        otherTargets: Set<String?>?
    ) {
        TODO("Not yet implemented")
    }

    override fun getMixins(): List<String?>? {
        TODO("Not yet implemented")
    }

    override fun preApply(
        targetClassName: String?,
        targetClass: ClassNode?,
        mixinClassName: String?,
        mixinInfo: IMixinInfo?
    ) {
        TODO("Not yet implemented")
    }

    override fun postApply(
        targetClassName: String?,
        targetClass: ClassNode?,
        mixinClassName: String?,
        mixinInfo: IMixinInfo?
    ) {
        TODO("Not yet implemented")
    }
}