package com.github.vahnatai.worldcommand;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * warp [srcplayer] (x y z [world])||(targetplayer)
 * 
 * @author vahnatai
 */

public class WorldCommandExecutor implements CommandExecutor {
	
	private WorldCommandPlugin plugin;
	
	public WorldCommandExecutor(WorldCommandPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 2) {
			sender.sendMessage("Insufficient arguments.");
			return false;
		}
		String operation = args[0];
		String target = args[1];
		
		if (operation.equalsIgnoreCase("create")) {
			if (args.length < 3) {
				sender.sendMessage("Missing world type.");
				return false;
			}
			String type = args[2];
			Environment environment = getEnvironment(type);
			if (environment == null) {
				sender.sendMessage("Invalid world type.");
				return true;
			}
			World world = createWorld(target, environment);
			if (world == null) {
				sender.sendMessage("World '" + target + "' could not be created.");
				return true;
			}
			sender.sendMessage("World '" + target + "' created.");
			return true;
		} else if (operation.equalsIgnoreCase("unload")) {
			World world = getWorld(target);
			if (world == null) {
				sender.sendMessage("Could not find world '" + target + "' to unload it.");
				return true;
			}
			if (unloadWorld(world)) {
				sender.sendMessage("World '" + target + "' unloaded.");
				return true;
			}
			sender.sendMessage("World '" + target + "' could not be unloaded.");
			return true;
		} else if (operation.equalsIgnoreCase("delete")) {
			World world = getWorld(target);
			if (world == null) {
				sender.sendMessage("Could not find world '" + target + "' to delete it.");
				return true;
			}
			if (deleteWorld(world)) {
				sender.sendMessage("World '" + target + "' deleted.");
				return true;
			}
			sender.sendMessage("World '" + target + "' could not be deleted.");
			return true;
		} else {
			sender.sendMessage("Invalid world operation.");
			return false;
		}
	}
	
	private World createWorld(String name, Environment environment) {
		return WorldCreator.name(name).environment(environment).createWorld();
	}
	
	private boolean unloadWorld(World world) {
		return plugin.getServer().unloadWorld(world, true);
	}
	
	private boolean deleteWorld(World world) {
		if (world == null) {
			return false;
		}
		return world.getWorldFolder().delete();
	}
	
	private World getWorld(String name) {
		return plugin.getServer().getWorld(name);
	}
	
	private Environment getEnvironment(String name) {
		Environment env = null;
		if (name.equalsIgnoreCase("normal")) {
			env = Environment.NORMAL;
		} else if (name.equalsIgnoreCase("nether")) {
			env = Environment.NETHER;
		} else if (name.equalsIgnoreCase("end")) {
			env = Environment.THE_END;
		} 
		return env;
	}

}
