package me.timlampen.extras;

import java.text.DecimalFormat;
import java.util.ArrayList;

import me.timlampen.commands.Rankup;
import me.timlampen.util.Lang;
import me.timlampen.util.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WarpInv implements Listener{
	Lang l;
	Main p;
	Rankup r;
	Inventory inv;
	String[] norm = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
	String[] prestige = {"Warlock", "Guardian", "Phantom", "Emperor", "Spartan", "Siren", "Argos", "Saytre", "Kronos", "Hades"};
	String[] donor = {"Thief", "Guard", "Warrior", "Assassin", "King", "Titan", "God", "Demonic", "*Coming-Soon*"};
	public WarpInv(Player player){
		Inventory inv  = Bukkit.createInventory(player, 54, ChatColor.RED + "" + ChatColor.BOLD + "DarkNess" + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Craft " + ChatColor.GOLD + ChatColor.BOLD + "Mines");
		this.inv = inv;
		for(int i = 0; i<26; i++){
			inv.setItem(i, getItem(player, norm[i]));
		}
		for(int i = 0; i<10; i++){
			inv.setItem(i+26, getItem(player, prestige[i]));
		}
		for(int i = 0; i<9; i++){
			inv.setItem(i+36, getSpacer());
		}
		for(int i = 0; i<8; i++){
				inv.setItem(i+45, getDonorItem(player, donor[i]));
		}
		player.openInventory(inv);
	}
	@EventHandler
	public void onInvClick(InventoryClickEvent event){
		if(event.getWhoClicked() instanceof Player){
			Player player = (Player)event.getWhoClicked();
			if(event.getInventory()!=null && event.getInventory().getSize()==54 && event.getInventory().getName()!=null && event.getInventory().getName().contains(ChatColor.GOLD + "" + ChatColor.BOLD + "Mines")){
				event.setCancelled(true);
				if(event.getCurrentItem()!=null && event.getCurrentItem().getType()==Material.EMERALD_BLOCK){
					if(event.getCurrentItem().getItemMeta().hasDisplayName()){
						player.chat("/warp " + parseWarp(event.getCurrentItem().getItemMeta().getDisplayName()));
						player.closeInventory();
					}
				}
			}
		}
	}
	public WarpInv(Main p, Lang l, Rankup r){
		this.p = p;
		this.l = l;
		this.r = r;
	}
	public String parseWarp(String s){
		s = s.toLowerCase();
		s = ChatColor.stripColor(s);
		s = s.replace("♦ ", "");
		s = s.replace(" block ♦", "");
		return s;
	}
	public ItemStack getSpacer(){
		ItemStack is = new ItemStack(Material.THIN_GLASS);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "↓ Donor Mines ↓");
		is.setItemMeta(im);
		
		return is;
	}
	public ItemStack getDonorItem(Player player, String s){
		ItemStack is = null;
		ItemMeta im;
		String perm = s.toLowerCase();
		ArrayList<String> lore = new ArrayList<String>();
		if(player.hasPermission("essentials.warps." + perm)){
			is = new ItemStack(Material.DIAMOND_BLOCK);
			im = is.getItemMeta();
			im.setDisplayName(ChatColor.GREEN + "♦ " + s + " Block ♦");
			lore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Status: " + ChatColor.GREEN + ChatColor.BOLD + "Unlocked!");
			im.setLore(lore);
		}
		else{
			is = new ItemStack(Material.REDSTONE_BLOCK);
			im = is.getItemMeta();
			im.setDisplayName(ChatColor.RED + "♦ " + s + " Block ♦");
			lore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Status: " + ChatColor.RED + ChatColor.BOLD + "Locked");
			im.setLore(lore);
		}
		is.setItemMeta(im);
		return is;
	}
	public ItemStack getItem(Player player, String s){
		ItemStack is = null;
		ItemMeta im;
		String perm = s.toLowerCase();
		ArrayList<String> lore = new ArrayList<String>();
		if(player.hasPermission("essentials.warps." + perm)){
			is = new ItemStack(Material.EMERALD_BLOCK);
			im = is.getItemMeta();
			im.setDisplayName(ChatColor.GREEN + "♦ " + s + " Block ♦");
			lore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Progress: 100%");
			lore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Status: " + ChatColor.GREEN + ChatColor.BOLD + "Unlocked");
			im.setLore(lore);
		}
		else{
			is = new ItemStack(Material.REDSTONE_BLOCK);
			im = is.getItemMeta();
			im.setDisplayName(ChatColor.RED + "♦ " + s + " Block ♦");
			lore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Progress: " + getActualPercent(p.eco.getBalance(player), getRankCost(s)));
			lore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Status: " + ChatColor.RED + ChatColor.BOLD + "Locked");
			im.setLore(lore);
		}
		is.setItemMeta(im);
		return is;
	}
	 public String getActualPercent(Double init, Double goal){
		 double value = (init/goal) * 100;
		 DecimalFormat df = new DecimalFormat("#.###");
		 String s = null;
		 s = df.format(value);
		 if(value<=0){
			 s="0.000";
		 }
		 if(value>100){
			 s="100";
		 }
		 s = s + "%";
		 return s;
	 }
		public Double getRankCost(String rank){
		    if (Main.getPlugin().getConfig().getString("rankup." + rank + ".cost") != null) {
			      return Main.getPlugin().getConfig().getDouble("rankup." + rank + ".cost");
			    }
		    return new Double("1000000000000000000.0");
		}
}
