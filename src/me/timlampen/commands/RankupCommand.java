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
	        player.sendMessage(rank);
	        if(rank.equalsIgnoreCase("a")){
	        	Rankup rankup = new Rankup(player);
	        	rankup.rankup(rank);
	        }
	        else if ((p.getConfig().getDouble("rankup." + rank + ".cost") != 0.0D) && (p.getConfig().getString("rankup." + rank + ".nextrank") != null)){
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
	    else if(cmd.getName().equalsIgnoreCase("nextrank")){
	    	if(sender instanceof Player){
	    		Player player = (Player)sender;
	    		String nextrank = p.getConfig().getString("rankup." + p.perms.getPrimaryGroup(player) + ".nextrank");
	    		double cost = p.getConfig().getDouble("rankup." + nextrank + ".cost");
	    		player.sendMessage(p.getPrefix() + ChatColor.DARK_GRAY+ "" + ChatColor.BOLD + "Stats For Next Rank:");
	    		player.sendMessage(p.getPrefix() + ChatColor.DARK_GRAY + "Rank Name: " + ChatColor.DARK_RED + nextrank);
	    		player.sendMessage(p.getPrefix() + ChatColor.DARK_GRAY + "Rank Cost: " + ChatColor.DARK_RED + cost);
	    		player.sendMessage(p.getPrefix() + ChatColor.DARK_GRAY + "You are " + ChatColor.DARK_RED + "" + ChatColor.BOLD + (p.eco.getBalance(player.getName())/cost*100) + "%" + ChatColor.DARK_GRAY + " from ranking up to " + ChatColor.DARK_RED + nextrank);
	    	}
	    }
	    return false;
	  }

}
