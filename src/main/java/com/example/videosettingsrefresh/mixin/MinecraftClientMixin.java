package com.example.videosettingsrefresh.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CancellationException;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/MinecraftClient;setWorld(Lnet/minecraft/client/world/ClientWorld;)V"
            ),
            method = "joinWorld(Lnet/minecraft/client/world/ClientWorld;)V"
    )
    private void onJoinWorld(ClientWorld world, CallbackInfo ci) {
        try {
            // This code will execute after the world is set in MinecraftClient

            // Refresh video settings
            MinecraftClient client = MinecraftClient.getInstance();
            if (client != null) {
                client.options.load();
                System.out.println("Video settings refreshed via Mixin!");
            }
        } catch (CancellationException ignored) {
        }
    }
}
