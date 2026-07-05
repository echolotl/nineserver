package lol.echolotl.nine.motd

import org.yaml.snakeyaml.Yaml

data class NineMOTDEntry(
    val line1: String,
    val line2: String,
    val icon: String? = null,
    val ignoreTemplate: Boolean = false
)

data class NineMOTDConfig(
    val enabled: Boolean,
    val template1: String,
    val template2: String,
    val motds: List<NineMOTDEntry>,
) {


    fun random(): NineMOTDEntry = motds.random()

    fun render(motd: NineMOTDEntry): Pair<String, String> {
        val renderedLine1 = if (template1.isNotBlank() && !motd.ignoreTemplate) template1.replace(
            TEMPLATE_STRING,
            motd.line1
        ) else motd.line1
        val renderedLine2 = if (template2.isNotBlank() && !motd.ignoreTemplate) template2.replace(
            TEMPLATE_STRING,
            motd.line2
        ) else motd.line2
        return renderedLine1 to renderedLine2
    }

    companion object {
        val DEFAULT = NineMOTDConfig(
            enabled = true,
            template1 = "",
            template2 = "",
            motds = listOf(
                NineMOTDEntry(
                    line1 = "<gradient:#0DFF00:#00B84D:#048582><b>nine</b></gradient>",
                    line2 = "<gray>did you know</gray>"
                )
            )
        )

        private const val TEMPLATE_STRING = $$"$temp$"

        private val yaml = Yaml()

        val DEFAULT_CONFIG: String = """
            |# NineMOD MOTD module config
            |#
            |# Just like MiniMOTD, this supports MiniMessage:
            |# https://docs.advntr.dev/minimessage/format.html
            |
            |# Whether the custom MOTD is enabled at all.
            |enabled: true
            |
            |# Optional templates for each line. If set, the entry's line1/line2 content
            |# is inserted into the template wherever $TEMPLATE_STRING appears. Useful for
            |# if we, say, had a long list of quotes.
            |#
            |# Example:
            |# template1: "<gray>[</gray>$TEMPLATE_STRING<gray>]</gray>"
            |template1: ""
            |template2: ""
            |
            |# The big list of MOTDs...
            |#
            |# Each entry can optionally specify an 'icon', which is a favicon file name
            |# (must be a 64x64 PNG) placed in the 'icons' subfolder of this config
            |# directory, e.g. `config/nine/icons/icon.png`. If omitted, the server's
            |# default icon is used.
            |#
            |# Each entry can also specify whether to ignore the templates.
            |# If set to true, the entry's line1/line2 content will be used as-is.
            |# ignoreTemplate: false
            |
            |motds:
            |  - line1: "${DEFAULT.motds[0].line1}"
            |    line2: "${DEFAULT.motds[0].line2}"
            |    # icon: icon.png
            |    # ignoreTemplate: false
            |
        """.trimMargin()

        fun parse(text: String): NineMOTDConfig {
            return try {
                val data = yaml.load<Map<String, Any?>>(text) ?: return DEFAULT

                val enabled = (data["enabled"] as? Boolean) ?: DEFAULT.enabled

                val template1 = (data["template1"] as? String) ?: DEFAULT.template1
                val template2 = (data["template2"] as? String) ?: DEFAULT.template2

                val rawMotds = data["motds"] as? List<*>
                val motds = rawMotds
                    ?.mapNotNull { entry ->
                        val cast = entry as Map<*, *>
                        val line1 = cast["line1"] as? String
                        val line2 = cast["line2"] as? String
                        val icon = (cast["icon"] as? String)?.takeIf { it.isNotBlank() }
                        val ignoreTemplate = (cast["ignoreTemplate"] as? Boolean) ?: false
                        if (line1.isNullOrEmpty() && line2.isNullOrEmpty()) null
                        else NineMOTDEntry(line1 ?: "", line2 ?: "", icon, ignoreTemplate)
                    }
                    ?.takeIf { it.isNotEmpty() }
                    ?: DEFAULT.motds

                NineMOTDConfig(enabled = enabled, template1, template2, motds = motds)
            } catch (_: Exception) {
                DEFAULT
            }
        }
    }
}

