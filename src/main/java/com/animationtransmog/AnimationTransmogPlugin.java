package com.animationtransmog;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.Player;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Animation Transmog"
)
public class AnimationTransmogPlugin extends Plugin
{
	AnimationTransmogConfigManager configManager;
	AnimationSetManager animationSetManager = new AnimationSetManager();

	@Inject
	private Client client;

	int previousPose = -1;

	@Inject
	private AnimationTransmogConfig config;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Animation Transmog started!");
		configManager = new AnimationTransmogConfigManager(config);
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Animation Transmog stopped!");
	}

	@Subscribe
	public void onAnimationChanged(AnimationChanged e)
	{
		Player local = client.getLocalPlayer();

		int currentAnimation = local.getAnimation();
		String currentAnimationType = animationSetManager.GetAnimationType(currentAnimation);

		if (currentAnimationType != null)
		{
			String configOption = configManager.getConfigOption(currentAnimationType);
			if (!configOption.equals("Default")) {
				int newAnimation = animationSetManager.GetAnimationID(configOption);

				local.setAnimation(newAnimation);
				local.setActionFrame(0);
			}
		}
	}

	@Subscribe
	public void onClientTick(ClientTick event)
	{
		Player local = client.getLocalPlayer();

		// If current pose set is not recognized, disable animation pose transmog
		if (animationSetManager.GetAnimationType(local.getWalkAnimation()) == null) return;

		// Updated pose
		int currentPose = local.getPoseAnimation();
		String currentConfigOption = configManager.getConfigOption("Movement");

		if (!currentConfigOption.equals("Default") && currentPose != previousPose)
		{
			String currentPoseType = animationSetManager.GetAnimationType(currentPose);
			if (currentPoseType != null) {
				int newPoseAnimation = animationSetManager.GetPoseID(currentConfigOption, currentPoseType);

				if (newPoseAnimation != -1) local.setPoseAnimation(newPoseAnimation);
			}
		}

		previousPose = local.getPoseAnimation();


		// Updated idle pose
		int currentIdlePose = local.getIdlePoseAnimation();
		int selectedIdlePose = animationSetManager.GetPoseID(currentConfigOption,"Idle");

		if (currentIdlePose != selectedIdlePose) local.setIdlePoseAnimation(selectedIdlePose);
	}

	@Provides
	AnimationTransmogConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AnimationTransmogConfig.class);
	}
}
