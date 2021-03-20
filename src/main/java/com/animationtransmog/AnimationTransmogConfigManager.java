package com.animationtransmog;

import java.util.HashMap;
import java.util.function.Supplier;

public class AnimationTransmogConfigManager
{
    HashMap<String, Supplier<String>> configGetters;
    public AnimationTransmogConfigManager(AnimationTransmogConfig config)
    {
        configGetters = new HashMap<>();

        configGetters.put("Movement", () -> config.swapMovementMode().getOption());
        configGetters.put("Teleport", () -> config.swapTeleportAnimation().getOption());
    }

    public String getConfigOption(String configType)
    {
        return configGetters.get(configType).get();
    }
}
