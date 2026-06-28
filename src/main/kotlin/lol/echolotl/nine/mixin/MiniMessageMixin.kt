package lol.echolotl.nine.mixin

import net.minecraft.network.chat.PlayerChatMessage
import net.minecraft.server.network.ServerGamePacketListenerImpl
import lol.echolotl.nine.util.MiniMessageUtil
import net.kyori.adventure.text.minimessage.MiniMessage
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.ModifyVariable

@Mixin(ServerGamePacketListenerImpl::class)
abstract class MiniMessageMixin {
    @ModifyVariable(
        method = ["broadcastChatMessage"],
        at = At("HEAD"),
        argsOnly = true
    )
    private fun nineMiniMessage(message: PlayerChatMessage): PlayerChatMessage {
        return message.withUnsignedContent(MiniMessageUtil.deserialize(message.decoratedContent().string))
    }
}