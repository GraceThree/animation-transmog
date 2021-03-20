package com.animationtransmog;

import java.util.HashMap;

public class AnimationSetManager {
    HashMap<Integer, String> animationTypes;
    HashMap<String, Integer> animationIDs;
    HashMap<String, HashMap<String, Integer>> poseSets;

    public AnimationSetManager()
    {
        // Defining animation types
        animationTypes = new HashMap<>();

        animationTypes.put(4069, "Teleport");
        animationTypes.put(714, "Teleport");
        animationTypes.put(808, "Idle");
        animationTypes.put(819, "Walk");
        animationTypes.put(824, "Run");


        // Defining animation IDs
        animationIDs = new HashMap<>();

        animationIDs.put("Dig", 830);
        animationIDs.put("Die", 836);


        // Defining pose sets
        poseSets = new HashMap<>();

        // Default movement
        HashMap<String, Integer> defaultPoses = new HashMap<>();
        defaultPoses.put("Idle", 808);
        defaultPoses.put("Walk", 819);
        defaultPoses.put("Run", 824);
        poseSets.put("Default", defaultPoses);

        // Monke movement
        HashMap<String, Integer> monkePoses = new HashMap<>();
        monkePoses.put("Idle", 4646);
        monkePoses.put("Walk", 4682);
        monkePoses.put("Run", 6277);
        poseSets.put("Monke", monkePoses);
    }

    // Gets the type of animation given its ID number
    public String GetAnimationType(int animationID)
    {
        return animationTypes.get(animationID);
    }

    // Gets the ID of an animation given the name of animation
    public int GetAnimationID(String animationName)
    {
        return animationIDs.get(animationName);
    }

    // Gets the ID of a pos given the set and type of pose
    public int GetPoseID(String set, String poseType)
    {
        return poseSets.get(set).get(poseType);
    }
}