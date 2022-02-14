package rip.orbit.mars.nametag.nms;

import com.lunarclient.bukkitapi.LunarClientAPI;
import com.qrakn.morpheus.game.Game;
import com.qrakn.morpheus.game.GameQueue;
import com.qrakn.morpheus.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;
import rip.orbit.mars.Mars;
import rip.orbit.mars.match.Match;
import rip.orbit.mars.match.MatchTeam;
import rip.orbit.mars.nametag.PlayerScoreboard;
import rip.orbit.mars.nametag.ScoreboardInput;
import rip.orbit.mars.nametag.base.ScoreboardBase_1_7;
import rip.orbit.mars.nametag.util.NmsUtils;
import rip.orbit.mars.nametag.util.Tasks;
import rip.orbit.mars.pvpclasses.pvpclasses.ArcherClass;
import rip.orbit.nebula.util.CC;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerScoreboard_1_7 extends ScoreboardBase_1_7 implements PlayerScoreboard {

	private static final String SB_LINE = ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "------";
	private static final ScoreboardInput EMPTY_INPUT = new ScoreboardInput("", "", "");

	private final Deque<ScoreboardInput> entries;
	private Set<String> lastEntries;

	private final ScoreboardInput[] entryCache;

	private final AtomicBoolean update;
	private final AtomicBoolean lastLine;

	private Team members;
	private Team lobby;
	private Team following;
	private Team archers;
	private Team enemies;
	private Team spectators;
	private Team invis;

	public PlayerScoreboard_1_7(Player player) {
		super(player, NmsUtils.getInstance().getPlayerScoreboard(player));

		this.entries = new ArrayDeque<>();
		this.lastEntries = new HashSet<>();

		this.entryCache = new ScoreboardInput[15];

		for (int i = 0; i < 15; i++) {
			this.entryCache[i] = EMPTY_INPUT;
		}

		this.setupTeams();

		this.update = new AtomicBoolean(false);
		this.lastLine = new AtomicBoolean(false);

		player.setScoreboard(this.scoreboard);

	}

	@Override
	public void unregister() {
		synchronized (this.scoreboard) {
			for (Objective objective : this.scoreboard.getObjectives()) {
				objective.unregister();
			}

			for (Team team : this.scoreboard.getTeams()) {
				team.unregister();
			}
		}

		this.player = null;
	}

	private void setupTeams() {
		this.members = this.getTeam(CC.translate("members"));
		this.members.setPrefix(ChatColor.GREEN.toString());
		this.members.setCanSeeFriendlyInvisibles(true);

		this.following = this.getTeam(CC.translate("following"));
		this.following.setPrefix(ChatColor.AQUA.toString());

		this.lobby = this.getTeam(CC.translate("lobby"));
		this.lobby.setPrefix(ChatColor.GREEN.toString());
		this.lobby.setCanSeeFriendlyInvisibles(false);

		this.enemies = this.getTeam(CC.translate("enemies"));
		this.enemies.setPrefix(ChatColor.RED.toString());
		this.enemies.setCanSeeFriendlyInvisibles(false);

		this.spectators = this.getTeam(CC.translate("spectators"));
		this.spectators.setPrefix(ChatColor.GRAY.toString());
		this.spectators.setCanSeeFriendlyInvisibles(true);

		this.archers = this.getTeam(CC.translate("archers"));
		this.archers.setPrefix(ChatColor.YELLOW.toString());

		if ((this.invis = this.scoreboard.getTeam(CC.translate("inviss"))) == null) {
			try {
				Method method = this.scoreboard.getClass().getDeclaredMethod("registerNewTeam", String.class, boolean.class);
				method.setAccessible(true);

				this.invis = (Team) method.invoke(this.scoreboard, CC.translate("invis"), false);
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
			}
		}
	}

	@Override
	public void update() {
		if (!this.update.get() && this.lastEntries.isEmpty()) return;

		Set<String> addedEntries = new HashSet<>(this.entries.size());

		for (int i = this.entries.size(); i > 0; i--) {
			ScoreboardInput input = this.entries.pollFirst();
			if (input == null) return;

			addedEntries.add(input.getName());

			if (this.entryCache[i - 1].equals(input)) {
				continue;
			}

			Team team = this.getTeam(input.getName());

			if (!team.hasEntry(input.getName())) {
				team.addEntry(input.getName());
			}

			this.updateTeam(team.getName(), input.getPrefix(), input.getSuffix());

			this.entryCache[i - 1] = input;
		}

		if (addedEntries.size() < this.lastEntries.size()) {
			for (int i = addedEntries.size(); i < this.lastEntries.size(); i++) {
				this.entryCache[i] = EMPTY_INPUT;
			}
		}

		this.lastEntries = addedEntries;
		this.update.set(false);
	}

	@Override
	public boolean add(String value, String time) {
		if (value.isEmpty() || this.entries.size() >= 16) return false;

		if (time.length() > 16) {
			time = time.substring(0, 16);
		}

		if (value.length() <= 16) {
			this.entries.addLast(new ScoreboardInput("", value, time));
		} else if (value.length() <= 32) {
			this.entries.addLast(new ScoreboardInput(value.substring(0,
					value.length() - 16), value.substring(value.length() - 16), time));
		} else {
			this.entries.addLast(new ScoreboardInput(value.substring(0, 16),
					value.substring(16, 32), time));
		}

		this.lastLine.set(false);
		return true;
	}

	private Team getTeam(String name) {
		synchronized (this.scoreboard) {
			Team team = this.scoreboard.getTeam(name);
			return team == null ? this.scoreboard.registerNewTeam(name) : team;
		}
	}

	@Override
	public void setUpdate(boolean value) {
		this.update.set(value);
	}

	@Override
	public boolean isEmpty() {
		return this.entries.isEmpty();
	}

	@Override
	public void clear() {
		this.entries.clear();
	}

	@Override
	public void updateTabRelations(Iterable<? extends Player> players, boolean lunarOnly) {
		if (Thread.currentThread() == NmsUtils.getInstance().getMainThread()) {
			Tasks.async(() -> this.updateAllTabRelations(players));
		} else {
			this.updateAllTabRelations(players);
		}
	}

	private void updateAllTabRelations(Iterable<? extends Player> players) {
		if (this.player == null || this.scoreboard == null) return;

		Match match = Mars.getInstance().getMatchHandler().getMatchPlayingOrSpectating(player);

		if (match == null) {
			this.addAndUpdate(player, this.lobby);
		}

		for (Player online : players) {

			if (Mars.getInstance().getFollowHandler().getFollowers(online).contains(player.getUniqueId())) {
				this.addAndUpdate(online, this.following);
				continue;
			}

			if (match == null) {
				this.addAndUpdate(online, this.members);
				continue;
			}

			MatchTeam team = match.getTeam(player.getUniqueId());

			boolean isMemberOrAlly = (team != null && team.getAllMembers().contains(online.getUniqueId()));

			Game game = GameQueue.INSTANCE.getCurrentGame(online);

			if (this.invis != null && online.hasPotionEffect(PotionEffectType.INVISIBILITY) && !isMemberOrAlly) {
				this.addAndUpdate(online, this.invis);
			} else if (isMemberOrAlly) {
				this.addAndUpdate(online, this.members);
			} else if (ArcherClass.isMarked(online)) {
				this.addAndUpdate(online, this.archers);
			} else if (game != null && game.getPlayers().contains(player) && game.getPlayers().contains(online) && game.getState() != GameState.ENDED) {
				if (game.getSpectators().contains(player)) {
					this.addAndUpdate(online, this.spectators);
				}
			} else {
				this.addAndUpdate(online, this.enemies);
			}

		}

	}

	private void addAndUpdate(Player online, Team team) {
		team.addEntry(online.getName());
	}
}