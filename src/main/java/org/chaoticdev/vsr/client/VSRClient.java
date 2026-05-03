package org.chaoticdev.vsr.client;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VSRClient implements ClientModInitializer {

    public static final String MOD_ID = "videosettingsrefresh";
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath(MOD_ID, "general")
    );

    private static KeyMapping refreshKeyMapping;

    private static int ticksSinceJoin = -1;
    private static final int DELAY_TICKS = 20 * 20; // 20 seconds
    private static boolean hasAutoReloaded = false;
    private static boolean isInWorld = false;

    @Override
    public void onInitializeClient() {
        AutoConfig.register(VSRConfig.class, GsonConfigSerializer::new);

        refreshKeyMapping = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.videosettingsrefresh.refresh",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                CATEGORY
        ));

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            ticksSinceJoin = 0;
            hasAutoReloaded = false;
            isInWorld = true;
            LOGGER.info("[VideoSettingsRefresh] World join detected. Auto-refresh in 20s.");
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            ticksSinceJoin = -1;
            hasAutoReloaded = false;
            isInWorld = false;
            LOGGER.info("[VideoSettingsRefresh] Disconnected. Timer reset.");
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Auto-refresh after delay
            if (isInWorld && !hasAutoReloaded && ticksSinceJoin >= 0) {
                ticksSinceJoin++;
                if (ticksSinceJoin >= DELAY_TICKS) {
                    hasAutoReloaded = true;
                    ticksSinceJoin = -1;
                    refreshVideoSettings(client);
                    if (client.player != null) {
                        client.player.sendSystemMessage(
                                Component.literal("§aVideo settings auto-refreshed!"));
                    }
                }
            }

            // Manual hotkey
            while (refreshKeyMapping.consumeClick()) {
                VSRConfig config =
                        AutoConfig.getConfigHolder(VSRConfig.class).getConfig();
                if (config.enableHotkey) {
                    LOGGER.info("[VideoSettingsRefresh] Hotkey pressed.");
                    refreshVideoSettings(client);
                    if (client.player != null) {
                        client.player.sendSystemMessage(
                                Component.literal("§aVideo settings manually refreshed!"));
                    }
                }
            }
        });

        LOGGER.info("[VideoSettingsRefresh] Initialized.");
    }

    private void refreshVideoSettings(Minecraft client) {
        if (client == null || client.options == null) {
            LOGGER.warn("[VideoSettingsRefresh] Client or options null, skipping.");
            return;
        }
        client.options.load();
        client.options.save();
        LOGGER.info("[VideoSettingsRefresh] Video settings refreshed.");
    }
}

