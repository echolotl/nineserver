package lol.echolotl.nine.mixin;

import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import lol.echolotl.nine.util.MiniMessageUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class DefaultChatMessageMixin {
    @ModifyVariable(method = "broadcastChatMessage", at = @At("HEAD"), argsOnly = true)
    private static PlayerChatMessage nineMiniMessage(PlayerChatMessage message) {
        return message.withUnsignedContent(MiniMessageUtil.Companion.deserialize(message.decoratedContent().getString()));
    }
}
