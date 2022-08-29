package com.animationtransmog.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TeleportAnimation
{
    DEFAULT("Default"),
    HAILZAMORAK("Hail Zamorak"),
    PRAISESARADOMIN("Praise Saradomin"),
    ANCIENTDISCIPLE("Ancient Disciple"),
    GLITCH("Glitch"),
    POMMELSMASH("Pommel Smash"),
    UHH("???"),
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
