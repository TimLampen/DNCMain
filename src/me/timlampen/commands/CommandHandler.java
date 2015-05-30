package me.timlampen.commands;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import me.timlampen.currency.BackPack;
import me.timlampen.currency.RankDis;
import me.timlampen.currency.ScratchCards;
import me.timlampen.currency.ScratchInv;
import me.timlampen.currency.Tokens;
import me.timlampen.currency.UpgradeInv;
import me.timlampen.dcscoreboard.BlockListener;
import me.timlampen.dcscoreboard.DropParty;
import me.timlampen.dcscoreboard.SBVoteListener;
import me.timlampen.dcscoreboard.ScoreBoardListener;
import me.timlampen.explosions.GrenadeListener;
import me.timlampen.explosions.MineBow;
import me.timlampen.explosions.RocketListener;
import me.timlampen.extras.Goodies;
import me.timlampen.extras.Prestige;
import me.timlampen.extras.SellAll;
import me.timlampen.extras.WarpInv;
import me.timlampen.util.Lang;
import me.timlampen.util.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;


public class CommandHandler implements CommandExecutor{
	Prestige pre;
	UpgradeInv ui;
	SellAll sa;
	RankDis rd;
	Tokens t;
	GrenadeListener gl;
	SBVoteListener vl;
	BlockListener bl;
	Main p;
	RocketListener rl;
	DropParty dp;
	Lang l;
	ScoreBoardListener sbl;
	MineBow mb;
	Goodies g;
	ScratchCards sc;
	BackPack bp;
	public CommandHandler(GrenadeListener gl, DropParty dp, BlockListener bl, RocketListener rl, Main p, Lang l, ScoreBoardListener sbl, MineBow mb, Goodies g, Tokens t, ScratchCards sc, RankDis rd, BackPack bp, SellAll sa, UpgradeInv ui, Prestige pre){
		this.gl = gl;
		this.bl = bl;
		this.p = p;
		this.rl = rl;
		this.dp = dp;
		this.l = l;
		this.sbl = sbl;
		this.mb = mb;
		this.g = g;
		this.t = t;
		this.sc = sc;
		this.rd = rd;
		this.bp = bp;
		this.sa = sa;
		this.ui =ui;
		this.pre = pre;
	}
	@SuppressWarnings({ "deprecation", "static-access", "unused" })
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		ConsoleCommandSender console = Bukkit.getConsoleSender();
		if(sender instanceof Player){
			Player player = (Player)sender;
			if(cmd.getName().equals("dc") || cmd.getName().equals("darknesscraft")){
				if(args.length==0){
					player.sendMessage(ChatColor.DARK_GRAY + "--------------->>" + ChatColor.RED + "DarkNess" + ChatColor.GRAY + "Craft" + ChatColor.DARK_GRAY + "<<---------------");
					player.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.RED + "/dc crystals give <player> <amount>" + ChatColor.GRAY + " - Converts bank money into a cheque");
					player.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.RED + "/dc dp <start:restart:set:stop:toggle>" + ChatColor.GRAY + " - Commands dealing with the dropparty");
					player.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.RED + "/dc multi set <player> <1-5> (seconds)" + ChatColor.GRAY + " - Commands dealing with the player multiplier");
					player.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.RED + "/dc grenade give <player> <amount> <strength>" + ChatColor.GRAY + " - Commands dealing with mining grenades");
					player.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.RED + "/dc rocket give <player> <amount> <strength>" + ChatColor.GRAY + " - Commands dealing with mining rockets");
					player.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.RED + "/dc bow give <player> <strength>" + ChatColor.GRAY + " - Commands dealing with the mine bow");
					player.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.RED + "/dc bag give <player> <amount>" + ChatColor.GRAY + " - Commands dealing with the goodie bag");
				}
				else{
					if(args[0].equalsIgnoreCase("dp") || args[0].equalsIgnoreCase("dropparty")){
						if(args.length==1){
							l.doMessage(player, l.r1.INCORRECT_SYNTAX, "");
							return false;
						}
						else{
							if(args[1].equalsIgnoreCase("start")){
								if(player.hasPermission("dc.dp.start")){
									l.doMessage(player, l.s1.DP_START, "", "");
									Main.vote=p.getConfig().getInt("MaxVotes");
									for(Player p: Bukkit.getOnlinePlayers()){
										dp.doTitle(p);	
									}
								}
								else{
									l.doMessage(player, l.r1.NO_PERM, "");
									return false;
								}
							}
							else if(args[1].equalsIgnoreCase("set")){
								if(player.hasPermission("dc.dp.set")){
									try{
										Integer.parseInt(args[2]);
									}catch(NumberFormatException e){
										l.doMessage(player, l.r1.NULL_NUMBER, args[2]);
										return true;
									}
									int num = Integer.parseInt(args[2]);
									Main.vote = num;
									l.doMessage(player, l.s1.DP_SET, "", args[2]);
								}
								else{
									l.doMessage(player, l.r1.NO_PERM, "");
									return false;
								}
							}
							else if(args[1].equalsIgnoreCase("restart")){
								if(player.hasPermission("dc.dp.restart")){
									Main.vote = p.getConfig().getInt("MaxVotes");
									l.doMessage(player, l.s1.DP_RESTART, "", "");
								}
								else{
									l.doMessage(player, l.r1.NO_PERM, "");
									return false;
								}
							}
							else if(args[1].equalsIgnoreCase("stop")){
								if(player.hasPermission("dc.dp.stop")){
									for(Player playerz:Bukkit.getOnlinePlayers()){
										dp.item.put(playerz.getUniqueId(), 1000);
									}
								}
								else{
									l.doMessage(player, l.r1.NO_PERM, "");
									return false;
								}
							}
							else if(args[1].equalsIgnoreCase("toggle")){
								if(dp.toggle.contains(player.getUniqueId())){
									dp.toggle.remove(player.getUniqueId());
									l.doMessage(player, l.s1.TOGGLE_ON, "", "");
								}
								else{
									dp.toggle.add(player.getUniqueId());
									l.doMessage(player, l.s1.TOGGLE_OFF, "", "");
								}
							}
							else{
								l.doMessage(player, l.r1.INCORRECT_SYNTAX, "");
								return false;
							}
						}
					}
					else if(args[0].equalsIgnoreCase("multi") || args[0].equalsIgnoreCase("multiplier")){
						if(args.length==1){
							l.doMessage(player, l.r1.INCORRECT_SYNTAX, "");
						}
						else{
						if(args[1].equalsIgnoreCase("set")){
							if(player.hasPermission("dc.multi.set")){
								if(args[2].equalsIgnoreCase("server")){
									try{
										Double.parseDouble(args[3]);
									}catch(NumberFormatException e){
										l.doMessage(player, l.r1.NULL_NUMBER, args[3]);
										return false;
									}
									double amount = Double.parseDouble(args[3]);
									if(amount>5){
										amount =5;
									}
									for(final Player playerz : Bukkit.getOnlinePlayers()){
										if(args.length==5){
											bl.setMultiplier(playerz, amount);
											try{
												Integer.parseInt(args[4]);
											}catch(NumberFormatException nfe){
												l.doMessage(player, l.r1.NULL_NUMBER, args[4]);
												return false;
											}
											int time = Integer.parseInt(args[4]);
											Bukkit.getScheduler().runTaskLater(p, new Runnable(){

												@Override
												public void run() {
													bl.setMultiplier(playerz, 1);
													playerz.sendMessage(p.getPrefix() + ChatColor.RED + "Your timed multiplier has ran out!");
												}}, 20*time);
										}
									}
								}
								else if(Bukkit.getServer().getPlayer(args[2])!=null){
									try{
										Double.parseDouble(args[3]);
									}catch(NumberFormatException e){
										l.doMessage(player, l.r1.NULL_NUMBER, args[3]);
										return false;
									}
									double amount = Double.parseDouble(args[3]);
									if(amount>5){
										amount =5;
									}
									final Player target = Bukkit.getServer().getPlayer(args[2]);
									if(args.length==5){
										bl.setMultiplier(target, amount);
										try{
											Integer.parseInt(args[4]);
										}catch(NumberFormatException nfe){
											l.doMessage(player, l.r1.NULL_NUMBER, args[4]);
											return false;
										}
										l.doMessage(player, l.s1.MULT_SET, target.getName(), args[3]);
										l.doMessage(target, l.s1.MULTI_GET_SET, player.getName(), args[3]);
										int time = Integer.parseInt(args[4]);
										Bukkit.getScheduler().runTaskLater(p, new Runnable(){

											@Override
											public void run() {
												bl.setMultiplier(target, 1);
												target.sendMessage(p.getPrefix() + ChatColor.RED + "Your timed multiplier has ran out!");
											}}, 20*time);
										return false;
									}
									else{
										bl.setMultiplier(target, amount);
										l.doMessage(player, l.s1.MULT_SET, target.getName(), args[3]);
										l.doMessage(target, l.s1.MULTI_GET_SET, player.getName(), args[3]);
									}
								}
								else{
									l.doMessage(player, l.r1.NULL_PLAYER, args[2]);
									return false;
								}
							}
							else{
								l.doMessage(player, l.r1.NO_PERM, "");
								return false;
							}
						}
						else{
							l.doMessage(player, l.r1.INCORRECT_SYNTAX, "");
							return false;
						}
						}
					}
					//End of Scoreboard stuff
					
					
					
					
					else if(args[0].equalsIgnoreCase("grenade")){
						if(args.length==1){
							l.doMessage(player, l.r1.INCORRECT_SYNTAX, "");
							return false;
						}
						else if(args.length==5){
							if(args[1].equalsIgnoreCase("give")){
								if(player.hasPermission("dc.grenade.give")){
									if(Bukkit.getServer().getPlayer(args[2])!=null){
										try{
											Integer.parseInt(args[3]);
										}catch(NumberFormatException e){
											l.doMessage(player, l.r1.NULL_NUMBER, args[3]);
											return false;
										}
										int amount = Integer.parseInt(args[3]);
										try{
											Integer.parseInt(args[4]);
										}catch(NumberFormatException e){
											l.doMessage(player, l.r1.NULL_NUMBER, args[4]);
											return false;
										}
										int strength = Integer.parseInt(args[4]);
										Player target = Bukkit.getServer().getPlayer(args[2]);
										target.getInventory().addItem(gl.getGrenade(strength, amount));
										l.doMessage(player, l.s1.GRENADE_GIVE, target.getName(), args[3]);
										l.doMessage(target, l.s1.GRENADE_GET, player.getName(), args[3]);
									}
									else{
										l.doMessage(player, l.r1.NULL_PLAYER, args[2]);
										return false;
									}
								}
								else{
									l.doMessage(player, l.r1.NO_PERM, "");
									return false;
								}
							}
							else{
								l.doMessage(player, l.r1.INCORRECT_SYNTAX, "");
								return false;
							}
						}
						else{
							l.doMessage(player, l.r1.INCORRECT_SYNTAX, "");
							return false;
						}
					}
					else if(args[0].equalsIgnoreCase("rocket")){
						if(args.length==1){
							l.doMessage(player, l.r1.INCORRECT_SYNTAX, "");
							return false;
						}
						else if(args.length==5){
						if(args[1].equalsIgnoreCase("give")){
								if(player.hasPermission("dc.rocket.give")){
									if(Bukkit.getServer().getPlayer(args[2])!=null){
										try{
											Integer.parseInt(args[3]);
										}catch(NumberFormatException e){
											l.doMessage(player, l.r1.NULL_NUMBER, args[3]);
											return true;
										}
										int amount = Integer.parseInt(args[3]);
										try{
											Integer.parseInt(args[4]);
										}catch(NumberFormatException e){
											l.doMessage(player, l.r1.NULL_NUMBER, args[4]);
											return true;
										}
										int strength = Integer.parseInt(args[4]);
										Player target = Bukkit.getServer().getPlayer(args[2]);
										target.getInventory().addItem(rl.getGrenade(strength, amount));
										l.doMessage(player, l.s1.ROCKET_GIVE, target.getName(), args[3]);
										l.doMessage(target, l.s1.ROCKET_GET, player.getName(), args[3]);
									}
									else{
										l.doMessage(player, l.r1.NULL_PLAYER, args[2]);
										return false;
									}
								}
								else{
									l.doMessage(player, l.r1.NO_PERM, "");
									return false;
								}
							}
							else{
								l.doMessage(player, l.r1.INCORRECT_SYNTAX, "");
								return false;
							}
						}
						else{
							l.doMessage(player, l.r1.INCORRECT_SYNTAX, "");
							return false;
						}
					}
					else if(args[0].equalsIgnoreCase("bow")){
						if(args.length==1){
							l.doMessage(player, l.r1.INCORRECT_SYNTAX, "");
							return false;
						}
						if(args[1].equalsIgnoreCase("give")){
							if(player.hasPermission("dc.bow.give")){
								if(Bukkit.getServer().getPlayer(args[2])!=null){
									try{
										Integer.parseInt(args[3]);
									}catch(NumberFormatException e){
										l.doMessage(player, l.r1.NULL_NUMBER, args[3]);
										return false;
									}
									int strength = Integer.parseInt(args[3]);
									Player target = Bukkit.getServer().getPlayer(args[2]);
									target.getInventory().addItem(mb.getBow(strength));
									l.doMessage(player, l.s1.ROCKET_GIVE, target.getName(), args[3]);
									l.doMessage(target, l.s1.ROCKET_GET, player.getName(), args[3]);
								}
								else{
									l.doMessage(player, l.r1.NULL_PLAYER, args[2]);
									return false;
								}
							}
							else{
								l.doMessage(player, l.r1.NO_PERM, "");
								return false;
							}
						}
						else{
							player.sendMessage(p.getPrefix() + ChatColor.RED + "Incorrect Syntax: /dc bow give <player> <charges>");
							return false;
						}
					}
					
					//Explosives
					
					
					
					
					
					
					
					else if(args[0].equalsIgnoreCase("bag")){
						if(args.length==1){
							l.doMessage(player, l.r1.INCORRECT_SYNTAX, "");
							return false;
						}
						if(args[1].equalsIgnoreCase("give")){
								if(player.hasPermission("dc.bag.give")){
									if(Bukkit.getServer().getPlayer(args[2])!=null){
										try{
											Integer.parseInt(args[3]);
										}catch(NumberFormatException e){
											l.doMessage(player, l.r1.NULL_NUMBER, args[3]);
											return false;
										}
										int amount = Integer.parseInt(args[3]);
										Player target = Bukkit.getServer().getPlayer(args[2]);
										target.getInventory().addItem(g.getBag(amount));
										l.doMessage(player, l.s1.BAG_GIVE, target.getName(), args[3]);
										l.doMessage(target, l.s1.BAG_GET, player.getName(), args[3]);
									}
									else{
										l.doMessage(player, l.r1.NULL_PLAYER, args[2]);
										return false;
									}
								}
								else{
									l.doMessage(player, l.r1.NO_PERM, "");
									return false;
								}
							}
							else{
								player.sendMessage(p.getPrefix() + ChatColor.RED + "/dc bag give <player> <amount>");
								return false;
							}
					}
					else if(args[0].equalsIgnoreCase("crystal") || args[0].equalsIgnoreCase("crystals")){
						if(args[1].equalsIgnoreCase("give")){
								if(player.hasPermission("dc.crystal.give")){
									if(Bukkit.getServer().getPlayer(args[2])!=null){
										try{
											Integer.parseInt(args[3]);
										}catch(NumberFormatException e){
											l.doMessage(player, l.r1.NULL_NUMBER, args[3]);
											return false;
										}
										int amount = Integer.parseInt(args[3]);
										Player target = Bukkit.getServer().getPlayer(args[2]);
										t.addTokens(target, amount);
										l.doMessage(player, l.s1.TOKEN_GIVE, target.getName(), args[3]);
										l.doMessage(target, l.s1.TOKEN_GET, player.getName(), args[3]);
									}
									else{
										l.doMessage(player, l.r1.NULL_PLAYER, args[2]);
										return false;
									}
								}
								else{
									l.doMessage(player, l.r1.NO_PERM, "");
									return false;
								}
							}
						else if(args[1].equalsIgnoreCase("set")){
							if(player.hasPermission("dc.crystal.set")){
								if(Bukkit.getServer().getPlayer(args[2])!=null){
									try{
										Integer.parseInt(args[3]);
									}catch(NumberFormatException e){
										l.doMessage(player, l.r1.NULL_NUMBER, args[3]);
										return false;
									}
									int amount = Integer.parseInt(args[3]);
									Player target = Bukkit.getServer().getPlayer(args[2]);
									t.setTokens(player, amount);
								}
								else{
									l.doMessage(player, l.r1.NULL_PLAYER, args[2]);
									return false;
								}
							}
							else{
								l.doMessage(player, l.r1.NO_PERM, "");
								return false;
							}
						}
						else if(args[1].equalsIgnoreCase("remove")){
							if(player.hasPermission("dc.crystal.remove")){
								if(Bukkit.getServer().getPlayer(args[2])!=null){
									try{
										Integer.parseInt(args[3]);
									}catch(NumberFormatException e){
										l.doMessage(player, l.r1.NULL_NUMBER, args[3]);
										return false;
									}
									int amount = Integer.parseInt(args[3]);
									Player target = Bukkit.getServer().getPlayer(args[2]);
									t.removeTokens(player, amount);
								}
								else{
									l.doMessage(player, l.r1.NULL_PLAYER, args[2]);
									return false;
								}
							}
							else{
								l.doMessage(player, l.r1.NO_PERM, "");
								return false;
							}
						}
						else{
							player.sendMessage(p.getPrefix() + ChatColor.RED + "Incorrect Syntax: /dc crystals <give:remove:set> <player> <amount>");
							return false;
						}
					}
					else{
						l.doMessage(player, l.r1.INCORRECT_SYNTAX, "");
					}
				}
			}
			
			
			
			
			
			
			else if(cmd.getName().equalsIgnoreCase("crystal") || cmd.getName().equalsIgnoreCase("crystals")){
				if(args.length==0){
					player.sendMessage(ChatColor.AQUA + "-=-=-=-=-=-=-=" + ChatColor.GOLD + "[" + ChatColor.AQUA + "Crystals" + ChatColor.GOLD + "]" + ChatColor.AQUA + "=-=-=-=-=-=-=-");
					player.sendMessage(ChatColor.AQUA + "/crystal " + ChatColor.GOLD + "- Shows the help screen");
					player.sendMessage(ChatColor.AQUA + "/crystal balance" + ChatColor.GOLD + "- Shows amount of coins you have");
					player.sendMessage(ChatColor.AQUA + "/crystal shop" + ChatColor.GOLD + "- Opens pick upgrading shop");
					player.sendMessage(ChatColor.AQUA + "/crystal send <player> <amount>" + ChatColor.GOLD + "- Sends crystals to another player");
				}
				else if(args[0].equalsIgnoreCase("shop")){
					ui.openMainInv(player);
				}
				else if(args[0].equalsIgnoreCase("balance") || args[0].equalsIgnoreCase("bal")){
					l.doMessage(player, l.m1.GET_TOKENS, "", t.getTokens(player) + "");
				}
				else if(args[0].equalsIgnoreCase("send")){
					if(Bukkit.getPlayer(args[1])!=null){
						Player target = Bukkit.getPlayer(args[1]);
						try{
							Integer.parseInt(args[2]);
						}catch(NumberFormatException nfe){
							nfe.printStackTrace();
							l.doMessage(player, l.r1.NULL_NUMBER, args[2]);
							return false;
						}
						
						int amt = Integer.parseInt(args[2]);
						if(t.hasTokens(player, amt)){
							t.removeTokens(player, amt);
							t.addTokens(target, amt);
							l.doMessage(player, l.s1.TOKEN_GIVE, target.getName(), amt + "");
							l.doMessage(target, l.s1.TOKEN_GET, player.getName(), amt + "");
						}
						else{
							l.doMessage(player, l.r1.NO_TOKEN, player.getName());
						}
					}
					else{
						l.doMessage(player, l.r1.NULL_PLAYER, args[1]);
					}
				}
				else{
					l.doMessage(player, l.r1.INCORRECT_SYNTAX, "");
				}
			}
			
			else if(cmd.getName().equalsIgnoreCase("casino")){
				if(args.length==0){
					ScratchInv si = new ScratchInv(player);
				}
				else{
					l.doMessage(player, l.r1.INCORRECT_SYNTAX, "");
					return false;
				}
			}
			
			else if(cmd.getName().equalsIgnoreCase("warps")){
				if(args.length==0){
					WarpInv inv = new WarpInv(player);
				}
				else{
					l.doMessage(player, l.r1.INCORRECT_SYNTAX, "");
					return false;
				}
			}
			
			else if(cmd.getName().equalsIgnoreCase("ranks")){
				if(args.length==0){
					rd.doRank(player);
				}
				else{
					l.doMessage(player, l.r1.INCORRECT_SYNTAX, "");
				}
			}
			
			else if(cmd.getName().equalsIgnoreCase("backpack") || cmd.getName().equalsIgnoreCase("bag")){
				if(args.length==0){
					bp.openBag(player);
				}
			}
			
			else if(cmd.getName().equalsIgnoreCase("sall") || cmd.getName().equalsIgnoreCase("sellall")){
				if(args.length==0){
					sa.sellItems(player);
				}
			}
			
			else if(cmd.getName().equalsIgnoreCase("crystalinv")){
				if(args.length==0){
					ui.openMainInv(player);
				}
			}
			else if(cmd.getName().equalsIgnoreCase("prestige")){
				if(args.length==0){
					player.sendMessage(ChatColor.GREEN + "-=-=-=-=-=-=-=" + ChatColor.DARK_AQUA + "[" + ChatColor.BLUE + "Prestige" + ChatColor.DARK_AQUA + "]" + ChatColor.GREEN + "=-=-=-=-=-=-=-");
					player.sendMessage(ChatColor.GREEN + "/prestige do" + ChatColor.BLUE + "- Does the prestige sequence");
					player.sendMessage(ChatColor.GREEN + "/prestige set <player> <amount>" + ChatColor.BLUE + "- Sets another players prestige level");
				}
				else{
					if(args[0].equalsIgnoreCase("set")){
						if(player.hasPermission("dnc.prestige.set")){
							if(args.length==2){
								if(Bukkit.getPlayer(args[1])!=null){
									Player target = Bukkit.getPlayer(args[1]);
									try{
										Integer.parseInt(args[2]);
									}catch(NumberFormatException nfe){
										nfe.printStackTrace();
										l.doMessage(player, l.r1.NULL_NUMBER, args[2]);
										return false;
									}
									
									int amt = Integer.parseInt(args[2]);
									
									pre.setPrestge(target, amt);
									player.sendMessage(p.getPrefix() + ChatColor.GREEN + "You have set " + ChatColor.AQUA + target.getName() + "'s" + ChatColor.GREEN + " prestige to " + ChatColor.AQUA + amt);
									target.sendMessage(p.getPrefix() + ChatColor.GREEN + "Your prestige has been set to " + ChatColor.AQUA + amt + ChatColor.GREEN + " by " + ChatColor.AQUA + player.getName());
								}
								else{
									l.doMessage(player, l.r1.NULL_PLAYER, args[1]);
								}
							}
							else{
								l.doMessage(player, l.r1.INCORRECT_SYNTAX, "");
							}
						}
						else{
							l.doMessage(player, l.r1.NO_PERM, "");
						}
					}
					else if(args[0].equalsIgnoreCase("do")){
						pre.doPrestige(player);
					}
				}
				
			}
			else if(cmd.getName().equalsIgnoreCase("withdraw")){
				if(args.length==1){
					try{
						Double d = new Double(args[0]);
					}catch(NumberFormatException e){
						l.doMessage(player, l.r1.NULL_NUMBER, args[0]);
						return false;
					}
					Double d = new Double(args[0]);
					if(p.eco.getBalance(player.getName())>=d){
						p.eco.withdrawPlayer(player.getName(), d);
						player.getInventory().addItem(sc.getBankCard(player.getName(), d));
						l.doMessage(player, l.s1.BANK_WITHDRAW, "", d + "");
					}
					else{
						l.doMessage(player, l.r1.NO_MONEY, args[0]);
						return false;
					}
			}
			else{
				l.doMessage(player, l.r1.INCORRECT_SYNTAX, "");
				return false;
			}
			}
			else if(cmd.getName().equalsIgnoreCase("scoreboard")){
				if(args.length==0){
					if(sbl.toggle.contains(player.getUniqueId())){
						l.doMessage(player, l.s1.TOGGLE_OFF, "", "");
						sbl.toggle.remove(player.getUniqueId());
					}
					else{
						l.doMessage(player, l.s1.TOGGLE_ON, "", "");
						sbl.toggle.add(player.getUniqueId());
					}
				}else{
					if(args[0].equalsIgnoreCase("clear")){
						if(player.getScoreboard()!=null){
							player.getScoreboard().resetScores(player);
						}
					}
				}
			}
			else if(cmd.getName().equalsIgnoreCase("autosell")){
					if(p.asell.contains(player.getUniqueId())){
						p.asell.remove(player.getUniqueId());
						if(p.asellitems.containsRow(player.getUniqueId())){
							p.asellitems.row(player.getUniqueId()).clear();
						}
						if(p.aselltotal.containsKey(player.getUniqueId())){
							p.aselltotal.remove(player.getUniqueId());
						}
						player.sendMessage(p.getPrefix() + ChatColor.RED + "You have turned off autosell");
					}
					else{
						p.asell.add(player.getUniqueId());
						player.sendMessage(p.getPrefix() + ChatColor.GREEN + "You have turned on autosell");
					}
				}
		}
		else{
		if(cmd.getName().equals("dc") || cmd.getName().equals("darknesscraft")){
			if(args.length==0){
				sender.sendMessage(ChatColor.DARK_GRAY + "--------------->>" + ChatColor.RED + "DarkNess" + ChatColor.GRAY + "Craft" + ChatColor.DARK_GRAY + "<<---------------");
				sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.RED + "/dc crystals give <sender> <amount>" + ChatColor.GRAY + " - Converts bank money into a cheque");
				sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.RED + "/dc dp <start:restart:set:stop:toggle>" + ChatColor.GRAY + " - Commands dealing with the dropparty");
				sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.RED + "/dc multi set <sender> <1-5> (seconds)" + ChatColor.GRAY + " - Commands dealing with the sender multiplier");
				sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.RED + "/dc grenade give <sender> <amount> <strength>" + ChatColor.GRAY + " - Commands dealing with mining grenades");
				sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.RED + "/dc rocket give <sender> <amount> <strength>" + ChatColor.GRAY + " - Commands dealing with mining rockets");
				sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.RED + "/dc bow give <sender> <strength>" + ChatColor.GRAY + " - Commands dealing with the mine bow");
				sender.sendMessage(ChatColor.DARK_GRAY + "-> " + ChatColor.RED + "/dc bag give <sender> <amount>" + ChatColor.GRAY + " - Commands dealing with the goodie bag");
			}
			else{
				if(args[0].equalsIgnoreCase("dp") || args[0].equalsIgnoreCase("dropparty")){
					if(args.length==1){
						l.doMessage(sender, l.r1.INCORRECT_SYNTAX, "");
						return false;
					}
					else{
						if(args[1].equalsIgnoreCase("start")){
							if(sender.hasPermission("dc.dp.start")){
								l.doMessage(sender, l.s1.DP_START, "", "");
								Main.vote=p.getConfig().getInt("MaxVotes");
								for(Player p: Bukkit.getOnlinePlayers()){
									dp.doTitle(p);	
								}
							}
							else{
								l.doMessage(sender, l.r1.NO_PERM, "");
								return false;
							}
						}
						else if(args[1].equalsIgnoreCase("set")){
							if(sender.hasPermission("dc.dp.set")){
								try{
									Integer.parseInt(args[2]);
								}catch(NumberFormatException e){
									l.doMessage(sender, l.r1.NULL_NUMBER, args[2]);
									return true;
								}
								int num = Integer.parseInt(args[2]);
								Main.vote = num;
								l.doMessage(sender, l.s1.DP_SET, "", args[2]);
							}
							else{
								l.doMessage(sender, l.r1.NO_PERM, "");
								return false;
							}
						}
						else if(args[1].equalsIgnoreCase("restart")){
							if(sender.hasPermission("dc.dp.restart")){
								Main.vote = p.getConfig().getInt("MaxVotes");
								l.doMessage(sender, l.s1.DP_RESTART, "", "");
							}
							else{
								l.doMessage(sender, l.r1.NO_PERM, "");
								return false;
							}
						}
						else if(args[1].equalsIgnoreCase("stop")){
							if(sender.hasPermission("dc.dp.stop")){
								for(Player senderz:Bukkit.getOnlinePlayers()){
									dp.item.put(senderz.getUniqueId(), 1000);
								}
							}
							else{
								l.doMessage(sender, l.r1.NO_PERM, "");
								return false;
							}
						}
					}
				}
				else if(args[0].equalsIgnoreCase("multi") || args[0].equalsIgnoreCase("multiplier")){
					if(args.length==1){
						l.doMessage(sender, l.r1.INCORRECT_SYNTAX, "");
					}
					else{
					if(args[1].equalsIgnoreCase("set")){
						if(sender.hasPermission("dc.multi.set")){
							if(args[2].equalsIgnoreCase("server")){
								try{
									Double.parseDouble(args[3]);
								}catch(NumberFormatException e){
									l.doMessage(sender, l.r1.NULL_NUMBER, args[3]);
									return false;
								}
								double amount = Double.parseDouble(args[3]);
								if(amount>5){
									amount =5;
								}
								for(final Player senderz : Bukkit.getOnlinePlayers()){
									if(args.length==5){
										bl.setMultiplier(senderz, amount);
										try{
											Integer.parseInt(args[4]);
										}catch(NumberFormatException nfe){
											l.doMessage(sender, l.r1.NULL_NUMBER, args[4]);
											return false;
										}
										int time = Integer.parseInt(args[4]);
										Bukkit.getScheduler().runTaskLater(p, new Runnable(){

											@Override
											public void run() {
												bl.setMultiplier(senderz, 1);
												senderz.sendMessage(p.getPrefix() + ChatColor.RED + "Your timed multiplier has ran out!");
											}}, 20*time);
									}
								}
							}
							else if(Bukkit.getServer().getPlayer(args[2])!=null){
								try{
									Double.parseDouble(args[3]);
								}catch(NumberFormatException e){
									l.doMessage(sender, l.r1.NULL_NUMBER, args[3]);
									return false;
								}
								double amount = Double.parseDouble(args[3]);
								if(amount>5){
									amount =5;
								}
								final Player target = Bukkit.getServer().getPlayer(args[2]);
								if(args.length==5){
									bl.setMultiplier(target, amount);
									try{
										Integer.parseInt(args[4]);
									}catch(NumberFormatException nfe){
										l.doMessage(sender, l.r1.NULL_NUMBER, args[4]);
										return false;
									}
									l.doMessage(sender, l.s1.MULT_SET, target.getName(), args[3]);
									l.doMessage(target, l.s1.MULTI_GET_SET, sender.getName(), args[3]);
									int time = Integer.parseInt(args[4]);
									Bukkit.getScheduler().runTaskLater(p, new Runnable(){

										@Override
										public void run() {
											bl.setMultiplier(target, 1);
											target.sendMessage(p.getPrefix() + ChatColor.RED + "Your timed multiplier has ran out!");
										}}, 20*time);
									return false;
								}
								else{
									bl.setMultiplier(target, amount);
									l.doMessage(sender, l.s1.MULT_SET, target.getName(), args[3]);
									l.doMessage(target, l.s1.MULTI_GET_SET, sender.getName(), args[3]);
								}
							}
							else{
								l.doMessage(sender, l.r1.NULL_PLAYER, args[2]);
								return false;
							}
						}
						else{
							l.doMessage(sender, l.r1.NO_PERM, "");
							return false;
						}
					}
					else{
						l.doMessage(sender, l.r1.INCORRECT_SYNTAX, "");
						return false;
					}
					}
				}
				//End of Scoreboard stuff
				
				
				
				
				else if(args[0].equalsIgnoreCase("grenade")){
					if(args.length==1){
						l.doMessage(sender, l.r1.INCORRECT_SYNTAX, "");
						return false;
					}
					else if(args.length==5){
						if(args[1].equalsIgnoreCase("give")){
							if(sender.hasPermission("dc.grenade.give")){
								if(Bukkit.getServer().getPlayer(args[2])!=null){
									try{
										Integer.parseInt(args[3]);
									}catch(NumberFormatException e){
										l.doMessage(sender, l.r1.NULL_NUMBER, args[3]);
										return false;
									}
									int amount = Integer.parseInt(args[3]);
									try{
										Integer.parseInt(args[4]);
									}catch(NumberFormatException e){
										l.doMessage(sender, l.r1.NULL_NUMBER, args[4]);
										return false;
									}
									int strength = Integer.parseInt(args[4]);
									Player target = Bukkit.getServer().getPlayer(args[2]);
									target.getInventory().addItem(gl.getGrenade(strength, amount));
									l.doMessage(sender, l.s1.GRENADE_GIVE, target.getName(), args[3]);
									l.doMessage(target, l.s1.GRENADE_GET, sender.getName(), args[3]);
								}
								else{
									l.doMessage(sender, l.r1.NULL_PLAYER, args[2]);
									return false;
								}
							}
							else{
								l.doMessage(sender, l.r1.NO_PERM, "");
								return false;
							}
						}
						else{
							l.doMessage(sender, l.r1.INCORRECT_SYNTAX, "");
							return false;
						}
					}
					else{
						l.doMessage(sender, l.r1.INCORRECT_SYNTAX, "");
						return false;
					}
				}
				else if(args[0].equalsIgnoreCase("rocket")){
					if(args.length==1){
						l.doMessage(sender, l.r1.INCORRECT_SYNTAX, "");
						return false;
					}
					else if(args.length==5){
					if(args[1].equalsIgnoreCase("give")){
							if(sender.hasPermission("dc.rocket.give")){
								if(Bukkit.getServer().getPlayer(args[2])!=null){
									try{
										Integer.parseInt(args[3]);
									}catch(NumberFormatException e){
										l.doMessage(sender, l.r1.NULL_NUMBER, args[3]);
										return true;
									}
									int amount = Integer.parseInt(args[3]);
									try{
										Integer.parseInt(args[4]);
									}catch(NumberFormatException e){
										l.doMessage(sender, l.r1.NULL_NUMBER, args[4]);
										return true;
									}
									int strength = Integer.parseInt(args[4]);
									Player target = Bukkit.getServer().getPlayer(args[2]);
									target.getInventory().addItem(rl.getGrenade(strength, amount));
									l.doMessage(sender, l.s1.ROCKET_GIVE, target.getName(), args[3]);
									l.doMessage(target, l.s1.ROCKET_GET, sender.getName(), args[3]);
								}
								else{
									l.doMessage(sender, l.r1.NULL_PLAYER, args[2]);
									return false;
								}
							}
							else{
								l.doMessage(sender, l.r1.NO_PERM, "");
								return false;
							}
						}
						else{
							l.doMessage(sender, l.r1.INCORRECT_SYNTAX, "");
							return false;
						}
					}
					else{
						l.doMessage(sender, l.r1.INCORRECT_SYNTAX, "");
						return false;
					}
				}
				else if(args[0].equalsIgnoreCase("bow")){
					if(args.length==1){
						l.doMessage(sender, l.r1.INCORRECT_SYNTAX, "");
						return false;
					}
					if(args[1].equalsIgnoreCase("give")){
						if(sender.hasPermission("dc.bow.give")){
							if(Bukkit.getServer().getPlayer(args[2])!=null){
								try{
									Integer.parseInt(args[3]);
								}catch(NumberFormatException e){
									l.doMessage(sender, l.r1.NULL_NUMBER, args[3]);
									return false;
								}
								int strength = Integer.parseInt(args[3]);
								Player target = Bukkit.getServer().getPlayer(args[2]);
								target.getInventory().addItem(mb.getBow(strength));
								l.doMessage(sender, l.s1.ROCKET_GIVE, target.getName(), args[3]);
								l.doMessage(target, l.s1.ROCKET_GET, sender.getName(), args[3]);
							}
							else{
								l.doMessage(sender, l.r1.NULL_PLAYER, args[2]);
								return false;
							}
						}
						else{
							l.doMessage(sender, l.r1.NO_PERM, "");
							return false;
						}
					}
					else{
						sender.sendMessage(p.getPrefix() + ChatColor.RED + "Incorrect Syntax: /dc bow give <sender> <charges>");
						return false;
					}
				}
				
				//Explosives
				
				
				
				
				
				
				
				else if(args[0].equalsIgnoreCase("bag")){
					if(args.length==1){
						l.doMessage(sender, l.r1.INCORRECT_SYNTAX, "");
						return false;
					}
					if(args[1].equalsIgnoreCase("give")){
							if(sender.hasPermission("dc.bag.give")){
								if(Bukkit.getServer().getPlayer(args[2])!=null){
									try{
										Integer.parseInt(args[3]);
									}catch(NumberFormatException e){
										l.doMessage(sender, l.r1.NULL_NUMBER, args[3]);
										return false;
									}
									int amount = Integer.parseInt(args[3]);
									Player target = Bukkit.getServer().getPlayer(args[2]);
									target.getInventory().addItem(g.getBag(amount));
									l.doMessage(sender, l.s1.BAG_GIVE, target.getName(), args[3]);
									l.doMessage(target, l.s1.BAG_GET, sender.getName(), args[3]);
								}
								else{
									l.doMessage(sender, l.r1.NULL_PLAYER, args[2]);
									return false;
								}
							}
							else{
								l.doMessage(sender, l.r1.NO_PERM, "");
								return false;
							}
						}
						else{
							sender.sendMessage(p.getPrefix() + ChatColor.RED + "/dc bag give <sender> <amount>");
							return false;
						}
				}
				else if(args[0].equalsIgnoreCase("crystal") || args[0].equalsIgnoreCase("crystals")){
					if(args[1].equalsIgnoreCase("give")){
							if(sender.hasPermission("dc.crystal.give")){
								if(Bukkit.getServer().getPlayer(args[2])!=null){
									try{
										Integer.parseInt(args[3]);
									}catch(NumberFormatException e){
										l.doMessage(sender, l.r1.NULL_NUMBER, args[3]);
										return false;
									}
									int amount = Integer.parseInt(args[3]);
									Player target = Bukkit.getServer().getPlayer(args[2]);
									t.addTokens(target, amount);
									l.doMessage(sender, l.s1.TOKEN_GIVE, target.getName(), args[3]);
									l.doMessage(target, l.s1.TOKEN_GET, sender.getName(), args[3]);
								}
								else{
									l.doMessage(sender, l.r1.NULL_PLAYER, args[2]);
									return false;
								}
							}
							else{
								l.doMessage(sender, l.r1.NO_PERM, "");
								return false;
							}
						}
					else if(args[1].equalsIgnoreCase("set")){
						if(sender.hasPermission("dc.crystal.set")){
							if(Bukkit.getServer().getPlayer(args[2])!=null){
								try{
									Integer.parseInt(args[3]);
								}catch(NumberFormatException e){
									l.doMessage(sender, l.r1.NULL_NUMBER, args[3]);
									return false;
								}
								int amount = Integer.parseInt(args[3]);
								Player target = Bukkit.getServer().getPlayer(args[2]);
								t.setTokens(target, amount);
							}
							else{
								l.doMessage(sender, l.r1.NULL_PLAYER, args[2]);
								return false;
							}
						}
						else{
							l.doMessage(sender, l.r1.NO_PERM, "");
							return false;
						}
					}
					else if(args[1].equalsIgnoreCase("remove")){
						if(sender.hasPermission("dc.crystal.remove")){
							if(Bukkit.getServer().getPlayer(args[2])!=null){
								try{
									Integer.parseInt(args[3]);
								}catch(NumberFormatException e){
									l.doMessage(sender, l.r1.NULL_NUMBER, args[3]);
									return false;
								}
								int amount = Integer.parseInt(args[3]);
								Player target = Bukkit.getServer().getPlayer(args[2]);
								t.removeTokens(target, amount);
							}
							else{
								l.doMessage(sender, l.r1.NULL_PLAYER, args[2]);
								return false;
							}
						}
						else{
							l.doMessage(sender, l.r1.NO_PERM, "");
							return false;
						}
					}
					else{
						sender.sendMessage(p.getPrefix() + ChatColor.RED + "Incorrect Syntax: /dc crystals <give:remove:set> <sender> <amount>");
						return false;
					}
				}
				else{
					l.doMessage(sender, l.r1.INCORRECT_SYNTAX, "");
				}
			}
		}
		}
		
		return false;
	}
}
