package org.chaoticdev.vsr.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {

    @Inject(
            method = "setLevel(Lnet/minecraft/client/multiplayer/ClientLevel;)V",
            at = @At("TAIL")
    )
    private void onSetLevel(ClientLevel level, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (client.options != null) {
            client.options.load();
            client.options.save();
        }
    }
}
