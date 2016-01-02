 package me.damo1995.AnimalProtect;
 
 import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
 
 public class CommandHandler
   implements CommandExecutor
 {
   private AnimalProtect plugin;
 
   public CommandHandler(AnimalProtect plugin)
   {
     this.plugin = plugin;
   }
   
// - /Animalprotect - \\
   public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
	   
	   
	   // ANIMALPROTECT CMD ////
     if (cmd.getName().equalsIgnoreCase("animalprotect") && sender instanceof Player) {
         sender.sendMessage(ChatColor.YELLOW + "+++++++++AnimalProtect++++++++++");
         sender.sendMessage(ChatColor.GREEN + "An animal friendly plugin");
         sender.sendMessage(ChatColor.RED + "+ Version: " + this.plugin.getDescription().getVersion());
         sender.sendMessage(ChatColor.LIGHT_PURPLE + "+ Developer: " + this.plugin.getDescription().getAuthors());
         sender.sendMessage(ChatColor.AQUA + "http://www.dev.bukkit.org/server-mods/animalprotect");
         sender.sendMessage(ChatColor.YELLOW + "+++++++++++++++++++++++++++++");
         return true;
       }
     
     
     // APRELOAD CMD ////
	   if(cmd.getName().equalsIgnoreCase("apreload") && sender.hasPermission("animalprotect.reload")){
	         this.plugin.reloadConfig();
	         
	         this.plugin.validateConfig();
	         sender.sendMessage(this.plugin.success + "Configuration Reloaded!");
	         return true;
	   }
	   
	   
	   // APLIST CMD //
	   if(cmd.getName().equalsIgnoreCase("aplist")  && sender.hasPermission("animalprotect.list" )){
		   if(args.length == 0){
			   return false;
		   }
	   if(args[0].equalsIgnoreCase("player")){
		   List<String> pfp = this.plugin.getConfig().getStringList("protect-from-player");
	         sender.sendMessage(this.plugin.success + "The following are protected from players");
	         for (String i : pfp) {
	           sender.sendMessage(i);
	         }
	         return true;
	   }
	   if(args[0].equalsIgnoreCase("mobs") && sender.hasPermission("animalprotect.list")){
		   List<String> pfp = this.plugin.getConfig().getStringList("protect-from-monsters");
	         sender.sendMessage(this.plugin.success + "The following are protected from mobs");
	         for (String i : pfp){
	           sender.sendMessage(i);
	       } 
	         return true;
	         }
	 
	}
	   
	   
	   // APUPDATE CMD//
	   if(cmd.getName().equalsIgnoreCase("apupdate") && sender.hasPermission("animalprotect.update")){
		   plugin.update();
    	   return true;
       }
	   
	   
	   //APHT CMD///
	   if(cmd.getName().equalsIgnoreCase("apht") && sender.hasPermission("animalprotect.apht")){
		   Player p = Bukkit.getPlayer(sender.getName().toString());
		   if(args.length == 0){
			   return false;
		   }
		   if(p.isInsideVehicle() && p.getVehicle() instanceof Horse){
			 Horse h = (Horse)p.getVehicle();
			 if(args[0].equalsIgnoreCase("zombie")){
				 h.setVariant(Horse.Variant.UNDEAD_HORSE);
				 p.sendMessage("You changed the Horse Type!");
				 h.setOwner(p);
				 return true;
			 }
			 if(args[0].equalsIgnoreCase("skell")){
				 p.sendMessage("You changed the Horse Type!");
				 h.setOwner(p);
				 h.setVariant(Horse.Variant.SKELETON_HORSE);
				 return true;
			 }
			 if(args[0].equalsIgnoreCase("normal")){
				 p.sendMessage("You changed the Horse Type!");
				 h.setOwner(p);
				 h.setVariant(Horse.Variant.HORSE);
				 return true;
			 }
		   }
	   }
	   
	   //APUNCLAIM///
	   if(cmd.getName().equalsIgnoreCase("apunclaim") && sender.hasPermission("animalprotect.unclaim")){
		   Player p = Bukkit.getPlayer(sender.getName().toString());
		   if(p.isInsideVehicle() && p.getVehicle() instanceof Horse){
			 Horse h = (Horse)p.getVehicle();
			 
			 if(h.getOwner() == p || p.hasPermission("animalprotect.bypass")){
				 h.setOwner(null);
				 p.sendMessage(plugin.success + "You Unclaimed this horse!");
				 return true;
			 }
		   }
	   }

	   //APOWNER///
	   if(cmd.getName().equalsIgnoreCase("apowner") && sender.hasPermission("animalprotect.owner")){
		   Player p = Bukkit.getPlayer(sender.getName().toString());
		   if(p.isInsideVehicle() && p.getVehicle() instanceof Horse){
			 Horse h = (Horse)p.getVehicle();
			 
			 p.sendMessage(plugin.success + "The owner of this horse: " + h.getOwner().toString());
		   }
	   }
	   
     return false;
   }
   }
