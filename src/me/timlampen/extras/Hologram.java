package me.timlampen.extras;

import me.timlampen.util.Main;
import me.timlampen.util.Title;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class Hologram implements Listener{
	Main p;
	public Hologram(Main p){
		this.p = p;
	}
	
	@EventHandler
	public void onBlock(BlockBreakEvent event){
		Player player = event.getPlayer();
		if(player.getInventory().firstEmpty()==-1){
			Title title = new Title("", ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Messages.InvFull", ChatColor.YELLOW + "" + ChatColor.BOLD + "INVENTORY FULL")));
			title.setStayTime(1);
			title.setFadeInTime(0);
			title.setFadeOutTime(0);
			title.setTimingsToSeconds();
			title.send(player);
		}
	}
	
}
