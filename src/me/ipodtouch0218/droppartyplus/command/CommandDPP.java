package me.ipodtouch0218.droppartyplus.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ipodtouch0218.droppartyplus.DPPMain;
import me.ipodtouch0218.droppartyplus.manager.DropPartyManager;
import me.ipodtouch0218.droppartyplus.util.Util;

public class CommandDPP implements CommandExecutor {

	private DPPMain plugin;
	
	public CommandDPP(DPPMain p) {
		plugin = p;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length < 1) {
			sender.sendMessage(Util.msgFromConfig("invalidUsage", null));
			return true;
		}
		
		String sub = args[0].toLowerCase();
		
		if (sub.equals("reload")) {
			plugin.loadDropParties();
			sender.sendMessage(Util.msgFromConfig("partiesReloaded", null));
			return true;
		} else if (sub.equals("join")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Consoles can't join parties, silly!");
				return true;
			}
			
			if (plugin.getActiveParty().getStatus() == 0) {
				sender.sendMessage(Util.msgFromConfig("noParty", null));
				return true;
			}
			plugin.getActiveParty().addToParty(((Player) sender).getUniqueId());
			sender.sendMessage(Util.msgFromConfig("joinDropParty", null));
			return true;
		}
		
		DropPartyManager partyManager = null;
		if (sub.equals("start") || sub.equals("stop"))
			if (args.length > 1) {
				partyManager = plugin.getDropParty(args[1]);
				
				if (partyManager == null) {
					sender.sendMessage(Util.msgFromConfig("partyDoesntExist", null));
					return true;
				}
			} else {
				if (partyManager == null) {
					if (!plugin.getDropParties().values().isEmpty()) {
						partyManager = ((DropPartyManager) (plugin.getDropParties().values().toArray()[0]));
					} else {
						sender.sendMessage(Util.msgFromConfig("noPartiesExist", null));
						return true;
					}
				}
			}
		
		
		if (sub.equals("start")) {
			plugin.setActiveParty(partyManager);
			plugin.startActiveParty();
			sender.sendMessage(Util.msgFromConfig("partyStarted", partyManager.getName()));
			return true;
		} else if (sub.equals("stop")) {
			plugin.setActiveParty(null);
			sender.sendMessage(Util.msgFromConfig("partyStopped", partyManager.getName()));
			return true;
		}  else {
			sender.sendMessage(Util.msgFromConfig("invalidUsage", null));
		}
		return true;
	}

}
