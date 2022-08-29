package com.animationtransmog;

import com.animationtransmog.config.AnimationTransmogConfig;
import com.animationtransmog.config.AnimationTransmogConfigManager;
import com.animationtransmog.effect.AnimationPlayerController;
import com.animationtransmog.effect.EffectController;
import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.*;
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
	@Inject
	private AnimationTransmogConfig config;

	@Inject
	private Client client;

	AnimationTransmogConfigManager configManager;
	AnimationTypes animationTypes;
	EffectController effectController;
	AnimationPlayerController animationPlayerController;
	PoseController poseController;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Animation Transmog started!");
		configManager = new AnimationTransmogConfigManager(config);
		animationTypes = new AnimationTypes();
		effectController = new EffectController(animationTypes, configManager);
		animationPlayerController = new AnimationPlayerController(configManager);
		poseController = new PoseController(configManager);
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Animation Transmog stopped!");
		// If you disable the plugin, try and reset the idle pose
		poseController.reset();
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			// Setup effectController
			effectController.setPlayer(client.getLocalPlayer(), client);
			// Setup animationPlayerController
			animationPlayerController.setPlayer(client.getLocalPlayer(), client);
			// Setup poseController
			poseController.setPlayer(client.getLocalPlayer());
		}
	}

	@Subscribe
	public void onClientTick(ClientTick event)
	{
		// Setup poseController
		Player local = client.getLocalPlayer();
		if (local == null) return;
		if (poseController.actor == null) poseController.setPlayer(local);

		// Update animation player
		animationPlayerController.update();

		// Updated pose
		poseController.update();
	}

	@Subscribe
	public void onBeforeRender(BeforeRender event)
	{
		effectController.onBeforeRender();
	}

	@Subscribe
	public void onAnimationChanged(AnimationChanged e)
	{
		// Setup effectController and make sure the animation change is from your player
		Player local = client.getLocalPlayer();
		if (local == null || e.getActor() != local) return;
		if (effectController.actor == null) effectController.setPlayer(client.getLocalPlayer(), client);

		// Update effect
		effectController.onChange(true);
	}

	@Subscribe
	public void onGraphicChanged(GraphicChanged e)
	{
		// Make sure the graphics change is from your player
		Player local = client.getLocalPlayer();
		if (local == null || e.getActor() != local) return;
		if (effectController.actor == null) effectController.setPlayer(client.getLocalPlayer(), client);

		// If the game client is trying to override the plugin's effect, re-override it
		effectController.onChange(false);
	}

	@Provides
	AnimationTransmogConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AnimationTransmogConfig.class);
	}
}
