package com.animationtransmog;

import com.animationtransmog.config.AnimationTransmogConfigManager;
import net.runelite.api.Actor;

import java.util.HashMap;

public class PoseController {
    AnimationTransmogConfigManager configManager;

    HashMap<String, HashMap<String, Integer>> poseSets;

    int previousPose = -1;
    int previousIdlePose = -1;

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
        if (currentConfigOption.equals("Default")) {
            reset();
            return;
        }

        int currentPose = actor.getPoseAnimation();

        if (currentPose != previousPose)
        {
            int newPoseAnimation = -1;

            if(currentPose == actor.getWalkAnimation())
            {
                newPoseAnimation = getPoseID(currentConfigOption, "Walk");
            }
            else if(currentPose == actor.getWalkRotate180())
            {
                newPoseAnimation = getPoseID(currentConfigOption, "WalkBackwards");
            }
            else if(currentPose == actor.getWalkRotateLeft())
            {
                newPoseAnimation = getPoseID(currentConfigOption, "ShuffleLeft");
            }
            else if(currentPose == actor.getWalkRotateRight())
            {
                newPoseAnimation = getPoseID(currentConfigOption, "ShuffleRight");
            }
            else if(currentPose == actor.getRunAnimation())
            {
                newPoseAnimation = getPoseID(currentConfigOption, "Run");
            }
            else if(currentPose == actor.getIdleRotateLeft() || currentPose == actor.getIdleRotateRight())
            {
                newPoseAnimation = getPoseID(currentConfigOption, "Rotate");
            }

            if (newPoseAnimation != -1) actor.setPoseAnimation(newPoseAnimation);
        }

        previousPose = actor.getPoseAnimation();


        // Updated idle pose
        int currentIdlePose = actor.getIdlePoseAnimation();
        int selectedIdlePose = getPoseID(currentConfigOption,"Idle");

        if (currentIdlePose != selectedIdlePose) {
            previousIdlePose = currentIdlePose;
            actor.setIdlePoseAnimation(selectedIdlePose);
        }
    }

    void reset()
    {
        if (actor == null) return;
        if (previousIdlePose != -1) {
            actor.setIdlePoseAnimation(previousIdlePose);
            previousIdlePose = -1;
        }
    }

    // Gets the ID of a pos given the set and type of pose
    int getPoseID(String set, String poseType)
    {
        if(poseSets.get(set).get(poseType) == null) return -1;
        return poseSets.get(set).get(poseType);
    }
}
