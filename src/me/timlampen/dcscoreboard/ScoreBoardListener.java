package me.timlampen.dcscoreboard;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import me.timlampen.commands.Rankup;
import me.timlampen.util.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class ScoreBoardListener implements Listener{
	Main p;
	BlockListener bl;
	Rankup r;
    public ArrayList<String> safe = new ArrayList<String>();
	public ScoreBoardListener(Main p, BlockListener bl, Rankup r){
		this.p = p;
		this.bl = bl;
		this.r = r;
	}
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		player.setScoreboard(setupScoreboard(player));
		}
	public ArrayList<UUID> toggle = new ArrayList<>();
	@SuppressWarnings("deprecation")
	public Scoreboard setupScoreboard(Player player){
		    ScoreboardManager manager = Bukkit.getScoreboardManager();
		    Scoreboard pb = manager.getNewScoreboard();
		 if(toggle.contains(player.getUniqueId())){
			 return null;
		 }
		 int amtonline;
		     if(Bukkit.getOnlinePlayers().length<=1){
		    	 amtonline = 1;
		     }
		     else{
		    	 amtonline = Bukkit.getOnlinePlayers().length;
		     }
	     String onlinet = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Scoreboard.Plyerst.Display", ChatColor.GRAY + "" + ChatColor.BOLD + "Online"));
	     String online = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Scoreboard.Plyers.Display", ChatColor.RED + "" + ChatColor.BOLD + "" + amtonline + "/" + Bukkit.getMaxPlayers()).replace("%max%", Bukkit.getMaxPlayers() + "").replace("%players%", amtonline + ""));
	     
	     
	     String moneyt = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Scoreboard.Moneyt.Display", ChatColor.GRAY + "" + ChatColor.BOLD + "Money"));
	     String moneya = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Scoreboard.Money.Display", ChatColor.RED + "" + ChatColor.BOLD + getMoney(player)).replace("%money%", getMoney(player)));;
	     
	     String multit = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Scoreboard.Multit.Display", ChatColor.GRAY + "" + ChatColor.BOLD + "Multiplier"));
	     String multi = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Scoreboard.Multi.Display", ChatColor.RED + "" + ChatColor.BOLD + "x" + bl.getMultiplier(player) + "Block(s)").replace("%multi%", bl.getMultiplier(player) + ""));
	     
	     String rnkt = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Scoreboard.Crnkt.Display", ChatColor.GRAY + "" + ChatColor.BOLD + "Current Rank"));
	     String rnk = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Scoreboard.Crnk.Display", ChatColor.RED + "" + ChatColor.BOLD + Main.perms.getPrimaryGroup(player).toUpperCase()).replace("%rank%", Main.perms.getPrimaryGroup(player).toUpperCase()));
	     
	     String nextrnkt = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Scoreboard.Nrnkt.Display", ChatColor.GRAY + "" + ChatColor.BOLD + "Next Rank"));
	    // String nextrnk;
	     String nextpercent;
	     if(safe.contains(p.perms.getPrimaryGroup(player))){
		    // nextrnk = getPercent(Main.eco.getBalance(player), 100);
		    nextpercent = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Scoreboard.Nrnk.Display", ChatColor.RED + "" + ChatColor.BOLD + getActualPercent(Main.eco.getBalance(player), 100)).replace("%number%", getActualPercent(Main.eco.getBalance(player), 100)));
	     }
	     else{
	    	// nextrnk = getPercent(Main.eco.getBalance(player), r.getNextRankCost(p.perms.getPrimaryGroup(player), player));
	    	nextpercent = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Scoreboard.Nrnk.Display", ChatColor.RED + "" + ChatColor.BOLD + getActualPercent(Main.eco.getBalance(player), r.getNextRankCost(p.perms.getPrimaryGroup(player), player))).replace("%number%", getActualPercent(Main.eco.getBalance(player), r.getNextRankCost(p.perms.getPrimaryGroup(player), player))));
	     }
	     
	     String dropt = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Scoreboard.Dpt.Display", ChatColor.GRAY + "" + ChatColor.BOLD + "Votes Needed"));
	     String drop = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Scoreboard.Dp.Display", ChatColor.RED + "" + ChatColor.BOLD + Main.vote).replace("%vote%", Main.vote + ""));
		     
	     if(pb.getObjective("title")!=null){
	    	 pb.resetScores(player);
	     }
		     Objective objective = pb.registerNewObjective("title", "dummy");
		     objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Scoreboard.Title.Display", ChatColor.RED + "" + ChatColor.BOLD + "DarkNess" + ChatColor.GRAY + "" + ChatColor.BOLD + "Craft")));
		     Score onlinetitle = objective.getScore(onlinet);
		       onlinetitle.setScore(p.getConfig().getInt("Scoreboard.Plyerst.Pos", 11));
		     Score onlined = objective.getScore(online);
		       onlined.setScore(p.getConfig().getInt("Scoreboard.Plyers.Pos", 10));
			   
			 Score moneytitle = objective.getScore(moneyt);
			 	moneytitle.setScore(p.getConfig().getInt("Scoreboard.Moneyt.Pos", 9));
			 Score moneyamt = objective.getScore(moneya);
			 	moneyamt.setScore(p.getConfig().getInt("Scoreboard.Money.Pos", 8));
			 	
			 Score multipliert = objective.getScore(multit);
			 	multipliert.setScore(p.getConfig().getInt("Scoreboard.Multit.Pos", 7));
			 Score multiplier = objective.getScore(multi);
			 	multiplier.setScore(p.getConfig().getInt("Scoreboard.Multi.Pos", 6));

			 Score rankt = objective.getScore(rnkt);
				 	rankt.setScore(p.getConfig().getInt("Scoreboard.Crnkt.Pos", 5));
		     Score rank = objective.getScore(rnk);
					rank.setScore(p.getConfig().getInt("Scoreboard.Crnk.Pos", 4));
			 	
			 Score nextrankt = objective.getScore(nextrnkt);
			 	nextrankt.setScore(p.getConfig().getInt("Scoreboard.Nrnkt.Pos", 3));
			// Score nextrank = objective.getScore(nextrnk);
			// 	nextrank.setScore(3);
			 	if(nextpercent.equalsIgnoreCase(ChatColor.RED + "" + ChatColor.BOLD + "100%")){
			 		nextpercent = getCustomColor() + "" + ChatColor.BOLD + "/RANKUP";
			 	}
			 Score nextper = objective.getScore(nextpercent);
				 nextper.setScore(p.getConfig().getInt("Scoreboard.Nrnk.Pos", 2));
			 	
			 Score dpt = objective.getScore(dropt);
			 	dpt.setScore(p.getConfig().getInt("Scoreboard.Dpt.Pos", 1));
			 Score dp = objective.getScore(drop);
			 	dp.setScore(p.getConfig().getInt("Scoreboard.Dp.Pos", 0));
			 
		     objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		     return pb;
		   }
	 
	 public String getMoney(Player player){
		 DecimalFormat df = new DecimalFormat("#.###");
		 String s = "";
		 double amt = Main.eco.getBalance(player);
		 Double thou = new Double("1000");
		 Double mill = new Double("1000000");
		 Double bill = new Double("1000000000");
		 Double tril = new Double("1000000000000");
		 Double quad = new Double("1000000000000000");
		 Double quin = new Double("1000000000000000000");
		 if(amt>=quin){
			 amt = amt/quin;
			 s = df.format(amt) + "qu";
		 }
		 else if(amt>=quad){
			 amt = amt/quad;
			 s = df.format(amt) + "q";
		 }
		 else if(amt>=tril){
			 amt = amt/tril;
			 s = df.format(amt) + "t";
		 }
		 else if(amt>=bill){
			 amt = amt/bill;
			 s = df.format(amt) + "b";
		 }
		 else if(amt>=mill){
			 amt = amt/mill;
			 s = df.format(amt) + "m";
		 }
		 else if(amt>=thou){
			 amt = amt/thou;
			 s = df.format(amt) + "k";
		 }
		 else{
			 s = df.format(amt) + "";
		 }
		 return s;
	 }

	 public String getActualPercent(double init, double goal){
		 double value = (init/goal) * 100;
		 DecimalFormat df = new DecimalFormat("#.##");
		 String s = null;
		 s = df.format(value);
		 if(value>100){
			 s="100";
		 }
		 s = s + "%";
		 return s;
	 }
		private static ChatColor getCustomColor(){
			ChatColor c = null;
			Random ran = new Random();
			int num = ran.nextInt(14);
			switch(num){
			case 0:
				c = ChatColor.AQUA;
				break;
			case 1:
				c = ChatColor.BLACK;
				break;
			case 2:
				c = ChatColor.BLUE;
				break;
			case 3:
				c = ChatColor.DARK_AQUA;
				break;
			case 4:
				c = ChatColor.DARK_BLUE;
				break;
			case 5:
				c = ChatColor.DARK_GREEN;
				break;
			case 6:
				c = ChatColor.DARK_PURPLE;
				break;
			case 7:
				c = ChatColor.DARK_RED;
				break;
			case 8:
				c = ChatColor.GOLD;
				break;
			case 9:
				c = ChatColor.GREEN;
				break;
			case 10:
				c = ChatColor.LIGHT_PURPLE;
				break;
			case 11:
				c = ChatColor.RED;
				break;
			case 12:
				c = ChatColor.WHITE;
				break;
			case 13:
				c = ChatColor.YELLOW;
				break;
			}
			return c;
			
		}
}
