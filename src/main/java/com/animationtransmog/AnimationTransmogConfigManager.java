package com.animationtransmog;

import java.util.HashMap;
import java.util.function.Supplier;

public class AnimationTransmogConfigManager
{
    HashMap<String, Supplier<String>> configGetters;
    public AnimationTransmogConfigManager(AnimationTransmogConfig config)
    {
        configGetters = new HashMap<>();

        configGetters.put("Woodcut", () -> config.swapWoodcutAnimation().getOption());
        configGetters.put("Mine", () -> config.swapMineAnimation().getOption());
        configGetters.put("StandardSpell", () -> config.swapStandardSpellAnimation().getOption());
        configGetters.put("Teleport", () -> config.swapTeleportAnimation().getOption());
        configGetters.put("Movement", () -> config.swapMovementMode().getOption());
    }

    public String getConfigOption(String configType)
    {
        return configGetters.get(configType).get();
    }
}
