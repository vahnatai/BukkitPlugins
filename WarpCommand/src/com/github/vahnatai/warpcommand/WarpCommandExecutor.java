package com.github.vahnatai.warpcommand;

import java.util.Arrays;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * warp [srcplayer] (x y z [world])||(targetplayer)
 * 
 * @author vahnatai
 */

public class WarpCommandExecutor implements CommandExecutor {
	
	private static Logger logger = Logger.getLogger(WarpCommandExecutor.class.getCanonicalName());
	
	private WarpCommandPlugin plugin;
	
	public WarpCommandExecutor(WarpCommandPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player source = null;
		Player target = null;
		Location targetLocation = null;
		
		if (args.length == 0) {
			return false;
		}
		
		//check for source player name
		source = getPlayer(args[0]);
		if (source != null){
			if (args.length == 1) {
				target = source;
				source = null;
			}
			args = Arrays.copyOfRange(args, 1, args.length);
		}
		
		
		if (source == null) {
			// first arg isn't player, maybe you mean yourself
			if (!(sender instanceof Player)) {
				sender.sendMessage("You are not a player and cannot be teleported.");
				return false;
			}
			source = (Player)sender;
		}
		assert (source != null);
		
		if (args.length == 1) {
			// target should be a player
			target = getPlayer(args[0]);
			if (target == null) {
				source.sendMessage(String.format("Cannot find target player '%s'.", args[0]));
				return false;
			}
		}
		
		if (target != null) {
			return warp(source, target);
		}
		
		//target must be a location at this point
		targetLocation = getLocation(source, args);
		
		if (targetLocation == null) {
			source.sendMessage(String.format("Cannot find target location '%s'.", Arrays.toString(args)));
			return false;
		}
		
		return warp(source, targetLocation);
	}
	
	private Location getLocation(Player source, String... args) {
		int x;
		int y;
		int z;
		World world;
		
		if (args.length < 3) {
			return null;
		}
		x = Integer.parseInt(args[0]);
		y = Integer.parseInt(args[1]);
		z = Integer.parseInt(args[2]);

		if (args.length >= 4) {
			world = getWorld(args[3]);
		} else {
			world = source.getWorld();
		}
		return new Location(world, x, y, z);
	}
	
	private Player getPlayer(String name) {
		return plugin.getServer().getPlayer(name);
	}
	
	private World getWorld(String worldname) {
		return plugin.getServer().getWorld(worldname);
	}
	
	private boolean warp(Player source, Location location) {
		String message = String.format("Teleporting %s to %s...", source.getName(), location.toString());
		source.sendMessage(message);
		logger.info(message);
		source.teleport(location);
		return true;
	}

	private boolean warp(Player source, Player target) {
		String message = String.format("Teleporting %s to %s...", source.getName(), target.getName());
		source.sendMessage(message);
		logger.info(message);
		source.teleport(target);
		return true;
	}

}
