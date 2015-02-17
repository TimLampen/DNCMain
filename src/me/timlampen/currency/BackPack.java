package me.timlampen.currency;


import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.ChatColor;

import me.timlampen.util.ConfigMaker;
import me.timlampen.util.Lang;
import me.timlampen.util.Main;

public class BackPack implements Listener{
	private Main p;
	private Tokens t;
	private Lang l;
	private ConfigMaker config;

	public BackPack(Main p, Tokens t, Lang l){
		this.p = p;
		this.t = t;
		this.l = l;
		config = new ConfigMaker(p, "bags");
		config.save();
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		if(event.getClickedBlock()!=null && event.getAction()==Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType()==Material.ENDER_CHEST){
			event.setCancelled(true);
			openBag(player);
		}
	}
	@EventHandler
	public void onClose(InventoryCloseEvent event){
		if(event.getInventory().getName().equals(ChatColor.stripColor("BackPack")) && event.getPlayer() instanceof Player){
			Player player = (Player)event.getPlayer();
			putItemsToClass(player, event.getInventory());
		}
	}
	public void openBag(Player player){
		int size = 9;
		if(player.hasPermission("dc.bag.6")){
			size = 54;
		}
		else if(player.hasPermission("dc.bag.5")){
			size = 45;
		}
		else if(player.hasPermission("dc.bag.4")){
			size = 36;
		}
		else if(player.hasPermission("dc.bag.3")){
			size = 27;
		}
		else if(player.hasPermission("dc.bag.2")){
			size = 18;
		}
		player.openInventory(getItemsForInv(player, size));
		
	}
	public boolean saveItems(UUID uuid){
		if(config!=null){
			config.set(uuid.toString(), null);
			for(int i = 0; i<p.getPlayerInfo(uuid).getSlots().size(); i++){
				if(!(p.getPlayerInfo(uuid).getItem(i).getType()==Material.AIR)){
				config.set(uuid + "." + i, p.getPlayerInfo(uuid).getItem(i));
				}
			}
			config.save();
			return true;
		}
		return false;
	}
	
	public boolean loadItems(){
		if(config!=null){
			for(String str : config.getConfigurationSection("").getKeys(false)){
				UUID uuid = UUID.fromString(str);
				for(String slot : config.getConfigurationSection(str).getKeys(true)){
					int i = Integer.parseInt(slot);
					ItemStack is = config.getItemStack(str + "." + slot);
					p.getPlayerInfo(uuid).addItems(i, is);
				}
			}
			return true;
		}
		return false;
	}
	public void putItemsToClass(Player player, Inventory inv){
		p.getPlayerInfo(player.getUniqueId()).slots.clear();
		p.getPlayerInfo(player.getUniqueId()).items.clear();
		for(int i = 0; i<inv.getSize(); i++){
			if(inv.getItem(i)==null){
				p.getPlayerInfo(player.getUniqueId()).addItems(i, new ItemStack(Material.AIR));
			}
			else{
				p.getPlayerInfo(player.getUniqueId()).addItems(i, inv.getItem(i));
			}
		}
	}
	public Inventory getItemsForInv(Player player, int size){
		Inventory inv = Bukkit.createInventory(player, size, "BackPack");
		if(p.getPlayerInfo(player.getUniqueId()).getSlots()!=null){
			for(int i = 0; i<p.getPlayerInfo(player.getUniqueId()).getSlots().size(); i++){
				inv.setItem(p.getPlayerInfo(player.getUniqueId()).getSlot(i), p.getPlayerInfo(player.getUniqueId()).getItem(i));
			}
		}
		return inv;
	}
}
