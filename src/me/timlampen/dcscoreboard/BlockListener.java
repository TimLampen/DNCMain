package me.timlampen.dcscoreboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

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

public class BlockListener implements Listener{
  private Random rnd = new Random(System.currentTimeMillis());
	Main p;
	public BlockListener(Main p){
		this.p = p;
	}
	HashMap<UUID, Integer> multi = new HashMap<UUID, Integer>();
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		if(!multi.containsKey(player.getUniqueId())){
			multi.put(player.getUniqueId(), 1);
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
      int multi = getFortuneMultipler(player.getItemInHand());
      
      int min = p.getConfig().getInt("blocks." + block.getType().name() + ".rangeMin", 1) * multi;
      int max = p.getConfig().getInt("blocks." + block.getType().name() + ".rangeMax", 1) * multi;
      
      String drop = p.getConfig().getString("blocks." + block.getType().name() + ".drop", block.getType().name());
      
      int amount = min;
      if (min < max) {
        amount += this.rnd.nextInt(max - min);
      }
      event.setCancelled(true);
      ((ExperienceOrb)player.getWorld().spawn(block.getLocation(), ExperienceOrb.class)).setExperience(event.getExpToDrop());
      player.getWorld().getBlockAt(block.getLocation()).setType(Material.AIR);
      
      ItemStack is = new ItemStack(Material.getMaterial(drop));
      is.setAmount(amount*getMultiplier(player));
      player.getInventory().addItem(new ItemStack[] { is });
      
      player.updateInventory();
      

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
  
  private int getFortuneMultipler(ItemStack tool)
  {
    if (isPickaxe(tool).booleanValue())
    {
      Map<Enchantment, Integer> enchants = tool.getEnchantments();
      if (enchants.containsKey(Enchantment.LOOT_BONUS_BLOCKS))
      {
        int level = ((Integer)enchants.get(Enchantment.LOOT_BONUS_BLOCKS)).intValue();
        if (p.getConfig().get("fortuneLevels.lvl" + level) == null) {
          return 1;
        }
        return p.getConfig().getInt("fortuneLevels.lvl" + level, 1);
      }
    }
    return 1;
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
  
	public int getMultiplier(Player player){
		if(multi.containsKey(player.getUniqueId())){
			return multi.get(player.getUniqueId());
		}
		else{
			return 1;
		}
	}
	public void setMultiplier(Player player, Integer value){
		if(multi.containsKey(player.getUniqueId())){
			multi.remove(player.getUniqueId());
			multi.put(player.getUniqueId(), value);
		}
		else{
		multi.put(player.getUniqueId(), value);
		}
	}
  
  
}
