package me.timlampen.dcscoreboard;

import me.timlampen.util.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VoteListener;
import com.vexsoftware.votifier.model.VotifierEvent;

public class SBVoteListener implements Listener{
	Main p;
	DropParty dp;
	public SBVoteListener(Main p, DropParty dp){
		this.p = p;
		this.dp = dp;
	}
    public void onVotifierEvent(VotifierEvent event) {
		if(Main.vote!=0){
			Main.vote--;
			Bukkit.broadcastMessage(p.getPrefix() + ChatColor.GRAY + "Only " + ChatColor.RED + Main.vote + ChatColor.GRAY + " more votes until a " + ChatColor.RED + "" + ChatColor.BOLD + "Vote Party" + ChatColor.GRAY + "!");
		}
		else if(Main.vote<=0){
			Main.vote = p.getConfig().getInt("MaxVotes");
			for(Player player : Bukkit.getOnlinePlayers()){
				dp.Runnable(player);
			}
		}
    }
	
}
