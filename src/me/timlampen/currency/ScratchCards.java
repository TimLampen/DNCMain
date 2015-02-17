package me.timlampen.currency;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import me.timlampen.util.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ScratchCards implements Listener{
	Main p;
	public ScratchCards(Main p){
		this.p = p;
	}
	
	@EventHandler
	public void onCombine(InventoryClickEvent event){
		if(event.getWhoClicked() instanceof Player && event.getCursor()!=null && event.getCursor().getType()!=null && event.getCurrentItem()!=null && event.getCurrentItem().getType()!=null && event.getCursor().getType()==Material.PAPER && event.getCurrentItem().getType()==Material.PAPER && event.getCursor().getItemMeta().hasLore() && event.getCurrentItem().getItemMeta().hasLore() && event.getClickedInventory()!=null && event.getClickedInventory().getType()==InventoryType.PLAYER){
			Player player = (Player)event.getWhoClicked();
			ItemStack cursor = event.getCursor();
			ItemStack is = event.getCurrentItem();
			ItemStack r = getCard(player.getName(), parseDouble(player, cursor) + parseDouble(player, is));
			is.setType(Material.AIR);
			cursor.setType(Material.AIR);
			player.getInventory().addItem(r);
			player.updateInventory();
		}
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDeposit(PlayerInteractEvent event){
		Player player = event.getPlayer();
		if(event.getAction()==Action.RIGHT_CLICK_AIR || event.getAction()==Action.RIGHT_CLICK_BLOCK){
			if(event.getItem()!=null && event.getItem().getType()==Material.PAPER){
				ItemStack item = event.getItem();
				if(item.getItemMeta().hasLore() && parseDouble(player, item)!=0.0D){
					p.eco.depositPlayer(player.getName(), parseDouble(player, item));
					player.sendMessage(p.getPrefix() + ChatColor.GREEN + "You have just deposited " + parseDouble(player, item) + " into your bank account!");
					player.getInventory().removeItem(item);
				}
			}
		}
	}
	public ItemStack getCard(String s, Double value){
		ItemStack is = new ItemStack(Material.PAPER, 1);
		ItemMeta im = is.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Value of: " + ChatColor.GREEN + "$" + value);
		lore.add(ChatColor.GRAY + "Singed By: " + ChatColor.AQUA + s);
		im.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Bank Card " + ChatColor.GREEN + "" + ChatColor.BOLD + " (Right Click To Deposit)");
		im.setLore(lore);
		is.setItemMeta(im);
		lore.clear();
		return is;
	}
	
	public ItemStack getBankCard(String s, double value){
		Random ran = new Random();
		int r = ran.nextInt(10000)+1;
		ItemStack is = new ItemStack(Material.PAPER, 1);
		ItemMeta im = is.getItemMeta();
		String str = String.valueOf(value);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Value of: " + ChatColor.GREEN + "$" + str);
		lore.add(ChatColor.GRAY + "Singed By: " + s);
		lore.add(ChatColor.GRAY + "ID: " + r);
		im.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Bank Card " + ChatColor.GREEN + "" + ChatColor.BOLD + " (Right Click To Deposit)");
		im.setLore(lore);
		is.setItemMeta(im);
		lore.clear();
		return is;
	}
	public ItemStack getCard(Double value){
		ItemStack is = new ItemStack(Material.PAPER, 1);
		ItemMeta im = is.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Value of: " + ChatColor.GREEN + "$" + value);
		lore.add(ChatColor.GRAY + "Singed By: " + getName());
		im.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Scratch Card " + ChatColor.GREEN + "" + ChatColor.BOLD + " (Right Click To Deposit)");
		im.setLore(lore);
		is.setItemMeta(im);
		lore.clear();
		return is;
	}
	private Double parseDouble(Player player, ItemStack is){
		ItemMeta im = is.getItemMeta();
		ArrayList<String> lore = new ArrayList<>(im.getLore());
		String s = lore.get(0);
		s = ChatColor.stripColor(s);
		s = s.replace("Value of: $", "");
		try{
			Double.parseDouble(s);
		}catch(NumberFormatException e){
			player.sendMessage(p.getPrefix() + ChatColor.RED + "Unable to parse number!");
			return new Double(1.0);
		}
		return Double.parseDouble(s);
	}
	private String getName(){
		String s = null;
		Random ran = new Random();
		int r = ran.nextInt(100)+1;
		if(r>=70){
			s =  p.getConfig().getString("Cards.Name1", "Casino Owner") + " (30%)";
		}
		else if(r>=60){
			s =  p.getConfig().getString("Cards.Name2", "Penguin Man") + " (10%)";
		}
		else if(r>=50){
			s =  p.getConfig().getString("Cards.Name3", "KingTommy2013") + " (10%)";
		}
		else if(r>=40){
			s =  p.getConfig().getString("Cards.Name4", "Error: Not Cool Name") + " (10%)";
		}
		else if(r>=30){
			s =  p.getConfig().getString("Cards.Name5", "Breezer112") + " (10%)";
		}
		else if(r>=20){
			s =  p.getConfig().getString("Cards.Name6", "ItzNight") + " (10%)";
		}
		else if(r>=10){
			s =  p.getConfig().getString("Cards.Name7", "Emisnoo") + " (10%)";
		}
		else if(r>=5){
			s =  p.getConfig().getString("Cards.Name8", "Notch") + " (5%)";
		}
		else if(r>=2){
			s =  p.getConfig().getString("Cards.Name9", "TimLampen") + " (3%)";
		}
		else if(r>=0){
			s =  p.getConfig().getString("Cards.Name10", "DarkNessLord") + " (2%)";
		}
		return ChatColor.AQUA + s;
	}
	
	private Double generateNumber(Double base){
		Random ran = new Random();
		Double low = new Double(base/2); 
		Double multiplier = 0.0;
		int r = ran.nextInt(100)+1;
		if(r<=35){
			multiplier = new Double(1.7);
		}
		else{
			multiplier = new Double(0.8);
		}
		base = base*multiplier;
		Double num = Math.random() * (base - low) + low;
		num = Math.floor(num);
		return num;
	}
	private HashMap<UUID, Double> doub = new HashMap<UUID, Double>();
	private HashMap<UUID, Integer> times = new HashMap<UUID, Integer>();
	public void runnable(final Player player, final Double value){
		Bukkit.getScheduler().runTaskLater(p, new Runnable(){

			@Override
			public void run() {
				if(times.containsKey(player.getUniqueId()) && times.get(player.getUniqueId())>=1){
					times.put(player.getUniqueId(), times.get(player.getUniqueId())-1);
					player.sendMessage(p.getPrefix() + ChatColor.YELLOW + "" + ChatColor.BOLD + "Scratching... (" + times.get(player.getUniqueId()) + "s)");
					runnable(player, value);
				}
				else if(!times.containsKey(player.getUniqueId())){
					times.put(player.getUniqueId(), 5);
					player.sendMessage(p.getPrefix() + ChatColor.YELLOW + "" + ChatColor.BOLD + "Scratching... (5s)");
					runnable(player, value);
				}
				else{
					doub.put(player.getUniqueId(), generateNumber(value));
					times.remove(player.getUniqueId());
					if(doub.get(player.getUniqueId())<=value){
						player.sendMessage(p.getPrefix() + ChatColor.RED + "" + ChatColor.BOLD + "LOSS $" + (value-doub.get(player.getUniqueId())));
					}
					else{
						Bukkit.broadcastMessage(p.getPrefix() + ChatColor.GREEN + player.getUniqueId() + " has won a $" + (doub.get(player.getUniqueId())-value) + " payout from a /casino Scratch-Off card!");
					}
					player.getInventory().addItem(getCard(doub.get(player.getUniqueId())));
					doub.remove(player.getUniqueId());
				}
			}}, 20);
	}
}
