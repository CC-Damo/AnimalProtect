package me.damo1995.AnimalProtect;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;


public class DamageListeners implements Listener {
	long lnt;
	public static AnimalProtect plugin;
	String fail = ChatColor.DARK_RED + "You cannot attack mobs here!";
	
	public DamageListeners(AnimalProtect instance) {
		DamageListeners.plugin = instance;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamageEvent(EntityDamageByEntityEvent event){
		boolean debug = DamageListeners.plugin.getConfig().getBoolean("debug");
		Location loc = event.getEntity().getLocation();
		RegionManager rm = plugin.getWorldGuardPlugin().getRegionManager(loc.getWorld());
		@SuppressWarnings("unused")
		ApplicableRegionSet set = rm.getApplicableRegions(loc);
		//ANIMAL//
		//Check if its a farm/non hostile mob being attacked.
		if(event.getDamager() instanceof Player && event.getEntity() instanceof Animals){
			Player player = (Player) event.getDamager();
			if(event.isCancelled()){return;}
				if(plugin.getWorldGuardPlugin().canBuild(player, loc)){
					event.setCancelled(false);
					if(plugin.getConfig().getBoolean("debug") == true){
					player.sendMessage("DEBUG: Attacked Mob");
					player.sendMessage("DEBUG: ATTACK SUCCESSFULL");
					}
				}
				else{
					event.setCancelled(true); 
					if(plugin.getConfig().getBoolean("debug") == true){
					player.sendMessage("DEBUG: ATTACKED Mob");
					player.sendMessage("DEBUG: ATTACK FAILED");
					}
					player.sendMessage(fail);
					if(plugin.getConfig().getBoolean("notify") == true){
					notifyAdmin(player);
					}
					}
		}//Arrow Capture
		if(event.getDamager().getType() == EntityType.ARROW && event.getEntity() instanceof Animals){
			Projectile arrow = (Arrow)event.getDamager();
			Player player = (Player)arrow.getShooter();
			if(arrow.getShooter() instanceof Player){
				if(plugin.getWorldGuardPlugin().canBuild(player, loc)){
					event.setCancelled(false);
					if(debug == true){
					player.sendMessage("DEBUG: ATTACKED Mob");
					player.sendMessage("DEBUG: ATTACK SUCCESSFUL");
					}
				}
				else{
					event.setCancelled(true);
					if(debug == true){
					player.sendMessage("DEBUG: ATTACKED Mob");
					player.sendMessage("DEBUG: ATTACK FAILED");
					}
					player.sendMessage(fail);
					if(plugin.getConfig().getBoolean("notify") == true){
					notifyAdmin(player);
					}
					
				
				}
			}

		}
		//ANIMAL//	
		
		
		//HOSTILE//
		//Check if a hostile is being attacked and if to protect them.
		if(event.getDamager() instanceof Player && event.getEntity() instanceof Monster && plugin.getConfig().getBoolean("protect-hostiles") == true){
			Player player = (Player)event.getDamager();
			if(event.isCancelled() == true){return;}
			if(plugin.getWorldGuardPlugin().canBuild(player, loc)){
				event.setCancelled(false);
				if(debug == true){
				player.sendMessage("DEBUG: Attacked Mob");
				player.sendMessage("DEBUG: ATTACK SUCCESSFULL");
				}
			}
			else{
				event.setCancelled(true);
				if(debug == true){
				player.sendMessage("DEBUG: ATTACKED Mob");
				player.sendMessage("DEBUG: ATTACK FAILED");
				}
				player.sendMessage(fail);
				if(plugin.getConfig().getBoolean("notify") == true){
				notifyAdmin(player);
				}
			}
		}
		
		//Arrow capture for above ^^^^^^^
		if(event.getDamager().getType() == EntityType.ARROW && event.getEntity() instanceof Monster && plugin.getConfig().getBoolean("protect-hostiles") == true){
			Projectile arrow = (Arrow)event.getDamager();
			Player shooter = (Player)arrow.getShooter();
			if(arrow.getShooter() instanceof Player){
				if(plugin.getWorldGuardPlugin().canBuild(shooter, event.getEntity().getLocation())){
					event.setCancelled(false);
					if(debug == true){
					shooter.sendMessage("DEBUG: ATTACKED Mob");
					shooter.sendMessage("DEBUG: ATTACK SUCCESSFUL");
					}
				}else{
					event.setCancelled(true);
					if(debug == true){
					shooter.sendMessage("DEBUG: ATTACKED Mob");
					shooter.sendMessage("DEBUG: ATTACK FAILED");
					}
					shooter.sendMessage(fail);
					if(plugin.getConfig().getBoolean("notify") == true){
					this.notifyAdmin(shooter);
					}
				}
			}
			
		}
	//HOSTILE//	
		
		
		
		//NPC//
		//Check if a villiger is being attacked and if to protect them.
		if(event.getDamager() instanceof Player && event.getEntity() instanceof NPC && plugin.getConfig().getBoolean("protect-villiger") == true){
			Player player = (Player)event.getDamager();
			if(event.isCancelled() == true){return;}
			if(plugin.getWorldGuardPlugin().canBuild(player, loc)){
				event.setCancelled(false);
				if(debug == true){
				player.sendMessage("DEBUG: Attacked Mob");
				player.sendMessage("DEBUG: ATTACK SUCCESSFULL");
				}
			}
			else{
				event.setCancelled(true);
				if(debug  == true){
				player.sendMessage("DEBUG: ATTACKED Mob");
				player.sendMessage("DEBUG: ATTACK FAILED");
				}
				player.sendMessage(fail);
				if(plugin.getConfig().getBoolean("notify") == true){
				notifyAdmin(player);
				}
			}
		}
		
		//Arrow capture for above ^^^^^^^
		if(event.getDamager().getType() == EntityType.ARROW && event.getEntity() instanceof NPC && plugin.getConfig().getBoolean("protect-villiger") == true){
			Projectile arrow = (Arrow)event.getDamager();
			Player shooter = (Player)arrow.getShooter();
			if(arrow.getShooter() instanceof Player){
				if(plugin.getWorldGuardPlugin().canBuild(shooter, event.getEntity().getLocation())){
					event.setCancelled(false);
					if(debug == true){
					shooter.sendMessage("DEBUG: ATTACKED Mob");
					shooter.sendMessage("DEBUG: ATTACK SUCCESSFUL");
					}
				}else{
					event.setCancelled(true);
					if(debug == true){
					shooter.sendMessage("DEBUG: ATTACKED Mob");
					shooter.sendMessage("DEBUG: ATTACK FAILED");
					}
					shooter.sendMessage(fail);
					if(plugin.getConfig().getBoolean("notify") == true){
					this.notifyAdmin(shooter);
					}
				}
			}
			
		}
		//NPC//
		}	
	
	//Notify Admin List.
	public void notifyAdmin(Player player){
		long timesincelastnote = System.currentTimeMillis() - lnt;
		if(timesincelastnote > plugin.getConfig().getInt("notify-interval") * 1000){
			lnt = System.currentTimeMillis();
			if (DamageListeners.plugin.getConfig().getBoolean("notify") == true) {
			for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
				// Get a list of online players and check if they have permission/op //
				if (onlinePlayer.hasPermission("animalprotect-notify")
						|| onlinePlayer.isOp()) {
					onlinePlayer.sendMessage(plugin.fail + player.getName() + " " + "Attempted to kill protected animals");
					DamageListeners.plugin.logMessage(player.getName() + " " + "Attempted to kill protected animals");
				}
			}
		}
	}
		return;
	}

}
	
