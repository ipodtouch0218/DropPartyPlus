package me.ipodtouch0218.droppartyplus.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.ipodtouch0218.droppartyplus.DPPMain;

public class CommandDPP implements CommandExecutor {

	private DPPMain plugin;
	
	public CommandDPP(DPPMain p) {
		plugin = p;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length < 1) {
			sender.sendMessage(ChatColor.AQUA + "[DPP]" + ChatColor.WHITE + " Invalid Usage! /dpp <start/stop> [party]");
			return true;
		}
		
		String sub = args[0].toLowerCase();
		
		if (sub.equals("start")) {
			if (args.length == 2) {
				if (plugin.getDropParty(args[1]) != null) {
					plugin.getDropParty(args[1]).startParty();
				} else {
					sender.sendMessage(ChatColor.GREEN + "[DPP]" + ChatColor.WHITE + " Party " + ChatColor.YELLOW + args[1] + ChatColor.WHITE + " doesn't exist!");
					return true;
				}
			} else {
				if (plugin.getDropParties().get(0) != null) {
					plugin.getDropParties().get(0).startParty();
				} else {
					System.out.println(plugin.getDropParties());
					sender.sendMessage(ChatColor.GREEN + "[DPP]" + ChatColor.WHITE + " No Parties Exist!");
					return true;
				}
			}
			sender.sendMessage(ChatColor.GREEN + "[DPP]" + ChatColor.WHITE + " Party " + ChatColor.YELLOW + args[1] + ChatColor.WHITE + " started!");
			return true;
		} else if (sub.equals("stop")) {
			if (args.length == 2) {
				if (plugin.getDropParty(args[1]) != null) {
					plugin.getDropParty(args[1]).stopParty();
				} else {
					sender.sendMessage(ChatColor.GREEN + "[DPP]" + ChatColor.WHITE + " Party " + ChatColor.YELLOW + args[1] + ChatColor.WHITE + " doesn't exist!");
					return true;
				}
			} else {
				if (plugin.getDropParties().get(0) != null) {
					plugin.getDropParties().get(0).stopParty();
				} else {
					sender.sendMessage(ChatColor.GREEN + "[DPP]" + ChatColor.WHITE + " No Parties Exist!");
					return true;
				}
			}
			sender.sendMessage(ChatColor.GREEN + "[DPP]" + ChatColor.WHITE + " Party " + ChatColor.YELLOW + args[1] + ChatColor.WHITE + " stopped!");
			return true;
		} else if (sub.equals("reload")) {
			plugin.loadDropParties();
			sender.sendMessage(ChatColor.GREEN + "[DPP]" + ChatColor.WHITE + " Reloaded all DropParties from Config!");
			return true;
		} else {
			sender.sendMessage(ChatColor.GREEN + "[DPP]" + ChatColor.WHITE + " Invalid Usage! /dpp <start/stop> [party]");
			return true;
		}
	}

}
