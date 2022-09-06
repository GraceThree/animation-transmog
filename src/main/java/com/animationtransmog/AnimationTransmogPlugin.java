package com.animationtransmog;

import com.animationtransmog.config.AnimationTransmogConfig;
import com.animationtransmog.config.AnimationTransmogConfigManager;
import com.animationtransmog.effect.AnimationPlayerController;
import com.animationtransmog.effect.DatabaseManager;
import com.animationtransmog.effect.PlayerController;
import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.HashMap;

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
	boolean configChanged = true;

	DatabaseManager dbManager;

	AnimationTransmogConfigManager configManager;
	AnimationTypes animationTypes;
	AnimationPlayerController animationPlayerController;
//	PoseController poseController;

	HashMap<String, PlayerController> players;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Animation Transmog started!");
		dbManager = new DatabaseManager();
		configManager = new AnimationTransmogConfigManager(config);
		animationTypes = new AnimationTypes();
		animationPlayerController = new AnimationPlayerController(configManager);
		players = new HashMap<>();
//		poseController = new PoseController(configManager);
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Animation Transmog stopped!");
		// If you disable the plugin, try and reset the idle pose
//		poseController.reset();
	}

	@Subscribe
	public void onPlayerSpawned(PlayerSpawned e)
	{
		String playerName = e.getActor().getName();
		HashMap<String, String> settings = dbManager.getSettings(playerName);
		if (settings.size() == 0) return;
//		client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Adding " + playerName + " to players", null);


		PlayerController playerController = new PlayerController(dbManager, animationTypes, e.getActor(), client);
		players.put(playerName, playerController);
	}

	@Subscribe
	public void onPlayerDespawned(PlayerDespawned e)
	{
		String playerName = e.getActor().getName();
		if (players.containsKey(playerName))
		{
//			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Removing " + playerName + " from players", null);
			players.remove(playerName);
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			// Setup animationPlayerController
			animationPlayerController.setPlayer(client.getLocalPlayer(), client);
			// Setup poseController
//			poseController.setPlayer(client.getLocalPlayer());
		}
	}

	@Subscribe
	public void onClientTick(ClientTick event)
	{
		// Setup poseController
		Player local = client.getLocalPlayer();
		if (local == null || local.getName() == null) return;
//		if (poseController.actor == null) poseController.setPlayer(local);

		// Update animation player
		animationPlayerController.update();

		// Updated pose
//		poseController.update();


		if (configChanged)
		{
			configChanged = false;
			// If a DB update has been requested, update DB
			HashMap<String, String> settings = updateDBSettings();
			players.get(local.getName()).setSettings(settings);
		}
	}

	@Subscribe
	public void onBeforeRender(BeforeRender event)
	{
		// For each player stored, run the player's effectController
		for (PlayerController player : players.values())
		{
			player.effectController.onBeforeRender();
		}
	}

	@Subscribe
	public void onAnimationChanged(AnimationChanged e)
	{
		Actor playerActor = e.getActor();
		PlayerController playerController = players.get(playerActor.getName());
		if (playerController == null) return;

		// Update effect
		playerController.effectController.onChange(true);
	}

	@Subscribe
	public void onGraphicChanged(GraphicChanged e)
	{
		Actor playerActor = e.getActor();
		PlayerController playerController = players.get(playerActor.getName());
		if (playerController == null) return;

		// If the game client is trying to override the plugin's effect, re-override it
		playerController.effectController.onChange(false);
	}

	@Provides
	AnimationTransmogConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AnimationTransmogConfig.class);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged e)
	{
		// When a setting in thte plugin is changed, request a DB update
		if (e.getGroup().equals("animationtransmog"))
		{
			configChanged = true;
		}
	}

	HashMap<String, String> updateDBSettings()
	{
		// Generate HashMap of settings for a given player
		String playerName = client.getLocalPlayer().getName();
		HashMap<String, String> newSettings = new HashMap<>();
		Object[] keys = configManager.configGetters.keySet().toArray();
		for (int i = 0; i < configManager.configGetters.size(); i++)
		{
			String key = (String)keys[i];
			String value = configManager.configGetters.get(key).get();
			newSettings.put(key, value);
		}

		// Set player's settings in the DB
		dbManager.setSettings(playerName, newSettings);
		return newSettings;
	}
}
