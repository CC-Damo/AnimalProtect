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
import org.mcstats.MetricsLite;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class AnimalProtect extends JavaPlugin{
	Logger log = Logger.getLogger("Minecraft");

	String success = ChatColor.GREEN + "[AnimalProtect]: ";
	
	String fail = ChatColor.RED + "[AnimalProtect]: ";
	
	String mlversion = "";
	public boolean outdated = false;

//	public final DamageListeners dl = new DamageListeners(this);
	public final NewDamageListeners dl = new NewDamageListeners(this);
	public final ShearListener shear = new ShearListener(this);
	public final VersionCheck vc = new VersionCheck(this);
	
	//Enable stuff
	public void onEnable(){		
		//event registration
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(dl, this);
		pm.registerEvents(vc, this);
		pm.registerEvents(shear, this);
		
		//Log msg
		this.logMessage("Enabled!");
		//Check for WorldGuard
		getWorldGuardPlugin();
		//CFG Setup
		setupConfig();
		//Check for Updates
		updateCheck();
		//Check Config for any errors.
		validateConfig();
		collectStats();
	}
	
	public void collectStats(){
		try {
			MetricsLite metrics = new MetricsLite(this);
			metrics.start();
			this.logMessage("Collecting Stats");
			this.logMessage("If You do not wish for AnimalProtect to collect stats please set opt-out to true");
			} catch (IOException e) {
			this.logMessage("Coulden't submit stats!");
		}
		
	}
	
	public void onDisable(){
		//Log MSG disabled.
		this.logMessage("Disabled!");
		this.getServer().getPluginManager().disablePlugin(this);
		
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
		this.saveDefaultConfig();
		cfgOptions.copyDefaults(true);
		cfgOptions.header("Default Config for AnimalProtect");
		cfgOptions.copyHeader(true);
		saveConfig();
	}
	
	private void validateConfig(){
		if(this.getConfig().getInt("notify-interval") > 20){
			this.logWarning("Notify interval greater then 20");
			this.logMessage("Notify Interval set to 20");
			this.getConfig().set("notify-interval", 20);
			this.saveConfig();
		}
	}
	//Check for commands and such.
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
    	if(commandLabel.equalsIgnoreCase("animalprotect")){
    		if(args.length < 1){
				sender.sendMessage(ChatColor.YELLOW + "+++++++++AnimalProtect++++++++++");
				sender.sendMessage(ChatColor.GREEN + "+ A Animal Friendley Plugin!");
				sender.sendMessage(ChatColor.RED + "+ Version: " + getDescription().getVersion());
				sender.sendMessage(ChatColor.LIGHT_PURPLE + "+ Developer: " + getDescription().getAuthors());
				sender.sendMessage(ChatColor.AQUA + "http://www.dev.bukkit.org/AnimalProtect");
				sender.sendMessage(ChatColor.YELLOW + "+++++++++++++++++++++++++++++");
				return true;
    		}
    		if(args[0].equalsIgnoreCase("-reload") && sender.isOp() || sender.hasPermission("animalprotect.admin")){
    			//reload config stuff.
    			this.reloadConfig();
    			//Set string on reload of config.
    			this.validateConfig();
    			sender.sendMessage(success + "Configuration Reloaded!");
    			return true;
    		}
    		if(args[0].equalsIgnoreCase("-list") && args[1].equalsIgnoreCase("player") && sender.isOp() || sender.hasPermission("animalprotect.list")){
    			List<String> pfp = getConfig().getStringList("protect-from-player");
    			sender.sendMessage(success + "The following are protected from players");
    			for(String i : pfp){
    				sender.sendMessage(i);
    			}
    		}
    		if(args[0].equalsIgnoreCase("-list") && args[1].equalsIgnoreCase("mobs") && sender.isOp() || sender.hasPermission("animalprotect.list")){
    			List<String> pfp = getConfig().getStringList("protect-from-monsters");
    			sender.sendMessage(success + "The following are protected from mobs");
    			for(String i : pfp){
    				sender.sendMessage(i);
    				}
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
		    this.logMessage("Could not connect to Update Server.");
	        } catch(IOException e) {
	                e.printStackTrace();
	        }
	          catch(NumberFormatException e){
	        	  e.printStackTrace();
	        	  this.logMessage("Please Report this error to animalprotect@ddelay.co.uk");
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
