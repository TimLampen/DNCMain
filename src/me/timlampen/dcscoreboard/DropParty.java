package me.timlampen.dcscoreboard;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import me.timlampen.explosions.GrenadeListener;
import me.timlampen.explosions.RocketListener;
import me.timlampen.util.Main;
import me.timlampen.util.Title;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DropParty {
	public ArrayList<String> cmd = new ArrayList<String>();
	Main p;
	GrenadeListener gl;
	RocketListener rl;
	public int items = 0;
	public DropParty(Main p, GrenadeListener gl, RocketListener rl){
		this.p = p;
		this.gl = gl;
		this.rl = rl;
	}
	public void getItem(Player player){
		Random r = new Random();
		int ran = r.nextInt(p.getConfig().getInt("DropParty.AmountOfItems"));
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd.get(ran).replace("%player%", player.getName()));
	}
	public HashMap<UUID, Integer> item = new HashMap<>();
	public ArrayList<UUID> toggle = new ArrayList<>();
	public void Runnable(final Player player){
		Bukkit.getScheduler().scheduleSyncDelayedTask(p, new Runnable(){

			@Override
			public void run() {
				if(toggle.contains(player.getUniqueId())){
					player.sendMessage(p.getPrefix() + ChatColor.RED + "You did not recieve any items because your toggle is off");
					item.remove(player.getUniqueId());
					items = 0;
					Title title = new Title(ChatColor.RED + "Drop Party Ended", ChatColor.GREEN + "Until Next Time!");
					title.setFadeInTime(2);
					title.setFadeOutTime(1);
					title.setTimingsToSeconds();
					title.send(player);
				}
				else if(item.containsKey(player.getUniqueId()) && item.get(player.getUniqueId())<p.getConfig().getInt("DropParty.AmountOfItems")){
						getItem(player);
						item.put(player.getUniqueId(), item.get(player.getUniqueId())+1);
						Runnable(player);
				}
				else if(!item.containsKey(player.getUniqueId())){
						item.put(player.getUniqueId(), 1);
						getItem(player);
						Runnable(player);
				}
				else{
					item.remove(player.getUniqueId());
					items = 0;
					Title title = new Title(ChatColor.RED + "Drop Party Ended", ChatColor.GREEN + "Until Next Time!");
					title.setFadeInTime(2);
					title.setFadeOutTime(1);
					title.setTimingsToSeconds();
					title.send(player);
				}
			}}, 20*p.getConfig().getInt("DropParty.DelayBetweenItems"));
	}
	
	public void doTitle(final Player player){
		Title title = new Title(ChatColor.BLUE + "Drop Party Commenced", ChatColor.GREEN + "Starting Soon...");
		title.setFadeInTime(2);
		title.setFadeOutTime(1);
		title.setTimingsToSeconds();
		title.send(player);
			Bukkit.getScheduler().runTaskLater(p, new Runnable(){

				@Override
				public void run() {
					Runnable(player);
				}}, 100);
	}
}
