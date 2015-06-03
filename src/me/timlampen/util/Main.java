package me.timlampen.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.timlampen.chatreaction.ChatReactionListener;
import me.timlampen.chatreaction.IntervalTask;
import me.timlampen.chatreaction.ReactionConfig;
import me.timlampen.chatreaction.ReactionOptions;
import me.timlampen.chatreaction.ReactionPlayer;
import me.timlampen.chatreaction.WordFile;
import me.timlampen.commands.CommandHandler;
import me.timlampen.commands.Rankup;
import me.timlampen.commands.RankupCommand;
import me.timlampen.currency.BackPack;
import me.timlampen.currency.PackPlayer;
import me.timlampen.currency.RankDis;
import me.timlampen.currency.ScratchCards;
import me.timlampen.currency.ScratchInv;
import me.timlampen.currency.Tokens;
import me.timlampen.currency.UpgradeInv;
import me.timlampen.dcscoreboard.BlockListener;
import me.timlampen.dcscoreboard.DropParty;
import me.timlampen.dcscoreboard.SBVoteListener;
import me.timlampen.dcscoreboard.ScoreBoardListener;
import me.timlampen.explosions.ExtraExplosions;
import me.timlampen.explosions.GrenadeListener;
import me.timlampen.explosions.MineBow;
import me.timlampen.explosions.RocketListener;
import me.timlampen.extras.EnragedMining;
import me.timlampen.extras.Goodies;
import me.timlampen.extras.Hologram;
import me.timlampen.extras.NameInfo;
import me.timlampen.extras.Prestige;
import me.timlampen.extras.SellAll;
import me.timlampen.extras.WarpInv;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class Main extends JavaPlugin{
	public ReactionConfig config = new ReactionConfig(this);
	
	public WordFile wordFile = new WordFile(this);

	private ReactionOptions options;

	final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	private Random ran;

	private static boolean isRunning;
	
	private static boolean scrambled;

	private static String currentWord = null;
	
	private static String displayWord = null;
	
	private static long startTime;
	
	private static BukkitTask iTask = null;
	
	private static BukkitTask endTask = null;
	
	
	public static Map<String, ReactionPlayer> reactionPlayers = new HashMap<String, ReactionPlayer>();

	public static List<ReactionPlayer> topPlayers = new ArrayList<ReactionPlayer>();
	
	
	
	
	private Prestige pre;
	private NameInfo ni;
	private UpgradeInv ui;
	public SellAll sa;
	private BackPack bp;
	private RankDis rd;
	private CommandHandler ch;
	private WarpInv wi;
	private ScratchInv si;
	private ScratchCards sc;
	private Parse pa;
	private Tokens t;
	private EnragedMining enm;
	private MineBow mb;
	private BlockListener bl;
	private ScoreBoardListener sbl;
	private GrenadeListener gl;
	private SBVoteListener vl;
	private RocketListener rl;
	private DropParty dp;
	private Explosions e;
	private Rankup r;
	private Lang l;
	private Goodies g;
	private Hologram h;
	private ExtraExplosions ee;
	Logger log = Bukkit.getLogger();
    public static Permission perms = null;
    public static Economy eco = null;
    public static int vote;
   // public EffectLib lib;
    //public EffectManager em;
	public Map<UUID, PackPlayer> playerInfos;
    private static Plugin plugin;
    public Table<UUID, Material, Integer> asellitems = HashBasedTable.create();
    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
	public ArrayList<UUID> message = new ArrayList<UUID>();
	public HashMap<UUID, Double> aselltotal = new HashMap<UUID, Double>();
    public static Plugin getPlugin(){
    	return plugin;
    }
    public PackPlayer getPlayerInfo(UUID name) {
        PackPlayer pi = playerInfos.get(name);
        if (pi != null) {
           return pi;
        }
        pi = new PackPlayer();
        playerInfos.put(name, pi);
        return pi;
    }
    public String getPrefix(){
    	
    	return ChatColor.translateAlternateColorCodes('&', getConfig().getString("Prefix", ChatColor.BOLD + "["+ChatColor.RED+ChatColor.BOLD+"D"+ChatColor.GRAY+ChatColor.BOLD+"C"+ChatColor.WHITE+ChatColor.BOLD+"]"+ChatColor.RESET + " "));
    }
	public ArrayList<UUID> asell = new ArrayList<UUID>();
	@Override
	public void onEnable(){
		console.sendMessage(getPrefix() + "Starting Load...");
		playerInfos = new HashMap<UUID, PackPlayer>();
		plugin = this;
		//lib = EffectLib.instance();
		//em = new EffectManager(lib);
		console.sendMessage(getPrefix() + "Loading Config...");
		if(getConfig().getInt("CurrentVotes")==0){
			vote = getConfig().getInt("MaxVotes");
		}
		else{
			vote = getConfig().getInt("CurrentVotes");	
		}
		saveDefaultConfig();
		setupEconomy();
		setupPermissions();
		console.sendMessage(getPrefix() + "Success! Registering Events...");
		RankupCommand rc = new RankupCommand(this);
		e = new Explosions();
		pre = new Prestige(this);
		enm = new EnragedMining(this);
		h = new Hologram(this);
		l = new Lang(this);
		pa = new Parse(l);
		sc = new ScratchCards(this, pa);
		bl = new BlockListener(this);
		sa = new SellAll(this, pa, bl);
		r = new Rankup(l);
		si = new ScratchInv(sc, this, l);
		ee = new ExtraExplosions(this, l);
		rd = new RankDis(this, r);
		mb = new MineBow(this, e);
		gl = new GrenadeListener(this, e);
		rl = new RocketListener(this, e);
		dp = new DropParty(this, gl, rl);
		vl = new SBVoteListener(this, dp);
		t = new Tokens(this, l, pa);
		ni = new NameInfo(this, t, pre, r);
		bp = new BackPack(this, t, l);
		wi = new WarpInv(this, l, r);
		g = new Goodies(this, l, gl, rl);
		sbl = new ScoreBoardListener(this, bl, r);
		ui = new UpgradeInv(this, t, e);
		ch = new CommandHandler(gl, dp, bl, rl, this, l, sbl, mb, g, t, sc, rd, bp, sa, ui, pre);
		vl = new SBVoteListener(this, dp);
		Bukkit.getPluginManager().registerEvents(bl, this);
		Bukkit.getPluginManager().registerEvents(sbl, this);
		Bukkit.getPluginManager().registerEvents(rl, this);
		Bukkit.getPluginManager().registerEvents(gl, this);
		Bukkit.getPluginManager().registerEvents(g, this);
		Bukkit.getPluginManager().registerEvents(h, this);
		Bukkit.getPluginManager().registerEvents(ee, this);
		Bukkit.getPluginManager().registerEvents(mb, this);
		Bukkit.getPluginManager().registerEvents(t, this);
		Bukkit.getPluginManager().registerEvents(enm, this);
		Bukkit.getPluginManager().registerEvents(sc, this);
		Bukkit.getPluginManager().registerEvents(si, this);
		Bukkit.getPluginManager().registerEvents(wi, this);
		Bukkit.getPluginManager().registerEvents(bp, this);
		Bukkit.getPluginManager().registerEvents(ui, this);
		Bukkit.getPluginManager().registerEvents(ni, this);
		Bukkit.getPluginManager().registerEvents(vl, this);
		Bukkit.getPluginManager().registerEvents(new ChatReactionListener(this), this);
		getCommand("prestige").setExecutor(ch);
		getCommand("crystalinv").setExecutor(ch);
		getCommand("sall").setExecutor(ch);
		getCommand("sellall").setExecutor(ch);
		getCommand("bag").setExecutor(ch);
		getCommand("backpack").setExecutor(ch);
		getCommand("dc").setExecutor(ch);
		getCommand("darknesscraft").setExecutor(ch);
		getCommand("casino").setExecutor(ch);
		getCommand("warps").setExecutor(ch);
		getCommand("crystals").setExecutor(ch);
		getCommand("crystal").setExecutor(ch);
		getCommand("ranks").setExecutor(ch);
		getCommand("rankup").setExecutor(rc);
		getCommand("nextrank").setExecutor(rc);
		getCommand("withdraw").setExecutor(ch);
		getCommand("scoreboard").setExecutor(ch);
		getCommand("autosell").setExecutor(ch);
		console.sendMessage(getPrefix() + "Success! Implementing Scoreboard...");
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			@Override
			public void run() {
				for(Player player : Bukkit.getOnlinePlayers()){
					if(player.getScoreboard()==null){
						player.setScoreboard(sbl.setupScoreboard(player));
					}
					else{
						player.setScoreboard(sbl.setupScoreboard(player));
					}
					ui.checkEffects(player);
				}
				
			}}, 0, 20*5);
		console.sendMessage(getPrefix() + "Success! Cleaning up...");
		for(String s: getConfig().getStringList("SafeRanks")){
			sbl.safe.add(s);
		}
		for(String s: getConfig().getStringList("MineRegions")){
			regions.add(s);
		}
		for(String s: getConfig().getStringList("DropParty.Commands")){
			dp.cmd.add(s);
		}
		for(String s: getConfig().getStringList("Goodie.Commands")){
			g.cmd.add(s);
		}
		bl.loadMulti();
		bp.loadItems();
		ni.loadItems();
		pre.loadPresitge();
		addPermissions();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){

			@Override
			public void run(){
				for(Entry<UUID, Map<Material, Integer>> entry : asellitems.rowMap().entrySet()){
					UUID uuid = entry.getKey();
					if(Bukkit.getPlayer(uuid)!=null){
						Player player = Bukkit.getPlayer(uuid);
						player.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "----------" + ChatColor.RESET);
						player.sendMessage(ChatColor.RED + "You've Sold:");
						for(Material mate : entry.getValue().keySet()){
							int amt = entry.getValue().get(mate);
							player.sendMessage(ChatColor.GRAY + "[" + ChatColor.RED + amt + ChatColor.GRAY + "] " + mate.toString());
						}
						double multi = 1;
						for(double i = 1.0; i<=5.0; i=i+.1){
							if(player.hasPermission("multiplier." + i)){
								if(i<=bl.getMultiplier(player)){
									break;
								}
								else{
									multi = i;
									break;
								}
							}
						}
						if(bl.getMultiplier(player)>1){
							multi = bl.getMultiplier(player);
						}
						player.sendMessage(ChatColor.RED + "You've made: " + getMoney(aselltotal.get(player.getUniqueId())*multi));
						player.sendMessage(ChatColor.DARK_GRAY + "Your total has been multiplied by " + ChatColor.RED + "" + ChatColor.UNDERLINE + multi);
						player.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "----------" + ChatColor.RESET);
					}
				}
				
			}}, 0, 400);
		ran = new Random();

		config.loadCfg();

		options = new ReactionOptions(this);

		start();
		
		console.sendMessage(getPrefix() + "Success! Plugin Startup Complete!");
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onDisable(){
		console.sendMessage(getPrefix() + "Saving Files...");
		//em.dispose();
		getConfig().set("CurrentVotes", vote);
		saveDefaultConfig();
		t.saveTokens();
		for(Player player : Bukkit.getOnlinePlayers()){
			if(player.getScoreboard()!=null){
				player.getScoreboard().resetScores(player);
			}
		}
		for(UUID s : playerInfos.keySet()){
				bp.saveItems(s);
		}
		for(UUID s : ni.blks.keySet()){
			ni.saveItems(s);
		}
		for(UUID s : pre.rank.keySet()){
			pre.savePrestige(s);
		}
		bl.saveMulti();
		stop();
		Bukkit.getScheduler().cancelTasks(this);
		options = null;
		setCurrentWord(null);
		setDisplayWord(null);
		reactionPlayers = null;
		topPlayers = null;
		log.info("Config saving complete!");
	}
	
    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            eco = economyProvider.getProvider();
        }

        return (eco != null);
    }
    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            perms = permissionProvider.getProvider();
        }
        return (perms != null);
    }
	public WorldGuardPlugin getWorldGuard() {
	    Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
	 
	    // WorldGuard may not be loaded
	    if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
	    	Bukkit.broadcastMessage("Its Null!");
	        return null; // Maybe you want throw an exception instead
	    }
	 
	    return (WorldGuardPlugin) plugin;
	}
	private ArrayList<String> regions = new ArrayList<String>();
	public boolean isInRegion(Player player){
		RegionManager rm = getWorldGuard().getRegionManager(player.getWorld());
		ApplicableRegionSet set = rm.getApplicableRegions(player.getLocation());
		LinkedList< String > parentNames = new LinkedList< String >();
		LinkedList< String > regions = new LinkedList< String >();
		for(ProtectedRegion region : set){
			String id = region.getId();
			regions.add(id);
			ProtectedRegion parent = region.getParent();
			while(parent !=null){
				parentNames.add(parent.getId());
				parent = parent.getParent();
			}
		}
		for(String name: parentNames){
			regions.remove(name);
		}
		if(regions.size()<1){
			return true;
		} 
		for(String s: getConfig().getStringList("MineRegions")){
			if(regions.contains(s)){
				return true;
			}
		}
		parentNames.clear();
		regions.clear();
		return false;
	}
	public boolean isInAllowedRegion(Block e){
		RegionManager rm = getWorldGuard().getRegionManager(e.getWorld());
		ApplicableRegionSet set = rm.getApplicableRegions(new Location(e.getLocation().getWorld(), e.getLocation().getX(), e.getLocation().getY()-1, e.getLocation().getZ()));
		LinkedList< String > regions = new LinkedList< String >();
		for(ProtectedRegion region : set){
			String id = region.getId();
			regions.add(id);
		}
		for(String s: getConfig().getStringList("MineRegions")){
			if(regions.contains(s)){
				return true;
			}
		}
		regions.clear();
		return false;
	}
	 public String getMoney(double amt){
		 DecimalFormat df = new DecimalFormat("#.###");
		 String s = "";
		 Double thou = new Double("1000");
		 Double mill = new Double("1000000");
		 Double bill = new Double("1000000000");
		 Double tril = new Double("1000000000000");
		 Double quad = new Double("1000000000000000");
		 Double quin = new Double("1000000000000000000");
		 if(amt>=quin){
			 amt = amt/quin;
			 s = df.format(amt) + "QU";
		 }
		 else if(amt>=quad){
			 amt = amt/quad;
			 s = df.format(amt) + "Q";
		 }
		 else if(amt>=tril){
			 amt = amt/tril;
			 s = df.format(amt) + "T";
		 }
		 else if(amt>=bill){
			 amt = amt/bill;
			 s = df.format(amt) + "B";
		 }
		 else if(amt>=mill){
			 amt = amt/mill;
			 s = df.format(amt) + "M";
		 }
		 else if(amt>=thou){
			 amt = amt/thou;
			 s = df.format(amt) + "K";
		 }
		 else{
			 s = df.format(amt) + "";
		 }
		 return s;
	 }
	  public static List<String> permissions = new ArrayList<String>();
	  public void addPermissions(){
		  for(String key : getConfig().getConfigurationSection("prisonprefix").getKeys(false)){
			  String prefix = getConfig().getString("prisonprefix." + key + ".prefix");
			  String perm = getConfig().getString("prisonprefix." + key + ".permission");
			  int weight = getConfig().getInt("prisonprefix." + key + ".weight");
			  permissions.add(key + ":" + perm + ":" + weight + ":" + prefix);
		  }
	  }
	  
	  public static List<String> getPermissions()
	  {
	    return permissions;
	  }
	  
	  public ItemStack translateBlock(ItemStack is){
		  if(is!=null){
			  if(is.getType().toString().contains("_ORE")){
				  if(is.getType()==Material.DIAMOND_ORE){
					  is.setType(Material.DIAMOND);
				  }
				  else if(is.getType()==Material.EMERALD_ORE){
					  is.setType(Material.EMERALD);
				  }
				  else if(is.getType()==Material.REDSTONE_ORE){
					  is.setType(Material.REDSTONE);
				  }
				  else{
					  String[] split = is.getType().toString().split("_");
				  	  is.setType(Material.getMaterial(split[0]+"_INGOT"));
				  }
				  return is;
			  }
			  else if(is.getType()==Material.COBBLESTONE){
				  is.setType(Material.STONE);
			  }
			  return is;
		  }
		  return is;
	  }
	  
		

		public void debug(Level lv, String msg) {
			if (getOptions().debug()) {
				System.out.println("[ReactionDebug "+lv.getName()+"] "+msg);
			}
		}
		protected void reload() {
			stop();
			Bukkit.getScheduler().cancelTasks(this);
			reloadConfig();
			saveConfig();
			options = new ReactionOptions(this);
			start();
		}
		
		protected void start() {
			if (endTask != null) {
				debug(Level.INFO, "Cancelling previous reaction end task");
				endTask.cancel();
				endTask = null;
			}

			
			if (iTask == null) {
				debug(Level.INFO, "Starting interval task!");
				iTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this,
						new IntervalTask(this), getOptions().interval() * 20,
						getOptions().interval() * 20);
			} else {
				debug(Level.INFO, "Stopping old interval task!");
				iTask.cancel();
				iTask = null;
				debug(Level.INFO, "Starting new interval task!");
				iTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this,
						new IntervalTask(this), getOptions().interval() * 20,
						getOptions().interval() * 20);	
			}
		}
		
		protected void stop() {
			if (endTask != null) {
				debug(Level.INFO, "Stopping reaction end task!");
				endTask.cancel();
				endTask = null;
			}
			if (iTask != null) {
				debug(Level.INFO, "Stopping reaction interval task!");
				iTask.cancel();
				iTask = null;
			}
		}
		
		public String scramble(String input) {
			
			if (!getOptions().scrambleSpaces() || !input.contains(" ")) {
				
				List<Character> characters = new ArrayList<Character>();

				for (char c : input.toCharArray()) {
					characters.add(c);
				}

				StringBuilder output = new StringBuilder(input.length());
				
				while (characters.size() != 0) {
					
					int rndm = (int) (Math.random() * characters.size());
					
					output.append(characters.remove(rndm));
				}
				
				return output.toString();
			
			} else {
				
				String out = "";
				
				for (String part : input.split(" ")) {
					
					List<Character> characters = new ArrayList<Character>();

					for (char c : part.toCharArray()) {
						characters.add(c);
					}

					StringBuilder output = new StringBuilder(part.length());
					
					while (characters.size() != 0) {
						
						int rndm = (int) (Math.random() * characters.size());
						
						output.append(characters.remove(rndm));
					}
					out = out+output.toString()+" ";
				}
				
				return out.trim();
			}
		}

		public String pickWord() {
			
			String s = "";
			
			if (getOptions().useCustomWords() && getOptions().customWords() != null && !getOptions().customWords().isEmpty()) {
				
				s = getOptions().customWords().get(ran.nextInt(getOptions().customWords().size()));
				
			} else {
				
				for (int i = 0; i < getOptions().charLength(); i++) {
					
					s = s + CHARS.charAt(ran.nextInt(CHARS.length()));
				}		
			}
			return s;
		}

		public ReactionOptions getOptions() {
			
			if (options == null) {
				options = new ReactionOptions(this);
			}
			return options;
		}

		public void giveRewards(String playerName) {

			List<String> rewards = getOptions().rewards();

			if (rewards == null || rewards.isEmpty()) {
				return;
			}

			List<String> give = new ArrayList<String>();

			for (int i = 0; i < getOptions().rewardAmt(); i++) {
				String g = rewards.get(ran.nextInt(rewards.size()));

				if (give.contains(g)) {
					g = rewards.get(ran.nextInt(rewards.size()));
					
					if (give.contains(g)) {
						continue;
					}
				}
				give.add(g);
			}

			if (give.isEmpty()) {
				return;
			}
			
			for (String command : give) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("@p", playerName));
			}
		}
		
		public static int getOnline() {
	        try {
	            Method method = Bukkit.class.getMethod("getOnlinePlayers");
	            Object players = method.invoke(null);

	            if(players instanceof Player[]) {
	                Player[] playerArray = (Player[]) players;
	                return playerArray.length;
	            }
	            else {
	            	@SuppressWarnings("unchecked")
					Collection<Player> pl = (Collection<Player>) players;
	                return pl.size();
	            }
	        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
	            e.printStackTrace();
	        }
	        return 0;
	    }
		
		public String getTime(long time) {
			
			if (startTime <= 0) {
				return "0";
			}
			
			double s = (time-startTime)/1000.00;
			
			return String.format("%.2f", new Object[] { s });
		}
		
		public static boolean hasData(String uuid) {
			
			if (reactionPlayers == null || reactionPlayers.isEmpty()) {
				return false;
			}
			
			return reactionPlayers.keySet().contains(uuid) && reactionPlayers.get(uuid) != null;
		}

		public static boolean isRunning() {
			return isRunning;
		}

		public static void setRunning(boolean isRunning) {
			Main.isRunning = isRunning;
		}

		public static String getCurrentWord() {
			return currentWord;
		}

		public static void setCurrentWord(String currentWord) {
			Main.currentWord = currentWord;
		}

		public static String getDisplayWord() {
			return displayWord;
		}

		public static void setDisplayWord(String displayWord) {
			Main.displayWord = displayWord;
		}
		
		public static long getStartTime() {
			return startTime;
		}
		
		public static void setStartTime(long time) {
			Main.startTime = time;
		}
		
		public static BukkitTask getIntervalTask() {
			return iTask;
		}
		
		public static void setIntervalTask(BukkitTask task) {
			Main.iTask = task;
		}
		
		public static BukkitTask getEndTask() {
			return endTask;
		}
		
		public static void setEndTask(BukkitTask task) {
			Main.endTask = task;
		}
		
		public static boolean isScrambled() {
			return scrambled;
		}
		
		public static void setScrambled(boolean scramble) {
			Main.scrambled = scramble;
		}
}
