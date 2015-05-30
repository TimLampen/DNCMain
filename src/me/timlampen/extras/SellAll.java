package me.timlampen.extras;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map.Entry;

import me.timlampen.dcscoreboard.BlockListener;
import me.timlampen.util.ConfigMaker;
import me.timlampen.util.Main;
import me.timlampen.util.Parse;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SellAll{
	Main p;
	Parse pa;
	BlockListener bl;
	private ConfigMaker config;
	public SellAll(Main p, Parse pa, BlockListener bl){
		this.p = p;
		this.pa = pa;
		this.bl =  bl;
		config = new ConfigMaker(p, "shop");
		config.save();
	}
	public void sellItems(Player player){
		HashMap<Material, Integer> mat = new HashMap<Material, Integer>();
		String rank = p.perms.getPrimaryGroup(player);
		Inventory inv = player.getInventory();
		Double value = 0.0;
		for(ItemStack is : inv.getContents()){
			if(is!=null && is.getType()!=Material.AIR && (getItemPrice(rank, is.getType())!=1.0)){
				p.eco.depositPlayer(player, getItemPrice(rank, is.getType())*is.getAmount());
				if(mat.containsKey(is.getType())){
					mat.put(is.getType(), mat.get(is.getType())+is.getAmount());
				}
				else{
					mat.put(is.getType(), is.getAmount());
				}
				value+=getItemPrice(rank, is.getType())*is.getAmount();
				player.getInventory().removeItem(is);
				player.updateInventory();
			}
		}
		player.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "----------" + ChatColor.RESET);
		player.sendMessage(ChatColor.RED + "You've Sold:");
		for(Entry<Material, Integer> map : mat.entrySet()){
			player.sendMessage(ChatColor.GRAY + "[" + ChatColor.RED + map.getValue() + ChatColor.GRAY + "] " + map.getKey().toString());
		}
		double multi = 1;
		mat.clear();
		for(double i = 1.0; i<=5.0; i=i+.1){
			if(player.hasPermission("multiplier." + i)){
				if(i<=bl.getMultiplier(player)){
					break;
				}
				else{
					p.eco.depositPlayer(player, value*i);
					multi = i;
					break;
				}
			}
		}
		if(bl.getMultiplier(player)>1){
			p.eco.depositPlayer(player, value*bl.getMultiplier(player));
			multi = bl.getMultiplier(player);
		}
		player.sendMessage(ChatColor.RED + "You've made: " + getMoney(value*multi));
		player.sendMessage(ChatColor.DARK_GRAY + "Your total has been multiplied by " + ChatColor.RED + "" + ChatColor.UNDERLINE + multi);
		player.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "----------" + ChatColor.RESET);
	}
	
	public Double getItemPrice(String rank, Material type){
		for(String shoprank: config.getConfigurationSection("Shop").getKeys(false)){
			if(rank.equalsIgnoreCase(shoprank)){
				for(String item : config.getConfigurationSection("Shop." + shoprank).getKeys(false)){
					if(item.toString().equalsIgnoreCase(type.toString())){
						Double price = config.getDouble("Shop." + shoprank + "." + item + ".price");
						if(price!=0.00D){
							return price;
						}
						break;
					}
				}
				break;
			}
		}
		return 1.0;
	}
	 public String getMoney(double amt){
		 DecimalFormat df = new DecimalFormat("#.###");
		 String s = "";
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
	 public String getMoney(String rank, Material type, int amount){
		 String s = "";
		 DecimalFormat df = new DecimalFormat("#.###");
		 double amt = getItemPrice(rank, type)*amount;
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
}
