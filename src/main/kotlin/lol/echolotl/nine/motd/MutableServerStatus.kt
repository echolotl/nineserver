package lol.echolotl.nine.motd

import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.status.ServerStatus
import net.minecraft.network.protocol.status.ServerStatus.*
import java.util.*

class MutableServerStatus(status: ServerStatus) {
    var description: Component = status.description
    private var players: Optional<Players> = status.players
    private var version: Optional<Version> = status.version
    var favicon: Optional<Favicon> = status.favicon
    private var enforcesSecureChat: Boolean = status.enforcesSecureChat

    fun toServerStatus(): ServerStatus {
        return ServerStatus(description, players, version, favicon, enforcesSecureChat)
    }
}

