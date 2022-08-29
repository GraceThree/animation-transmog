package com.animationtransmog.effect;

public class Animation {
    int animationId;
    int startFrame;
    int endFrame;

    public Animation(int animationId, int startFrame) {
        this.animationId = animationId;
        this.startFrame = startFrame;
    }

    public Animation(int animationId, int startFrame,  int endFrame) {
        this.animationId = animationId;
        this.startFrame = startFrame;
        this.endFrame = endFrame;
    }
}
