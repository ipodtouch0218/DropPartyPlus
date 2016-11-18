package me.ipodtouch0218.droppartyplus;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import me.ipodtouch0218.droppartyplus.command.CommandDPP;
import me.ipodtouch0218.droppartyplus.manager.DropPartyManager;

public class DPPMain extends JavaPlugin {
	
	private Map<String, ConfigurationSection> partyList = new HashMap<String, ConfigurationSection>();
	private Map<String, DropPartyManager> parties = new HashMap<String, DropPartyManager>();
	
    @Override
    public void onEnable() {
    	saveDefaultConfig();
    	saveConfig();
    	getCommand("dpp").setExecutor(new CommandDPP(this));
    	loadDropParties();
    }
    
    public void loadDropParties() {
        ConfigurationSection dropSection = getConfig().getConfigurationSection("DropParties");
        for (String key : dropSection.getKeys(false)) {
            if (!dropSection.isConfigurationSection(key)) {
                continue;
            }
            partyList.put(key, dropSection.getConfigurationSection(key));
            parties.put(key, new DropPartyManager(dropSection.getConfigurationSection(key), this));
        }
    }
    
    public Map<String, DropPartyManager> getDropParties() {
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
}
