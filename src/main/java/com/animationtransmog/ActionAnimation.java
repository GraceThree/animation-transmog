package com.animationtransmog;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActionAnimation
{
    DEFAULT("Default"),
    DIG("Dig"),
    DIE("Die"),
    HEADBANG("Headbang"),
    TWIRL("Twirl");

    private final String option;

    @Override
    public String toString()
    {
        return option;
    }
}