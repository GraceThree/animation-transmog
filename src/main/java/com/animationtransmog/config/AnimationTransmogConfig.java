package com.animationtransmog.config;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("example")
public interface AnimationTransmogConfig extends Config
{
	@ConfigSection(
			name = "Skilling",
			description = "All options for various skilling animations",
			position = 0
	)
	String skillingSection = "skilling";

	@ConfigSection(
			name = "Combat",
			description = "All options for various combat animations",
			position = 1
	)
	String combatSection = "combat";

	@ConfigSection(
			name = "Action",
			description = "All options for various action animations",
			position = 2
	)
	String actionSection = "action";

	@ConfigSection(
			name = "Misc",
			description = "All other options for animations",
			position = 3
	)
	String miscSection = "misc";


	@ConfigItem(
			keyName = "swapWoodcutAnimation",
			name = "Woodcutting Animation",
			description = "Change the animation used for Woodcutting.",
			section = skillingSection
	)
	default ActionAnimation swapWoodcutAnimation()
	{
		return ActionAnimation.DEFAULT;
	}

	@ConfigItem(
			keyName = "swapMineAnimation",
			name = "Mining Animation",
			description = "Change the animation used for Mining.",
			section = skillingSection
	)
	default ActionAnimation swapMineAnimation()
	{
		return ActionAnimation.DEFAULT;
	}

	@ConfigItem(
			keyName = "swapStandardSpellAnimation",
			name = "Standard Spell Animation",
			description = "Change the animation used for casting from the Standard Spellbook.",
			section = combatSection
	)
	default ActionAnimation swapStandardSpellAnimation()
	{
		return ActionAnimation.DEFAULT;
	}

	@ConfigItem(
			keyName = "swapTeleportAnimation",
			name = "Teleport Animation",
			description = "Change the animation used to teleport.",
			section = actionSection
	)
	default TeleportAnimation swapTeleportAnimation()
	{
		return TeleportAnimation.DEFAULT;
	}

	@ConfigItem(
			keyName = "swapAshScatterAnimation",
			name = "Ash Scatter Animation",
			description = "Change the animation used to scatter ashes.",
			section = actionSection
	)
	default ActionAnimation swapAshScatterAnimation()
	{
		return ActionAnimation.DEFAULT;
	}

	@ConfigItem(
			keyName = "swapMovementMode",
			name = "Movement Mode",
			description = "Change the way your character looks when moving around",
			section = miscSection
	)
	default MovementMode swapMovementMode()
	{
		return MovementMode.DEFAULT;
	}
}
