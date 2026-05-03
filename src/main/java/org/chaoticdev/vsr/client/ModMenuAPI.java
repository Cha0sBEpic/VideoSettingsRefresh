package org.chaoticdev.vsr.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;

public class ModMenuAPI implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            VSRConfig config = AutoConfig.getConfigHolder(VSRConfig.class).getConfig();

            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Component.translatable("text.autoconfig.vsr.title"))
                    .setSavingRunnable(() -> AutoConfig.getConfigHolder(VSRConfig.class).save());

            ConfigCategory general = builder.getOrCreateCategory(
                    Component.translatable("text.autoconfig.vsr.category.default"));

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            general.addEntry(entryBuilder
                    .startBooleanToggle(
                            Component.translatable("text.autoconfig.vsr.option.enableHotkey"),
                            config.enableHotkey)
                    .setDefaultValue(true)
                    .setTooltip(Component.translatable("text.autoconfig.vsr.option.enableHotkey.tooltip"))
                    .setSaveConsumer(val -> config.enableHotkey = val)
                    .build());

            return builder.build();
        };
    }
}