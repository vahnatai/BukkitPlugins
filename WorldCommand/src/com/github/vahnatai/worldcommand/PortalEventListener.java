package com.github.vahnatai.worldcommand;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class PortalEventListener implements Listener {

	private WorldCommandPlugin plugin;
	
	public PortalEventListener(WorldCommandPlugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPortalCreate(EntityCreatePortalEvent event) {
		Entity entity = event.getEntity();
		if (!(entity instanceof Player)) {
			return;
		}
		Player player = (Player)entity;
		player.sendMessage("You created a portal.");
	}
	
	@EventHandler
	public void onPortalEnter(EntityPortalEnterEvent event) {
		Entity entity = event.getEntity();
		if (!(entity instanceof Player)) {
			return;
		}
		Player player = (Player)entity;
		player.sendMessage("You entered a portal.");
	}
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
		event.getPlayer().sendMessage("You teleported.");
	}
	
	@EventHandler
	public void onSwordBlock(PlayerInteractEvent event) {
	  Player player = event.getPlayer();
	  if (!player.isOp()) {
		  return;
	  }
	  if (event.getAction() == Action.RIGHT_CLICK_BLOCK  && player.getItemInHand().equals(Material.DIAMOND_SWORD)) {
		  Fireball fireball = player.getWorld().spawn(player.getLocation().add(0, 3, 0), Fireball.class);
		  fireball.setShooter(player);
		  fireball.setYield(0);
		  fireball.setFireTicks(0);
		  fireball.setDirection(player.getLocation().getDirection().multiply(Integer.MAX_VALUE).subtract(new Vector(0,3,0)));
	  }
	}
	
	@EventHandler
	public void onStrikeDiamond(BlockDamageEvent event) {
		Player player = event.getPlayer();
		if (!player.isOp()) {
			return;
		}
		ItemStack stack = player.getItemInHand();
		if (!stack.getType().equals(Material.DIAMOND_SWORD)) {
			return;
		}
		if (!event.getBlock().getType().equals(Material.DIAMOND_BLOCK)) {
			return;
		}

		Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
		enchantments.put(Enchantment.DAMAGE_ALL, 5);
		enchantments.put(Enchantment.DAMAGE_UNDEAD, 5);
		enchantments.put(Enchantment.FIRE_ASPECT, 2);
		enchantments.put(Enchantment.KNOCKBACK, 2);
		stack.addEnchantments(enchantments);
		
		player.sendMessage("Your sword glows briefly.");
	}
	
}
