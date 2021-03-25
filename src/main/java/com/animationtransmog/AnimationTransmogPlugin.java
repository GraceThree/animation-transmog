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
		Player local = client.getLocalPlayer();
		if (local == null) return;

		local.setIdlePoseAnimation(808);
	}

	@Subscribe
	public void onAnimationChanged(AnimationChanged e)
	{
		Player local = client.getLocalPlayer();
		if (local == null) return;

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
		if (local == null) return;

		// Updated pose
		int currentPose = local.getPoseAnimation();
		String currentConfigOption = configManager.getConfigOption("Movement");

		if (!currentConfigOption.equals("Default") && currentPose != previousPose)
		{
			int newPoseAnimation = -1;

			if(currentPose == local.getWalkAnimation())
			{
				newPoseAnimation = animationSetManager.GetPoseID(currentConfigOption, "Walk");
			}
			else if(currentPose == local.getWalkRotate180())
			{
				newPoseAnimation = animationSetManager.GetPoseID(currentConfigOption, "WalkBackwards");
			}
			else if(currentPose == local.getWalkRotateLeft())
			{
				newPoseAnimation = animationSetManager.GetPoseID(currentConfigOption, "ShuffleLeft");
			}
			else if(currentPose == local.getWalkRotateRight())
			{
				newPoseAnimation = animationSetManager.GetPoseID(currentConfigOption, "ShuffleRight");
			}
			else if(currentPose == local.getRunAnimation())
			{
				newPoseAnimation = animationSetManager.GetPoseID(currentConfigOption, "Run");
			}
			else if(currentPose == local.getIdleRotateLeft() || currentPose == local.getIdleRotateRight())
			{
				newPoseAnimation = animationSetManager.GetPoseID(currentConfigOption, "Rotate");
			}

			if (newPoseAnimation != -1) local.setPoseAnimation(newPoseAnimation);
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
