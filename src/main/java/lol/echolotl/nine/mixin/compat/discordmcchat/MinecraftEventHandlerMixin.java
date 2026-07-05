package lol.echolotl.nine.mixin.compat.discordmcchat;

import com.xujiayao.discord_mc_chat.network.message.TextSegment;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import lol.echolotl.nine.compat.discordmcchat.util.TextSegmentConverter;

import java.util.ArrayList;
import java.util.List;


@Mixin(targets = "com.xujiayao.discord_mc_chat.minecraft.events.MinecraftEventHandler")
public abstract class MinecraftEventHandlerMixin {

    @ModifyVariable(method = "buildComponentFromSegments", at = @At("HEAD"), argsOnly = true, require = 0)
    private static List<TextSegment> nine$convertDiscordMessage(List<TextSegment> segments) {
        List<TextSegment> converted = new ArrayList<>(segments.size());

        for (TextSegment segment : segments) {
            Component component = TextSegmentConverter.convertToComponent(segment);
            converted.addAll(TextSegmentConverter.convertFromComponent(component));
        }

        return converted;
    }
}

