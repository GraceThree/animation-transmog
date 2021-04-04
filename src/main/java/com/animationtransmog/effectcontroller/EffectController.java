package com.animationtransmog.effectcontroller;
/*
Given an actor, the Effect class can control the gfx, animation, and the timing
of the two to orchestrate and overall visual effect.
 */

import net.runelite.api.Actor;

import java.util.HashMap;

public class EffectController {
    HashMap<String, Effect> effects;

    public int currentAnimationId = -1;
    public int currentGfxId = -1;

    public Actor actor = null;

    public EffectController()
    {
        // Defining Effects
        effects = new HashMap<>();

        // Teleport Effects
        effects.put("Darkness Ascends", new Effect(3945, 1577));
        effects.put("2010 Vibes", new Effect(3945, 56));
        effects.put("Jad 2 OP", new Effect(836, 451));

        // Action Effects
        effects.put("Arcane Chop", new Effect(6298, 1063));
        effects.put("Arcane Mine", new Effect(4411, 739));
        effects.put("Blast Mine", new Effect(2107, 659));
        effects.put("Dig", new Effect(830, -1));
        effects.put("Headbang", new Effect(2108, -1));
    }

    public void setPlayer(Actor actor)
    {
        this.actor = actor;
    }

    public void play(String effectName)
    {
        Effect effect = getEffect(effectName);
        if (effect == null) return;

        Animation currentAnimation = effect.animation;
        GFX currentGfx = effect.gfx;

        currentAnimationId = currentAnimation.animationId;
        actor.setAnimation(currentAnimation.animationId);
        actor.setActionFrame(currentAnimation.startFrame);

        if (currentGfx.gfxId != -1)
        {
            currentGfxId = currentGfx.gfxId;
            actor.setGraphic(currentGfx.gfxId);
            actor.setSpotAnimFrame(currentGfx.startFrame);
        }
    }

    // Gets an Effect given the name of the effect
    Effect getEffect(String effectName)
    {
        return effects.get(effectName);
    }
}
