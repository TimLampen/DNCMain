package me.timlampen.currency;

import me.timlampen.util.CooldownAPI;
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

public class ScratchInv implements Listener{
	Lang l;
	Main p;
	ScratchCards sc;
	Inventory inv;
	public ScratchInv(Player player){
		Inventory inv  = Bukkit.createInventory(player, 9, ChatColor.GOLD + "" + ChatColor.BOLD + "DNC Casino ($$$)");
		this.inv = inv;
		for(int i = 0; i<9; i++){
			inv.setItem(i, getItem(i+1));
		}
		player.openInventory(inv);
	}
	public ScratchInv(ScratchCards sc, Main p, Lang l){
		this.p = p;
		this.l = l;
		this.sc = sc;
	}
	
	@SuppressWarnings({ "static-access", "deprecation" })
	@EventHandler
	public void onClick(InventoryClickEvent event){
		if(event.getWhoClicked() instanceof Player){
			Player player = (Player)event.getWhoClicked();
			if(event.getClickedInventory()!=null && event.getClickedInventory().getName()!=null && event.getClickedInventory().getName().equalsIgnoreCase(ChatColor.GOLD + "" + ChatColor.BOLD + "DNC Casino ($$$)")){
				event.setCancelled(true);
				player.closeInventory();
				if(CooldownAPI.tryCooldown(player, "scratch", 10*1000)){
					if(event.getCurrentItem()!=null && event.getCurrentItem().getItemMeta().hasDisplayName()){
						ItemStack is = event.getCurrentItem();
						if(is.getItemMeta().getDisplayName().contains(ChatColor.stripColor("($1Mill)"))){
							Double d = new Double("1000000");
							if(p.eco.getBalance(player.getName())>d){
								p.eco.withdrawPlayer(player.getName(), d);
								sc.runnable(player, d);	
							}
							else{
								player.sendMessage(p.getPrefix() + ChatColor.RED + "Unable to buy scratch card, you do not have enough money!");
							}
						}
						else if(is.getItemMeta().getDisplayName().contains(ChatColor.stripColor("($100Mill)"))){
							Double d = new Double("100000000");
							if(p.eco.getBalance(player.getName())>d){
								p.eco.withdrawPlayer(player.getName(), d);
								sc.runnable(player, d);	
							}
							else{
								player.sendMessage(p.getPrefix() + ChatColor.RED + "Unable to buy scratch card, you do not have enough money!");
							}
						}
						else if(is.getItemMeta().getDisplayName().contains(ChatColor.stripColor("($500Mill)"))){
							Double d = new Double("500000000");
							if(p.eco.getBalance(player.getName())>d){
								p.eco.withdrawPlayer(player.getName(), d);
								sc.runnable(player, d);	
							}
							else{
								player.sendMessage(p.getPrefix() + ChatColor.RED + "Unable to buy scratch card, you do not have enough money!");
							}
						}
						else if(is.getItemMeta().getDisplayName().contains(ChatColor.stripColor("($750Mill)"))){
							Double d = new Double("750000000");
							if(p.eco.getBalance(player.getName())>d){
								p.eco.withdrawPlayer(player.getName(), d);
								sc.runnable(player, d);	
							}
							else{
								player.sendMessage(p.getPrefix() + ChatColor.RED + "Unable to buy scratch card, you do not have enough money!");
							}
						}
						else if(is.getItemMeta().getDisplayName().contains(ChatColor.stripColor("($1Bill)"))){
							Double d = new Double("1000000000");
							if(p.eco.getBalance(player.getName())>d){
								p.eco.withdrawPlayer(player.getName(), d);
								sc.runnable(player, d);	
							}
							else{
								player.sendMessage(p.getPrefix() + ChatColor.RED + "Unable to buy scratch card, you do not have enough money!");
							}
						}
						else if(is.getItemMeta().getDisplayName().contains(ChatColor.stripColor("($100Bill)"))){
							Double d = new Double("100000000000");
							if(p.eco.getBalance(player.getName())>d){
								p.eco.withdrawPlayer(player.getName(), d);
								sc.runnable(player, d);	
							}
							else{
								player.sendMessage(p.getPrefix() + ChatColor.RED + "Unable to buy scratch card, you do not have enough money!");
							}
						}
						else if(is.getItemMeta().getDisplayName().contains(ChatColor.stripColor("($500Bill)"))){
							Double d = new Double("500000000000");
							if(p.eco.getBalance(player.getName())>d){
								p.eco.withdrawPlayer(player.getName(), d);
								sc.runnable(player, d);	
							}
							else{
								player.sendMessage(p.getPrefix() + ChatColor.RED + "Unable to buy scratch card, you do not have enough money!");
							}
						}
						else if(is.getItemMeta().getDisplayName().contains(ChatColor.stripColor("($750Bill)"))){
							Double d = new Double("750000000000");
							if(p.eco.getBalance(player.getName())>d){
								p.eco.withdrawPlayer(player.getName(), d);
								sc.runnable(player, d);	
							}
							else{
								player.sendMessage(p.getPrefix() + ChatColor.RED + "Unable to buy scratch card, you do not have enough money!");
							}
						}
						else if(is.getItemMeta().getDisplayName().contains(ChatColor.stripColor("($1Trill)"))){
							Double d = new Double("1000000000000");
							if(p.eco.getBalance(player.getName())>d){
								p.eco.withdrawPlayer(player.getName(), d);
								sc.runnable(player, d);	
							}
							else{
								player.sendMessage(p.getPrefix() + ChatColor.RED + "Unable to buy scratch card, you do not have enough money!");
							}
						}
					}
				}
				else{
					l.doMessage(player, l.r1.COOLDOWN, CooldownAPI.getCooldown(player, "scratch")/1000 + "");
				}
			}
		}
	}
	
	public ItemStack getItem(int value){
		ItemStack is = new ItemStack(Material.PAPER);
		ItemMeta im = is.getItemMeta();
		switch(value){
		case 1:
			im.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Scratch Card " + ChatColor.GREEN + "" + ChatColor.BOLD + " ($1Mill)");
			break;
		case 2:
			im.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Scratch Card " + ChatColor.GREEN + "" + ChatColor.BOLD + " ($100Mill)");
			break;
		case 3:
			im.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Scratch Card " + ChatColor.GREEN + "" + ChatColor.BOLD + " ($500Mill)");
			break;
		case 4:
			im.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Scratch Card " + ChatColor.GREEN + "" + ChatColor.BOLD + " ($750Mill)");
			break;
		case 5:
			im.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Scratch Card " + ChatColor.GREEN + "" + ChatColor.BOLD + " ($1Bill)");
			break;
		case 6:
			im.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Scratch Card " + ChatColor.GREEN + "" + ChatColor.BOLD + " ($100Bill)");
			break;
		case 7:
			im.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Scratch Card " + ChatColor.GREEN + "" + ChatColor.BOLD + " ($500Bill)");
			break;
		case 8:
			im.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Scratch Card " + ChatColor.GREEN + "" + ChatColor.BOLD + " ($750Bill)");
			break;
		case 9:
			im.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Scratch Card " + ChatColor.GREEN + "" + ChatColor.BOLD + " ($1Trill)");
			break;
		}
		is.setItemMeta(im);
		return is;
	}
}
