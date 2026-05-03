package org.chaoticdev.vsr.client;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "vsr")
public class VSRConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    public boolean enableHotkey = true;
}