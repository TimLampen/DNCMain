package me.timlampen.dcscoreboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import me.timlampen.commands.CommandHandler;
import me.timlampen.extras.SellAll;
import me.timlampen.util.Main;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class BlockListener implements Listener{
  private Random rnd = new Random();
	Main p;
	SellAll sa;
	public BlockListener(Main p){
		this.p = p;
		this.sa = p.sa;
	}
	HashMap<UUID, Double> multi = new HashMap<UUID, Double>();
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		if(!multi.containsKey(player.getUniqueId())){
			multi.put(player.getUniqueId(), 1.0);
		}
	}
	
  @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=false)
  public void onBlockBreak(BlockBreakEvent event)
  {
    Player player = event.getPlayer();
    Block block = event.getBlock();
    if ((isPickaxe(player.getItemInHand()).booleanValue()) && (isValidBlock(block).booleanValue()) && (!isDisabledWorld(block.getWorld().getName())))
    {
      if (event.isCancelled()) {
        return;
      }
      
      
      String drop = p.getConfig().getString("blocks." + block.getType().name() + ".drop", block.getType().name());
      
      event.setCancelled(true);
      ((ExperienceOrb)player.getWorld().spawn(block.getLocation(), ExperienceOrb.class)).setExperience(event.getExpToDrop());
      player.getWorld().getBlockAt(block.getLocation()).setType(Material.AIR);
      
      ItemStack is = new ItemStack(Material.getMaterial(drop));
      String s = getFortuneMultipler(player.getItemInHand());
      String[] ssplit = s.split("-");
      int min = Integer.parseInt(ssplit[0]);
      int max = Integer.parseInt(ssplit[1]);
      if(min>max || s.equals("1-1")){
    	  is.setAmount(1);
      }
      else{
    	  int ran = rnd.nextInt(max-min)+min;
    	  is.setAmount(ran);
      }
      if(p.asell.contains(player.getUniqueId())){
    	  if(p.asellitems.contains(player.getUniqueId(), is.getType())){
    		  p.asellitems.put(player.getUniqueId(), is.getType(), p.asellitems.get(player.getUniqueId(), is.getType())+is.getAmount());
    	  }
    	  else{
    		  p.asellitems.put(player.getUniqueId(), is.getType(), is.getAmount());
    	  }
    	  if(p.aselltotal.containsKey(player.getUniqueId())){
    		  p.aselltotal.put(player.getUniqueId(), p.aselltotal.get(player.getUniqueId()) + sa.getItemPrice(p.perms.getPrimaryGroup(player).toString(), is.getType())*is.getAmount());
    	  }
    	  else{
    		  p.aselltotal.put(player.getUniqueId(), sa.getItemPrice(p.perms.getPrimaryGroup(player).toString(), is.getType())*is.getAmount());
    	  }
			p.eco.depositPlayer(player, sa.getItemPrice(p.perms.getPrimaryGroup(player).toString(), is.getType())*is.getAmount());
      }
      else{
    	  player.getInventory().addItem(new ItemStack[] { is });
    	  player.updateInventory();
      }

      ItemStack tool = player.getItemInHand();
      if (tool.containsEnchantment(Enchantment.DURABILITY))
      {
        int unbreaklevel = getUnbreakingLevel(tool);
        int percent = 100 / (unbreaklevel + 1);
        int rndpercent = this.rnd.nextInt(99) + 1;
        if (rndpercent <= percent) {
          tool.setDurability((short)(tool.getDurability() + 1));
        }
      }
      else
      {
        tool.setDurability((short)(tool.getDurability() + 1));
      }
      if (tool.getDurability() == tool.getType().getMaxDurability()) {
        player.setItemInHand(new ItemStack(Material.AIR));
      } else {
        player.setItemInHand(tool);
      }
    }
  }
  
  private Boolean isValidBlock(Block block)
  {
    if (p.getConfig().get("blocks." + block.getType().name()) != null) {
    	return Boolean.valueOf(true);
    }
    return Boolean.valueOf(false);
  }
  
  private String getFortuneMultipler(ItemStack tool)
  {
    if (isPickaxe(tool).booleanValue())
    {
      Map<Enchantment, Integer> enchants = tool.getEnchantments();
      if (enchants.containsKey(Enchantment.LOOT_BONUS_BLOCKS))
      {
        int level = ((Integer)enchants.get(Enchantment.LOOT_BONUS_BLOCKS)).intValue();
        if (p.getConfig().get("fortuneLevels.lvl" + level) == null) {
          return "1-1";
        }
        return p.getConfig().getString("fortuneLevels.lvl" + level, "1-1");
      }
    }
    return "1-1";
  }
  
  private int getUnbreakingLevel(ItemStack tool)
  {
    Map<Enchantment, Integer> enchants = tool.getEnchantments();
    if (enchants.containsKey(Enchantment.DURABILITY)) {
      return ((Integer)enchants.get(Enchantment.DURABILITY)).intValue();
    }
    return 0;
  }
  
  private Boolean isPickaxe(ItemStack tool)
  {
    if (tool.getType().equals(Material.WOOD_PICKAXE)) {
      return Boolean.valueOf(true);
    }
    if (tool.getType().equals(Material.STONE_PICKAXE)) {
      return Boolean.valueOf(true);
    }
    if (tool.getType().equals(Material.IRON_PICKAXE)) {
      return Boolean.valueOf(true);
    }
    if (tool.getType().equals(Material.GOLD_PICKAXE)) {
      return Boolean.valueOf(true);
    }
    if (tool.getType().equals(Material.DIAMOND_PICKAXE)) {
      return Boolean.valueOf(true);
    }
    return Boolean.valueOf(false);
  }
  
  public boolean isDisabledWorld(String name)
  {
    List<String> worlds = p.getConfig().getStringList("settings.disabled-worlds");
    if (worlds.size() == 0) {
      return false;
    }
    for (String world : worlds) {
      if (world.equalsIgnoreCase(name)) {
        return true;
      }
    }
    return false;
  }
  
	public double getMultiplier(Player player){
		if(multi.containsKey(player.getUniqueId())){
			return multi.get(player.getUniqueId());
		}
		else{
			return 1;
		}
	}
	public void setMultiplier(Player player, double value){
		if(multi.containsKey(player.getUniqueId())){
			multi.remove(player.getUniqueId());
			multi.put(player.getUniqueId(), value);
		}
		else{
			multi.put(player.getUniqueId(), value);
		}
	}
  
  
}
