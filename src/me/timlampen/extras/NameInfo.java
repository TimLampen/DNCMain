package me.timlampen.extras;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.timlampen.commands.Rankup;
import me.timlampen.currency.Tokens;
import me.timlampen.util.ConfigMaker;
import me.timlampen.util.JSONUtil;
import me.timlampen.util.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import com.lenis0012.bukkit.marriage.MPlayer;
import com.lenis0012.bukkit.marriage.Marriage;

public class NameInfo implements Listener{
	Prestige pre;
	Main p;
	Tokens t;
	Rankup r;
	private ConfigMaker config;
	
	public NameInfo(Main p, Tokens t, Prestige pre, Rankup r){
		this.p = p;
		this.r = r;
		this.t = t;
		this.pre = pre;
		config = new ConfigMaker(p, "blocks");
		config.save();
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event){
		Player player = event.getPlayer();
		Marriage m = Marriage.instance;
		MPlayer mplayer = m.getMPlayer(player);
		String married = mplayer.isMarried() ? "<3 " : "";
		if(event.getMessage().contains("http:") || event.getMessage().contains("www.") || event.getMessage().contains(".com") || event.getMessage().contains(".net") || event.getMessage().contains(".ca") || event.getMessage().contains(".org")){
			String s = ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + married + ChatColor.GRAY + "" + ChatColor.BOLD + pre.getNumber(pre.getPrestige(player)) + "" + getPrefix(player) + " " + player.getDisplayName() + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + event.getMessage();
			for(Player plyer : Bukkit.getOnlinePlayers()){
				plyer.sendMessage(s);
			}
			event.setCancelled(true);
		}
		else{
			TextComponent mar = new TextComponent(married);
			mar.setColor(ChatColor.LIGHT_PURPLE);
			mar.setBold(true);
			TextComponent prestige = new TextComponent(pre.getNumber(pre.getPrestige(player)));
			prestige.setColor(ChatColor.GRAY);
			prestige.setBold(true);
			TextComponent rank = new TextComponent(getPrefix(player) + " ");
			
			TextComponent name = new TextComponent(player.getDisplayName());
			TextComponent arrow = new TextComponent(" » ");
			arrow.setColor(ChatColor.DARK_GRAY);
			arrow.setBold(false);
			mar.addExtra(prestige);
			mar.addExtra(rank);
			mar.addExtra(name);
			mar.addExtra(arrow);
			
			Date d = new Date(player.getFirstPlayed());
			ComponentBuilder cb = new ComponentBuilder("-=[").color(ChatColor.GRAY).append("♦").color(ChatColor.GOLD).append("]=- -=[").color(ChatColor.GRAY).append(player.getName()).color(ChatColor.YELLOW).append("]=- -=[").color(ChatColor.GRAY).append("♦").color(ChatColor.GOLD).append("]=-").color(ChatColor.GRAY)
					.append("\n").append("Joined: ").color(ChatColor.RED).append(d.toLocaleString()).color(ChatColor.WHITE)
					.append("\n").append("Rank: ").color(ChatColor.GOLD).append("[").color(ChatColor.DARK_GRAY).append(p.perms.getPrimaryGroup(player)).color(ChatColor.GREEN).append("]").color(ChatColor.DARK_GRAY)
					.append("\n").append("Balance: ").color(ChatColor.YELLOW).append("$").color(ChatColor.GREEN).append(p.getMoney(p.eco.getBalance(player))).color(ChatColor.WHITE)
					.append("\n").append("Prestige: ").color(ChatColor.GREEN).append(pre.getNumber(pre.getPrestige(player))).color(ChatColor.WHITE)
					.append("\n").append("Progress: ").color(ChatColor.AQUA).append("|").color(ChatColor.YELLOW).bold(true).append(numToDis(player)).bold(false).append("|").color(ChatColor.YELLOW).bold(true).append(" ").append(getActualPercent(p.eco.getBalance(player), r.getNextRankCost(p.perms.getPrimaryGroup(player), player)) + "%").color(ChatColor.RESET).bold(false)
					.append("\n").append("Next rank: ").color(ChatColor.DARK_AQUA).append("[").color(ChatColor.DARK_GRAY).append(r.getNextRank(p.perms.getPrimaryGroup(player))).color(ChatColor.BLUE).append("]").color(ChatColor.DARK_GRAY)
					.append("\n").append("Crystals: ").color(ChatColor.DARK_BLUE).append(t.getTokens(player) + " crystals").color(ChatColor.WHITE)
					.append("\n").append("Blocks broken: ").color(ChatColor.DARK_PURPLE).append(getBlocks(player) + " blocks").color(ChatColor.RESET);
			TextComponent msg = new TextComponent(event.getMessage());
			msg.setColor(ChatColor.GRAY);
			msg.setBold(false);
			mar.addExtra(msg);
			mar.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, cb.create()));
			for(Player plyer : Bukkit.getOnlinePlayers()){
				plyer.spigot().sendMessage(mar);
			}
			event.setCancelled(true);
		}
	}
	
	public HashMap<UUID, Integer> blks = new HashMap<UUID, Integer>();
	@EventHandler
	public void onBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		if(p.isInAllowedRegion(event.getBlock())){
			if(blks.containsKey(player.getUniqueId())){
				blks.put(player.getUniqueId(), blks.get(player.getUniqueId())+1);
			}
			else if(!blks.containsKey(player.getUniqueId())){
				blks.put(player.getUniqueId(), 1);
			}
		}
	}
	
	public int getBlocks(Player player){
		if(blks.containsKey(player.getUniqueId())){
			return blks.get(player.getUniqueId());
		}
		else{
			return 1;
		}
	}
	
	public boolean loadItems(){
		if(config!=null){
			for(String str : config.getConfigurationSection("").getKeys(false)){
				UUID uuid = UUID.fromString(str);
				int i = Integer.parseInt(config.getString(str));
				blks.put(uuid, i);
			}
			return true;
		}
		return false;
	}
	
	public boolean saveItems(UUID uuid){
		if(config!=null){
			config.set(uuid + "", blks.get(uuid));
			config.save();
			return true;
		}
		return false;
	}
	
	public String numToDis(Player player){
		double num = Double.parseDouble(getActualPercent(p.eco.getBalance(player), r.getNextRankCost(p.perms.getPrimaryGroup(player), player)));
		int rounded = (int)Math.floor(num/10);
		String s = ChatColor.AQUA + "";
		for(int i = 1; i<=rounded; i++){
			s = s + ":";
		}
		int remaining = 10-rounded;
		if(remaining==0){
			return s;
		}
		else{
			s = s + ChatColor.GRAY;
			for(int i = 1; i<=remaining; i++){
				s = s + ":";
			}
			return s;
		}
	}
	 public String getActualPercent(double init, double goal){
		 double value = (init/goal) * 100;
		 DecimalFormat df = new DecimalFormat("#.##");
		 String s = null;
		 s = df.format(value);
		 if(value>100){
			 s="100";
		 }
		 return s;
	 }
	 
	 public String getPrefix(Player player){
		 String prefix = "";
		 int cweight = 1000000;
		 for(String s : Main.permissions){
			 String[] split = s.split(":");
			 String perm = split[1];
			 String nprefix = split[3];
			 int nweight = Integer.parseInt(split[2]);
			 if(player.hasPermission(perm)){
				 if(nweight<cweight){
					 prefix = nprefix;
					 cweight = nweight;
				 }
			 }
		 }
		 return ChatColor.translateAlternateColorCodes('&', prefix);
	 }
}
