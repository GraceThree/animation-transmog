package com.animationtransmog;

import com.animationtransmog.config.AnimationTransmogConfigManager;
import net.runelite.api.Actor;

import java.util.HashMap;

public class PoseController {
    AnimationTransmogConfigManager configManager;

    HashMap<String, HashMap<String, Integer>> poseSets;

    String previousConfig = "";

    public Actor actor = null;

    public PoseController(AnimationTransmogConfigManager configManager)
    {
        this.configManager = configManager;

        // Defining pose sets
        poseSets = new HashMap<>();

        // Default movement
        HashMap<String, Integer> defaultPoses = new HashMap<>();
        defaultPoses.put("Idle", 808);
        defaultPoses.put("Walk", 819);
        defaultPoses.put("Run", 824);
        defaultPoses.put("WalkBackwards", 820);
        defaultPoses.put("ShuffleLeft", 821);
        defaultPoses.put("ShuffleRight", 822);
        defaultPoses.put("Rotate", 823);
        poseSets.put("Default", defaultPoses);

        // Monke movement
        HashMap<String, Integer> monkePoses = new HashMap<>();
        monkePoses.put("Idle", 4646);
        monkePoses.put("Walk", 4682);
        monkePoses.put("Run", 6277);
        monkePoses.put("WalkBackwards", 6276);
        monkePoses.put("ShuffleLeft", 6268);
        monkePoses.put("ShuffleRight", 6275);
        monkePoses.put("Rotate", 823);
        poseSets.put("Monke", monkePoses);
    }

    public void setPlayer(Actor actor)
    {
        this.actor = actor;
    }

    public void update()
    {
        String currentConfigOption = configManager.getConfigOption("Movement");

        // If you are changing back to Default, reset all the poses then do nothing
        // till a different config is chosen.
        if (currentConfigOption.equals("Default"))
        {
            if (!previousConfig.equals("Default")) reset();
            return;
        }

        if(actor.getWalkAnimation() != getPoseID(currentConfigOption, "Walk"))
            actor.setWalkAnimation(getPoseID(currentConfigOption, "Walk"));
        if(actor.getWalkRotate180() != getPoseID(currentConfigOption, "WalkBackwards"))
            actor.setWalkRotate180(getPoseID(currentConfigOption, "WalkBackwards"));
        if(actor.getWalkRotateLeft() != getPoseID(currentConfigOption, "ShuffleLeft"))
            actor.setWalkRotateLeft(getPoseID(currentConfigOption, "ShuffleLeft"));
        if(actor.getWalkRotateRight() != getPoseID(currentConfigOption, "ShuffleRight"))
            actor.setWalkRotateRight(getPoseID(currentConfigOption, "ShuffleRight"));
        if(actor.getRunAnimation() != getPoseID(currentConfigOption, "Run"))
            actor.setRunAnimation(getPoseID(currentConfigOption, "Run"));
        if(actor.getIdleRotateLeft() != getPoseID(currentConfigOption, "Rotate"))
            actor.setIdleRotateLeft(getPoseID(currentConfigOption, "Rotate"));
        if(actor.getIdleRotateRight() != getPoseID(currentConfigOption, "Rotate"))
            actor.setIdleRotateRight(getPoseID(currentConfigOption, "Rotate"));
        if (actor.getIdlePoseAnimation() != getPoseID(currentConfigOption,"Idle"))
            actor.setIdlePoseAnimation(getPoseID(currentConfigOption,"Idle"));

        previousConfig = currentConfigOption;
    }

    void reset()
    {
        if (actor == null) return;

        actor.setWalkAnimation(getPoseID("Default", "Walk"));
        actor.setWalkRotate180(getPoseID("Default", "WalkBackwards"));
        actor.setWalkRotateLeft(getPoseID("Default", "ShuffleLeft"));
        actor.setWalkRotateRight(getPoseID("Default", "ShuffleRight"));
        actor.setRunAnimation(getPoseID("Default", "Run"));
        actor.setIdleRotateLeft(getPoseID("Default", "Rotate"));
        actor.setIdleRotateRight(getPoseID("Default", "Rotate"));
        actor.setIdlePoseAnimation(getPoseID("Default","Idle"));

        previousConfig = "Default";
    }

    // Gets the ID of a pos given the set and type of pose
    int getPoseID(String set, String poseType)
    {
        if(poseSets.get(set).get(poseType) == null) return -1;
        return poseSets.get(set).get(poseType);
    }
}
