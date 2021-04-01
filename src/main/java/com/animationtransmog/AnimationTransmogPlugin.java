package com.animationtransmog;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GraphicChanged;
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

	EffectController effectController = new EffectController();

	@Inject
	private Client client;

	int previousPose = -1;
	int previousIdlePose = -1;

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

		if (previousIdlePose != -1) local.setIdlePoseAnimation(previousIdlePose);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			// Setup effectController
			effectController.setPlayer(client.getLocalPlayer());
		}
	}

	@Subscribe
	public void onClientTick(ClientTick event)
	{
		String currentConfigOption = configManager.getConfigOption("Movement");
		if (currentConfigOption.equals("Default")) return;

		Player local = client.getLocalPlayer();
		if (local == null) return;

		// Updated pose
		int currentPose = local.getPoseAnimation();

		if (currentPose != previousPose)
		{
			int newPoseAnimation = -1;

			if(currentPose == local.getWalkAnimation())
			{
				newPoseAnimation = animationSetManager.getPoseID(currentConfigOption, "Walk");
			}
			else if(currentPose == local.getWalkRotate180())
			{
				newPoseAnimation = animationSetManager.getPoseID(currentConfigOption, "WalkBackwards");
			}
			else if(currentPose == local.getWalkRotateLeft())
			{
				newPoseAnimation = animationSetManager.getPoseID(currentConfigOption, "ShuffleLeft");
			}
			else if(currentPose == local.getWalkRotateRight())
			{
				newPoseAnimation = animationSetManager.getPoseID(currentConfigOption, "ShuffleRight");
			}
			else if(currentPose == local.getRunAnimation())
			{
				newPoseAnimation = animationSetManager.getPoseID(currentConfigOption, "Run");
			}
			else if(currentPose == local.getIdleRotateLeft() || currentPose == local.getIdleRotateRight())
			{
				newPoseAnimation = animationSetManager.getPoseID(currentConfigOption, "Rotate");
			}

			if (newPoseAnimation != -1) local.setPoseAnimation(newPoseAnimation);
		}

		previousPose = local.getPoseAnimation();


		// Updated idle pose
		int currentIdlePose = local.getIdlePoseAnimation();
		int selectedIdlePose = animationSetManager.getPoseID(currentConfigOption,"Idle");

		if (currentIdlePose != selectedIdlePose) {
			previousIdlePose = currentIdlePose;
			local.setIdlePoseAnimation(selectedIdlePose);
		}
	}

	@Subscribe
	public void onAnimationChanged(AnimationChanged e)
	{
		Player local = client.getLocalPlayer();
		if (local == null || e.getActor() != local) return;

		// Setup effectController
		if (effectController.actor == null)
		{
			effectController.setPlayer(client.getLocalPlayer());
		}

		int currentAnimation = local.getAnimation();
		String currentAnimationType = animationSetManager.getAnimationType(currentAnimation);

		// If an animation plays that there is a config for, play the configured effect
		if (currentAnimationType != null)
		{
			String configOption = configManager.getConfigOption(currentAnimationType);
			if (!configOption.equals("Default")) {
				effectController.play(configOption);
			}
		}

		// If animation is over but gfx is still playing, kill it
		if (currentAnimation == -1 && effectController.currentGfxId != -1)
		{
			local.setGraphic(-1);
			effectController.currentGfxId = -1;
		}
	}

	@Subscribe
	public void onGraphicChanged(GraphicChanged e)
	{
		Player local = client.getLocalPlayer();
		if (local == null || e.getActor() != local) return;

		int currentGfx = local.getGraphic();

		// If gfx is play and client tries to override it, re-override it with effect gfx
		if (effectController.currentGfxId != -1 && currentGfx != effectController.currentGfxId)
		{
			local.setGraphic(effectController.currentGfxId);
		}
	}

	@Provides
	AnimationTransmogConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AnimationTransmogConfig.class);
	}
}
