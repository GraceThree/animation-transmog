package com.animationtransmog;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TeleportAnimation
{
    DEFAULT("Default"),
    DARKNESSASCENDS("Darkness Ascends"),
    HDVIBES("2010 Vibes"),
    JAD2OP("Jad 2 OP");

    private final String option;

    @Override
    public String toString()
    {
        return option;
    }
}
