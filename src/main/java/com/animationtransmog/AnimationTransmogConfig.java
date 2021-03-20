package com.animationtransmog;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("example")
public interface AnimationTransmogConfig extends Config
{
	@ConfigItem(
			keyName = "swapMovementMode",
			name = "Movement Mode",
			description = "Change the way your character looks when moving around"
	)
	default MovementMode swapMovementMode()
	{
		return MovementMode.DEFAULT;
	}

	@ConfigItem(
			keyName = "swapTeleportAnimation",
			name = "Teleport Animation",
			description = "Change the animation used to teleport."
	)
	default ActionAnimation swapTeleportAnimation()
	{
		return ActionAnimation.DEFAULT;
	}
	// gfx 678
}
