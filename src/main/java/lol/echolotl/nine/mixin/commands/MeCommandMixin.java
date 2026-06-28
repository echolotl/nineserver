package lol.echolotl.nine.mixin.commands;

import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.commands.EmoteCommands;
import lol.echolotl.nine.util.MiniMessageUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EmoteCommands.class)
public abstract class MeCommandMixin {
    @ModifyVariable(method="lambda$register$1", at=@At("HEAD"), argsOnly=true)
    private static PlayerChatMessage nine$formatMe(PlayerChatMessage message) {
        return message.withUnsignedContent(MiniMessageUtil.Companion.deserialize(message.decoratedContent().getString()));
    }
}
