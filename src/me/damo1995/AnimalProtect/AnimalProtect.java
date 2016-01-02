package me.damo1995.AnimalProtect;
 
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import net.gravitydevelopment.updater.Updater;
import net.gravitydevelopment.updater.Updater.ReleaseType;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.MetricsLite;

 public class AnimalProtect extends JavaPlugin
 {
   Logger log = Logger.getLogger("Minecraft");
 
   String success = ChatColor.GREEN + "[AnimalProtect]: ";
   
   
   
 
   public boolean outdated = false;
   public String failMsg;
   public String cmdFail;
   public String adminNotifyMsg;
   public String fail;
   public String ridemsg;
   public final NewDamageListeners dl = new NewDamageListeners(this);
   public final InteractListener shear = new InteractListener(this);
   public final RideListener rl = new RideListener(this);
   public final LeashManager lm = new LeashManager(this);
   public final UpdateListeners updateListeners = new UpdateListeners(this);
   
   
   
   //UPDATE VARIABLES
   public static boolean update = false;
   public static String name = "";
   public static ReleaseType type = null;
   public static String version = "";
   public static String link = "";
 
   public void onEnable()
   {
	 fail = ChatColor.RED + "[AnimalProtect]: ";
     PluginManager pm = getServer().getPluginManager();
     pm.registerEvents(this.dl, this);
     pm.registerEvents(this.shear, this);
     pm.registerEvents(this.rl, this);
     pm.registerEvents(this.lm, this);
     pm.registerEvents(this.updateListeners, this);
     if(getConfig().getBoolean("update-check") == true){
     updateCheck();
     }
     logMessage("Enabled!");
 
     getWorldGuardPlugin();
 
     setupConfig();
  
     validateConfig();
     collectStats();
 
     getCommand("animalprotect").setExecutor(new CommandHandler(this));
     getCommand("apreload").setExecutor(new CommandHandler(this));
     getCommand("aplist").setExecutor(new CommandHandler(this));
     getCommand("apupdate").setExecutor(new CommandHandler(this));
     getCommand("apht").setExecutor(new CommandHandler(this));
     getCommand("apunclaim").setExecutor(new CommandHandler(this));
     
     this.failMsg = (this.fail + getConfig().getString("FailMessage").replaceAll("(&([a-f0-9]))", "\u00A7$2"));
     this.ridemsg = (this.fail + getConfig().getString("RideMessage").replaceAll("(&([a-f0-9]))", "\u00A7$2"));
     this.cmdFail = (this.fail + getConfig().getString("CommandFail").replaceAll("(&([a-f0-9]))", "\u00A7$2"));
     this.adminNotifyMsg = getConfig().getString("AdminNotification").replaceAll("(&([a-f0-9]))", "\u00A7$2");
     if (getConfig().getBoolean("debug")) {
       logMessage(this.failMsg);
       logMessage(this.ridemsg);
       logMessage(this.cmdFail);
       logMessage(this.adminNotifyMsg);
     }
   }
   
   private void updateCheck(){
	   Updater updater = new Updater(this, 39111, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false); // Start Updater but just do a version check
	   update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE; // Determine if there is an update ready for us
	   name = updater.getLatestName(); // Get the latest name
	   version = updater.getLatestGameVersion(); // Get the latest game version
	   type = updater.getLatestType(); // Get the latest file's type
	   link = updater.getLatestFileLink(); // Get the latest link
   }
 @SuppressWarnings("unused")
public void update(){
	 Updater updater = new Updater(this, 39111, this.getFile(), Updater.UpdateType.NO_VERSION_CHECK, true); // Go straight to downloading, and announce progress to console.
 }
   public void collectStats() {
     try {
       MetricsLite metrics = new MetricsLite(this);
       metrics.start();
       logMessage("Collecting Stats");
       logMessage("If You do not wish for AnimalProtect to collect stats please set opt-out to true");
     } catch (IOException e) {
       logMessage("Coulden't submit stats!");
     }
   }
 
   public void onDisable()
   {
     logMessage("Disabled!");
     getServer().getPluginManager().disablePlugin(this);
   }
 
   public WorldGuardPlugin getWorldGuardPlugin()
   {
     Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
     PluginManager pm = getServer().getPluginManager();
     if ((plugin == null) || (!(plugin instanceof WorldGuardPlugin)))
     {
       logWarning("WorldGuard Plugin Not found!");
       logWarning("AnimalProtect Disabled!");
 
       pm.disablePlugin(this);
       return null;
     }
     return (WorldGuardPlugin)plugin;
   }
 
   
   protected void logMessage(String msg)
   {
     PluginDescriptionFile pdFile = getDescription();
     this.log.info("[" + pdFile.getName() + " " + pdFile.getVersion() + "]: " + msg);
   }
 
   protected void logWarning(String msg)
   {
     PluginDescriptionFile pdFile = getDescription();
     this.log.warning("[" + pdFile.getName() + " " + pdFile.getVersion() + "]: " + msg);
   }
 
   private void setupConfig() {
     FileConfiguration cfg = getConfig();
     FileConfigurationOptions cfgOptions = cfg.options();
     File file = new File(getDataFolder() + File.separator + "config.yml");
     if (!file.exists()) {
       logMessage("No Config found Defaulting.");
       saveDefaultConfig();
       cfgOptions.copyDefaults(true);
       cfgOptions.header("Default Config for AnimalProtect");
       cfgOptions.copyHeader(true);
       saveConfig();
     }
   }
 
   public void validateConfig() {
     if (getConfig().getInt("notify-interval") > 20) {
       logWarning("Notify interval greater then 20");
       logMessage("Notify Interval set to 20");
       getConfig().set("notify-interval", Integer.valueOf(20));
       saveConfig();
     }
   }
   
   
 }
