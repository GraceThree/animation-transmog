package com.animationtransmog.effect;
import com.animationtransmog.config.AnimationTransmogConfigManager;
import net.runelite.api.Actor;
import net.runelite.api.Client;

public class AnimationPlayerController {
    AnimationTransmogConfigManager configManager;

    private int selectedAnimationId = -1;
    private int selectedGFXId = -1;

    public Actor actor = null;
    public Client client = null;

    public AnimationPlayerController(AnimationTransmogConfigManager configManager) {
        this.configManager = configManager;
    }

    public void setPlayer(Actor actor, Client client)
    {
        this.actor = actor;
        this.client = client;
    }

    public void update()
    {
        // Get selected animation configs
        int tempAnimationId = configManager.getAnimationPlayerOption("SelectedAnimation");
        int tempAnimationFrame = configManager.getAnimationPlayerOption("SelectedAnimationFrame");
        if (tempAnimationId == -1) tempAnimationFrame = 0;

        // Get selected GFX configs
        int tempGFXId = configManager.getAnimationPlayerOption("SelectedGFX");
        int tempGFXFrame = configManager.getAnimationPlayerOption("SelectedGFXFrame");
        int tempGFXHeight = configManager.getAnimationPlayerOption("SelectedGFXHeight");
        if (tempGFXId == -1) tempGFXFrame = 0;

        // Disable Animation Player Controller if not being used
        if (tempAnimationId == -1 && tempGFXId == -1) return;

        // Play selected animation
        int currentAnimation = actor.getAnimation();
        if(currentAnimation == -1 && actor.getAnimationFrame() != 0) actor.setAnimationFrame(0);
        if(tempAnimationFrame != -1) actor.setAnimationFrame(tempAnimationFrame);
        if(tempAnimationId != selectedAnimationId || currentAnimation == -1) actor.setAnimation(tempAnimationId);

        // Create selected GFX
        if (actor.hasSpotAnim(selectedGFXId))
            if(tempGFXFrame != -1) actor.getSpotAnims().get(selectedGFXId).setFrame(tempGFXFrame);
        else actor.createSpotAnim(tempGFXId, tempGFXId, tempGFXHeight, 0);

        // Keep track of selected animation and GFX
        selectedAnimationId = tempAnimationId;
        selectedGFXId = tempGFXId;
    }
}
