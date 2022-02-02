package rip.bridge.practice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import mkremins.fanciful.FancyMessage;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import rip.bridge.practice.arena.ArenaHandler;
import rip.bridge.practice.chat.ChatModeMap;
import rip.bridge.practice.duel.DuelHandler;
import rip.bridge.practice.elo.EloHandler;
import rip.bridge.practice.follow.FollowHandler;
import rip.bridge.practice.kit.KitHandler;
import rip.bridge.practice.kittype.KitType;
import rip.bridge.practice.kittype.KitTypeJsonAdapter;
import rip.bridge.practice.kittype.KitTypeParameterType;
import rip.bridge.practice.listener.*;
import rip.bridge.practice.lobby.LobbyHandler;
import rip.bridge.practice.match.Match;
import rip.bridge.practice.match.MatchHandler;
import rip.bridge.practice.nametag.PracticeNametagProvider;
import rip.bridge.practice.party.PartyHandler;
import rip.bridge.practice.postmatchinv.PostMatchInvHandler;
import rip.bridge.practice.pvpclasses.PvPClassHandler;
import rip.bridge.practice.queue.QueueHandler;
import rip.bridge.practice.rematch.RematchHandler;
import rip.bridge.practice.scoreboard.PracticeScoreboardConfiguration;
import rip.bridge.practice.setting.SettingHandler;
import rip.bridge.practice.statistics.StatisticsHandler;
import rip.bridge.practice.tab.PracticeLayoutProvider;
import rip.bridge.qlib.command.FrozenCommandHandler;
import rip.bridge.qlib.nametag.FrozenNametagHandler;
import rip.bridge.qlib.scoreboard.FrozenScoreboardHandler;
import rip.bridge.qlib.serialization.*;
import rip.bridge.qlib.tab.FrozenTabHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

public final class Practice extends JavaPlugin {

    public static com.google.gson.Gson plainGson;
    private static Practice instance;
    @Getter
    private static Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(PotionEffect.class, new PotionEffectAdapter())
            .registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter())
            .registerTypeHierarchyAdapter(Location.class, new LocationAdapter())
            .registerTypeHierarchyAdapter(Vector.class, new VectorAdapter())
            .registerTypeAdapter(BlockVector.class, new BlockVectorAdapter())
            .registerTypeHierarchyAdapter(KitType.class, new KitTypeJsonAdapter()) // custom KitType serializer
            .registerTypeAdapter(ChunkSnapshot.class, new ChunkSnapshotAdapter())
            .serializeNulls()
            .create();

    private MongoClient mongoClient;
    @Getter
    private MongoDatabase mongoDatabase;

    @Getter
    private SettingHandler settingHandler;
    @Getter
    private DuelHandler duelHandler;
    @Getter
    private KitHandler kitHandler;
    @Getter
    private LobbyHandler LobbyHandler;
    private ArenaHandler arenaHandler;
    @Getter
    private MatchHandler matchHandler;
    @Getter
    private PartyHandler partyHandler;
    @Getter
    private QueueHandler queueHandler;
    @Getter
    private RematchHandler rematchHandler;
    @Getter
    private PostMatchInvHandler postMatchInvHandler;
    @Getter
    private FollowHandler followHandler;
    @Getter
    private EloHandler eloHandler;
//    @Getter
//    private TournamentHandler tournamentHandler;
    @Getter
    private PvPClassHandler pvpClassHandler;

    @Getter private ChatModeMap chatModeMap;

    @Getter private String serverName;
    @Getter private String networkWebsite;
    @Getter
    private ChatColor dominantColor = ChatColor.GOLD;

    @Override
    public void onEnable() {
        instance = this;
        serverName = getConfig().getString("serverName");
        networkWebsite = getConfig().getString("networkWebsite");

        saveDefaultConfig();
        setupMongo();

        for (World world : Bukkit.getWorlds()) {
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("doMobSpawning", "false");
            world.setTime(6_000L);
        }
        AtomicInteger index = new AtomicInteger(0);
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            FancyMessage message = new FancyMessage("TIP: ").color(ChatColor.GOLD);

            if (index.get() == 0) {
                message.then("Don't like the server? Knockback sucks? ").color(ChatColor.GRAY)
                        .then("[Click Here]").color(ChatColor.GREEN).command("/showmethedoor").tooltip(ChatColor.GREEN + ":)");

                index.incrementAndGet();
            } else {
                message.then("Pots too slow? Learn to pot or disconnect!").color(ChatColor.GRAY);

                index.set(0);
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                message.send(player);
            }
        }, 5 * 60 * 20L, 5 * 60 * 20L);

        settingHandler = new SettingHandler();
        duelHandler = new DuelHandler();
        kitHandler = new KitHandler();
        LobbyHandler = new LobbyHandler();
        arenaHandler = new ArenaHandler();
        matchHandler = new MatchHandler();
        partyHandler = new PartyHandler();
        queueHandler = new QueueHandler();
        rematchHandler = new RematchHandler();
        postMatchInvHandler = new PostMatchInvHandler();
        followHandler = new FollowHandler();
        eloHandler = new EloHandler();
        pvpClassHandler = new PvPClassHandler();
//        tournamentHandler = new TournamentHandler();

        getServer().getPluginManager().registerEvents(new BasicPreventionListener(), this);
        getServer().getPluginManager().registerEvents(new BowHealthListener(), this);
        getServer().getPluginManager().registerEvents(new ChatFormatListener(), this);
        getServer().getPluginManager().registerEvents(new ChatToggleListener(), this);
        getServer().getPluginManager().registerEvents(new NightModeListener(), this);
        getServer().getPluginManager().registerEvents(new PearlCooldownListener(), this);
        getServer().getPluginManager().registerEvents(new RankedMatchQualificationListener(), this);
        getServer().getPluginManager().registerEvents(new TabCompleteListener(), this);
        getServer().getPluginManager().registerEvents(new StatisticsHandler(), this);

        FrozenCommandHandler.registerAll(this);
        FrozenCommandHandler.registerParameterType(KitType.class, new KitTypeParameterType());
        FrozenTabHandler.setLayoutProvider(new PracticeLayoutProvider());
        FrozenNametagHandler.registerProvider(new PracticeNametagProvider());
        FrozenScoreboardHandler.setConfiguration(PracticeScoreboardConfiguration.create());

    }

    @Override
    public void onDisable() {
        for (Match match : this.matchHandler.getHostedMatches()) {
            if (match.getKitType().isBuildingAllowed()) match.getArena().restore();
        }

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

    private void setupPersistence() {
        (chatModeMap = new ChatModeMap()).loadFromRedis();
    }

    private void setupMongo() {
        if(!getConfig().getBoolean("Mongo.Auth.Enabled")) {
            mongoClient = new MongoClient(
                    getConfig().getString("Mongo.Host"),
                    getConfig().getInt("Mongo.Port")
            );
        }else {
            mongoClient = new MongoClient(
                    new ServerAddress(getConfig().getString("Mongo.Host"), getConfig().getInt("Mongo.Port")),
                    Collections.singletonList(MongoCredential.createCredential(getConfig().getString("Mongo.Auth.Username"),
                            getConfig().getString("Mongo.Auth.Database"), getConfig().getString("Mongo.Auth.Password").toCharArray())));
        }

        String databaseId = getConfig().getString("Mongo.Database");
        mongoDatabase = mongoClient.getDatabase(databaseId);
    }

    // This is here because chunk snapshots are (still) being deserialized, and serialized sometimes.
    private static class ChunkSnapshotAdapter extends TypeAdapter<ChunkSnapshot> {

        @Override
        public ChunkSnapshot read(JsonReader arg0) {
            return null;
        }

        @Override
        public void write(JsonWriter arg0, ChunkSnapshot arg1) {

        }
    }

    private void registerPersistence() {

    }

    public ArenaHandler getArenaHandler() {
        return arenaHandler;
    }

    public static Practice getInstance() {
        return instance;
    }
}