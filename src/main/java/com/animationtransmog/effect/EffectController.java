package com.animationtransmog.effect;
/*
Given an actor, the Effect class can control the gfx, animation, and the timing
of the two to orchestrate and overall visual effect.
 */

import com.animationtransmog.AnimationTypes;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;

import java.util.*;
import java.util.stream.Collectors;

public class EffectController
{
    AnimationTypes animationTypes;
    HashMap<String, String> settings;

    List<Integer> regionBlacklist;
    List<String> regionBlacklistAnimations;

    HashMap<String, Effect> effects;

    private int customEffectLength = 0;
    private int customEffectLengthTimer = 0;
    private List<Integer> customEffectSpotAnimBlockList;

    private int customAnimationID = -1;
    private int customAnimationFrame = 0;
    private int customAnimationStartFrame = 0;
    private int customAnimationEndFrame = -1;

    private int customGfxID = -1;
    private int customGfxFrame = 0;
    private int customGfxStartFrame = 0;
    private int customGfxEndFrame = -1;

    private int customSoundID = -1;
    private int customSoundFrameDelay = 0;

    private WorldPoint effectLocation = null;

    private boolean isPlaying = false;
    private boolean isReadyToPlay = false;
    private String playingAnimationType = "";

    public Actor actor = null;
    public Client client = null;

    public EffectController(AnimationTypes animationTypes, HashMap<String, String> settings)
    {
        this.animationTypes = animationTypes;
        this.settings = settings;

        regionBlacklist = new ArrayList<>();
        regionBlacklistAnimations = new ArrayList<>();

        // ToA Regions
        regionBlacklist.add(14164);
        regionBlacklist.add(14676);
        regionBlacklist.add(15188);
        regionBlacklist.add(15444);
        regionBlacklist.add(15700);
        regionBlacklist.add(15955);
        regionBlacklist.add(13906);
        regionBlacklist.add(14162);
        regionBlacklist.add(14674);
        regionBlacklist.add(15186);
        regionBlacklist.add(15698);
        regionBlacklist.add(15954);
        regionBlacklist.add(15953);
        regionBlacklist.add(14160);
        regionBlacklist.add(14672);
        regionBlacklist.add(15184);
        regionBlacklist.add(15696);

        regionBlacklistAnimations.add("Teleport");

        // Defining Effects
        effects = new HashMap<>();

        // Teleport Effects
        effects.put("Hail Zamorak", new Effect(1500, 246, 0, 17, 0, 6, -1, 0, 150));
        effects.put("Praise Saradomin", new Effect(1500, 247, 0, 17, 0, 6, -1, 0, 150));
        effects.put("Ancient Disciple", new Effect(1500, 332, 0, 17, 0, 6, -1, 0,150));
        effects.put("Glitch", new Effect(7040, 482, 0, 12, 0, 48, -1, 0, 150));
        effects.put("Pommel Smash", new Effect(9131, 559,  0, 35,0, 10, -1, 0, 150));
        effects.put("???", new Effect(9286, -1, 0, 31, 0, 0, -1, 0, 150));
        effects.put("Darkness Ascends", new Effect(3945, 1577, 0, 12, 0, 57, -1, 0, 150));
        effects.put("2010 Vibes", new Effect(3945, 56, 0, 12, 0, 31, -1, 0, 150));
        effects.put("Jad 2 OP", new Effect(836, 451, 0, 9, 0, 20, -1, 0, 150));

        // Action Effects
        effects.put("Arcane Chop", new Effect(6298, 1063, 0, 159, 0, 40, -1, 0, -1));
        effects.put("Bronze Chop", new Effect(879, -1, 0, 5, 0, 0, -1, 0,  500));
        effects.put("Arcane Mine", new Effect(4411, 739, 0, 45, 15, 35, -1, 0, -1));

        // Credit goes to @Cyborger1 for name and effect IDs
        effects.put("Smooth Scatter", new Effect(7533, 1103, 0, 32, 0, 32, -1, 0, -1));
        effects.put("Brutal", new Effect(9544, 1103, 0, 13, 0, 32, -1, 0, -1));
        effects.put("Blast Mine", new Effect(2107, 659, 0, 16, 0, 16, 163, 0, -1));
        effects.put("Dig", new Effect(830, -1, 0, 7, 0, 0, -1, 0, -1));
        effects.put("Headbang", new Effect(2108, -1, 0, 48, 0, 0, -1, 0, -1));

        // Death Effects
        // Credit goes to @geheur for idea
        effects.put("Plank", new Effect(837, -1, 0, 4, 0, 0, -1, 0, 200));
        effects.put("Vaporize", new Effect(11035, -1, 0, 8, 0, 0, -1, 0, -1));
        effects.put("Blaze", new Effect(10873, 2610, 0, 42, 0, 38, -1, 0, -1));
        effects.put("Melt", new Effect(10873, 2711, 0, 42, 0, 74, -1, 0, -1));

        customEffectSpotAnimBlockList = new ArrayList<>();
    }

    public void setPlayer(Actor actor, Client client)
    {
        this.actor = actor;
        this.client = client;
    }

    public void setSettings(HashMap<String, String> settings)
    {
        this.settings = settings;
    }

    public void onChange() {
        // Get plugin config for current animation
        String currentAnimationType = animationTypes.getAnimationType(actor.getAnimation());
        List<Integer> currentAnimationTypeSpotAnimIDs = animationTypes.getAnimationTypeSpotAnimIds(currentAnimationType);
        if (currentAnimationType == null) return;

        // Check if player is in a region that causes issues for the plugin
        int[] regionIDs = client.getMapRegions();
        if (!Collections.disjoint(regionBlacklist, Arrays.stream(regionIDs).boxed().collect(Collectors.toList())))
        {
            if (regionBlacklistAnimations.contains(currentAnimationType)) return;
        }

        // If a custom effect is already playing, ignore any future changes
        if (isPlaying && playingAnimationType.equals(currentAnimationType)) return;

        // Set the new effect based on the client animation
        setEffect(currentAnimationType, currentAnimationTypeSpotAnimIDs);
    }

    void setEffect(String currentAnimationType, List<Integer> currentAnimationTypeSpotAnimIDs)
    {
        String configOption = settings.get(currentAnimationType);

        if (configOption.equals("Default")) return;

        // Get custom effect for config
        Effect effect = getEffect(configOption);
        if (effect == null) return;

        resetController();

        // Set information for new custom effect
        Animation newAnimation = effect.animation;
        GFX newGfx = effect.gfx;
        Sound newSound = effect.sound;
        customEffectLength = effect.length;
        if (currentAnimationTypeSpotAnimIDs.size() > 0)
        {
            customEffectSpotAnimBlockList = currentAnimationTypeSpotAnimIDs;
        }

        customAnimationID = newAnimation.animationId;
        customAnimationStartFrame = newAnimation.startFrame;
        customAnimationEndFrame = newAnimation.endFrame;

        if (newGfx.gfxId != -1)
        {
            customGfxID = newGfx.gfxId;
            customGfxStartFrame = newGfx.startFrame;
            customGfxEndFrame = newGfx.endFrame;
        }

        if (newSound.soundId != -1)
        {
            customSoundID = newSound.soundId;
            customSoundFrameDelay = newSound.delayFrame;
        }

        isReadyToPlay = true;
        playingAnimationType = currentAnimationType;
    }


    public void onBeforeRender()
    {
        // If there is a new effect to play, and one isn't already playing, start the new one
        if (!isPlaying)
        {
            if (isReadyToPlay)
            {
                playEffect();
            }
            return;
        }

        //  Once the effect timer has reached the effect length, or player has moved, reset everything
        if (
                (customEffectLength != -1 && customEffectLengthTimer >= customEffectLength) ||
                (customEffectLength == -1 && customAnimationFrame >= customAnimationEndFrame) ||
                !effectLocation.equals(actor.getWorldLocation())
        )
        {
            resetController();
            return;
        }

        // Track the state of the current animation and overwrite any client animations that try to play
        int currentAnimationID = actor.getAnimation();
        if (customAnimationID != -1)
        {
            if (currentAnimationID != customAnimationID)
            {
                actor.setAnimation(customAnimationID);
                actor.setAnimationFrame(customAnimationFrame);
            }
            if (currentAnimationID == customAnimationID)
            {
                if (customAnimationFrame > customAnimationEndFrame) actor.setAnimationFrame(customAnimationEndFrame);
                customAnimationFrame = actor.getAnimationFrame();
            }
        }

        // Track the state of the current GFX
        IterableHashTable<ActorSpotAnim> spotAnims = actor.getSpotAnims();
        if (actor.hasSpotAnim(customGfxID))
        {
            if (customGfxFrame > customGfxEndFrame) spotAnims.get(customGfxID).setFrame(customGfxEndFrame);
            customGfxFrame = spotAnims.get(customGfxID).getFrame();

        }

        // Block any spot anims that are in the block list
        if (spotAnims != null && customEffectSpotAnimBlockList.size() > 0)
        {
            for (ActorSpotAnim spotAnim : spotAnims)
            {
                if (customEffectSpotAnimBlockList.contains(spotAnim.getId()))
                {
                    actor.removeSpotAnim((int)spotAnim.getHash());
                }
            }
        }
    }

    public void updateTimer()
    {
        if (isPlaying) customEffectLengthTimer++;
    }

    void resetController()
    {
        customEffectLength = 0;
        customEffectLengthTimer = 0;
        if (customEffectSpotAnimBlockList.size() > 0)
        {
            customEffectSpotAnimBlockList = new ArrayList<>();
        }

        customAnimationFrame = 0;
        customAnimationStartFrame = 0;
        customAnimationEndFrame = -1;
        if (actor.getAnimation() == customAnimationID) actor.setAnimation(-1);
        customAnimationID = -1;

        customGfxFrame = 0;
        customGfxStartFrame = 0;
        customGfxEndFrame = -1;
        if (actor.hasSpotAnim(customGfxID)) actor.removeSpotAnim(customGfxID);
        customGfxID = -1;

        customSoundID = -1;
        customSoundFrameDelay = 0;

        effectLocation = null;

        isPlaying = false;
        isReadyToPlay = false;
        playingAnimationType = "";
    }

    void playEffect()
    {
        // Set the animation and GFX of the effect as the current ones on the player
        actor.setAnimation(customAnimationID);
        actor.setAnimationFrame(customAnimationStartFrame);

        if (customGfxID != -1)
        {
            actor.createSpotAnim(customGfxID, customGfxID, 0, 0);
            actor.getSpotAnims().get(customGfxID).setFrame(customGfxStartFrame);
        }

        if (customSoundID != -1)
        {
            int sceneX = actor.getLocalLocation().getSceneX();
            int sceneY = actor.getLocalLocation().getSceneY();
            client.playSoundEffect(customSoundID, sceneX, sceneY, 1, customSoundFrameDelay);
        }

        effectLocation = actor.getWorldLocation();

        isPlaying = true;
        isReadyToPlay = false;
    }


    // Gets an Effect given the name of the effect
    Effect getEffect(String effectName)
    {
        return effects.get(effectName);
    }
}
