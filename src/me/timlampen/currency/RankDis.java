package me.timlampen.currency;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.timlampen.commands.Rankup;
import me.timlampen.util.Main;

public class RankDis{
	Main p;
	Rankup r;
	public RankDis(Main p, Rankup r){
		this.p = p;
		this.r = r;
	}
	
	public void doRank(Player player){
		Set<String> set = p.getConfig().getConfigurationSection("rankup").getKeys(false);
		int i = 0;
		for(String s : set){
			i++;
			if(p.perms.getPrimaryGroup(player).equals(s)){
				player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + ">>>YOU ARE HERE<<<");
			}
			else{
				player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD +  + i + ChatColor.RED + " " + s + ChatColor.GREEN + " Cost: " + ChatColor.DARK_GRAY + r.getNextRankCost(s, player));
			}
		}
	}
}
