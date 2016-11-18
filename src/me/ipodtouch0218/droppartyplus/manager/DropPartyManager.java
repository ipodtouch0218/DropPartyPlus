package me.ipodtouch0218.droppartyplus.manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.ipodtouch0218.droppartyplus.DPPMain;

public class DropPartyManager {
	
	private DPPMain mainInstance;
	private boolean status;
	private int interval;
	private List<String> commandList = new ArrayList<String>();
	private List<String> messageList = new ArrayList<String>();

	public DropPartyManager(ConfigurationSection configurationSection, DPPMain pl) {
		status = false;
		this.mainInstance = pl;
		
		this.interval = configurationSection.getInt("delay-between-items");
		ConfigurationSection dropSection = configurationSection.getConfigurationSection("drops");
		for (String key : dropSection.getKeys(false)) {
			messageList.add(dropSection.getString(key + ".message"));
			commandList.add(dropSection.getString(key + ".command"));
		}
	}
	
	public void startParty() {
		status = true;
		for (int i = 0; i < commandList.size(); i++) {
			if (status) {
			    String command = commandList.get(i);
			    if (command == null) {
			    	command = "null";
			    }
			    if (command.startsWith("/")) { command = command.substring(1); }
	
				List<Player> onlinePlayers = new ArrayList<Player>(Bukkit.getOnlinePlayers());
			    Player random = onlinePlayers.get((int) (Math.random() * (onlinePlayers.size() - 1)));
			    if (command.contains("{player}")) {
			    	command.replaceAll("{player}", random.getName());
			    }
			    final String command2 = command;
			    String message = ChatColor.translateAlternateColorCodes('&', messageList.get(i));
			    if (message == null) {
			    	message = "null";
			    } else if (message.contains("{player}")) {
			    	message.replaceAll("{player}", random.getName());
			    }
			    final String message2 = message;
			    new BukkitRunnable() {
			        @Override
			        public void run() {
			        	if (!command2.equals("null")) {
			        		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command2);
			        	}
			        	if (!message2.equals("null")) {
			        		Bukkit.broadcastMessage(message2);
			        	}
			        }
			    }.runTaskLater(mainInstance, i * (this.interval * 20L));
			}
		}
	}
	
	public void stopParty() {
		status = false;
	}
}
