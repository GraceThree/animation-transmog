package com.animationtransmog.effect;

public class Effect
{
    Animation animation;
    GFX gfx;
    Sound sound;

    int length;

    public Effect(int animationId, int gfxId, int animationStartFrame, int animationEndFrame, int gfxStartFrame, int gfxEndFrame, int soundId, int soundDelayFrame, int length)
    {
        animation = new Animation(animationId, animationStartFrame, animationEndFrame);
        gfx = new GFX(gfxId, gfxStartFrame, gfxEndFrame);
        sound = new Sound(soundId, soundDelayFrame);
        this.length = length;
    }
}
