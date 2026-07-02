package lol.echolotl.nine.mixin.motd;

import lol.echolotl.nine.motd.NineMOTD;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerStatusPacketListenerImpl;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerStatusPacketListenerImpl.class)
public abstract class HandleStatusRequestMOTDMixin {
    @Redirect(
        method = "handleStatusRequest",
        at = @At(
                value = "FIELD",
                target = "Lnet/minecraft/server/network/ServerStatusPacketListenerImpl;status:Lnet/minecraft/network/protocol/status/ServerStatus;",
                opcode = Opcodes.GETFIELD)
    )
    private ServerStatus nine$applyMOTD(ServerStatusPacketListenerImpl instance) {
        MinecraftServer server = NineMOTD.server();
        assert server != null;
        ServerStatus vanilla = server.getStatus();
        assert vanilla != null;
        return NineMOTD.applyToStatus(vanilla);
    }
}

