package me.timlampen.currency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import me.timlampen.util.Explosions;
import me.timlampen.util.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class UpgradeInv implements Listener{
	Main p;
	Tokens t;
	Explosions e;
	public UpgradeInv(Main p, Tokens t, Explosions e){
		this.p = p;
		this.t = t;
		this.e = e;
	}
	//Main Iventory
	public void openMainInv(Player player){
		Inventory inv = Bukkit.createInventory(player, 9, "Crystal Upgrades");
		inv.setItem(0, getCustomItem(player, "upgrade"));
		inv.setItem(1, getCustomItem(player, "repair"));
		inv.setItem(4, getCustomItem(player, "crystals"));
		inv.setItem(8, getCustomItem(player, "back"));
		player.openInventory(inv);
	}
	
	//Upgrade Inventory
	public void openUpgradeInv(Player player){
		Inventory inv = Bukkit.createInventory(player, 27, "Upgrade Inventory");
		inv.setItem(1, new ItemStack(Material.GLASS));
		inv.setItem(3, new ItemStack(Material.GLASS));
		inv.setItem(5, new ItemStack(Material.GLASS));
		inv.setItem(7, new ItemStack(Material.GLASS));
		inv.setItem(9, getCustomItem(player, "crystals"));
		inv.setItem(13, getCustomItem(player, "pickholder"));
		inv.setItem(17, getCustomItem(player, "back"));
		inv.setItem(19, new ItemStack(Material.GLASS));
		inv.setItem(21, new ItemStack(Material.GLASS));
		inv.setItem(23, new ItemStack(Material.GLASS));
		inv.setItem(25, new ItemStack(Material.GLASS));
		player.openInventory(inv);
	}
	
	
	//When the main menu gets clicked
	@EventHandler
	public void onMainClick(InventoryClickEvent event){
		if(event.getWhoClicked() instanceof Player && event.getInventory()!=null && event.getInventory().getName()!=null && event.getInventory().getName().equalsIgnoreCase("crystal upgrades")){
			Player player = (Player)event.getWhoClicked();
			ItemStack is = event.getCurrentItem();
			event.setCancelled(true);
			if(is.getType()==Material.COMPASS && is.getItemMeta().hasLore()){
				player.closeInventory();
			}
			else if(event.getCurrentItem().getType()==Material.ENCHANTMENT_TABLE && is.getItemMeta().hasLore()){
				openUpgradeInv(player);
			}
			else if(event.getCurrentItem().getType()==Material.ANVIL && is.getItemMeta().hasLore()){
				if(player.getItemInHand()!=null){
					ItemStack hand = player.getItemInHand();
					if(t.hasTokens(player, 8)){
						t.removeTokens(player, 8);
						hand.setDurability((short) (hand.getDurability()-hand.getDurability()));
					}
					else{
						player.sendMessage(p.getPrefix() + ChatColor.RED + "Unable to purchase upgrade, not enough crystals!");
						player.closeInventory();
					}
				}
			}
		}
	}
	
	
	
	
	
	//When the upgrade inventory gets clicked
	private HashMap<String, ItemStack> cursor = new HashMap<String, ItemStack>();
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onUpgradeClick(InventoryClickEvent event){
		if(event.getWhoClicked() instanceof Player && event.getView().getTopInventory()!=null && event.getView().getTopInventory().getName()!=null && event.getView().getTopInventory()!=null && event.getView().getTopInventory().getName().equalsIgnoreCase("upgrade inventory")){
			Inventory inv = event.getView().getTopInventory();
			Inventory bot = event.getView().getBottomInventory();
			Player player = (Player)event.getWhoClicked();
			if(event.getCurrentItem()!=null && event.getClickedInventory().equals(inv)){
				event.setCancelled(true);
				//back button
				ItemStack is = event.getCurrentItem();
				if(is.getType()==Material.COMPASS && is.getItemMeta().hasLore()){
					player.closeInventory();
				}
				//player puts item in the slot
				else if(is.getType()==Material.EMERALD && is.getItemMeta().hasLore() && event.getCursor()!=null){
					is = event.getCursor();
					if(!is.getType().toString().contains("PICKAXE")){
						player.sendMessage(p.getPrefix() + ChatColor.RED + "The item has to be a pickaxe!");
					}
					else{
						inv.setItem(13, is);
						event.setCursor(new ItemStack(Material.AIR));
						cursor.put(player.getName(), is);
						inv.setItem(1, getUpgrade(1));
						inv.setItem(3, getUpgrade(2));
						inv.setItem(5, getUpgrade(3));
						inv.setItem(7, getUpgrade(4));
						inv.setItem(19, getUpgrade(5));
						player.updateInventory();
					}
				}
				/*
				*	Explosives Item
				*/
				else if(is.getType()==Material.FIREBALL && is.getItemMeta().hasLore()){
					is = inv.getItem(13);
					ItemMeta im = is.getItemMeta();
					ArrayList<String> lore = new ArrayList<String>();
					if(t.hasTokens(player, 8)){
						t.removeTokens(player, 8);
						if(!is.getItemMeta().hasLore()){
							lore.add(ChatColor.RED + "Explosions I");
						}
						else{
							boolean hasench = false;
							for(String str : im.getLore()){
								if(str.equalsIgnoreCase(ChatColor.RED + "Explosions III")){
									t.addTokens(player, 8);
									player.sendMessage(p.getPrefix() + ChatColor.RED + "Unable to apply enchantment, enchantment level already at maximum level!");
									lore.add(str);
									hasench = true;
								}
								else if(str.contains(ChatColor.RED + "Explosions")){
									str = str + "I";
									lore.add(str);
									hasench = true;
								}
								else{
									lore.add(str);
								}
							}
							if(hasench==false){
								lore.add(ChatColor.RED + "Explosions I");
							}
						}
						im.setLore(lore);
						is.setItemMeta(im);
						lore.clear();
						cursor.put(player.getName(), is);
					}
					else{
						player.sendMessage(p.getPrefix() + ChatColor.RED + "Unable to purchase upgrade, not enough crystals!");
						player.closeInventory();
					}
				}
				/*
				*	NightVision Item
				*/
				else if(is.getType()==Material.EYE_OF_ENDER && is.getItemMeta().hasLore()){
					is = inv.getItem(13);
					ItemMeta im = is.getItemMeta();
					ArrayList<String> lore = new ArrayList<String>();
					if(t.hasTokens(player, 6)){
						t.removeTokens(player, 6);
						if(!is.getItemMeta().hasLore()){
							lore.add(ChatColor.DARK_GREEN + "Night Vision I");
						}
						else{
							boolean hasench = false;
							for(String str : im.getLore()){
								if(str.equalsIgnoreCase(ChatColor.DARK_GREEN + "Night Vision I")){
									t.addTokens(player, 6);
									player.sendMessage(p.getPrefix() + ChatColor.RED + "Unable to apply enchantment, enchantment level already at maximum level!");
									lore.add(str);
									hasench = true;
								}
								else{
									lore.add(str);
								}
							}
							if(hasench == false){
								lore.add(ChatColor.DARK_GREEN + "Night Vision I");
							}
						}
						im.setLore(lore);
						is.setItemMeta(im);
						lore.clear();
						cursor.put(player.getName(), is);
					}
					else{
						player.sendMessage(p.getPrefix() + ChatColor.RED + "Unable to purchase upgrade, not enough crystals!");
						player.closeInventory();
					}
				}
				/*
				*	Speed Item
				*/
				else if(is.getType()==Material.SUGAR && is.getItemMeta().hasLore()){
					is = inv.getItem(13);
					ItemMeta im = is.getItemMeta();
					ArrayList<String> lore = new ArrayList<String>();
					if(t.hasTokens(player, 7)){
						t.removeTokens(player, 7);
						if(!is.getItemMeta().hasLore()){
							lore.add(ChatColor.WHITE + "Speed I");
						}
						else{
							boolean hasench = false;
							for(String str : im.getLore()){
								if(str.equalsIgnoreCase(ChatColor.WHITE + "Speed IV")){
									t.addTokens(player, 7);
									player.sendMessage(p.getPrefix() + ChatColor.RED + "Unable to apply enchantment, enchantment level already at maximum level!");
									lore.add(str);
									hasench = true;
								}
								else if(str.equalsIgnoreCase(ChatColor.WHITE + "Speed III")){
									str = ChatColor.WHITE + "Speed IV";
									lore.add(str);
									hasench = true;
								}
								else if(str.contains(ChatColor.WHITE + "Speed")){
									str = str + "I";
									lore.add(str);
									hasench = true;
								}
								else{
									lore.add(str);
								}
							}
							if(hasench==false){
								lore.add(ChatColor.WHITE + "Speed I");
							}
						}
						im.setLore(lore);
						is.setItemMeta(im);
						lore.clear();
						cursor.put(player.getName(), is);
					}
					else{
						player.sendMessage(p.getPrefix() + ChatColor.RED + "Unable to purchase upgrade, not enough crystals!");
						player.closeInventory();
					}
				}
				/*
				*	Efficiency Item
				*/
				else if(is.getType()==Material.REDSTONE && is.getItemMeta().hasLore()){
					is = inv.getItem(13);
					if(t.hasTokens(player, 5)){
						t.removeTokens(player, 5);
						if(is.containsEnchantment(Enchantment.DIG_SPEED)){
							is.addUnsafeEnchantment(Enchantment.DIG_SPEED, is.getEnchantmentLevel(Enchantment.DIG_SPEED)+1);
						}
						else{
							is.addEnchantment(Enchantment.DIG_SPEED, 1);
						}
						cursor.put(player.getName(), is);
					}
					else{
						player.sendMessage(p.getPrefix() + ChatColor.RED + "Unable to purchase upgrade, not enough crystals!");
						player.closeInventory();
					}
				}
				/*
				*	Silk Touch Item
				*/
				else if(is.getType()==Material.QUARTZ && is.getItemMeta().hasLore()){
					is = inv.getItem(13);
					if(t.hasTokens(player, 8)){
						t.removeTokens(player, 8);
						if(is.containsEnchantment(Enchantment.SILK_TOUCH)){
							t.addTokens(player, 8);
							player.sendMessage(p.getPrefix() + ChatColor.RED + "Unable to apply enchantment, enchantment level already at maximum level!");
						}
						else{
							is.addEnchantment(Enchantment.SILK_TOUCH, 1);
						}
						cursor.put(player.getName(), is);
					}
					else{
						player.sendMessage(p.getPrefix() + ChatColor.RED + "Unable to purchase upgrade, not enough crystals!");
						player.closeInventory();
					}
				}
			}
			else if(event.getClickedInventory()!=null && bot!=null && event.getClickedInventory().equals(bot)){
				if(event.isShiftClick()){
					player.sendMessage(p.getPrefix() + ChatColor.RED + "Error: Shiftclicking is not avaliable at this time");
					event.setCancelled(true);
				}
			}
		}
	}
	
	/*
	 * Explosions
	 * 
	 * 
	 * 
	 * 
	 * 
	 * */
	
	
	@EventHandler
	public void onExplodeBlock(BlockBreakEvent event){
		Block block = event.getBlock();
		if(p.isInAllowedRegion(block)){
			Player player = event.getPlayer();
			Random ran = new Random();
			int r = ran.nextInt(100)+1;
			if(player.getItemInHand()!=null && player.getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta().hasLore()){
				ItemStack is = player.getItemInHand();
					int chance = 0;
					for(String str :is.getItemMeta().getLore()){
						if(str.contains(ChatColor.RED + "Explosions ")){
							chance = parseExplode(str);
							break;
						}
					}
					if(r<=chance){
						e.explode(player.getLocation(), player, 12, false, true);
					}
			}
		}
	}
	public int parseExplode(String str){
		str = ChatColor.stripColor(str);
		str = str.replace("Explosions ", "");
		if(str.equalsIgnoreCase("I")){
			return 3;
		}
		else if(str.equalsIgnoreCase("II")){
			return 6;
		}
		else if(str.equalsIgnoreCase("III")){
			return 10;
		}
		else{
			return 0;
		}
	}
	
	public String getExplosions(List<String> lore){
		for(String s : lore){
			if(s.contains("Explosions ")){
				s = s.replace("Explosions ", "");
				return s;
			}
		}
		return null;
		
	}
	
	/*
	 * Speed
	 * 
	 * 
	 * 
	 * 
	 * 
	 * */
	
	public String getSpeed(List<String> lore){
		for(String s : lore){
			if(s.contains("Speed ")){
				s = s.replace("Speed ", "");
				return s;
			}
		}
		return null;
		
	}
	
	@EventHandler
	public void onUpgradeClose(InventoryCloseEvent event){
		if(event.getPlayer() instanceof Player && event.getInventory().getName().equalsIgnoreCase("upgrade inventory")){
			Player player = (Player)event.getPlayer();
			if(cursor.containsKey(player.getName())){
				player.getInventory().addItem(cursor.get(player.getName()));
				cursor.remove(player.getName());
				player.updateInventory();
			}
		}
	}
	private ItemStack getUpgrade(Integer i){
		ItemStack is = null;
		ItemMeta im;
		ArrayList<String> lore = new ArrayList<String>();
		switch(i){
		//explosion
		case 1:
			is = new ItemStack(Material.FIREBALL);
			im = is.getItemMeta();
			im.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Explosions");
			lore.add(ChatColor.AQUA + "MaxLevel: 3, Cost: 8 Tokens");
			im.setLore(lore);
			is.setItemMeta(im);
			lore.clear();
			break;
			//night vision
		case 2:
			is = new ItemStack(Material.EYE_OF_ENDER);
			im = is.getItemMeta();
			im.setDisplayName(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Night Vision");
			lore.add(ChatColor.AQUA + "MaxLevel: 1, Cost: 6 Tokens");
			im.setLore(lore);
			is.setItemMeta(im);
			lore.clear();
			break;
			//speed
		case 3:
			is = new ItemStack(Material.SUGAR);
			im = is.getItemMeta();
			im.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Speed");
			lore.add(ChatColor.AQUA + "MaxLevel: 4, Cost: 7 Tokens");
			im.setLore(lore);
			is.setItemMeta(im);
			lore.clear();
			break;
		case 4:
			is = new ItemStack(Material.REDSTONE);
			im = is.getItemMeta();
			im.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Efficiency");
			lore.add(ChatColor.AQUA + "MaxLevel: >9000, Cost: 5 Tokens");
			im.setLore(lore);
			is.setItemMeta(im);
			lore.clear();
			break;
		case 5:
			is = new ItemStack(Material.QUARTZ);
			im = is.getItemMeta();
			im.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Silk Touch");
			lore.add(ChatColor.AQUA + "MaxLevel: 1, Cost: 8 Tokens");
			im.setLore(lore);
			is.setItemMeta(im);
			lore.clear();
			break;
		case 6:
			break;
		}
		return is;
	}
	private ItemStack getCustomItem(Player player, String s){
		ItemStack is = null;
		ItemMeta im;
		ArrayList<String> lore = new ArrayList<String>();
		switch(s){
		case "upgrade":
			is = new ItemStack(Material.ENCHANTMENT_TABLE, 1);
			im = is.getItemMeta();
			im.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Apply Enchantments");
			lore.add(ChatColor.AQUA + "Apply unique enchantments to your tool or weapon with tokens");
			im.setLore(lore);
			is.setItemMeta(im);
			lore.clear();
			break;
		case "repair":
			is = new ItemStack(Material.ANVIL, 1);
			im = is.getItemMeta();
			im.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Repair Tool");
			lore.add(ChatColor.AQUA + "Repairs the item in your hand to max durability for 21 crystals");
			im.setLore(lore);
			lore.clear();
			is.setItemMeta(im);
			break;
		case "crystals":
			is = new ItemStack(Material.NETHER_STAR, 1);
			im = is.getItemMeta();
			im.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "You have " + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + t.getTokens(player) + ChatColor.GOLD + ChatColor.BOLD + " crystal(s)");
			lore.add(ChatColor.AQUA + "Shows the amount of crystals that you currently have");
			im.setLore(lore);
			lore.clear();
			is.setItemMeta(im);
			break;
		case "back":
			is = new ItemStack(Material.COMPASS, 1);
			im = is.getItemMeta();
			im.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Exit");
			lore.add(ChatColor.AQUA + "Exits the upgrade inventory");
			im.setLore(lore);
			lore.clear();
			is.setItemMeta(im);
			break;
		case "pickholder":
			is = new ItemStack(Material.EMERALD, 1);
			im = is.getItemMeta();
			im.setDisplayName(" ");
			lore.add(ChatColor.AQUA + "Put the item you wish to enchant here");
			im.setLore(lore);
			lore.clear();
			is.setItemMeta(im);
			break;
			default:
				break;
		}
		return is;
	}
	
	public void checkEffects(Player player){
		ItemStack is = player.getItemInHand();
		if(is.hasItemMeta() && is.getItemMeta().hasLore()){
			ArrayList<String> itemlore = new ArrayList<String>(is.getItemMeta().getLore());
			if(itemlore.contains(ChatColor.DARK_GREEN + "Night Vision I")){
				player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 100*20, 0));
			}
			if(itemlore.contains(ChatColor.WHITE + "Speed IV")){
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10*20, 1));
			}
			else if(itemlore.contains(ChatColor.WHITE + "Speed III")){
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*20, 0));
			}
			else if(itemlore.contains(ChatColor.WHITE + "Speed II")){
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10*20, 0));
			}
			else if(itemlore.contains(ChatColor.WHITE + "Speed I")){
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5*20, 0));
			}
			else if(!itemlore.contains(ChatColor.DARK_GREEN + "Night Vision I")){
				player.removePotionEffect(PotionEffectType.NIGHT_VISION);
			}
		}
	}
	private ArrayList<Block> tempblks = new ArrayList<>();
	    @EventHandler
	    public void onExplode(EntityExplodeEvent event){
	        Iterator<Block> blocks = event.blockList().iterator();
	        if(event.getEntity() instanceof Player){
	        	Player player = (Player)event.getEntity();
	        	LivingEntity item = (LivingEntity)event.getEntity();
	            while(blocks.hasNext()){
	                Block block = blocks.next();
	                if(p.isInAllowedRegion(block)){
	                	if(!tempblks.contains(block)){
		                	tempblks.add(block);
		                    Iterator<ItemStack> stack = block.getDrops().iterator();
		                    while(stack.hasNext()){
		                        ItemStack items = stack.next();
		                        if(items != null) {
		                        	if(items.getItemMeta().equals(Material.BEDROCK)){
		                        		stack.remove();
		                        		item.remove();
		                        		blocks.remove();
		                        	}
		                        	else{
		                        		if(p.asell.contains(player.getUniqueId())){
		                        			p.eco.depositPlayer(player, p.sa.getItemPrice(p.perms.getPrimaryGroup(player).toString(), p.translateBlock(items).getType())*items.getAmount());
		                        		}
		                        		else{
		                        			player.getInventory().addItem(p.translateBlock(items));
		                        		}
		                        		stack.remove();
		                        	}
		                        }
		                    }
		                }
	                }
	                else{
	                    blocks.remove();
	                }
	        }
	     }
	        
	        Bukkit.getScheduler().runTaskLater(p, new Runnable(){

				@Override
				public void run() {
					tempblks.clear();
					
				}}, 40);
	    }
}
