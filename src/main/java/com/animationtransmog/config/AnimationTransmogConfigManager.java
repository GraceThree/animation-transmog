package com.animationtransmog.config;

import java.util.HashMap;
import java.util.function.Supplier;

public class AnimationTransmogConfigManager
{
    HashMap<String, Supplier<String>> configGetters;
    HashMap<String, Supplier<Integer>> animationPlayerConfigGetters;
    public AnimationTransmogConfigManager(AnimationTransmogConfig config)
    {
        configGetters = new HashMap<>();

        configGetters.put("Woodcut", () -> config.swapWoodcutAnimation().getOption());
        configGetters.put("Mine", () -> config.swapMineAnimation().getOption());
        configGetters.put("Alch", () -> config.swapAlchAnimation().getOption());
        configGetters.put("StandardSpell", () -> config.swapStandardSpellAnimation().getOption());
        configGetters.put("Teleport", () -> config.swapTeleportAnimation().getOption());
        configGetters.put("AshScatter", () -> config.swapAshScatterAnimation().getOption());
        configGetters.put("Death", () -> config.swapDeathAnimation().getOption());
        configGetters.put("Movement", () -> config.swapMovementMode().getOption());

        animationPlayerConfigGetters = new HashMap<>();
        animationPlayerConfigGetters.put("SelectedAnimation", config::selectedAnimation);
        animationPlayerConfigGetters.put("SelectedAnimationFrame", config::selectedAnimationFrame);
        animationPlayerConfigGetters.put("SelectedGFX", config::selectedGFX);
        animationPlayerConfigGetters.put("SelectedGFXFrame", config::selectedGFXFrame);
        animationPlayerConfigGetters.put("SelectedGFXHeight", config::selectedGFXHeight);
    }

    public String getConfigOption(String configType)
    {
        return configGetters.get(configType).get();
    }
    public int getAnimationPlayerOption(String configType)
    {
        return animationPlayerConfigGetters.get(configType).get();
    }
}
