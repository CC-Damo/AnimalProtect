package me.damo1995.AnimalProtect;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdateListeners implements Listener{

	private AnimalProtect plugin;

	public UpdateListeners(AnimalProtect animalProtect) {
		plugin = animalProtect;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoinEvent(PlayerJoinEvent event){
		Player player = event.getPlayer();
		if(plugin.getConfig().getBoolean("notify-update") == true){
		if(player.hasPermission("animalprotect.notify") && AnimalProtect.update){
			player.sendMessage("An update is available: " + AnimalProtect.name + ", a " + AnimalProtect.type + " for " + AnimalProtect.version + " available at " + AnimalProtect.link);
		    player.sendMessage("Type /animalprotect -update if you would like to automatically update.");
		}
	}}
}
