package me.timlampen.explosions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import me.timlampen.util.CooldownAPI;
import me.timlampen.util.Explosions;
import me.timlampen.util.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.metadata.FixedMetadataValue;

import de.slikey.effectlib.effect.TraceEffect;
import de.slikey.effectlib.util.ParticleEffect;
public class GrenadeListener implements Listener{
	
	Main p;
	Explosions e;
	public GrenadeListener(Main p, Explosions e){
		this.p = p;
		this.e = e;
	}
	private ArrayList<Item> pickup = new ArrayList<Item>();
	
	@EventHandler
	public void onPickup(PlayerPickupItemEvent event){
		if(pickup.contains(event.getItem())){
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		final Player player = event.getPlayer();
		if(event.getAction()==Action.RIGHT_CLICK_AIR || event.getAction()==Action.RIGHT_CLICK_BLOCK){
			if(event.getItem()!=null && event.getItem().hasItemMeta() && event.getItem().getItemMeta().hasDisplayName() && event.getItem().getItemMeta().getDisplayName().contains(ChatColor.BLUE + "" + ChatColor.BOLD + ">>>" + ChatColor.YELLOW + "" + ChatColor.BOLD + "MineGrenade" + ChatColor.BLUE + "" + ChatColor.BOLD + "<<<"+ ChatColor.RED + "" + ChatColor.BOLD + " Level: ")){
				if(CooldownAPI.tryCooldown(player, "grenade", 1000*p.getConfig().getInt("GrenadeCooldown"))){
					ItemStack item = event.getItem();
					final Item grenade = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.FIREWORK_CHARGE));
					grenade.setMetadata("grenade", new FixedMetadataValue(p, "grenade"));
					final int level = 4*getLevel(item, player);
					if(item.getAmount()<=1){
						player.getInventory().removeItem(player.getItemInHand());
					}
					else{
						item.setAmount(item.getAmount()-1);
					}
					grenade.setVelocity(player.getEyeLocation().getDirection().multiply(1.0));
					pickup.add(grenade);
					boom.put(grenade, player.getName());
					//final TraceEffect tee = new TraceEffect(p.em);
					//tee.setEntity(grenade);
					//tee.particle = ParticleEffect.FLAME;
					//tee.start();
					Bukkit.getScheduler().runTaskLater(p, new Runnable(){
		
						@Override
						public void run() {
							//tee.cancel();
							e.explode(new Location(grenade.getWorld(), grenade.getLocation().getX(), grenade.getLocation().getY()-1, grenade.getLocation().getZ()), grenade, level, false, true);
							grenade.remove();
							pickup.remove(grenade);
						}}, 60);
				}
				else{
					player.sendMessage(p.getPrefix() + ChatColor.RED + "Error: You have to wait " + Math.round(CooldownAPI.getCooldown(player, "grenade")/1000) + " more seconds to use another grenade!");
				}
			}
		}
	}
	
	
	public ItemStack getGrenade(int strength, int amount){
		if(strength>8){
			strength = 8;
		}
		else if(amount>64){
			amount = 64;
		}
		ItemStack is = new ItemStack(Material.FIREWORK_CHARGE, amount);
		FireworkEffectMeta fm = (FireworkEffectMeta)is.getItemMeta();
		Builder effect = FireworkEffect.builder();
		if(strength==1){
			effect.withColor(Color.SILVER);
		}
		else if(strength==2){
			effect.withColor(Color.WHITE);
		}
		else if(strength==3){
			effect.withColor(Color.AQUA);
		}
		else if(strength==4){
			effect.withColor(Color.TEAL);
		}
		else if(strength==5){
			effect.withColor(Color.LIME);
		}
		else if(strength==6){
			effect.withColor(Color.GREEN);
		}
		else if(strength==7){
			effect.withColor(Color.YELLOW);
		}
		else if(strength==8){
			effect.withColor(Color.RED);
		}
		fm.setEffect(effect.build());
		fm.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + ">>>" + ChatColor.YELLOW + "" + ChatColor.BOLD + "MineGrenade" + ChatColor.BLUE + "" + ChatColor.BOLD + "<<<"
		+ ChatColor.RED + "" + ChatColor.BOLD + " Level: " + strength);
		is.setItemMeta(fm);
		return is;
	}
	public static int getLevel(ItemStack item, Player player){
		int l = 0;
		if(item!=null && item.getItemMeta().hasDisplayName()){
			String line = item.getItemMeta().getDisplayName();
			line = ChatColor.stripColor(line);
			String prefix = ">>>MineGrenade<<< Level: ";
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
	//public static final StateFlag TNT = new StateFlag("other-explosion", true);
	private ArrayList<Block> tempblks = new ArrayList<>();
	private HashMap<Item, String> boom = new HashMap<Item, String>();
	    @EventHandler
	    public void onExplode(EntityExplodeEvent event){
	        Iterator<Block> blocks = event.blockList().iterator();
	        Player player = null;
	        if(event.getEntity() instanceof Item){
	            Item item = (Item)event.getEntity();
	            if(item.hasMetadata("grenade")){
	                if(boom.containsKey(item) && Bukkit.getServer().getPlayer(boom.get(item))!=null){
	                    player = Bukkit.getServer().getPlayer(boom.get(item));
	                }
	                else{
	                    item.remove();
	                    event.setCancelled(true);
	                    player = null;
	                }
	            while(blocks.hasNext()){
	                Block block = blocks.next();
	                if(p.isInAllowedRegion(block)){
	                	if(!tempblks.contains(block)){
		                	tempblks.add(block);
		                    Iterator<ItemStack> stack = block.getDrops().iterator();
		                    while(stack.hasNext()){
		                        ItemStack items = stack.next();
		                        if(items != null && player!=null) {
		                        	if(items.getItemMeta().equals(Material.BEDROCK)){
		                        		stack.remove();
		                        		item.remove();
		                        		blocks.remove();
		                        	}
		                        	else{
		                        		player.getInventory().addItem(items);
		                        		stack.remove();
		                        	}
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
	        
	        Bukkit.getScheduler().runTaskLater(p, new Runnable(){

				@Override
				public void run() {
					tempblks.clear();
					
				}}, 40);
	  }
}
