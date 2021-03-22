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

        animationTypes.put(879, "Woodcut");
        animationTypes.put(877, "Woodcut");
        animationTypes.put(875, "Woodcut");
        animationTypes.put(873, "Woodcut");
        animationTypes.put(871, "Woodcut");
        animationTypes.put(869, "Woodcut");
        animationTypes.put(867, "Woodcut");
        animationTypes.put(2846, "Woodcut");
        animationTypes.put(2117, "Woodcut");
        animationTypes.put(7264, "Woodcut");
        animationTypes.put(8324, "Woodcut");

        animationTypes.put(710, "StandardSpell");
        animationTypes.put(711, "StandardSpell");
        animationTypes.put(717, "StandardSpell");
        animationTypes.put(718, "StandardSpell");
        animationTypes.put(727, "StandardSpell");
        animationTypes.put(728, "StandardSpell");
        animationTypes.put(729, "StandardSpell");
        animationTypes.put(1161, "StandardSpell");
        animationTypes.put(1162, "StandardSpell");
        animationTypes.put(1165, "StandardSpell");
        animationTypes.put(1167, "StandardSpell");
        animationTypes.put(1169, "StandardSpell");
        animationTypes.put(7855, "StandardSpell");

        animationTypes.put(830, "Dig");
        animationTypes.put(836, "Die");
        animationTypes.put(4069, "Teleport");
        animationTypes.put(714, "Teleport");

        animationTypes.put(808, "Idle");
        animationTypes.put(4646, "Idle");

        animationTypes.put(819, "Walk");
        animationTypes.put(4682, "Walk");
        animationTypes.put(1660, "Walk");

        animationTypes.put(824, "Run");
        animationTypes.put(6277, "Run");
        animationTypes.put(1661, "Run");

        animationTypes.put(820, "WalkBackwards");
        animationTypes.put(6276, "WalkBackwards");

        animationTypes.put(821, "ShuffleLeft");
        animationTypes.put(6268, "ShuffleLeft");

        animationTypes.put(822, "ShuffleRight");
        animationTypes.put(6275, "ShuffleRight");

        animationTypes.put(823, "Rotate");


        // Defining animation IDs
        animationIDs = new HashMap<>();

        animationIDs.put("Dig", 830);
        animationIDs.put("Die", 836);
        animationIDs.put("Twirl", 2107);
        animationIDs.put("Headbang", 2108);


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