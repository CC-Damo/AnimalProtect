package me.damo1995.AnimalProtect;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Golem;
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
	public void handAttack(EntityDamageByEntityEvent event){
		Location loc = event.getEntity().getLocation();
		RegionManager rm = plugin.getWorldGuardPlugin().getRegionManager(loc.getWorld());
		@SuppressWarnings("unused")
		ApplicableRegionSet set = rm.getApplicableRegions(loc);

		//-------- Non-Hostile Mob Check --------\\
		if(event.getDamager() instanceof Player && event.getEntity() instanceof Animals){
			Player player = (Player) event.getDamager();
			if(event.isCancelled()){return;}
				if(plugin.getWorldGuardPlugin().canBuild(player, loc) || player.hasPermission("animalprotect.bypass")){
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
		}
		
		//-------- END Non-Hostile Mob Check --------\\
		
		
		//-------- Hostile Mob Check --------\\
		if(event.getDamager() instanceof Player && event.getEntity() instanceof Monster && plugin.getConfig().getBoolean("protect-hostiles") == true){
			Player player = (Player)event.getDamager();
			if(event.isCancelled() == true){return;}
			if(plugin.getWorldGuardPlugin().canBuild(player, loc) || player.hasPermission("animalprotect.bypass")){
				event.setCancelled(false);
				if(DamageListeners.plugin.getConfig().getBoolean("debug") == true){
				player.sendMessage("DEBUG: Attacked Mob");
				player.sendMessage("DEBUG: ATTACK SUCCESSFULL");
				}
			}
			else{
				event.setCancelled(true);
				if(DamageListeners.plugin.getConfig().getBoolean("debug") == true){
				player.sendMessage("DEBUG: ATTACKED Mob");
				player.sendMessage("DEBUG: ATTACK FAILED");
				}
				player.sendMessage(fail);
				if(plugin.getConfig().getBoolean("notify") == true){
				notifyAdmin(player);
				}
			}
		}
		//-------- END Hostile Mob Check --------\\
		
		
		//-------- NPC Mob Check --------\\
		if(event.getDamager() instanceof Player && event.getEntity() instanceof NPC && plugin.getConfig().getBoolean("protect-villiger") == true){
			Player player = (Player)event.getDamager();
			if(plugin.getWorldGuardPlugin().canBuild(player, loc) || player.hasPermission("animalprotect.bypass")){
				event.setCancelled(false);
				if(DamageListeners.plugin.getConfig().getBoolean("debug") == true){
				player.sendMessage("DEBUG: Attacked Mob");
				player.sendMessage("DEBUG: ATTACK SUCCESSFULL");
				}
			}
			else{
				event.setCancelled(true);
				if(DamageListeners.plugin.getConfig().getBoolean("debug")  == true){
				player.sendMessage("DEBUG: ATTACKED Mob");
				player.sendMessage("DEBUG: ATTACK FAILED");
				}
				player.sendMessage(fail);
				if(plugin.getConfig().getBoolean("notify") == true){
				notifyAdmin(player);
				}
			}
		}
		//-------- END NPC Mob Check --------\\
		
		//-------- Golem Mob Check --------\\
		if(event.getDamager() instanceof Player && event.getEntity() instanceof Golem && plugin.getConfig().getBoolean("protect-golems") == true){
			Player player = (Player)event.getDamager();
			if(plugin.getWorldGuardPlugin().canBuild(player, loc) || player.hasPermission("animalprotect.bypass")){
				event.setCancelled(false);
				if(DamageListeners.plugin.getConfig().getBoolean("debug") == true){
				player.sendMessage("DEBUG: Attacked Mob");
				player.sendMessage("DEBUG: ATTACK SUCCESSFULL");
				}
			}
			else{
				event.setCancelled(true);
				if(DamageListeners.plugin.getConfig().getBoolean("debug")  == true){
				player.sendMessage("DEBUG: ATTACKED Mob");
				player.sendMessage("DEBUG: ATTACK FAILED");
				}
				player.sendMessage(fail);
				if(plugin.getConfig().getBoolean("notify") == true){
				notifyAdmin(player);
				}
			}
		}
		//-------- Golem NPC Mob Check --------\\
		}	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void rangeAttack(EntityDamageByEntityEvent event){
		
		//-------- Non-Hostile Mob Check --------\\
		if(event.getDamager() instanceof Arrow){
        
			
			Projectile arrow = (Arrow)event.getDamager();

			if(arrow.getShooter() instanceof Player && event.getEntity() instanceof Animals){
				Player player = (Player)arrow.getShooter();
				Location loc = event.getEntity().getLocation();
				if(plugin.getWorldGuardPlugin().canBuild(player, loc) || player.hasPermission("animalprotect.bypass")){
					event.setCancelled(false);
					if(plugin.getConfig().getBoolean("debug") == true){
					player.sendMessage("DEBUG: ATTACKED Mob");
					player.sendMessage("DEBUG: ATTACK SUCCESSFUL");
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
                                                                                            }
		}
		
		//-------- END Non-Hostile Mob Check --------\\
		
		//-------- Hostile Mob Check --------\\
		if(event.getDamager() instanceof Arrow){
			Projectile arrow = (Arrow)event.getDamager();

			if(arrow.getShooter() instanceof Player && event.getEntity() instanceof Monster && plugin.getConfig().getBoolean("protect-hostiles") == true){
				Player player = (Player)arrow.getShooter();
				Location loc = event.getEntity().getLocation();
				if(plugin.getWorldGuardPlugin().canBuild(player, loc) || player.hasPermission("animalprotect.bypass")){
					event.setCancelled(false);
					if(plugin.getConfig().getBoolean("debug") == true){
					player.sendMessage("DEBUG: ATTACKED Mob");
					player.sendMessage("DEBUG: ATTACK SUCCESSFUL");
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
			}
		}
		
		//-------- END Hostile Mob Check --------\\
		
		//-------- Villager Mob Check --------\\
		if(event.getDamager() instanceof Arrow){
			Projectile arrow = (Arrow)event.getDamager();

			if(arrow.getShooter() instanceof Player && event.getEntity() instanceof NPC && plugin.getConfig().getBoolean("protect-villager") == true){
				Player player = (Player)arrow.getShooter();
				Location loc = event.getEntity().getLocation();
				if(plugin.getWorldGuardPlugin().canBuild(player, loc) || player.hasPermission("animalprotect.bypass")){
					event.setCancelled(false);
					if(plugin.getConfig().getBoolean("debug") == true){
					player.sendMessage("DEBUG: ATTACKED Mob");
					player.sendMessage("DEBUG: ATTACK SUCCESSFUL");
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
			}
		}
		//-------- END Villager Mob Check --------\\
		
		//-------- Golem Mob Check --------\\
		if(event.getDamager() instanceof Arrow){
			Projectile arrow = (Arrow)event.getDamager();

			if(arrow.getShooter() instanceof Player && event.getEntity() instanceof Golem && plugin.getConfig().getBoolean("protect-golems") == true){
				Player player = (Player)arrow.getShooter();
				Location loc = event.getEntity().getLocation();
				if(plugin.getWorldGuardPlugin().canBuild(player, loc) || player.hasPermission("animalprotect.bypass")){
					event.setCancelled(false);
					if(plugin.getConfig().getBoolean("debug") == true){
					player.sendMessage("DEBUG: ATTACKED Mob");
					player.sendMessage("DEBUG: ATTACK SUCCESSFUL");
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
			}
		}
		//-------- END Golem Mob Check --------\\
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
	

