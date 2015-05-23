package me.timlampen.extras;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.timlampen.util.ConfigMaker;
import me.timlampen.util.Main;
import me.timlampen.util.Title;

public class Prestige {
	
	private Main p;
	private ConfigMaker config;
	
	public Prestige(Main p){
		this.p = p;
		config = new ConfigMaker(p, "prestige");
		config.save();
	}
	public HashMap<UUID, Integer> rank = new HashMap<UUID, Integer>();
	@SuppressWarnings("deprecation")
	public void doPrestige(Player player){
		if(p.perms.getPrimaryGroup(player).equalsIgnoreCase("hades")){
			if(getPrestige(player)<=9){
				addPrestige(player, 1);
				Title title = new Title("", ChatColor.GREEN + player.getDisplayName() + "" + ChatColor.YELLOW + " Has Prestiged To " + getNumber(getPrestige(player)));
				title.send(player);
				player.getEnderChest().clear();
				player.sendMessage(p.getPrefix() + ChatColor.GRAY + "Clearing EnderChest...");
				p.getPlayerInfo(player.getUniqueId()).clear();
				player.sendMessage(p.getPrefix() + ChatColor.GRAY + "Clearing Money...");
				p.eco.withdrawPlayer(player.getName(), p.eco.getBalance(player.getName()));
				player.sendMessage(p.getPrefix() + ChatColor.GRAY + "Clearing BackPack...");
				player.getInventory().clear();
				player.sendMessage(p.getPrefix() + ChatColor.GRAY + "Clearing Inventory...");
				player.chat("/spawn");
				player.sendMessage(p.getPrefix() + ChatColor.GREEN + "Complete!");
				p.perms.playerRemoveGroup(player, "Hades");
				p.perms.playerAddGroup(player, "A");
			}else if(!rank.containsKey(player.getUniqueId())){
				addPrestige(player, 1);
				Title title = new Title("", ChatColor.GREEN + player.getDisplayName() + "" + ChatColor.YELLOW + " Has Prestiged To " + getNumber(getPrestige(player)));
				title.send(player);
				player.getEnderChest().clear();
				player.sendMessage(p.getPrefix() + ChatColor.GRAY + "Clearing EnderChest...");
				p.getPlayerInfo(player.getUniqueId()).clear();
				player.getEnderChest().clear();
				player.sendMessage(p.getPrefix() + ChatColor.GRAY + "Clearing Money...");
				p.eco.withdrawPlayer(player.getName(), p.eco.getBalance(player.getName()));
				player.sendMessage(p.getPrefix() + ChatColor.GRAY + "Clearing BackPack...");
				player.getInventory().clear();
				player.sendMessage(p.getPrefix() + ChatColor.GRAY + "Clearing Inventory...");
				player.chat("/spawn");
				player.sendMessage(p.getPrefix() + ChatColor.GREEN + "Complete!");
				p.perms.playerRemoveGroup(player, "Hades");
				p.perms.playerAddGroup(player, "A");
			}else{
				player.sendMessage(p.getPrefix() + ChatColor.RED + "Group Error: You are already the maximum prestige!");
			}
		}else{
			player.sendMessage(p.getPrefix() + ChatColor.RED + "Group Error: You need to be group Hades to do this command!");
		}
	}
	
	public void addPrestige(Player player, int amt){
		if(rank.containsKey(player.getUniqueId())){
			rank.put(player.getUniqueId(), rank.get(player.getUniqueId())+amt);
		}
		else{
			rank.put(player.getUniqueId(), amt);
		}
	}
	
	public void setPrestge(Player player, int amt){
		rank.put(player.getUniqueId(), amt);
	}
	
	public int getPrestige(Player player){
		if(rank.containsKey(player.getUniqueId())){
			return rank.get(player.getUniqueId());
		}
		else{
			return 0;
		}
	}
	
	public void loadPresitge(){
		if(config!=null){
			for(String str : config.getConfigurationSection("").getKeys(false)){
				UUID uuid = UUID.fromString(str);
				Integer i = config.getInt(str);
				rank.put(uuid, i);
			}
		}
	}
	
	public void savePrestige(UUID uuid){
		if(config!=null){
			config.set(uuid + "", rank.get(uuid));
			config.save();
		}
	}
	
	public String getNumber(int num){
		if(num==1){
			return "I ";
		}
		else if(num==2){
			return "II ";
		}
		else if(num==3){
			return "III ";
		}
		else if(num==4){
			return "IV ";
		}
		else if(num==5){
			return "V ";
		}
		else if(num==6){
			return "VI ";
		}
		else if(num==7){
			return "VII ";
		}
		else if(num==8){
			return "VIII ";
		}
		else if(num==9){
			return "IX ";
		}
		else if(num==10){
			return "X ";
		}
		else{
			return "0 ";
		}
	}
}
