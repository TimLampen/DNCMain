package me.timlampen.commands;

import java.text.DecimalFormat;
import java.util.logging.Level;

import me.timlampen.util.Lang;
import me.timlampen.util.Main;
import me.timlampen.util.Title;

import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Rankup {
	Lang l;
	public Rankup(Lang l){
		this.l =l;
	}
	private String prefix = ChatColor.BOLD + "["+ChatColor.RED+ChatColor.BOLD+"D"+ChatColor.GRAY+ChatColor.BOLD+"C"+ChatColor.WHITE+ChatColor.BOLD+"]"+ChatColor.RESET + " ";
	private Player player;
	public Rankup(Player player){
		this.player = player;
	}
	 public String getRank(String rank){
	    if (Main.getPlugin().getConfig().getString("rankup." + rank + ".cost") != null) {
	      return rank;
	    }
	    return "";
	  }
	  
	  public String getNextRank(String rank){
	    if (Main.getPlugin().getConfig().getString("rankup." + rank + ".nextrank") != null) {
	      return Main.getPlugin().getConfig().getString("rankup." + rank + ".nextrank");
	    }
	    return "N/A";
	  }
	  
	  public double getNextRankCost(String rank, Player player){
	    if (Main.getPlugin().getConfig().getDouble("rankup." + rank + ".cost") != 0.0D) {
	      return Main.getPlugin().getConfig().getDouble("rankup." + rank + ".cost");
	    }
	    return 0L;
	  }
	  
	  public void rankup(String rank){
	    double cost = Main.getPlugin().getConfig().getDouble("rankup." + rank + ".cost");
	    double balance = Math.round(Main.eco.getBalance(player));
	    if (cost == 0.0D && !rank.equalsIgnoreCase("a")){
	      player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + Main.getPlugin().getConfig().getString("Invaid-Group")));
	      return;
	    }
	    if (balance < cost){
	      double remainder = cost - balance;
	      DecimalFormat formatter = new DecimalFormat("#,###");
	      player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + Main.getPlugin().getConfig().getString("Not-Enough-Money").replace("[Remainder]", new StringBuilder().append(formatter.format(remainder)).toString())));
	    }
	    else{
	      String nextrank = Main.getPlugin().getConfig().getString("rankup." + rank + ".nextrank");
	      if (Main.getPlugin().getConfig().getString("rankup." + rank + ".nextrank") == null){
	        Main.getPlugin().getLogger().log(Level.SEVERE, player.getName() + " tried to rank up even though he's not in a group that can't");
	        return;
	      }
	      Main.eco.withdrawPlayer(player, cost);
	      Main.perms.playerRemoveGroup(player, rank);
	      Main.perms.playerAddGroup(player, nextrank);
	      Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.LIGHT_PURPLE + "Ranks" + ChatColor.DARK_GRAY + "] " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " is now " + ChatColor.GREEN + "" + ChatColor.BOLD + nextrank + ChatColor.GRAY + "");
	      player.chat("/warp " + nextrank);
	      doTitle(player, player, nextrank);
	    }
	  }
		public void doTitle(Player player, Player orig, String nextrank){
			Title title = new Title("", ChatColor.GREEN + orig.getName() + " Has Rankup Up To " + nextrank);
			title.send(player);
	}
}
