package lol.echolotl.nine.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import lol.echolotl.nine.motd.NineMOTD
import lol.echolotl.nine.util.MiniMessageUtil
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.server.permissions.Permissions

class NineCommand {
    private val message: String = """
        |<gradient:#0DFF00:#00B84D:#048582><b>9❇🟩🟢</b>mod</gradient> <gray>v%s<br>by <reset><head:echolotl> <gray>echolotl</gray>
        |<gray>Updated on %s</gray>
        |
        |- <click:OPEN_URL:'https://docs.papermc.io/adventure/minimessage/format/'><blue><u>MiniMessage docs</u></blue></click>
    """.trimMargin()

    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(
            Commands.literal("nine")
                .executes { nine(it) }
                .then(
                    Commands.literal("motd")
                        .requires {source -> source.permissions().hasPermission(Permissions.COMMANDS_OWNER)}
                        .executes { reloadMOTD(it) }
                )
        )
    }

    private fun nine(ctx: CommandContext<CommandSourceStack>): Int {
        val metadata = FabricLoader.getInstance().getModContainer("nine")
            .orElseThrow().metadata
        val modVersion = metadata.version.friendlyString
        val buildDate = metadata.getCustomValue("build_date")?.asString ?: "unknown"
        ctx.source.sendSuccess({ MiniMessageUtil.deserialize(message.format(modVersion, buildDate)) }, false)
        return 1
    }

    private fun reloadMOTD(ctx: CommandContext<CommandSourceStack>): Int {
        NineMOTD.reload()
        ctx.source.sendSuccess({ MiniMessageUtil.deserialize("<green>☺ </green><gray>Reloaded all MOTDs") }, true)
        return 1
    }
}