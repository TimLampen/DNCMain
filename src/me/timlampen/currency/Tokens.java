package me.timlampen.currency;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.timlampen.util.ConfigMaker;
import me.timlampen.util.Lang;
import me.timlampen.util.Main;
import me.timlampen.util.Parse;

public class Tokens implements Listener{
	public HashMap<UUID, Integer> tokens = new HashMap<UUID, Integer>();
	Main p;
	Lang l;
	Parse pa;
	private ConfigMaker config;
	public Tokens(Main p, Lang l, Parse pa){
		this.p = p;
		this.l = l;
		this.pa = pa;
		config = new ConfigMaker(p, "tokens");
		config.save();
	        Set<String> set = config.getConfigurationSection("").getKeys(false);
	        for(String credit : set) {
	        	UUID uuid = UUID.fromString(credit);
	            int value = config.getInt(credit);
	            tokens.put(uuid, value);
	    }
	}
	private HashMap<UUID, Integer> blocks = new HashMap<UUID, Integer>();
	@EventHandler
	public void onBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		if(blocks.containsKey(player.getUniqueId())){
			if(blocks.get(player.getUniqueId())>=10000){
				Random ran = new Random();
				int r = ran.nextInt(3)+1;
				addTokens(player, r);
				player.sendMessage(p.getPrefix() + ChatColor.DARK_GRAY + "You just recieved " + r +" crystal(s) for mining 1000 blocks!");
				blocks.remove(player.getUniqueId());
			}
			else{
				blocks.put(player.getUniqueId(), blocks.get(player.getUniqueId())+1);
			}
		}
		else{
			blocks.put(player.getUniqueId(), 1);
		}
	}
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		if(event.getAction()==Action.RIGHT_CLICK_BLOCK && event.getClickedBlock()!=null && (event.getClickedBlock().getType()==Material.SIGN_POST || event.getClickedBlock().getType()==Material.WALL_SIGN) && event.getItem()!=null){
			Sign sign = (Sign)event.getClickedBlock().getState();
			Player player = event.getPlayer();
			ItemStack item = event.getItem();
			if(sign.getLine(0).contains(ChatColor.RED + "[" + ChatColor.DARK_GRAY + "DEnch" + ChatColor.RED + "]")){
				Integer line2 = parseInt(player, sign.getLine(1)); 
				Enchantment line3 = pa.parseEnchantment(player, sign.getLine(2));
				Integer line4 = parseInt(player, sign.getLine(3));
				if(tokens.containsKey(player.getUniqueId()) && tokens.get(player.getUniqueId())>=line4){
					removeTokens(player, line4);
					player.setItemInHand(applySign(player, item, item.getItemMeta(), line2, line3));
					player.updateInventory();
					l.doMessage(player, l.s1.SIGN_USE, item.getType().toString(), item.getEnchantmentLevel(line3) + "");
				}
				else{
					l.doMessage(player, l.r1.NO_TOKEN, line4 + "");
				}
			}
		}
	}
	
	private ItemStack applySign(Player player, ItemStack is, ItemMeta im, int add, Enchantment ench){
		if(im.getEnchantLevel(ench)>=0){
			is.addUnsafeEnchantment(ench, im.getEnchantLevel(ench)+add);
		}
		else{
			is.addUnsafeEnchantment(ench, add);
		}
		return is;
	}
	
	@EventHandler
	public void onChange(SignChangeEvent event){
		Player player = event.getPlayer();
		if(event.getLine(0)!=null && event.getLine(0).equalsIgnoreCase(ChatColor.stripColor("[DEnch]"))){
			if(player.hasPermission("dc.token.sign")){
				if(event.getLine(1)!=null && event.getLine(2)!=null && event.getLine(3)!=null){
					String line1 = ChatColor.RED + "[" + ChatColor.DARK_GRAY + "DEnch" + ChatColor.RED + "]";
					String line2 = ChatColor.RED + "" + ChatColor.BOLD + event.getLine(1);
					String line3 = ChatColor.DARK_GRAY + "" + ChatColor.BOLD + event.getLine(2);
					String line4 = ChatColor.RED +  "" + ChatColor.BOLD + event.getLine(3);
					event.setLine(0, line1);
					event.setLine(1, line2);
					event.setLine(2, line3);
					event.setLine(3, line4);
					l.doMessage(player, l.s1.SIGN_MAKE, "", "");
				}
				else{
				event.getBlock().breakNaturally();
				}
			}
			else{
				l.doMessage(player, l.r1.NO_PERM, "");
				event.getBlock().breakNaturally();
			}
		}
	}
	public void addTokens(Player player, int amount){
		if(tokens.containsKey(player.getUniqueId())){
			tokens.put(player.getUniqueId(), tokens.get(player.getUniqueId())+amount);
		}
		else{
			tokens.put(player.getUniqueId(), amount);
		}
		player.sendMessage(p.getPrefix() + ChatColor.GREEN + amount + " crystal(s) have been added to your account!");
	}
	public void removeTokens(Player player, int amount){
		if(tokens.containsKey(player.getUniqueId()) && tokens.get(player.getUniqueId())>amount){
			tokens.put(player.getUniqueId(), tokens.get(player.getUniqueId())-amount);
		}
		else{
			tokens.put(player.getUniqueId(), 0);
		}
		player.sendMessage(p.getPrefix() + ChatColor.RED + amount + " crystal(s) have been removed from your account!");
	}
	public void setTokens(Player player, int amount){
		tokens.put(player.getUniqueId(), amount);
		player.sendMessage(p.getPrefix() + ChatColor.YELLOW + "Your balance has been set to " + amount + " crystal(s)!");
	}
	public int getTokens(Player player){
		if(tokens.containsKey(player.getUniqueId())){
			return tokens.get(player.getUniqueId());
		}
		else{
			return 0;
		}
	}
	
	public boolean hasTokens(Player player, int amount){
		if(tokens.containsKey(player.getUniqueId())){
			if(tokens.get(player.getUniqueId())>=amount){
				return true;
			}
		}
		return false;
	}
	@SuppressWarnings("static-access")
	public int parseInt(Player player, String string){
		int i = 0;
		string = ChatColor.stripColor(string);
		string.replace("+", "");
		string.replace("$", "");
		try{
			Integer.parseInt(string);
		}catch(NumberFormatException e){
			l.doMessage(player, l.r1.NULL_NUMBER, string);
			return 0;
		}
		i = Integer.parseInt(string);
		return i;
	}
	public void saveTokens(){
		for(UUID s: tokens.keySet()){
			config.set(s.toString(), tokens.get(s));
		}
		config.save();
	}
}
