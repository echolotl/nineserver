package lol.echolotl.nine.util

import com.mojang.serialization.JsonOps
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.minecraft.network.chat.ComponentSerialization

class MiniMessageUtil {
    companion object {
        private val mm = net.kyori.adventure.text.minimessage.MiniMessage.miniMessage()
        fun deserialize(input: String): net.minecraft.network.chat.Component {
            val miniMessageComponent = mm.deserialize(input)
            return convertComponents(miniMessageComponent)
        }
        fun convertComponents(input: Component): net.minecraft.network.chat.Component {
            return ComponentSerialization.CODEC
                .parse(JsonOps.INSTANCE, GsonComponentSerializer.gson().serializeToTree(input))
                .result()
                .orElse(net.minecraft.network.chat.Component.literal(""))
        }
    }
}