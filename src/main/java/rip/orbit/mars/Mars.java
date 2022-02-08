package rip.orbit.mars;

import cc.fyre.proton.Proton;
import cc.fyre.proton.command.CommandHandler;
import cc.fyre.proton.nametag.FrozenNametagHandler;
import cc.fyre.proton.serialization.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.qrakn.morpheus.Morpheus;
import com.qrakn.morpheus.game.Game;
import com.qrakn.morpheus.game.GameListeners;
import com.qrakn.morpheus.game.GameQueue;
import com.qrakn.morpheus.game.event.GameEvent;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import rip.orbit.mars.arena.ArenaHandler;
import rip.orbit.mars.duel.DuelHandler;
import rip.orbit.mars.elo.EloHandler;
import rip.orbit.mars.follow.FollowHandler;
import rip.orbit.mars.hologram.HologramHandler;
import rip.orbit.mars.kit.KitHandler;
import rip.orbit.mars.kittype.KitType;
import rip.orbit.mars.kittype.KitTypeJsonAdapter;
import rip.orbit.mars.kittype.KitTypeParameterType;
import rip.orbit.mars.listener.*;
import rip.orbit.mars.lobby.LobbyHandler;
import rip.orbit.mars.match.Match;
import rip.orbit.mars.match.MatchHandler;
import rip.orbit.mars.morpheus.EventListeners;
import rip.orbit.mars.morpheus.EventTask;
import rip.orbit.mars.nametag.PotPvPNametagProvider;
import rip.orbit.mars.party.PartyHandler;
import rip.orbit.mars.postmatchinv.PostMatchInvHandler;
import rip.orbit.mars.pvpclasses.PvPClassHandler;
import rip.orbit.mars.queue.QueueHandler;
import rip.orbit.mars.rematch.RematchHandler;
import rip.orbit.mars.scoreboard.PotPvPScoreboardConfiguration;
import rip.orbit.mars.setting.SettingHandler;
import rip.orbit.mars.statistics.StatisticsHandler;
import rip.orbit.mars.tab.PotPvPLayoutProvider;
import rip.orbit.mars.tournament.TournamentHandler;

import java.io.IOException;

@Getter
public final class Mars extends JavaPlugin {

	@Getter private static Mars instance;
	@Getter private static final Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(PotionEffect.class, new PotionEffectAdapter())
			.registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter())
			.registerTypeHierarchyAdapter(Location.class, new LocationAdapter())
			.registerTypeHierarchyAdapter(Vector.class, new VectorAdapter())
			.registerTypeAdapter(BlockVector.class, new BlockVectorAdapter())
//            .registerTypeHierarchyAdapter(KitType.class, new KitTypeJsonAdapter()) // custom KitType serializer
			.registerTypeAdapter(ChunkSnapshot.class, new ChunkSnapshotAdapter())
			.serializeNulls()
			.create();


	private MongoClient mongoClient;
	private MongoDatabase mongoDatabase;

	private SettingHandler settingHandler;
	private DuelHandler duelHandler;
	private KitHandler kitHandler;
	private LobbyHandler lobbyHandler;
	private ArenaHandler arenaHandler;
	private MatchHandler matchHandler;
	private PartyHandler partyHandler;
	private QueueHandler queueHandler;
	private RematchHandler rematchHandler;
	private PostMatchInvHandler postMatchInvHandler;
	private FollowHandler followHandler;
	private EloHandler eloHandler;
	private TournamentHandler tournamentHandler;
	private PvPClassHandler pvpClassHandler;
	private HologramHandler hologramHandler;
	private StatisticsHandler statisticsHandler;

	private ChatColor dominantColor = ChatColor.GOLD;

	public Mars() {
	}

	@Override
	public void onEnable() {
		//SpigotConfig.onlyCustomTab = true; // because we'll definitely forget
		//this.dominantColor = ChatColor.DARK_PURPLE;
		instance = this;
		saveDefaultConfig();

		setupMongo();

		for (World world : Bukkit.getWorlds()) {
			world.setGameRuleValue("doDaylightCycle", "false");
			world.setGameRuleValue("doMobSpawning", "false");
			world.setTime(6_000L);
		}



        /*
        Removed the PotPvp 'showmethedoor' message.
         */

//        AtomicInteger index = new AtomicInteger(0);
//        Bukkit.getScheduler().runTaskTimer(this, () -> {
//            FancyMessage message = new FancyMessage("TIP: ").color(ChatColor.GOLD);
//
//            if (index.get() == 0) {
//                message.then("Don't like the server? Knockback sucks? ").color(ChatColor.GRAY)
//                        .then("[Click Here]").color(ChatColor.GREEN).command("/showmethedoor").tooltip(ChatColor.GREEN + ":)");
//
//                index.set(0);
//            } else {
//                message.then("Pots too slow? Learn to pot or disconnect!").color(ChatColor.GRAY);
//
//                index.incrementAndGet();
//            }
//
//            for (Player player : Bukkit.getOnlinePlayers()) {
//                message.send(player);
//            }
//        }, 5 * 60 * 20L, 5 * 60 * 20L);

		settingHandler = new SettingHandler();
		duelHandler = new DuelHandler();
		kitHandler = new KitHandler();
		lobbyHandler = new LobbyHandler();
		arenaHandler = new ArenaHandler();
		matchHandler = new MatchHandler();
		partyHandler = new PartyHandler();
		queueHandler = new QueueHandler();
		rematchHandler = new RematchHandler();
		postMatchInvHandler = new PostMatchInvHandler();
		followHandler = new FollowHandler();
		eloHandler = new EloHandler();
		pvpClassHandler = new PvPClassHandler();
		hologramHandler = new HologramHandler();
		tournamentHandler = new TournamentHandler();

		new Morpheus(this); // qrakn game events
		new EventTask().runTaskTimerAsynchronously(this, 1L, 1L);

		for (GameEvent event : GameEvent.getEvents()) {
			for (Listener listener : event.getListeners()) {
				getServer().getPluginManager().registerEvents(listener, this);
			}
		}

		getServer().getPluginManager().registerEvents(new BasicPreventionListener(), this);
		getServer().getPluginManager().registerEvents(new BowHealthListener(), this);
		getServer().getPluginManager().registerEvents(new ChatFormatListener(), this);
		getServer().getPluginManager().registerEvents(new ChatToggleListener(), this);
		getServer().getPluginManager().registerEvents(new NightModeListener(), this);
		getServer().getPluginManager().registerEvents(new PearlCooldownListener(), this);
		getServer().getPluginManager().registerEvents(new RankedMatchQualificationListener(), this);
		getServer().getPluginManager().registerEvents(new TabCompleteListener(), this);
		getServer().getPluginManager().registerEvents((this.statisticsHandler = new StatisticsHandler()), this);
		getServer().getPluginManager().registerEvents(new GameListeners(), this);
		getServer().getPluginManager().registerEvents(new EventListeners(), this);

		registerCommands();

		Proton.getInstance().getCommandHandler().registerParameterType(KitType.class, new KitTypeParameterType());
		Proton.getInstance().getTabHandler().setLayoutProvider(new PotPvPLayoutProvider());
		FrozenNametagHandler.registerProvider(new PotPvPNametagProvider());
		Proton.getInstance().getScoreboardHandler().setConfiguration(PotPvPScoreboardConfiguration.create());

	}


	@Override
	public void onDisable() {
		for (Match match : this.matchHandler.getHostedMatches()) {
			if (match.getKitType().isBuildingAllowed()) match.getArena().restore();
		}

		GameQueue.INSTANCE.getCurrentGames().forEach(Game::end);

		try {
			arenaHandler.saveSchematics();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (String playerName : PvPClassHandler.getEquippedKits().keySet()) {
			PvPClassHandler.getEquippedKits().get(playerName).remove(getServer().getPlayerExact(playerName));
		}

		instance = null;
	}

	private void setupMongo() {
		mongoClient = new MongoClient(
				getConfig().getString("Mongo.Host"),
				getConfig().getInt("Mongo.Port")
		);

		String databaseId = getConfig().getString("Mongo.Database");
		mongoDatabase = mongoClient.getDatabase(databaseId);
	}

	// This is here because chunk snapshots are (still) being deserialized, and serialized sometimes.
	private static class ChunkSnapshotAdapter extends TypeAdapter<ChunkSnapshot> {

		@Override
		public ChunkSnapshot read(JsonReader arg0) throws IOException {
			return null;
		}

		@Override
		public void write(JsonWriter arg0, ChunkSnapshot arg1) throws IOException {

		}

	}

	public ArenaHandler getArenaHandler() {
		return arenaHandler;
	}

	public static Mars getInstance() {
		return instance;
	}

	private void registerCommands() {

		CommandHandler commandHandler = Proton.getInstance().getCommandHandler();

		commandHandler.registerAll(this);

	}
}