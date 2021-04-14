package com.animationtransmog.effect;
/*
Given an actor, the Effect class can control the gfx, animation, and the timing
of the two to orchestrate and overall visual effect.
 */

import com.animationtransmog.AnimationTypes;
import com.animationtransmog.config.AnimationTransmogConfigManager;
import net.runelite.api.Actor;
import net.runelite.api.Client;

import java.util.HashMap;

public class EffectController {
    AnimationTypes animationTypes;
    AnimationTransmogConfigManager configManager;

    HashMap<String, Effect> effects;

    private int currentAnimationId = -1;
    private int currentGfxId = -1;

    private int currentGfxFrame = 0;

    private boolean isPlaying = false;

    public Actor actor = null;
    public Client client = null;

    public EffectController(AnimationTypes animationTypes, AnimationTransmogConfigManager configManager)
    {
        this.animationTypes = animationTypes;
        this.configManager = configManager;

        // Defining Effects
        effects = new HashMap<>();

        // Teleport Effects
        effects.put("Darkness Ascends", new Effect(3945, 1577, -1));
        effects.put("2010 Vibes", new Effect(3945, 56, -1));
        effects.put("Jad 2 OP", new Effect(836, 451, -1));

        // Action Effects
        effects.put("Arcane Chop", new Effect(6298, 1063, -1));
        effects.put("Arcane Mine", new Effect(4411, 739, -1));
        effects.put("Blast Mine", new Effect(2107, 659, 163));
        effects.put("Dig", new Effect(830, -1, -1));
        effects.put("Headbang", new Effect(2108, -1, -1));
    }

    public void setPlayer(Actor actor, Client client)
    {
        this.actor = actor;
        this.client = client;
    }

    public void update()
    {
        int currentAnimation = actor.getAnimation();
        String currentAnimationType = animationTypes.getAnimationType(currentAnimation);
        if (currentAnimationType == null)
        {
            if (currentGfxId != -1)
            {
                actor.setGraphic(-1);
                currentGfxId = -1;
                currentGfxFrame = 0;
            }
            isPlaying = false;
            return;
        }

        String configOption = configManager.getConfigOption(currentAnimationType);
        if (configOption.equals("Default")) return;

        Effect effect = getEffect(configOption);
        if (effect == null) return;

        Animation newAnimation = effect.animation;
        GFX newGfx = effect.gfx;
        Sound newSound = effect.sound;

        currentAnimationId = newAnimation.animationId;
        actor.setAnimation(newAnimation.animationId);
        actor.setAnimationFrame(newAnimation.startFrame);

        if (newGfx.gfxId != -1)
        {
            currentGfxId = newGfx.gfxId;
            actor.setGraphic(newGfx.gfxId);
            actor.setSpotAnimFrame(newGfx.startFrame);
        }

        if (newSound.soundId != -1)
        {
            int sceneX = actor.getLocalLocation().getSceneX();
            int sceneY = actor.getLocalLocation().getSceneY();
            client.playSoundEffect(newSound.soundId, sceneX, sceneY, 1, newSound.delayFrame);
        }
        isPlaying = true;
    }

    public void override()
    {
        // If gfx is play and client tries to override it, re-override it with effect gfx
        if (currentGfxId != -1 && actor.getGraphic() != currentGfxId)
        {
            actor.setGraphic(currentGfxId);
        }
    }

    public void updateGfxFrame()
    {
        if (!isPlaying) return;
        if (actor.getGraphic() == currentGfxId)
        {
            if (actor.getSpotAnimFrame() == 0) actor.setSpotAnimFrame(currentGfxFrame+1);
            currentGfxFrame = actor.getSpotAnimFrame();
        }
    }

    // Gets an Effect given the name of the effect
    Effect getEffect(String effectName)
    {
        return effects.get(effectName);
    }
}
