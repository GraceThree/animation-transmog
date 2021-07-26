package com.animationtransmog.effect;

public class GFX {
    int gfxId;
    int startFrame;
    int endFrame;

    public GFX(int gfxId, int startFrame) {
        this.gfxId = gfxId;
        this.startFrame = startFrame;
        this.endFrame = -1;
    }

    public GFX(int gfxId, int startFrame, int endFrame) {
        this.gfxId = gfxId;
        this.startFrame = startFrame;
        this.endFrame = endFrame;
    }
}
