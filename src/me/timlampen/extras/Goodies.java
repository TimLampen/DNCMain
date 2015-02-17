package me.timlampen.extras;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.timlampen.explosions.GrenadeListener;
import me.timlampen.explosions.RocketListener;
import me.timlampen.util.Lang;
import me.timlampen.util.Main;

public class Goodies implements Listener{
	Main p;
	Lang l;
	GrenadeListener gl;
	RocketListener rl;
	public Goodies(Main p, Lang l, GrenadeListener gl, RocketListener rl){
		this.p = p;
		this.l = l;
		this.rl = rl;
		this.gl = gl;
	}
	
	@SuppressWarnings("static-access")
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		if(event.getAction()==Action.RIGHT_CLICK_AIR || event.getAction()==Action.RIGHT_CLICK_BLOCK){
			if(event.getItem()!=null && event.getItem().getItemMeta().hasDisplayName() && event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "Satchel o' Goodies")){
				if(player.getInventory().firstEmpty()==-1){
					l.doMessage(player, l.r1.INV_FULL, "");
				}
				else{
					l.doMessage(player, l.s1.GOODIES_OPEN, "", "");
					if(event.getItem().getAmount()>1){
						event.getItem().setAmount(event.getItem().getAmount()-1);
					}
					else{
						player.getInventory().removeItem(event.getItem());
					}
					getItems(player);
				}
			}
		}
	}
	
	
	public ItemStack getBag(){
		ItemStack is = new ItemStack(Material.IRON_BARDING, 1);
		ItemMeta im = is.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Try your luck and open me! ");
		im.setLore(lore);
		im.setDisplayName(ChatColor.LIGHT_PURPLE + "Satchel o' Goodies");
		is.setItemMeta(im);
		return is;
	}
	
	public ItemStack getBag(Integer amount){
		ItemStack is = new ItemStack(Material.IRON_BARDING, amount);
		ItemMeta im = is.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Try your luck and open me! ");
		im.setLore(lore);
		im.setDisplayName(ChatColor.LIGHT_PURPLE + "Satchel o' Goodies");
		is.setItemMeta(im);
		return is;
	}
	public void getItems(Player player){
		for(int i=1; i<=p.getConfig().getInt("AmountOfGoodieItems");i++){
			getItem(player);
			player.updateInventory();
		}
	}
	public void runnable(final Player player){
		final int i=0;
		Bukkit.getScheduler().scheduleSyncDelayedTask(p, new Runnable(){

			@Override
			public void run() {
				if(i<=3){
					getItem(player);
					player.updateInventory();
					runnable(player);
				}
				
			}}, 5);
	}
	
	public ArrayList<String> cmd = new ArrayList<String>();
	public void getItem(Player player){
		Random r = new Random();
		int ran = r.nextInt(p.getConfig().getInt("AmountOfDifferentGoodies"));
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd.get(ran).replace("%player%", player.getName()));
	}

}
