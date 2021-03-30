package com.animationtransmog;

import java.util.ArrayList;
import java.util.List;

public class Effect {
    Animation animation;
    GFX gfx;

    public Effect(int animationId, int gfxId)
    {
        animation = new Animation(animationId, 0);
        gfx = new GFX(gfxId, 0);
    }

    public Effect(int animationId, int gfxId, int animtaionStartFrame, int gfxStartFrame)
    {
        animation = new Animation(animationId, animtaionStartFrame);
        gfx = new GFX(gfxId, gfxStartFrame);
    }
}
