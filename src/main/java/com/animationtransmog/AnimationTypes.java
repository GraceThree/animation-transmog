package com.animationtransmog;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AnimationTypes {
    HashMap<Integer, String> animationTypes;
    HashMap<String, List<Integer>> animationTypeSpotAnimIds;

    public AnimationTypes()
    {
        // Defining animation types
        animationTypes = new HashMap<>();
        animationTypeSpotAnimIds = new HashMap<>();


        animationTypes.put(4069, "Teleport");
        animationTypes.put(714, "Teleport");
        animationTypeSpotAnimIds.put("Teleport", Arrays.asList(111, 678));

        animationTypes.put(2295, "AshScatter");

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

        animationTypes.put(625, "Mine");
        animationTypes.put(626, "Mine");
        animationTypes.put(627, "Mine");
        animationTypes.put(3873, "Mine");
        animationTypes.put(629, "Mine");
        animationTypes.put(628, "Mine");
        animationTypes.put(624, "Mine");
        animationTypes.put(8313, "Mine");
        animationTypes.put(7139, "Mine");
        animationTypes.put(642, "Mine");
        animationTypes.put(8346, "Mine");
        animationTypes.put(8887, "Mine");
        animationTypes.put(4482, "Mine");
        animationTypes.put(7283, "Mine");
        animationTypes.put(8347, "Mine");
        animationTypes.put(8787, "Mine");
        animationTypes.put(8788, "Mine");
        animationTypes.put(8789, "Mine");
        animationTypes.put(6753, "Mine");
        animationTypes.put(6754, "Mine");
        animationTypes.put(6755, "Mine");
        animationTypes.put(3866, "Mine");
        animationTypes.put(6757, "Mine");
        animationTypes.put(6756, "Mine");
        animationTypes.put(6752, "Mine");
        animationTypes.put(8312, "Mine");
        animationTypes.put(6758, "Mine");
        animationTypes.put(335, "Mine");
        animationTypes.put(8344, "Mine");
        animationTypes.put(8886, "Mine");
        animationTypes.put(4481, "Mine");
        animationTypes.put(7282, "Mine");
        animationTypes.put(8345, "Mine");
        animationTypes.put(8786, "Mine");

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

        animationTypes.put(713, "Alch");
        animationTypes.put(712, "Alch");
        animationTypeSpotAnimIds.put("Alch", Arrays.asList(112, 113));

        animationTypes.put(836, "Death");
    }

    // Gets the type of animation given its ID number
    public String getAnimationType(int animationID)
    {
        return animationTypes.get(animationID);
    }
    public List<Integer> getAnimationTypeSpotAnimIds(String animationType)
    {
        return animationTypeSpotAnimIds.get(animationType);
    }
}