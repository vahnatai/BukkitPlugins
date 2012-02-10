package com.github.vahnatai.warpcommand;

import org.bukkit.plugin.java.JavaPlugin;

public class WarpCommandPlugin extends JavaPlugin {
	
	private WarpCommandExecutor executor;
	
	@Override
	public void onEnable() {
		executor = new WarpCommandExecutor(this);
		getCommand("warp").setExecutor(executor);
	}
	
	@Override
	public void onDisable() {
		getCommand("warp").setExecutor(null);
	}

}
