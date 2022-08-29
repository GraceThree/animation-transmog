package com.animationtransmog.effect;

public class Effect {
    Animation animation;
    GFX gfx;
    Sound sound;

    int length;

    public Effect(int animationId, int gfxId, int soundId)
    {
        animation = new Animation(animationId, 0);
        gfx = new GFX(gfxId, 0);
        sound = new Sound(soundId, 0);
        length = -1;
    }

    public Effect(int animationId, int gfxId, int animationStartFrame, int animationEndFrame, int soundId, int length)
    {
        animation = new Animation(animationId, animationStartFrame, animationEndFrame);
        gfx = new GFX(gfxId, 0);
        sound = new Sound(soundId, 0);
        this.length = length;
    }

    public Effect(int animationId, int gfxId, int animationStartFrame, int gfxStartFrame, int gfxEndFrame, int soundId, int soundDelayFrame)
    {
        animation = new Animation(animationId, animationStartFrame);
        gfx = new GFX(gfxId, gfxStartFrame, gfxEndFrame);
        sound = new Sound(soundId, soundDelayFrame);
        length = -1;
    }

    public Effect(int animationId, int gfxId, int animationStartFrame, int animationEndFrame, int gfxStartFrame, int gfxEndFrame, int soundId, int soundDelayFrame, int length)
    {
        animation = new Animation(animationId, animationStartFrame, animationEndFrame);
        gfx = new GFX(gfxId, gfxStartFrame, gfxEndFrame);
        sound = new Sound(soundId, soundDelayFrame);
        this.length = length;
    }
}
