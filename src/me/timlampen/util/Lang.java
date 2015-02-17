package me.timlampen.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.timlampen.util.Lang.Error;

public class Lang {
	Main p;
	public Error r1;
	public Success s1;
	public Mutual m1;
	public Lang(Main p){
		this.p = p;
	}
	
	public enum Error{
		NO_PERM, INCORRECT_SYNTAX, NULL_PLAYER, NULL_NUMBER, NO_MONEY, COOLDOWN, NO_TOKEN, INV_FULL
	}
	public enum Success{
		DP_START, DP_RESTART, DP_SET, MULT_SET, MULTI_GET_SET, GRENADE_GIVE, GRENADE_GET, ROCKET_GIVE, ROCKET_GET, GOODIES_OPEN, KITTY_UNEARTH, EGG_GET, TOGGLE_ON, TOGGLE_OFF, BAG_GIVE, BAG_GET, SIGN_MAKE, SIGN_USE, TOKEN_GIVE, TOKEN_GET, BANK_WITHDRAW
	}
	public enum Mutual{
		GET_TOKENS
	}
	public void doMessage(Player player, Error r, String args){
		String s = null;
		switch(r){
		case INCORRECT_SYNTAX:
			s = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Messages.SyntaxError", "&4Error: [Incorrect Syntax] Options: [/dc]"));
			//Does not need args
			break;
		case NO_PERM:
			s = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Messages.PermissionError", "&4Error: [No Permission] Options: [/dc] [Ask for permission]"));
			//Does not need args
			break;
		case NULL_NUMBER:
			s = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Messages.NullNumber".replace("%number%", args), "&4Error: [Null Number] Options: [/dc] [Change " + args + " to a number]"));
			//Does need args
			break;
		case NULL_PLAYER:
			s = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Messages.NullPlayer".replace("%name%", args), "&4Error: [Null Player] Options: [/dc] [Change " + args + " to a player]"));
			//Does need args
			break;
		case NO_MONEY:
			s = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Messages.NoMoney".replace("%name%", args), "&4Error: [Not Enough Money] Options: [/dc] [You need $" + args + "]"));
			//Does need args
			break;
		case NO_TOKEN:
			s = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Messages.NoTokens".replace("%name%", args), "&4Error: [Not Enough Money] Options: [/dc] [You need " + args + " crystal(s)]"));
			//Does need args
			break;
		case COOLDOWN:
			s = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Messages.Cooldown".replace("%number%", args), "&4Error: [On Cooldown For " + args + "s] Options: [/getsomepatience]"));
			break;
		case INV_FULL:
			s = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Messages.InvFull".replace("%number%", args), "&4Error: Your inventory is currently full!"));
			break;
		default:
			break;
		}
		player.sendMessage(p.getPrefix() + s);
		}
	public void doMessage(Player player, Success r, String name, String num){
		String s = null;
		switch(r){
		case DP_RESTART:
			s = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Messages.DPRestart", "&7You have restarted the drop party!"));
			break;
		case DP_START:
			s = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Messages.DPStart", "&7You have started the drop party!"));
			break;
		case DP_SET:
			s = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Messages.DPSet".replace("%number%", num), "&7You have set the drop party counter to " + num));
			break;
		case MULTI_GET_SET:
			s = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Messages.MultiGetSet".replace("%player%", name).replace("%number%", num), "&7Your multipler has been set to " + num + " by " + name + "!"));
			break;
		case MULT_SET:
			s = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Messages.MultiSet".replace("%player%", name).replace("%number%", num), "&7You have set " + name + "'s multiplier to " + num + "!"));
			break;
		case GRENADE_GET:
			s = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Messages.GrenadeGet".replace("%player%", name).replace("%number%", num), "&7You have been given " + num + " grenade(s) by " + name + "!"));
			break;
		case GRENADE_GIVE:
			s = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Messages.GrenadeGive".replace("%player%", name).replace("%number%", num), "&7You have given " + name + " " + num + " grenade(s)!"));
			break;
		case ROCKET_GET:
			s = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Messages.RocketGet".replace("%player%", name).replace("%number%", num), "&7You have been given " + num + " rocket(s) by " + name + "!"));
			break;
		case ROCKET_GIVE:
			s = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Messages.RocketGive".replace("%player%", name).replace("%number%", num), "&7You have given " + name + " " + num + " rocket(s)!"));
			break;
		case GOODIES_OPEN:
			s = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Messages.GoodiesOpen", "&7You have opened a goodie bag!"));
			break;
		case KITTY_UNEARTH:
			s = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Messages.KittyUnEarth", "&7You have unearthed an explosive kitten!"));			
			break;
		case EGG_GET:
			s = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Messages.EggGet", "&7You just got a special egg!"));			
			break;
		case TOGGLE_ON:
			s = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Messages.ToggleOn", "&7You toggled the event on!"));			
			break;
		case TOGGLE_OFF:
			s = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Messages.ToggleOn", "&7You toggled the event off!"));			
			break;
		case BAG_GET:
			s = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Messages.BagGet".replace("%player%", name).replace("%value%", num), "&7You have just recived " + num + " loot bag(s) from " + name + "!"));			
			break;
		case BAG_GIVE:
			s = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Messages.BagGive".replace("%player%", name).replace("%value%", num), "&7You have just given " + num + " loot bag(s) to " + name + "!"));				
			break;
		case SIGN_MAKE:
			s = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Messages.SignMake", "&7You just made a DNC Enchantment sign!"));				
			break;
			//add to config
		case SIGN_USE:
			s = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Messages.SignUse", "&7You have applied enchantments to your tool!"));				
			break;
			//add to config
		case TOKEN_GET:
			s = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Messages.TokenGet".replace("%player%", name).replace("%number%", num), "&7You have been given " + num + " crystal(s) by " + name + "!"));
			break;
			//add to config
		case TOKEN_GIVE:
			s = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Messages.TokenGive".replace("%player%", name).replace("%number%", num), "&7You have given " + name + " " + num + " crystal(s)!"));
			break;
			//add to config
		case BANK_WITHDRAW:
			s = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Messages.BankWithdraw".replace("%number%", num), "&7You just wrote a check for " + num + "! The money has been withdrawn"));
			break;
			//add to config
		default:
			break;
		
		}
		player.sendMessage(p.getPrefix() + s);
	}
	public void doMessage(Player player, Mutual m, String name, String num){
		String s = null;
		switch(m){
		case GET_TOKENS:
			s = ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("Messages.GetTokens".replace("%value%", num), "&cYou have: &7" + num + " &ccrystal(s)"));				
			break;
		}
		player.sendMessage(p.getPrefix() + s);
	}
}
