package me.ipodtouch0218.droppartyplus.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import me.ipodtouch0218.droppartyplus.DPPMain;
import me.ipodtouch0218.droppartyplus.util.Util;

public class DropPartyManager {
	
	private DPPMain mainInstance;
	private String name;
	private int status;    // 0 = not started, 1 = players joining, 2 = started
	private double interval;
	private List<String> commandList = new ArrayList<String>();
	private List<String> messageList = new ArrayList<String>();
	
	private List<UUID> inParty = new ArrayList<UUID>();

	public DropPartyManager(ConfigurationSection configurationSection, DPPMain pl) {
		status = 0;
		this.mainInstance = pl;
		this.name = configurationSection.getName();
		
		this.interval = configurationSection.getDouble("delay-between-items");
		ConfigurationSection dropSection = configurationSection.getConfigurationSection("drops");
		for (String key : dropSection.getKeys(false)) {
			messageList.add(dropSection.getString(key + ".message"));
			commandList.add(dropSection.getString(key + ".command"));
		}
	}
	
	public int getStatus() {
		return status;
	}
	
	public String getName() {
		return name;
	}
	
	public void queueParty() {
		status = 1;
		Bukkit.broadcastMessage(Util.msgFromConfig("dropPartyStarting", null));
		
		new BukkitRunnable() {
			@Override
			public void run() {
				startParty();
			}
		}.runTaskLater(mainInstance, 20*60);
	}
	
	private void startParty() {
		status = 2;
		for (int i = 0; i < commandList.size(); i++) {
			if (status == 2) {
	
				if (inParty.isEmpty()) {
					Bukkit.broadcastMessage(Util.msgFromConfig("noOnlinePlayers", null));
					return;
				}
			    UUID random = inParty.get((int) (Math.random() * (inParty.size() - 1)));
			    
			    String command = null;
			    if (commandList.get(i) != null) {
			    	command = ChatColor.translateAlternateColorCodes('&', commandList.get(i));
			    	if (command.contains("%player%")) {
				    	command = command.replaceAll("%player%", Bukkit.getPlayer(random).getName());
				    }
			    	if (command.startsWith("/")) {
			    		command = command.substring(1);
			    	}
			    } else {
			    	command = "null";
			    }
			    
			    if (command.contains("%player%")) {
			    	command = command.replaceAll("%player%", Bukkit.getPlayer(random).getName());
			    }
			    final String command2 = command;
			    String message = null;
			    if (messageList.get(i) != null) {
			    	message = ChatColor.translateAlternateColorCodes('&', messageList.get(i));
			    	if (message.contains("%player%")) {
				    	message = message.replaceAll("%player%", Bukkit.getPlayer(random).getName());
				    }
			    } else {
			    	message = "null";
			    }
			    final String message2 = message;
			    new BukkitRunnable() {
			        @Override
			        public void run() {
			        	if (!command2.equals("null")) {
			        		if (command2.contains("%all_players%")) {
			        			for (UUID p : inParty) {
			        				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command2.replaceAll("%all_players%", Bukkit.getPlayer(p).getName()));
			        			}
			        		} else {
			        			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command2);
			        		}
			        	}
			        	if (!message2.equals("null")) {
			        		if (message2.contains("\\n")) {
			        			for (String msg : message2.split("\\n")) {
			        				Bukkit.broadcastMessage(msg);
			        			}
			        		} else {
			        			Bukkit.broadcastMessage(message2);
			        		}
			        	}
			        }
			    }.runTaskLater(mainInstance, (long) (i * (this.interval * 20L)));
			}
		}
	}
	
	public void addToParty(UUID pl) {
		inParty.add(pl);
	}
	
	public void clearEntries() {
		inParty.clear();
	}
	
	public void stopParty() {
		status = 0;
	}
}
