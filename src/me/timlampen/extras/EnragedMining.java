package me.timlampen.extras;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import me.timlampen.util.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EnragedMining implements Listener{
	Main p;
	
	public EnragedMining(Main p){
		this.p = p;
	}
	
	public HashMap<UUID, Integer> ready = new HashMap<UUID, Integer>();
	//1 = he clicked once
	//2 = he clicked twice, and got the effect
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		final Player player = event.getPlayer();
		if(player.getItemInHand().getType()!=null && player.getItemInHand().getType().toString().contains("PICKAXE") && (event.getAction()==Action.RIGHT_CLICK_AIR || event.getAction()==Action.RIGHT_CLICK_BLOCK)){
			
			if(ready.containsKey(player.getUniqueId()) && ready.get(player.getUniqueId())==2){
				player.sendMessage(p.getPrefix() + ChatColor.RED + "There is a five minute cooldown inbetween 'Enraged Mining' boosts!");
			}
			if(!ready.containsKey(player.getUniqueId())){
				ready.put(player.getUniqueId(), 1);
				player.sendMessage(p.getPrefix() + ChatColor.GRAY + "*You Raise Your Pickaxe*");
				Bukkit.getScheduler().runTaskLater(p, new Runnable(){
					@Override
					public void run() {
						if(ready.containsKey(player.getUniqueId()) && ready.get(player.getUniqueId())==2){
							return;
						}
						player.sendMessage(p.getPrefix() + ChatColor.GRAY + "*You Lower Your Pickaxe*");
						ready.remove(player.getUniqueId());
					}
				}, 100);
				return;
			}
			else if(ready.containsKey(player.getUniqueId()) && ready.get(player.getUniqueId())==1){
				ready.put(player.getUniqueId(), 2);
				player.sendMessage(p.getPrefix() + ChatColor.YELLOW + "" + ChatColor.MAGIC + "G" + ChatColor.YELLOW + "" + ChatColor.BOLD + "ENRAGED MINING ACTIVATED!!!" + ChatColor.YELLOW + "" + ChatColor.MAGIC + "G");
				player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING,  20*5, p.getConfig().getInt("Enraged", 5)-1));
				runnable(player);
				Bukkit.getScheduler().runTaskLater(p, new Runnable(){
	
					@Override
					public void run() {
						ready.remove(player.getUniqueId());
						player.sendMessage(p.getPrefix() + ChatColor.YELLOW + "Enraged Mining is now off of cooldown");
					}}, 20*300);
			}
		}
		
	}
	private HashMap<String, Integer> run = new HashMap<String, Integer>();
	public void runnable(final Player player){
		Bukkit.getScheduler().runTaskLater(p, new Runnable(){

			@Override
			public void run() {
				if(run.containsKey(player.getName()) && run.get(player.getName())<=10){
					getFirework(player);
					run.put(player.getName(), run.get(player.getName())+1);
					runnable(player);
				}
				else if(!run.containsKey(player.getName())){
					run.put(player.getName(), 1);
					runnable(player);
				}
				else{
					run.remove(player.getName());
				}
			}}, 20);
	}
	
	public void getFirework(Player player){
		Firework f = (Firework)player.getWorld().spawn(player.getLocation(), Firework.class);
		FireworkMeta data = f.getFireworkMeta();
		data.addEffects(FireworkEffect.builder().withColor(getColor()).with(Type.BALL_LARGE).withFade(getColor()).withFlicker().build());
		data.setPower(0);
		f.setFireworkMeta(data);
		
	}
	public Color getColor(){
		Random r = new Random();
		int ran = r.nextInt(17)+1;
		Color c = null;
		switch(ran){
		case 1:
			c = Color.AQUA;
			break;
		case 2:
			c = Color.BLACK;
			break;
		case 3:
			c = Color.BLUE;
			break;
		case 4:
			c = Color.FUCHSIA;
			break;
		case 5:
			c = Color.GRAY;
			break;
		case 6:
			c = Color.GREEN;
			break;
		case 7:
			c = Color.LIME;
			break;
		case 8:
			c = Color.MAROON;
			break;
		case 9:
			c = Color.NAVY;
			break;
		case 10:
			c = Color.OLIVE;
			break;
		case 11:
			c = Color.ORANGE;
			break;
		case 12:
			c = Color.PURPLE;
			break;
		case 13:
			c = Color.RED;
			break;
		case 14:
			c = Color.RED;
			break;
		case 15:
			c = Color.SILVER;
			break;
		case 16:
			c = Color.TEAL;
			break;
		case 17:
			c = Color.YELLOW;
			break;
		}
		return c;
	}
}
