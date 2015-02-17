package me.timlampen.util;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Parse {
	Lang l;
	public Parse(Lang l){
		this.l = l;
	}
	public Enchantment parseEnchantment(Player player, String string){
		Enchantment e = null;
		string = ChatColor.stripColor(string);
		string = string.toLowerCase();
		switch(string){
		case "fortune":
			e = Enchantment.LOOT_BONUS_BLOCKS;
			break;
		case "unbreaking":
			e = Enchantment.DURABILITY;
			break;
		case "efficiency":
			e = Enchantment.DIG_SPEED;
			break;
		case "sharpness":
			e = Enchantment.DAMAGE_ALL;
			break;
		case "protection":
			e = Enchantment.PROTECTION_ENVIRONMENTAL;
			break;
		case "fireprotection":
			e = Enchantment.PROTECTION_FIRE;
			break;
		case "looting":
			e = Enchantment.LOOT_BONUS_MOBS;
			break;
		case "featherfalling":
			e = Enchantment.PROTECTION_FALL;
			break;
		case "projectileprotection":
			e = Enchantment.PROTECTION_PROJECTILE;
			break;
		}
		return e;
	}
	 public String getMoney(double amt){
		 String s = "";
		 Double thou = new Double("1000");
		 Double mill = new Double("1000000");
		 Double bill = new Double("1000000000");
		 Double tril = new Double("1000000000000");
		 Double quad = new Double("1000000000000000");
		 Double quin = new Double("1000000000000000000");
		 if(amt>=quin){
			 amt = amt/quin;
			 amt = ((int)amt);
			 s = amt + "qu";
		 }
		 else if(amt>=quad){
			 amt = amt/quad;
			 amt = ((int)amt);
			 s = amt + "q";
		 }
		 else if(amt>=tril){
			 amt = amt/tril;
			 amt = ((int)amt);
			 s = amt + "t";
		 }
		 else if(amt>=bill){
			 amt = amt/bill;
			 amt = ((int)amt);
			 s = amt + "b";
		 }
		 else if(amt>=mill){
			 amt = amt/mill;
			 amt = ((int)amt);
			 s = amt + "m";
		 }
		 else if(amt>=thou){
			 amt = amt/thou;
			 amt = ((int)amt);
			 s = amt + "k";
		 }
		 else{
			 s = amt + "";
		 }
		 return s;
	 }
	 
	
}
