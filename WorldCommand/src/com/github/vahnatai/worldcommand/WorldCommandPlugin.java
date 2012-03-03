package com.github.vahnatai.worldcommand;

import org.bukkit.plugin.java.JavaPlugin;

public class WorldCommandPlugin extends JavaPlugin {
	
	private WorldCommandExecutor executor;
	
	@Override
	public void onEnable() {
		executor = new WorldCommandExecutor(this);
		getServer().getPluginManager().registerEvents(new PortalEventListener(this), this);
		getCommand("world").setExecutor(executor);
	}
	
	@Override
	public void onDisable() {
		getCommand("world").setExecutor(null);
	}

}
