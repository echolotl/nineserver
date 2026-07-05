package lol.echolotl.nine.util

import com.mojang.serialization.JsonOps
import lol.echolotl.nine.Nine
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.minecraft.network.chat.ComponentSerialization

class MiniMessageUtil {
    companion object {
        private val mm = net.kyori.adventure.text.minimessage.MiniMessage.miniMessage()
        fun deserialize(input: String): net.minecraft.network.chat.Component {
            val miniMessageComponent = mm.deserialize(input)
            Nine.LOGGER.debug(miniMessageComponent.toString())
            return convertComponents(miniMessageComponent)
        }

        fun convertComponents(input: Component): net.minecraft.network.chat.Component {
            return ComponentSerialization.CODEC
                .parse(JsonOps.INSTANCE, GsonComponentSerializer.gson().serializeToTree(input))
                .result()
                .orElse(net.minecraft.network.chat.Component.literal(""))
        }

        fun applyToComponent(input: net.minecraft.network.chat.Component): net.minecraft.network.chat.Component {
            // Reparse the component's plain text as MiniMessage to pick up any tags contained within it
            val plainText = input.string
            val deserialized = deserialize(plainText)

            val mergedStyle = deserialized.style.applyTo(input.style)

            return deserialized.copy().withStyle(mergedStyle)
        }
    }
}