package me.damo1995.AnimalProtect;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
@SuppressWarnings("deprecation")
public class LeashManager implements Listener {

	private AnimalProtect plugin;
	   long lnt;
	public LeashManager(AnimalProtect instance) {
		plugin = instance;
	}
	
	@EventHandler(priority =EventPriority.HIGHEST)
	public void leashEvent(PlayerLeashEntityEvent event){
		Player player = event.getPlayer();
		Location loc = event.getEntity().getLocation();
		String entity = event.getEventName();
		List<String> protect = plugin.getConfig().getStringList("protect-from-player");
		if(protect.contains(entity)){
			if((plugin.getWorldGuardPlugin().canBuild(player, loc) || player.hasPermission("animalprotect.bypass"))){
				event.setCancelled(false);
			}else{
				event.setCancelled(true);
		         if(plugin.getConfig().getBoolean("notify-player") == true){
		             player.sendMessage(plugin.failMsg);
		             }
		             if (plugin.getConfig().getBoolean("notify"))
		               notifyAdmin(player);
		           }
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
