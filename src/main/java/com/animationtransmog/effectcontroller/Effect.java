package com.animationtransmog.effectcontroller;

public class Effect {
    Animation animation;
    GFX gfx;

    public Effect(int animationId, int gfxId)
    {
        animation = new Animation(animationId, 0);
        gfx = new GFX(gfxId, 0);
    }

    public Effect(int animationId, int gfxId, int animationStartFrame, int gfxStartFrame)
    {
        animation = new Animation(animationId, animationStartFrame);
        gfx = new GFX(gfxId, gfxStartFrame);
    }
}
