package me.damo1995.AnimalProtect;
 
import java.util.List;
 
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
 
import com.sk89q.worldguard.protection.managers.RegionManager;
 
@SuppressWarnings("unused")
public class NewDamageListeners implements Listener {
 
String fail = ChatColor.DARK_RED + "You cannot attack mobs here!";
public static AnimalProtect plugin;

long lnt;
 
public NewDamageListeners(AnimalProtect instance) {
plugin = instance;
}
 
@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=true)
public void onAttacked(EntityDamageByEntityEvent event){
	if(event.getDamager() instanceof Player){
		String entity = event.getEntityType().toString();
		List<String> protect = plugin.getConfig().getStringList("protect-from-player");
		Boolean debug = plugin.getConfig().getBoolean("debug");
		Player player = (Player)event.getDamager();
		Location loc = event.getEntity().getLocation();
		if(debug == true){
			player.sendMessage(entity);
		}
		RegionManager rm = plugin.getWorldGuardPlugin().getRegionManager(loc.getWorld());
		if(protect.contains(entity)){
		if(plugin.getWorldGuardPlugin().canBuild(player, loc) || player.hasPermission("animalprotect.bypass")){
			event.setCancelled(false);
			if(debug == true){
				player.sendMessage(event.getDamager().getType().toString() + " Attacked " + event.getEntity().getType().toString());
				player.sendMessage("Attack Sucessfull");
			}
			return;
		}else{
			event.setCancelled(true);
			if(debug == true){
			player.sendMessage(event.getDamager().getType().toString() + " Attacked " + event.getEntity().getType().toString()); 
			player.sendMessage("Attack Failed");
			}
			player.sendMessage(fail);
			if(plugin.getConfig().getBoolean("notify") == true){
			notifyAdmin(player);
			}
		}
			
		}
	
	}
}
@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=true)
public void onAttackArrow(EntityDamageByEntityEvent event){
	if(event.getDamager() instanceof Arrow){
		Projectile arrow = (Arrow)event.getDamager();
		String entity = event.getEntity().getType().toString();
		Boolean debug = plugin.getConfig().getBoolean("debug");
		List<String> pfa = plugin.getConfig().getStringList("protect-from-player");
		if(arrow.getShooter() instanceof Player && pfa.contains(entity)){
			Player player = (Player)arrow.getShooter();
			Location loc = event.getEntity().getLocation();
			if(plugin.getWorldGuardPlugin().canBuild(player, loc) || player.hasPermission("animalprotect.bypass")){
				event.setCancelled(false);
				if(debug == true){
					player.sendMessage(event.getDamager().getType().toString() + " Attacked " + event.getEntity().getType().toString());
					player.sendMessage("Attack Sucessfull");
				}
			}
			else{
				event.setCancelled(true);
				if(debug == true){
					player.sendMessage(event.getDamager().getType().toString() + " Attacked " + event.getEntity().getType().toString()); 
					player.sendMessage("Attack Failed");
				}
				player.sendMessage(fail);
				if(plugin.getConfig().getBoolean("notify") == true){
				notifyAdmin(player);
				}
			}
		}
	}
	
}

@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=true)
public void onMonsterDamage(EntityDamageByEntityEvent event){
	if(event.getDamager() instanceof Monster){
		List<String> pfm = plugin.getConfig().getStringList("protect-from-monsters");
		if(pfm.contains(event.getEntityType().toString())){
			event.setCancelled(true);
		}
	}
}
public void notifyAdmin(Player player){
	long timesincelastnote = System.currentTimeMillis() - lnt;
	if(timesincelastnote > plugin.getConfig().getInt("notify-interval") * 1000){
		lnt = System.currentTimeMillis();
		if (NewDamageListeners.plugin.getConfig().getBoolean("notify") == true) {
		for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
			// Get a list of online players and check if they have permission/op //
			if (onlinePlayer.hasPermission("animalprotect-notify")
					|| onlinePlayer.isOp()) {
				onlinePlayer.sendMessage(plugin.fail + player.getName() + " " + "Attempted to kill protected animals");
				NewDamageListeners.plugin.logMessage(player.getName() + " " + "Attempted to kill protected animals");
			}
		}
	}
}
	return;
}
}