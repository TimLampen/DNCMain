package me.timlampen.commands;


import me.timlampen.util.Main;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankupCommand implements CommandExecutor{
	Main p;
	public RankupCommand(Main p){
		this.p = p;
	}
	  @Override
	  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
	    if (cmd.getName().equalsIgnoreCase("rankup")){
	      if(sender instanceof Player){
	        Player player = (Player)sender;
	        String rank = Main.perms.getPrimaryGroup(player);
	        if ((p.getConfig().getDouble("rankup." + rank + ".cost") != 0.0D) && (p.getConfig().getString("rankup." + rank + ".nextrank") != null)){
	        	Rankup rankup = new Rankup(player);
	        	rankup.rankup(rank);
	        }
	        else{
	  	      player.sendMessage(ChatColor.translateAlternateColorCodes('&', p.getPrefix() + p.getConfig().getString("Invaid-Group")));
	        }
	      }
	      else{
	    	  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', p.getPrefix() + p.getConfig().getString("Must-Be-Player")));
	      }
	      return true;
	    }
	    return false;
	  }

}
