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
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;

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

	@Inject
	private ChatMessageManager chatMessageManager;

	boolean configChanged = true;
	boolean welcomeMessagePlayed = false;

	DatabaseManager dbManager;

	AnimationTransmogConfigManager configManager;
	AnimationTypes animationTypes;
	AnimationPlayerController animationPlayerController;

	HashMap<String, PlayerController> players;

	String messageFromAnotherThread = "";

	@Override
	protected void startUp() throws Exception
	{
		log.info("Animation Transmog started!");
		dbManager = new DatabaseManager();
		configManager = new AnimationTransmogConfigManager(config);
		animationTypes = new AnimationTypes();
		animationPlayerController = new AnimationPlayerController(configManager);
		players = new HashMap<>();
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Animation Transmog stopped!");
	}

	@Subscribe
	public void onPlayerSpawned(PlayerSpawned e)
	{
		String playerName = e.getActor().getName();
		addNewPlayer(playerName, e.getActor());
	}

	void addNewPlayer(String playerName, Actor actor)
	{
		if (playerName == null) return;

		// If not using the DB, or player is the local player, get settings locally
		if (!configManager.getCanUseDB() || actor == client.getLocalPlayer())
		{
			// If not using the DB, ignore other players
			if (actor != client.getLocalPlayer()) return;

			HashMap<String, String> settings = getLocalSettings();
			PlayerController playerController = new PlayerController(dbManager, animationTypes, actor, client, settings);
			players.put(playerName, playerController);

			return;
		}

		// Only add new player if they are not already in the players Hash Map
		if (!players.containsKey(playerName))
		{
			dbManager.getSettings(playerName, (settings) -> {
				// If they are not in the DB, just add a null under their name to be ignored
				if (settings.size() == 0)
				{
					players.put(playerName, null);
					return;
				}
				PlayerController playerController = new PlayerController(dbManager, animationTypes, actor, client, settings);
				players.put(playerName, playerController);
			});
		}
		else
		{
			// If player already exists in Hash Map, and has a valid PlayerController, simply update player actor
			PlayerController playerController = players.get(playerName);
			if (playerController == null) return;

			playerController.effectController.setPlayer(actor, client);
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			// Setup animationPlayerController
			animationPlayerController.setPlayer(client.getLocalPlayer(), client);

			if (!configManager.getCanUseDB() && !welcomeMessagePlayed)
			{
				welcomeMessagePlayed = true;
				chatMessageManager.queue(
					QueuedMessage.builder()
						.type(ChatMessageType.CONSOLE)
						.runeLiteFormattedMessage(
							new ChatMessageBuilder()
								.append(ChatColorType.HIGHLIGHT)
								.append("Animation Transmog Plugin: To enable new functionality, please go to the plugin settings and check \"Enable Multiplayer\" on.")
								.build()
						)
						.build()
				);
			}
		}
	}

	@Subscribe
	public void onClientTick(ClientTick event)
	{
		if (!messageFromAnotherThread.equals(""))
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", messageFromAnotherThread, null);
			messageFromAnotherThread = "";
		}

		Player local = client.getLocalPlayer();
		if (local == null || local.getName() == null) return;

		// Update animation player
		animationPlayerController.update();

		// Update timers on player's effect controllers
		for (PlayerController player : players.values())
		{
			if (player != null) player.effectController.updateTimer();
		}

		// When the local plugin configs are changed, update the DB if applicable, and then update the playerController
		if (configChanged)
		{
			configChanged = false;
			HashMap<String, String> settings = updateSettings();

			if (!players.containsKey(local.getName())) addNewPlayer(local.getName(), client.getLocalPlayer());
			else players.get(local.getName()).setSettings(settings);
		}
	}

	@Subscribe
	public void onBeforeRender(BeforeRender event)
	{
		// For each player stored, run the player's effectController
		for (PlayerController player : players.values())
		{
			if (player != null) player.effectController.onBeforeRender();
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
		// When a setting in the plugin is changed, request a DB update
		if (e.getGroup().equals("animationtransmog"))
		{
			configChanged = true;
		}
	}

	HashMap<String, String> updateSettings()
	{
		// Get local settings
		HashMap<String, String> newSettings = getLocalSettings();

		// Set player's settings in the DB, if opted into the DB
		String playerName = client.getLocalPlayer().getName();
		if (configManager.getCanUseDB()) dbManager.setSettings(playerName, newSettings);

		return newSettings;
	}

	HashMap<String, String> getLocalSettings()
	{
		// Generate HashMap of settings for a given player
		HashMap<String, String> settings = new HashMap<>();
		Object[] keys = configManager.configGetters.keySet().toArray();
		for (int i = 0; i < configManager.configGetters.size(); i++)
		{
			String key = (String)keys[i];
			String value = configManager.configGetters.get(key).get();
			settings.put(key, value);
		}
		return settings;
	}
}
