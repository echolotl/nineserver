package lol.echolotl.nine.compat.discordmcchat.util

import com.xujiayao.discord_mc_chat.network.message.TextSegment
import lol.echolotl.nine.util.MiniMessageUtil
import net.minecraft.network.chat.*
import java.net.URI
import java.util.Optional
import kotlin.reflect.KClass

object TextSegmentConverter {
    // This has to be initialized as a safety measure so we don't try and access a non-existant class
    private lateinit var textSegmentClass: KClass<TextSegment>
    private const val NOT_REGISTERED_ERROR_MESSAGE =
        "TextSegmentConverter is not registered. Call `TextSegmentConverter.register()` before using it."

    fun register() {
        textSegmentClass = TextSegment::class
    }

    @JvmStatic
    fun convertToComponent(textSegment: TextSegment): Component {
        if (::textSegmentClass.isInitialized) {
            var style = Style.EMPTY

            if (!textSegment.color.isNullOrEmpty()) {
                val parsedColor = TextColor.parseColor(textSegment.color).result().orElse(null)
                if (parsedColor != null) {
                    style = style.withColor(parsedColor)
                }
            }

            if (textSegment.bold) style = style.withBold(true)
            if (textSegment.italic) style = style.withItalic(true)
            if (textSegment.underlined) style = style.withUnderlined(true)
            if (textSegment.strikethrough) style = style.withStrikethrough(true)
            if (textSegment.obfuscated) style = style.withObfuscated(true)

            if (!textSegment.clickUrl.isNullOrEmpty()) {
                try {
                    style = style.withClickEvent(ClickEvent.OpenUrl(URI.create(textSegment.clickUrl)))
                } catch (_: Exception) {
                }
            }

            if (!textSegment.hoverText.isNullOrEmpty()) {
                style = style.withHoverEvent(HoverEvent.ShowText(Component.literal(textSegment.hoverText)))
            }

            val component = Component.literal(textSegment.text ?: "").withStyle(style)
            return MiniMessageUtil.applyToComponent(component)
        } else {
            throw IllegalStateException(NOT_REGISTERED_ERROR_MESSAGE)
        }
    }

    @JvmStatic
    fun convertFromComponent(component: Component): List<TextSegment> {
        val segments = mutableListOf<TextSegment>()

        if (::textSegmentClass.isInitialized) {
            fun toSegment(style: Style, text: String): TextSegment {
                val segment = TextSegment(text)

                val color = style.color
                if (color != null) {
                    segment.color = color.serialize()
                }

                segment.bold = style.isBold
                segment.italic = style.isItalic
                segment.underlined = style.isUnderlined
                segment.strikethrough = style.isStrikethrough
                segment.obfuscated = style.isObfuscated

                val clickEvent = style.clickEvent
                if (clickEvent is ClickEvent.OpenUrl) {
                    segment.clickUrl = clickEvent.uri().toString()
                }

                val hoverEvent = style.hoverEvent
                if (hoverEvent is HoverEvent.ShowText) {
                    segment.hoverText = hoverEvent.value().string
                }

                return segment
            }
            component.visit<Component>({ style, text ->
                if (text.isNotEmpty()) {
                    segments.add(toSegment(style, text))
                }
                Optional.empty()
            }, Style.EMPTY)

        } else {
            throw IllegalStateException(NOT_REGISTERED_ERROR_MESSAGE)
        }
        return segments
    }
}