package com.animationtransmog.effect;

public class Effect {
    Animation animation;
    GFX gfx;
    Sound sound;

    public Effect(int animationId, int gfxId, int soundId)
    {
        animation = new Animation(animationId, 0);
        gfx = new GFX(gfxId, 0);
        sound = new Sound(soundId, 0);
    }

    public Effect(int animationId, int gfxId, int animationStartFrame, int gfxStartFrame, int soundId, int soundDelayFrame)
    {
        animation = new Animation(animationId, animationStartFrame);
        gfx = new GFX(gfxId, gfxStartFrame);
        sound = new Sound(soundId, soundDelayFrame);
    }
}
