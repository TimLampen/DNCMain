package me.timlampen.extras;

import java.util.HashMap;
import java.util.UUID;

import me.timlampen.currency.Tokens;
import me.timlampen.util.ConfigMaker;
import me.timlampen.util.JSONUtil;
import me.timlampen.util.Main;
import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.PacketPlayOutChat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import fanciful.FancyMessage;

public class NameInfo implements Listener{
	Prestige pre;
	Main p;
	Tokens t;
	private ConfigMaker config;
	
	public NameInfo(Main p, Tokens t, Prestige pre){
		this.p = p;
		this.t = t;
		this.pre = pre;
		config = new ConfigMaker(p, "blocks");
		config.save();
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event){
		Player player = event.getPlayer();
		if(event.getMessage().contains("http:") || event.getMessage().contains("www.") || event.getMessage().contains(".com") || event.getMessage().contains(".net") || event.getMessage().contains(".ca") || event.getMessage().contains(".org")){
			String s = ChatColor.AQUA + "" + ChatColor.BOLD + pre.getNumber(pre.getPrestige(player)) + " " + player.getDisplayName() + ChatColor.GRAY + " » " + ChatColor.AQUA + event.getMessage();
			for(Player plyer : Bukkit.getOnlinePlayers()){
				plyer.sendMessage(s);
			}
			event.setCancelled(true);
		}
		else{
			String nameToolTip = new FancyMessage(ChatColor.AQUA + "" + ChatColor.BOLD + pre.getNumber(pre.getPrestige(player)) + " " + player.getDisplayName() + ChatColor.GRAY + " » ").tooltip(
			ChatColor.WHITE + "----------------------------\n" + 
			ChatColor.GOLD + "Money: " + ChatColor.GREEN + "$" + p.eco.getBalance(player.getName()) + "\n\n" + 
			ChatColor.GOLD + "Rank: " + ChatColor.AQUA + p.perms.getPrimaryGroup(player) + "\n\n" +
			ChatColor.GOLD + "Crystal(s): " + ChatColor.DARK_GRAY + t.getTokens(player) + "\n\n" + 
			ChatColor.GOLD + "Blocks Broken: " + ChatColor.RED + getBlocks(player) + "\n" +
			ChatColor.WHITE + "----------------------------").toJSONString();
			
			String jMsg = JSONUtil.toJSON(ChatColor.AQUA + event.getMessage());
			PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(ChatSerializer.a(nameToolTip).addSibling(ChatSerializer.a(jMsg)));
			
			for(Player plyer : Bukkit.getOnlinePlayers()){
				((CraftPlayer) plyer).getHandle().playerConnection.sendPacket(packetPlayOutChat);
			}
			event.setCancelled(true);
		}
	}
	
	public HashMap<UUID, Integer> blks = new HashMap<UUID, Integer>();
	@EventHandler
	public void onBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		if(p.isInRegion(event.getBlock())){
			if(blks.containsKey(player.getUniqueId())){
				blks.put(player.getUniqueId(), blks.get(player.getUniqueId())+1);
			}
			else if(!blks.containsKey(player.getUniqueId())){
				blks.put(player.getUniqueId(), 1);
			}
		}
	}
	
	public int getBlocks(Player player){
		if(blks.containsKey(player.getUniqueId())){
			return blks.get(player.getUniqueId());
		}
		else{
			return 1;
		}
	}
	
	public boolean loadItems(){
		if(config!=null){
			for(String str : config.getConfigurationSection("").getKeys(false)){
				UUID uuid = UUID.fromString(str);
				int i = Integer.parseInt(config.getString(str));
				blks.put(uuid, i);
			}
			return true;
		}
		return false;
	}
	
	public boolean saveItems(UUID uuid){
		if(config!=null){
			config.set(uuid + "", blks.get(uuid));
			config.save();
			return true;
		}
		return false;
	}
}
