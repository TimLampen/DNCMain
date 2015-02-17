package me.timlampen.extras;
import java.util.ArrayList;
import java.util.Random;

import me.timlampen.util.Main;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ColorPick implements Listener{
	Main p;
	public ColorPick(Main p){
		this.p = p;
	}
	
	private ArrayList<String> lore = new ArrayList<>();
	@EventHandler
	public void onBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		Material type = player.getItemInHand().getType();
		ItemStack item = player.getItemInHand();
		ItemMeta im = player.getItemInHand().getItemMeta();
		if(player.getItemInHand()!=null && (type==Material.WOOD_PICKAXE || type==Material.STONE_PICKAXE || type==Material.GOLD_PICKAXE || type==Material.DIAMOND_PICKAXE || type==Material.IRON_PICKAXE)){
			if(p.isInRegion(event.getBlock())){
				im.setDisplayName(ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("ColorPick.Prefix".replace("%random%", getCustomColor() + ""), getCustomColor()+ "" + ChatColor.BOLD + "DNC" + getCustomColor() + "" + ChatColor.BOLD + "Pick " + ChatColor.GRAY + "[" + ChatColor.RED)) + getLevel(item, player) + ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("ColorPick.Suffix".replace("%random%", getCustomColor() + ""), ChatColor.GRAY + " Block(s) Broken]")));
				item.setItemMeta(im);
			}
		}
	}
	
	public int getLevel(ItemStack item, Player player){
		int l = 0;
		if(item!=null && item.getItemMeta().hasDisplayName()){
			String line = item.getItemMeta().getDisplayName();
			line = ChatColor.stripColor(line);
			String prefix = p.getConfig().getString("ColorPick.Prefix", "DNCPick [");
			String suffix = p.getConfig().getString("ColorPick.Suffix", " Block(s) Broken]");
			prefix = ChatColor.stripColor(prefix);
			suffix = ChatColor.stripColor(suffix);
			String level = line.replace(prefix, "").replace(suffix, "");
			level = ChatColor.stripColor(level);
			try{
			l = Integer.parseInt(level)+1;
			}catch(NumberFormatException e){
				player.sendMessage(p.getPrefix() + ChatColor.RED + "Error: Unable to parse level from item, reconfiguring");
				e.printStackTrace();
			}
				return l;
		}
		return l;
	}
	
	private static ChatColor getCustomColor(){
		ChatColor c = null;
		Random ran = new Random();
		int num = ran.nextInt(15);
		switch(num){
		case 0:
			c = ChatColor.AQUA;
			break;
		case 1:
			c = ChatColor.BLACK;
			break;
		case 2:
			c = ChatColor.BLUE;
			break;
		case 3:
			c = ChatColor.DARK_AQUA;
			break;
		case 4:
			c = ChatColor.DARK_BLUE;
			break;
		case 5:
			c = ChatColor.DARK_GRAY;
			break;
		case 6:
			c = ChatColor.DARK_GREEN;
			break;
		case 7:
			c = ChatColor.DARK_PURPLE;
			break;
		case 8:
			c = ChatColor.DARK_RED;
			break;
		case 9:
			c = ChatColor.GOLD;
			break;
		case 10:
			c = ChatColor.GRAY;
			break;
		case 11:
			c = ChatColor.GREEN;
			break;
		case 12:
			c = ChatColor.LIGHT_PURPLE;
			break;
		case 13:
			c = ChatColor.RED;
			break;
		case 14:
			c = ChatColor.WHITE;
			break;
		case 15:
			c = ChatColor.YELLOW;
			break;
		}
		return c;
		
	}
}
