package lol.echolotl.nine.mixin.commands;

import net.minecraft.network.chat.PlayerChatMessage;

import net.minecraft.server.commands.MsgCommand;
import lol.echolotl.nine.util.MiniMessageUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MsgCommand.class)
public abstract class MsgCommandMixin {
    @ModifyVariable(method="sendMessage", at=@At("HEAD"), argsOnly=true, name = "message")
    private static PlayerChatMessage nine$formatMsg(PlayerChatMessage message) {
        return message.withUnsignedContent(MiniMessageUtil.Companion.deserialize(message.decoratedContent().getString()));
    }
}
