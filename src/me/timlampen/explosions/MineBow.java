package me.timlampen.explosions; 

import java.util.HashMap;
import java.util.Iterator;

import me.timlampen.util.Explosions;
import me.timlampen.util.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MineBow implements Listener{
	Main p;
	Explosions e;
	public MineBow(Main p, Explosions e){
		this.e = e;
		this.p = p;
	}
	
	@EventHandler
	public void onShoot(EntityShootBowEvent event){
		if(event.getEntity() instanceof Player){
			Player player = (Player)event.getEntity();
			if(getLevel(event.getBow(), player)>0){
				int l = getLevel(event.getBow(), player);
				int le = l-1;
				ItemStack is = event.getBow();
				ItemMeta im = event.getBow().getItemMeta();
				im.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + ">>>" + ChatColor.RED + "" + ChatColor.BOLD + "MineBow" + ChatColor.YELLOW + "" + ChatColor.BOLD + "<<< " + ChatColor.RED + "" + ChatColor.BOLD + 
				"Charges: " + le);
				is.setItemMeta(im);
				event.setProjectile(null);
				final Item grenade = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.FIREWORK));
				boomz.put(grenade, player.getName());
				grenade.getItemStack().addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 2);
				grenade.setVelocity(player.getEyeLocation().getDirection().multiply(1.0));
				runnable(player, grenade, 8);
			}
			else if(getLevel(event.getBow(), player)==0){
				player.getInventory().removeItem(event.getBow());
			}
		}
	}
	public ItemStack getBow(int strength){
		ItemStack is = new ItemStack(Material.BOW);
		ItemMeta im = is.getItemMeta();
		if(strength < 0){
			strength = 0;
		}
		im.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + ">>>" + ChatColor.RED + "" + ChatColor.BOLD + "MineBow" + ChatColor.YELLOW + "" + ChatColor.BOLD + "<<< " + ChatColor.RED + "" + ChatColor.BOLD + 
				"Charges: " + strength);
		is.setItemMeta(im);
		return is;
	}
	
	public static int getLevel(ItemStack item, Player player){
		int l = 0;
		if(item!=null && item.getItemMeta().hasDisplayName()){
			String line = item.getItemMeta().getDisplayName();
			line = ChatColor.stripColor(line);
			String prefix = ">>>MineBow<<< Charges: ";
			String level = line.replace(prefix, "");
			level = ChatColor.stripColor(level);
			try{
			l = Integer.parseInt(level);
			}catch(NumberFormatException e){
				player.sendMessage(ChatColor.RED + "Error: Unable to parse level from item");
				e.printStackTrace();
			}
				return l;
		}
		return l;
	}
	
	private HashMap<String, Integer> boom = new HashMap<>();
	public void runnable(final Player player, final Item item, final int level){
		Bukkit.getScheduler().scheduleSyncDelayedTask(p, new Runnable(){

			@Override
			public void run() {
				if(boom.containsKey(player.getName()) && boom.get(player.getName())<=4){
					e.explode(item.getLocation(), item, level, false, true);
					boom.put(player.getName(), boom.get(player.getName())+1);
					runnable(player, item, level);
				}
				else if(!boom.containsKey(player.getName())){
					e.explode(item.getLocation(), item, level, false, true);
					boom.put(player.getName(), 2);
					runnable(player, item, level);
				}
				else if(boom.get(player.getName())>4){
					boom.remove(player.getName());
					item.remove();
				}
			}}, 5);
	}
	
	private HashMap<Item, String> boomz = new HashMap<Item, String>();
	@EventHandler(ignoreCancelled = false)
	public void onExplode(EntityExplodeEvent event){
		Iterator<Block> blocks = event.blockList().iterator();
		Player player = null;
		if(event.getEntity() instanceof Item){
			Item item = (Item)event.getEntity();
			if(item.getItemStack().getEnchantmentLevel(Enchantment.ARROW_DAMAGE)!=0){
				if(boomz.containsKey(item) && Bukkit.getServer().getPlayer(boomz.get(item))!=null){
					player = Bukkit.getServer().getPlayer(boomz.get(item));
				}
				else{
					item.remove();
					event.setCancelled(true);
					player = null;
				}
				while(blocks.hasNext()){
					if(p.isInRegion(blocks.next())){
						Iterator<ItemStack> stack = blocks.next().getDrops().iterator();
						while(stack.hasNext()){
							ItemStack items = stack.next();
							if(items != null && player!=null) {
							   player.getInventory().addItem(items);
							   stack.remove();
							}
						}
					}
					else{
						blocks.remove();
					}
				}
			}
		}
	}

}
