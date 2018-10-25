package me.ipodtouch0218.droppartyplus.util;

import org.bukkit.ChatColor;

import me.ipodtouch0218.droppartyplus.DPPMain;

public class Util {

	private static DPPMain p;
	
	public Util(DPPMain p) {
		Util.p = p;
	}
	
	public static void nullify() {
		p = null;
	}
	
	public static String msgFromConfig(String str, String name) {
		String msg = p.getMessageConfig().getString(str);
		if (msg != null) {
			if (str.equals("partyStarted") || str.equals("partyStopped")){
				return ChatColor.translateAlternateColorCodes('&', msg.replaceAll("%party%", name));
			} else {
				return ChatColor.translateAlternateColorCodes('&', msg);
			}
		} 
		return ChatColor.RED + "[DPP] Value " + name + " is invalid in messages.yml!";
	}
	
}
