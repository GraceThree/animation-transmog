package com.animationtransmog;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MovementMode
{
    DEFAULT("Default"),
    MONKE("Monke");

    private final String option;

    @Override
    public String toString()
    {
        return option;
    }
}