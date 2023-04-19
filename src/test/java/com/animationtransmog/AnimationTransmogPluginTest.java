package com.animationtransmog;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Map;

public class AnimationTransmogPluginTest
{
	private static void setEnv(String key, String value)
	{
		try
		{
			Map<String, String> env = System.getenv();
			Class<?> cl = env.getClass();
			Field field = cl.getDeclaredField("m");
			field.setAccessible(true);
			Map<String, String> writableEnv = (Map<String, String>) field.get(env);
			writableEnv.put(key, value);
		}
		catch (Exception e)
		{
			throw new IllegalStateException("Failed to set environment variable", e);
		}
	}

	private static void bootstrapJagexAccount() throws Exception
	{
		ProcessBuilder pb = new ProcessBuilder(
			"/bin/sh", "-c",
			"ps Eww -o command $(ps -A | grep -m 1 RuneLite | awk '{print $1}') | " +
			"sed 's/ JX/\\nJX/g' | " +
			"grep JX_"
		);
		pb.redirectErrorStream(true);
		Process process = pb.start();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;
		while ((line = reader.readLine()) != null)
		{
			String[] newVar = line.split("=");
			if (newVar.length == 2) setEnv(newVar[0], newVar[1]);
		}
		reader.close();
		process.waitFor();

		pb = new ProcessBuilder(
			"/bin/sh", "-c",
			"kill $(ps -A | grep -m 1 RuneLite | awk '{print $1}')"
		);
		pb.inheritIO();
		pb.redirectErrorStream(true);
		process = pb.start();
		process.waitFor();
	}

	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(AnimationTransmogPlugin.class);
		bootstrapJagexAccount();
		RuneLite.main(args);
	}
}