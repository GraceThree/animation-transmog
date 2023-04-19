package com.animationtransmog.config;

import java.util.HashMap;
import java.util.function.Supplier;

public class AnimationTransmogConfigManager
{
    public Supplier<Boolean> canUseDB;
    public HashMap<String, Supplier<String>> configGetters;
    HashMap<String, Supplier<Integer>> animationPlayerConfigGetters;
    public AnimationTransmogConfigManager(AnimationTransmogConfig config)
    {
        canUseDB = config::swapPluginDBUsage;

        configGetters = new HashMap<>();
        configGetters.put("Woodcut", () -> config.swapWoodcutAnimation().getOption());
        configGetters.put("Mine", () -> config.swapMineAnimation().getOption());
        configGetters.put("Alch", () -> config.swapAlchAnimation().getOption());
        configGetters.put("StandardSpell", () -> config.swapStandardSpellAnimation().getOption());
        configGetters.put("Teleport", () -> config.swapTeleportAnimation().getOption());
        configGetters.put("AshScatter", () -> config.swapAshScatterAnimation().getOption());
        configGetters.put("Death", () -> config.swapDeathAnimation().getOption());

        animationPlayerConfigGetters = new HashMap<>();
        animationPlayerConfigGetters.put("SelectedAnimation", config::selectedAnimation);
        animationPlayerConfigGetters.put("SelectedAnimationFrame", config::selectedAnimationFrame);
        animationPlayerConfigGetters.put("SelectedGFX", config::selectedGFX);
        animationPlayerConfigGetters.put("SelectedGFXFrame", config::selectedGFXFrame);
        animationPlayerConfigGetters.put("SelectedGFXHeight", config::selectedGFXHeight);
    }

    public int getAnimationPlayerOption(String configType)
    {
        return animationPlayerConfigGetters.get(configType).get();
    }

    public boolean getCanUseDB() { return canUseDB.get(); }
}
