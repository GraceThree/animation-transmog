package com.animationtransmog.effect;

import com.animationtransmog.AnimationTypes;
import net.runelite.api.Actor;
import net.runelite.api.Client;

import java.util.*;

public class PlayerController {

    public EffectController effectController;
    HashMap<String, String> settings;

    // Constructor for non-local player
    public PlayerController(AnimationTypes animationTypes, Actor actor, Client client, HashMap<String, String> settings)
    {
        this.settings = settings;
        String playerName = actor.getName();
        if (playerName == null) return;
        effectController = new EffectController(animationTypes, settings);
        effectController.setPlayer(actor, client);
    }

    // Used for updating playerController and effectController settings when the DB updates
    public void setSettings(HashMap<String, String> settings)
    {
        this.settings = settings;
        effectController.setSettings(settings);
    }
}
