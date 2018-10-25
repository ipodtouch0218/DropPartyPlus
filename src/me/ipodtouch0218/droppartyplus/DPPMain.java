package me.ipodtouch0218.droppartyplus;

import java.io.File;
import java.util.HashMap;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.ipodtouch0218.droppartyplus.command.CommandDPP;
import me.ipodtouch0218.droppartyplus.manager.DropPartyManager;
import me.ipodtouch0218.droppartyplus.util.Util;

public class DPPMain extends JavaPlugin {
	
	private HashMap<String, DropPartyManager> parties = new HashMap<String, DropPartyManager>();
	private File messageFile = new File(getDataFolder() + "/messages.yml");
	private FileConfiguration messageConfig = YamlConfiguration.loadConfiguration(messageFile);
	
	private DropPartyManager activeParty = null;
	
    @Override
    public void onEnable() {
    	saveDefaultConfig();
    	saveConfig();
    	getCommand("dpp").setExecutor(new CommandDPP(this));
    	loadDropParties();
    	loadOtherConfigs();
    	new Util(this);
    }
    
    @Override
    public void onDisable() {
    	Util.nullify();
    }
    
    private void loadOtherConfigs() {
    	if (!messageFile.exists()) {
    		saveResource("messages.yml", true);
    	}
    }
    
    public void loadDropParties() {
        ConfigurationSection dropSection = getConfig().getConfigurationSection("DropParties");
        for (String key : dropSection.getKeys(false)) {
            if (!dropSection.isConfigurationSection(key)) {
                continue;
            }
            parties.put(key, new DropPartyManager(dropSection.getConfigurationSection(key), this));
        }
    }
    
    public HashMap<String, DropPartyManager> getDropParties() {
    	return parties;
    }
    
    public DropPartyManager getDropParty(String name) {
    	if (parties.get(name) != null) {
    		return parties.get(name);
    	} else {
    		return null;
    	}
    }
    
    public ConfigurationSection getPartyInfo(String key) {
    	ConfigurationSection dropSection = getConfig().getConfigurationSection("DropParties");
    	if (dropSection.isConfigurationSection(key)) {
    		return dropSection.getConfigurationSection(key);
    	} else { 
    		return null;
    	}
    }
    
    public FileConfiguration getMessageConfig() {
    	return messageConfig;
    }
    
    public void setActiveParty(DropPartyManager mngr) {
    	activeParty = mngr;
    }
    
    public void startActiveParty() {
    	activeParty.queueParty();
    }
    
    public DropPartyManager getActiveParty() {
    	return activeParty;
    }
}
