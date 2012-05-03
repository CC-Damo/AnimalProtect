package me.damo1995.AnimalProtect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class AnimalProtect extends JavaPlugin{
	Logger log = Logger.getLogger("Minecraft");

	String success = ChatColor.GREEN + "[AnimalProtect]: ";
	
	String fail = ChatColor.RED + "[AnimalProtect]: ";
	
	String protectH = "";	
	
	String mlversion = "";
	public boolean outdated = false;

	public final DamageListeners dl = new DamageListeners(this);
	public final VersionCheck vc = new VersionCheck(this);
	
	//Enable stuff
	public void onEnable(){
		//Setting string for Command Info.
		if(this.getConfig().getBoolean("protect-hostiles") == true){
			this.protectH = "Yes";
		}
		else{ this.protectH = "No";}
		
		//event registration
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(this.dl, this);
		pm.registerEvents(vc, this);
		
		//Log msg
		this.logMessage("Enabled!");
		//Check for WorldGuard
		getWorldGuardPlugin();
		//CFG Setup
		setupConfig();
		//Check for Updates
		updateCheck();
		
	}
	
	
	public void onDisable(){
		//Log MSG disabled.
		this.logMessage("Disabled!");
		
	}
	
	//WorldGuard Check
	public WorldGuardPlugin getWorldGuardPlugin(){
		Plugin plugin = this.getServer().getPluginManager().getPlugin("WorldGuard");
		PluginManager pm = this.getServer().getPluginManager();
		if(plugin == null || !(plugin instanceof WorldGuardPlugin)){
			//warn WG was not found.
			logWarning("WorldGuard Plugin Not found!");
			logWarning("AnimalProtect Disabled!");
			//Disable the plugin.
			pm.disablePlugin(this);
			return null;
		}
		return (WorldGuardPlugin) plugin;
	}
	
	
	//Log Message to console stuff
	protected void logMessage(String msg){
	PluginDescriptionFile pdFile = this.getDescription();
	log.info(pdFile.getName() + " " + pdFile.getVersion() + " : " + msg);
	
	}
	//Warn for WorldGuard not found.
	protected void logWarning(String msg){
		PluginDescriptionFile pdFile = this.getDescription();
		log.warning(pdFile.getName() + " " + pdFile.getVersion() + " : " + msg);
	}
	//Configuration setup
	private void setupConfig(){
		final FileConfiguration cfg = getConfig();
		FileConfigurationOptions cfgOptions = cfg.options();
		getConfig().addDefault("protect-hostiles", false);
		getConfig().addDefault("protect-villiger", true);
		getConfig().addDefault("notify", true);
		getConfig().addDefault("notify-interval", "10");
		getConfig().addDefault("notify-outdated", true);
		getConfig().addDefault("debug", false);
		cfgOptions.copyDefaults(true);
		cfgOptions.header("Default Config for AnimalProtect");
		cfgOptions.copyHeader(true);
		saveConfig();
	}
	//Check for commands and such.
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
    	if(commandLabel.equalsIgnoreCase("animalprotect")){
    		if(args.length < 1){
				sender.sendMessage(ChatColor.YELLOW + "+++++++++AnimalProtect++++++++++");
				sender.sendMessage(ChatColor.GREEN + "+ A Animal Friendley Plugin!");
				sender.sendMessage(ChatColor.RED + "+ Version: " + getDescription().getVersion());
				sender.sendMessage(ChatColor.LIGHT_PURPLE + "+ Developer: " + getDescription().getAuthors());
				sender.sendMessage(ChatColor.GOLD + "+ Protect Hostiles: " + this.protectH);
				sender.sendMessage(ChatColor.YELLOW + "+++++++++++++++++++++++++++++");
				return true;
    		}
    		if(args[0].equalsIgnoreCase("-reload") && sender.isOp() || sender.hasPermission("animalprotect.admin")){
    			//reload config stuff.
    			this.reloadConfig();
    			//Set string on reload of config.
    			if(this.getConfig().getBoolean("protect-hostiles") == true){
    				this.protectH = "Yes";
    			}
    			else{ this.protectH = "No";}
    			sender.sendMessage(success + "Configuration Reloaded!");
    			return true;
    		}
    		else{ sender.sendMessage(fail + "You lack the necessary permissions to perform this action.");
    		return true;
    		}
    	}
    	return false; 
    }
	//Update checking 
	public List<String> readURL(String url)
	{
	        try {
	                URL site = new URL(url);
	                URLConnection urlC = site.openConnection();
	                BufferedReader in = new BufferedReader(new InputStreamReader(urlC.getInputStream()));

	                List<String> lines = new ArrayList<String>();
	                String line;
	                while((line = in.readLine()) != null)
	                {
	                        lines.add(line);
	                }

	                in.close();

	                return lines;
	        } catch(MalformedURLException e) {
	                e.printStackTrace();
	        } catch(IOException e) {
	                e.printStackTrace();
	        }

	        return null;
	}
	//run the check for an update
	public void updateCheck(){
		PluginDescriptionFile pdfFile = getDescription();

		VersionNumber currentVersion = new VersionNumber(pdfFile.getVersion());
		List<String> versionURL = readURL("http://ddelay.co.uk/bukkit/AnimalProtect/ver.html");
		String lVersion = versionURL.get(0) + "." + versionURL.get(1) + "." + versionURL.get(2);
		VersionNumber latestVersion = new VersionNumber(lVersion);

		if(currentVersion.version[0] < latestVersion.version[0] ||
		                currentVersion.version[1] < latestVersion.version[1] ||
		                currentVersion.version[2]< latestVersion.version[2])
		{
		        //UPDATE AVAILABLE
			this.logMessage("Update Avaliable" + " " + "Latest Version: " + lVersion);
			this.mlversion = lVersion;
			outdated = true;
		} else {
		        //UP TO DATE
			outdated = false;
		} 
	}



}
