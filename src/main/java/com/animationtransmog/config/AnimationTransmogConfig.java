package com.animationtransmog.config;

import net.runelite.client.config.*;

@ConfigGroup("animationtransmog")
public interface AnimationTransmogConfig extends Config
{
	@ConfigSection(
			name = "Very Important!",
			description = "",
			position = 0
	)
	String importantSection = "important";

	@ConfigSection(
			name = "Skilling",
			description = "All options for various skilling animations",
			position = 1
	)
	String skillingSection = "skilling";

	@ConfigSection(
			name = "Combat",
			description = "All options for various combat animations",
			position = 2
	)
	String combatSection = "combat";

	@ConfigSection(
			name = "Action",
			description = "All options for various action animations",
			position = 3
	)
	String actionSection = "action";

	@ConfigSection(
			name = "Misc",
			description = "All other options for animations",
			position = 4
	)
	String miscSection = "misc";

	@ConfigSection(
			name = "Animation Player",
			description = "Play a certain animation on demand",
			position = 5,
			closedByDefault = true
	)
	String playerSection = "player";


	@ConfigItem(
			keyName = "swapPluginDBUsage",
			name = "Enable Multiplayer",
			description = "This enables the plugin to show custom effects of other players around you, if they also have the plugin installed.",
			warning = "By enabling this you opt into sending your player name and IP Address to a Third-Party Database in order to store these plugin settings.",
			section = importantSection
	)
	default boolean swapPluginDBUsage()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapWoodcutAnimation",
			name = "Woodcutting Animation",
			description = "Change the effect used for Woodcutting.",
			section = skillingSection
	)
	default ActionAnimation swapWoodcutAnimation()
	{
		return ActionAnimation.DEFAULT;
	}

	@ConfigItem(
			keyName = "swapMineAnimation",
			name = "Mining Animation",
			description = "Change the effect used for Mining.",
			section = skillingSection
	)
	default ActionAnimation swapMineAnimation()
	{
		return ActionAnimation.DEFAULT;
	}

	@ConfigItem(
			keyName = "swapStandardSpellAnimation",
			name = "Standard Spell Animation",
			description = "Change the effect used for casting from the Standard Spellbook.",
			section = combatSection
	)
	default ActionAnimation swapStandardSpellAnimation()
	{
		return ActionAnimation.DEFAULT;
	}

	@ConfigItem(
			keyName = "swapTeleportAnimation",
			name = "Teleport Animation",
			description = "Change the effect used to teleport.",
			section = actionSection
	)
	default TeleportAnimation swapTeleportAnimation()
	{
		return TeleportAnimation.DEFAULT;
	}

	@ConfigItem(
			keyName = "swapAlchAnimation",
			name = "Alch Animation",
			description = "Change the effect used for high and low alchemy.",
			section = actionSection
	)
	default ActionAnimation swapAlchAnimation()
	{
		return ActionAnimation.DEFAULT;
	}

	@ConfigItem(
			keyName = "swapAshScatterAnimation",
			name = "Ash Scatter Animation",
			description = "Change the effect used to scatter ashes.",
			section = actionSection
	)
	default ActionAnimation swapAshScatterAnimation()
	{
		return ActionAnimation.DEFAULT;
	}

	@ConfigItem(
			keyName = "swapDeathAnimation",
			name = "Death Animation",
			description = "Change the effect used when you die.",
			section = actionSection
	)
	default DeathAnimation swapDeathAnimation()
	{
		return DeathAnimation.PLANK;
	}

//	@ConfigItem(
//			keyName = "swapMovementMode",
//			name = "Movement Mode",
//			description = "Change the way your character looks when moving around",
//			section = miscSection
//	)
//	default MovementMode swapMovementMode()
//	{
//		return MovementMode.DEFAULT;
//	}


	@Range(min=-1)@ConfigItem(
			keyName = "selectedAnimation",
			name = "Selected Animation",
			description = "Animation to play, -1 to stop",
			position = 1,
			section = playerSection
	)
	default int selectedAnimation()
	{
		return -1;
	}

	@Range(min=-1)@ConfigItem(
			keyName = "selectedAnimationFrame",
			name = "Selected Animation Frame",
			description = "Animation frame to show, -1 to loop whole animation",
			position = 2,
			section = playerSection
	)
	default int selectedAnimationFrame()
	{
		return -1;
	}

	@Range(min=-1)@ConfigItem(
			keyName = "selectedGFX",
			name = "Selected GFX",
			description = "GFX to create, -1 to stop",
			position = 3,
			section = playerSection
	)
	default int selectedGFX()
	{
		return -1;
	}

	@Range(min=-1)@ConfigItem(
			keyName = "selectedGFXFrame",
			name = "Selected GFX Frame",
			description = "GFX frame to show, -1 to loop whole GFX",
			position = 4,
			section = playerSection
	)
	default int selectedGFXFrame()
	{
		return -1;
	}

	@Range(min=-10)@ConfigItem(
			keyName = "selectedGFXHeight",
			name = "Selected GFX Height",
			description = "GFX height compared to the character",
			position = 5,
			section = playerSection
	)
	default int selectedGFXHeight()
	{
		return 0;
	}
}
