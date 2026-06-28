package lol.echolotl.nine.mixin.commands;

import lol.echolotl.nine.util.MiniMessageUtil;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.commands.TeamMsgCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TeamMsgCommand.class)
public abstract class TeamMsgCommandMixin {
    @ModifyVariable(method="sendMessage", at=@At("HEAD"), argsOnly=true, name = "message")
    private static PlayerChatMessage nine$formatTeamMsg(PlayerChatMessage message) {
        return message.withUnsignedContent(MiniMessageUtil.Companion.deserialize(message.decoratedContent().getString()));
    }
}
