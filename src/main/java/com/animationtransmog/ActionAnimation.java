package com.animationtransmog;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActionAnimation
{
    DEFAULT("Default"),
    ARCANECHOP("Arcane Chop"),
    ARCANEMINE("Arcane Mine"),
    BLASTMINE("Blast Mine"),
    DIG("Dig"),
    HEADBANG("Headbang");

    private final String option;

    @Override
    public String toString()
    {
        return option;
    }
}