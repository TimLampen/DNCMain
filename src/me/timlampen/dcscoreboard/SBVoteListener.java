package me.timlampen.dcscoreboard;

import me.timlampen.util.Main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VoteListener;

public class SBVoteListener implements VoteListener{
	Main p;
	DropParty dp;
	public SBVoteListener(Main p, DropParty dp){
		this.p = p;
		this.dp = dp;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void voteMade(Vote vote) {
		if(Main.vote!=0){
			Main.vote--;
		}
		else if(Main.vote<=0){
			Main.vote = p.getConfig().getInt("MaxVotes");
			for(Player player : Bukkit.getOnlinePlayers()){
				dp.Runnable(player);
			}
		}
	}
	
}
