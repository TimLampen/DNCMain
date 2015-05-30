package me.timlampen.explosions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import me.timlampen.util.Lang;
import me.timlampen.util.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ExtraExplosions implements Listener{
	Main p;
	Lang l;
	public ExtraExplosions(Main p, Lang l){
		this.p = p;
		this.l = l;
	}
	@EventHandler
	public void onBlock1(BlockBreakEvent event){
		Player player = event.getPlayer();
		if(event.getBlock().getType()==Material.TNT){
			if(p.isInAllowedRegion(event.getBlock())){
				event.getBlock().setType(Material.AIR);
				Entity tnt = player.getWorld().spawn(event.getBlock().getLocation(), TNTPrimed.class);
				((TNTPrimed)tnt).setFuseTicks(4);
			}
		}
	}
	@SuppressWarnings({ "static-access", "deprecation" })
	@EventHandler
	public void onBlock2(BlockBreakEvent event){
		final Player player = event.getPlayer();
		Random ran = new Random();
		int r = ran.nextInt(p.getConfig().getInt("KittyChance")+1);
		if(r==1){
			if(p.isInAllowedRegion(event.getBlock())){
				l.doMessage(player, l.s1.KITTY_UNEARTH, "", "");
				final LivingEntity ocelot = player.getWorld().spawnCreature(event.getBlock().getLocation(), EntityType.OCELOT);
				boom.put(ocelot, player.getName());
				ocelot.setMetadata("cat", new FixedMetadataValue(p, "true"));
				ocelot.setCustomName(ChatColor.RED + "Explosive Cat");
				ocelot.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 2));
				Bukkit.getScheduler().runTaskLater(p, new Runnable(){
	
					@Override
					public void run() {
						player.getWorld().createExplosion(ocelot.getLocation(), 8F);
						ocelot.remove();
						
					}}, 5*20);
			}
		}
	}
	
	private ArrayList<Block> tempblks = new ArrayList<>();
	private HashMap<LivingEntity, String> boom = new HashMap<LivingEntity, String>();
	    @EventHandler
	    public void onExplode(EntityExplodeEvent event){
	        Iterator<Block> blocks = event.blockList().iterator();
	        Player player = null;
	        if(event.getEntity() instanceof LivingEntity){
	        	LivingEntity item = (LivingEntity)event.getEntity();
	            if(item.hasMetadata("cat")){
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
