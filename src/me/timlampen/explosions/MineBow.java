package me.timlampen.explosions; 

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

public class MineBow implements Listener{
	Main p;
	Explosions e;
	public MineBow(Main p, Explosions e){
		this.e = e;
		this.p = p;
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent event){
		Player player = event.getPlayer();
		if(player.getItemInHand()!=null && player.getItemInHand().getType()==Material.BOW){
			if(getLevel(player.getItemInHand(), player)>0){
				int l = getLevel(player.getItemInHand(), player);
				int le = l-1;
				ItemStack is = player.getItemInHand();
				ItemMeta im = is.getItemMeta();
				im.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + ">>>" + ChatColor.RED + "" + ChatColor.BOLD + "MineBow" + ChatColor.YELLOW + "" + ChatColor.BOLD + "<<< " + ChatColor.RED + "" + ChatColor.BOLD + 
				"Charges: " + le);
				is.setItemMeta(im);
				final Item grenade = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.FIREWORK));
				boomz.put(grenade, player.getUniqueId());
				grenade.setMetadata("bow", new FixedMetadataValue(p, "bow"));
				grenade.setVelocity(player.getEyeLocation().getDirection().multiply(1.0));
				runnable(player, grenade, 8);
			}
			else if(getLevel(player.getItemInHand(), player)==0){
				player.getInventory().removeItem(player.getItemInHand());
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
	
	private HashMap<Item, UUID> boomz = new HashMap<Item, UUID>();
	@EventHandler(ignoreCancelled = false)
	public void onExplode(EntityExplodeEvent event){
		Iterator<Block> blocks = event.blockList().iterator();
		Player player = null;
		if(event.getEntity() instanceof Item){
			Item item = (Item)event.getEntity();
			if(item.hasMetadata("bow")){
				if(boomz.containsKey(item) && Bukkit.getServer().getPlayer(boomz.get(item))!=null){
					player = Bukkit.getServer().getPlayer(boomz.get(item));
				}
				else{
					item.remove();
					event.setCancelled(true);
					player = null;
				}
				while(blocks.hasNext()){
					if(p.isInAllowedRegion(blocks.next())){
						if(blocks.next().getDrops()!=null && blocks.next().getType()!=Material.AIR){
							Iterator<ItemStack> stack = blocks.next().getDrops().iterator();
							while(stack.hasNext()){
								ItemStack items = stack.next();
								if(items != null && player!=null) {
	                        		player.getInventory().addItem(p.translateBlock(items));
								   stack.remove();
								}
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
	
	@EventHandler
	public void onDmg(EntityDamageEvent event){
		if(event.getCause()==DamageCause.ENTITY_EXPLOSION && event.getEntity() instanceof Player){
			event.setCancelled(true);
		}
	}
}
