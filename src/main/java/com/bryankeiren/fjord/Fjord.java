package com.bryankeiren.fjord;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public final class Fjord extends JavaPlugin {
	@Override
	public void onDisable() {
	}

	@Override
	public void onEnable() {
		getCommand("fjord").setExecutor(new FjordCommandExecutor(this));
	}

	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		return new FjordChunkGenerator(this);
	}
}
