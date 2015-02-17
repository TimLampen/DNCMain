package me.timlampen.explosions;

import java.util.HashMap;
import java.util.Iterator;

import me.timlampen.util.CooldownAPI;
import me.timlampen.util.Explosions;
import me.timlampen.util.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
public class RocketListener implements Listener{
	
	Main p;
	Explosions e;
	public RocketListener(Main p, Explosions e){
		this.p = p;
		this.e = e;
	}
	@EventHandler
    public void onEntityDamage(EntityDamageByBlockEvent event) {
        if(event.getCause().equals(DamageCause.BLOCK_EXPLOSION) && event.getEntity() instanceof Player){
        	event.setCancelled(true);
        }
    }
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		final Player player = event.getPlayer();
		if(event.getAction()==Action.RIGHT_CLICK_AIR || event.getAction()==Action.RIGHT_CLICK_BLOCK){
			if(event.getItem()!=null && event.getItem().hasItemMeta() && event.getItem().getItemMeta().hasDisplayName() && event.getItem().getItemMeta().getDisplayName().contains(ChatColor.YELLOW+ "" + ChatColor.BOLD + ">>>" + ChatColor.BLUE + "" + ChatColor.BOLD + "MineRocket" + ChatColor.YELLOW + "" + ChatColor.BOLD + "<<<"+ ChatColor.RED + "" + ChatColor.BOLD + " Level: ")){
				if(CooldownAPI.tryCooldown(player, "rocket", 1000*p.getConfig().getInt("RocketCooldown"))){
					p.message.add(player.getUniqueId());
					ItemStack item = event.getItem();
					final Item grenade = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.FIREWORK));
					boomz.put(grenade, player.getName());
					final int level = 2*getLevel(item, player);
					grenade.getItemStack().addUnsafeEnchantment(Enchantment.DURABILITY, 2);
					grenade.setVelocity(player.getEyeLocation().getDirection().multiply(1.0));
					if(item.getAmount()<=1){
						player.getInventory().removeItem(player.getItemInHand());
					}
					else{
						item.setAmount(item.getAmount()-1);
					}
					runnable(player, grenade, level);
				}
				else{
					if(p.message.contains(player.getUniqueId())){
						p.message.remove(player.getUniqueId());
					}
					else{
						player.sendMessage(p.getPrefix() + ChatColor.RED + "Error: You have to wait " + Math.round(CooldownAPI.getCooldown(player, "rocket")/1000) + " more seconds to use another rocket!");
					}
				}
			}
		}
	}
	
	public ItemStack getGrenade(int strength, int amount){
		if(strength>4){
			strength = 4;
		}
		else if(amount>64){
			amount = 64;
		}
		ItemStack is = new ItemStack(Material.FIREWORK_CHARGE, amount);
		FireworkEffectMeta fm = (FireworkEffectMeta)is.getItemMeta();
		Builder effect = FireworkEffect.builder();
		if(strength==1){
			effect.withColor(Color.AQUA);
		}
		else if(strength==2){
			effect.withColor(Color.GREEN);
		}
		else if(strength==3){
			effect.withColor(Color.YELLOW);
		}
		else if(strength==4){
			effect.withColor(Color.RED);
		}
		fm.setEffect(effect.build());
		fm.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + ">>>" + ChatColor.BLUE + "" + ChatColor.BOLD + "MineRocket" + ChatColor.YELLOW + "" + ChatColor.BOLD + "<<<"
		+ ChatColor.RED + "" + ChatColor.BOLD + " Level: " + strength);
		is.setItemMeta(fm);
		return is;
	}
	public static int getLevel(ItemStack item, Player player){
		int l = 0;
		if(item!=null && item.getItemMeta().hasDisplayName()){
			String line = item.getItemMeta().getDisplayName();
			line = ChatColor.stripColor(line);
			String prefix = ">>>MineRocket<<< Level: ";
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
	@EventHandler
    public void onExplode(EntityExplodeEvent event){
        Iterator<Block> blocks = event.blockList().iterator();
        Player player = null;
        if(event.getEntity() instanceof Item){
            Item item = (Item)event.getEntity();
            if(item.getItemStack().getEnchantmentLevel(Enchantment.DURABILITY)!=0){
                if(boomz.containsKey(item) && Bukkit.getServer().getPlayer(boomz.get(item))!=null){
                    player = Bukkit.getServer().getPlayer(boomz.get(item));
                }
                else{
                    item.remove();
                    event.setCancelled(true);
                    player = null;
                }
            while(blocks.hasNext()){
                Block block = blocks.next();
                if(p.isInRegion(block)){
                    Iterator<ItemStack> stack = block.getDrops().iterator();
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
