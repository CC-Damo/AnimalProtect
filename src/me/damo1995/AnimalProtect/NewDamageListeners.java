 package me.damo1995.AnimalProtect;
 
 import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
@SuppressWarnings("deprecation")
 public class NewDamageListeners
   implements Listener
 {
   String fail = ChatColor.DARK_RED + "You cannot attack mobs here!";
   public static AnimalProtect plugin;
   long lnt;
 
   public NewDamageListeners(AnimalProtect instance)
   {
     plugin = instance;
   }
   @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
   public void onAttacked(EntityDamageByEntityEvent event) {
     if ((event.getDamager() instanceof Player)) {
       String entity = event.getEntityType().toString();
       List<String> protect = plugin.getConfig().getStringList("protect-from-player");
       Boolean debug = Boolean.valueOf(plugin.getConfig().getBoolean("debug"));
       Player player = (Player)event.getDamager();
       Location loc = event.getEntity().getLocation();
       if (debug.booleanValue()) {
         player.sendMessage(entity);
       }
       if (protect.contains(entity)) {
         if ((plugin.getWorldGuardPlugin().canBuild(player, loc)) || (player.hasPermission("animalprotect.bypass"))) {
           event.setCancelled(false);
           if (debug.booleanValue()) {
             player.sendMessage(event.getDamager().getType().toString() + " Attacked " + event.getEntity().getType().toString());
             player.sendMessage("Attack Sucessfull");
           }
           return;
         }
         event.setCancelled(true);
         if (debug.booleanValue()) {
           player.sendMessage(event.getDamager().getType().toString() + " Attacked " + event.getEntity().getType().toString());
           player.sendMessage("Attack Failed");
         }
         if(plugin.getConfig().getBoolean("notify-player") == true){
         player.sendMessage(plugin.failMsg);
         }
         if (plugin.getConfig().getBoolean("notify"))
           notifyAdmin(player);
       }
     }
   }
 

@EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
   public void onAttackArrow(EntityDamageByEntityEvent event)
   {
     if ((event.getDamager() instanceof Projectile)) {
       Projectile arrow = (Projectile)event.getDamager();
       String entity = event.getEntity().getType().toString();
       Boolean debug = Boolean.valueOf(plugin.getConfig().getBoolean("debug"));
       List<String> pfa = plugin.getConfig().getStringList("protect-from-player");
       if (((arrow.getShooter() instanceof Player)) && (pfa.contains(entity))) {
         Player player = (Player)arrow.getShooter();
         Location loc = event.getEntity().getLocation();
         if ((plugin.getWorldGuardPlugin().canBuild(player, loc)) || (player.hasPermission("animalprotect.bypass"))) {
           event.setCancelled(false);
           if (debug.booleanValue()) {
             player.sendMessage(event.getDamager().getType().toString() + " Attacked " + event.getEntity().getType().toString());
             player.sendMessage("Attack Sucessfull");
           }
         }
         else {
           event.setCancelled(true);
           if (debug.booleanValue()) {
             player.sendMessage(event.getDamager().getType().toString() + " Attacked " + event.getEntity().getType().toString());
             player.sendMessage("Attack Failed");
           }
           if(plugin.getConfig().getBoolean("notify-player") == true){
           player.sendMessage(plugin.failMsg);
           }
           if (plugin.getConfig().getBoolean("notify"))
             notifyAdmin(player);
         }
       }
     }
   }
 
   @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
   public void onMonsterDamage(EntityDamageByEntityEvent event)
   {
     if ((event.getDamager() instanceof Monster)) {
       List<String> pfm = plugin.getConfig().getStringList("protect-from-monsters");
       if (pfm.contains(event.getEntityType().toString()))
         event.setCancelled(true);
     }
   }
 
   public void notifyAdmin(Player player) {
     long timesincelastnote = System.currentTimeMillis() - this.lnt;
     if (timesincelastnote > plugin.getConfig().getInt("notify-interval") * 1000) {
       this.lnt = System.currentTimeMillis();
       if (plugin.getConfig().getBoolean("notify"))
         for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers())
         {
           if ((!onlinePlayer.hasPermission("animalprotect.notify")) && 
             (!onlinePlayer.isOp())) continue;
           onlinePlayer.sendMessage(plugin.fail + player.getName() + " " + plugin.adminNotifyMsg);
           plugin.logMessage(plugin.fail + player.getName() + " " + plugin.adminNotifyMsg);
         }
     }
   }
 }
