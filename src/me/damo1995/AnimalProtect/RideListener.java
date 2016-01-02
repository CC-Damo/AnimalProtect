 package me.damo1995.AnimalProtect;
 
 import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
@SuppressWarnings("deprecation")
 public class RideListener
   implements Listener
 {
   public static AnimalProtect plugin;
   long lnt;
 
   public RideListener(AnimalProtect I)
   {
     plugin = I;
   }
   @EventHandler(priority=EventPriority.LOWEST)
   public void onRideEvent1(PlayerInteractEntityEvent event){
	  if(plugin.getConfig().getBoolean("horse-rideprotect") == true){
	   Player p = event.getPlayer();
	   Entity e = event.getRightClicked();
	   
	   if((e instanceof Horse)){
		   Horse h = (Horse)e;
		   if((h.getOwner() == p) || (p.hasPermission("animalprotect.bypass")) || h.getOwner() == null){
			   event.setCancelled(false);
		   }else{
		         if(plugin.getConfig().getBoolean("notify-player") == true){ 
		             p.sendMessage(plugin.ridemsg);
		             event.setCancelled(true);
		             }
		             if (plugin.getConfig().getBoolean("notify"))
		               notifyAdmin(p);
		           }
		   }
	  }
		   }

	  
 

public void notifyAdmin(Player player)
   {
     long timesincelastnote = System.currentTimeMillis() - this.lnt;
     if (timesincelastnote > plugin.getConfig().getInt("notify-interval") * 1000) {
       this.lnt = System.currentTimeMillis();
       if (NewDamageListeners.plugin.getConfig().getBoolean("notify"))
         for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers())
         {
           if ((!onlinePlayer.hasPermission("animalprotect.notify")) && 
             (!onlinePlayer.isOp())) continue;
           onlinePlayer.sendMessage(ChatColor.RED + player.getName() + " " + plugin.failMsg);
           NewDamageListeners.plugin.logMessage(player.getName() + " " + plugin.failMsg);
         }
     }
   }
 }
