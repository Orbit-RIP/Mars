package rip.orbit.mars.tournament;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.Setter;
import rip.orbit.mars.MarsLang;
import rip.orbit.mars.Mars;
import rip.orbit.mars.kittype.KitType;
import rip.orbit.mars.match.Match;
import rip.orbit.mars.match.MatchState;
import rip.orbit.mars.match.MatchTeam;
import rip.orbit.mars.party.Party;
import cc.fyre.proton.Proton;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import cc.fyre.proton.event.HalfHourEvent;
import cc.fyre.proton.util.UUIDUtils;

public class TournamentHandler implements Listener {

    @Getter @Setter private Tournament tournament = null;
    private static TournamentHandler instance;

    public TournamentHandler() {
        instance = this;
        Proton.getInstance().getCommandHandler().registerClass(this.getClass());
        Bukkit.getPluginManager().registerEvents(this, Mars.getInstance());
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Mars.getInstance(), () -> {
            if (tournament != null) tournament.check();
        }, 20L, 20L);

        populateTournamentStatuses();
    }

    public boolean isInTournament(Party party) {
        return tournament != null && tournament.isInTournament(party);
    }

    public boolean isInTournament(Match match) {
        return tournament != null && tournament.getMatches().contains(match);
    }

    @Command(names = { "tournament start" }, permission = "tournament.create")
    public static void tournamentCreate(CommandSender sender, @Parameter(name = "kit-type") KitType type, @Parameter(name = "teamSize") int teamSize, @Parameter(name = "requiredTeams") int requiredTeams) {
        if (instance.getTournament() != null) {
            sender.sendMessage(ChatColor.RED + "There's already an ongoing tournament!");
            return;
        }

        if (type == null) {
            sender.sendMessage(ChatColor.RED + "Kit type not found!");
            return;
        }

        if (teamSize < 1 || 10 < teamSize) {
            sender.sendMessage(ChatColor.RED + "Invalid team size range. Acceptable inputs: 1 -> 10");
            return;
        }

        if (requiredTeams < 4) {
            sender.sendMessage(ChatColor.RED + "Required teams must be at least 4.");
            return;
        }

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7A &c&ltournament&7 has started. Type &5/join&7 to play. (0/" + (teamSize < 3 ? teamSize * requiredTeams : requiredTeams) + ")"));
        Bukkit.broadcastMessage("");

        Tournament tournament;
        instance.setTournament(tournament = new Tournament(type, teamSize, requiredTeams));

        new BukkitRunnable() {
            @Override
            public void run() {
                if (instance.getTournament() == tournament) {
                    tournament.broadcastJoinMessage();
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(Mars.getInstance(), 60 * 20, 60 * 20);
    }

    @Command(names = { "tournament join", "join", "jointournament" }, permission = "")
    public static void tournamentJoin(Player sender) {

        if (instance.getTournament() == null) {
            sender.sendMessage(ChatColor.RED + "There is no running tournament to join.");
            return;
        }

        int tournamentTeamSize = instance.getTournament().getRequiredPartySize();

        if ((instance.getTournament().getCurrentRound() != -1 || instance.getTournament().getBeginNextRoundIn() != 31) && (instance.getTournament().getCurrentRound() != 0 || !sender.hasPermission("tournaments.joinduringcountdown"))) {
            sender.sendMessage(ChatColor.RED + "This tournament is already in progress.");
            return;
        }

        Party senderParty = Mars.getInstance().getPartyHandler().getParty(sender);
        if (senderParty == null) {
            if (tournamentTeamSize == 1) {
                senderParty = Mars.getInstance().getPartyHandler().getOrCreateParty(sender); // Will auto put them in a party
            } else {
                sender.sendMessage(ChatColor.RED + "You don't have a team to join the tournament with!");
                return;
            }
        }

        int notInLobby = 0;
        int queued = 0;
        for (UUID member : senderParty.getMembers()) {
            if (!Mars.getInstance().getLobbyHandler().isInLobby(Bukkit.getPlayer(member))) {
                notInLobby++;
            }

            if (Mars.getInstance().getQueueHandler().getQueueEntry(member) != null) {
                queued++;
            }
        }

        if (notInLobby != 0) {
            sender.sendMessage(ChatColor.RED.toString() + notInLobby + "member" + (notInLobby == 1 ? "" : "s") + " of your team aren't in the lobby.");
            return;
        }

        if (queued != 0) {
            sender.sendMessage(ChatColor.RED.toString() + notInLobby + "member" + (notInLobby == 1 ? "" : "s") + " of your team are currently queued.");
            return;
        }

        if (!senderParty.getLeader().equals(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "You must be the leader of your team to join the tournament.");
            return;
        }

        if (instance.isInTournament(senderParty)) {
            sender.sendMessage(ChatColor.RED + "Your team is already in the tournament!");
            return;
        }

        if (senderParty.getMembers().size() != instance.getTournament().getRequiredPartySize()) {
            sender.sendMessage(ChatColor.RED + "You need exactly " + instance.getTournament().getRequiredPartySize() + " members in your party to join the tournament.");
            return;
        }

        if (Mars.getInstance().getQueueHandler().getQueueEntry(senderParty) != null) {
            sender.sendMessage(ChatColor.RED + "You can't join the tournament if your party is currently queued.");
            return;
        }

        senderParty.message(ChatColor.GREEN + "Joined the tournament.");
        instance.getTournament().addParty(senderParty);
    }

    @Command(names = { "tournament status", "tstatus", "status" }, permission = "")
    public static void tournamentStatus(CommandSender sender) {
        if (instance.getTournament() == null) {
            sender.sendMessage(ChatColor.RED + "There is no ongoing tournament to get the status of.");
            return;
        }

        sender.sendMessage(MarsLang.LONG_LINE);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Live &cTournament &7Fights"));
        sender.sendMessage("");
        List<Match> ongoingMatches = instance.getTournament().getMatches().stream().filter(m -> m.getState() != MatchState.TERMINATED).collect(Collectors.toList());

        for (Match match : ongoingMatches) {
            MatchTeam firstTeam = match.getTeams().get(0);
            MatchTeam secondTeam = match.getTeams().get(1);

            if (firstTeam.getAllMembers().size() == 1) {
                sender.sendMessage("  " + ChatColor.GRAY + "» " + ChatColor.LIGHT_PURPLE + UUIDUtils.name(firstTeam.getFirstMember()) + ChatColor.GRAY + " vs " + ChatColor.LIGHT_PURPLE + UUIDUtils.name(secondTeam.getFirstMember()));
            } else {
                sender.sendMessage("  " + ChatColor.GRAY + "» " + ChatColor.LIGHT_PURPLE + UUIDUtils.name(firstTeam.getFirstMember()) + ChatColor.GRAY + "'s team vs " + ChatColor.LIGHT_PURPLE + UUIDUtils.name(secondTeam.getFirstMember()) + ChatColor.GRAY + "'s team");
            }
        }
        sender.sendMessage(MarsLang.LONG_LINE);
    }

    @Command(names = { "tournament cancel", "tcancel"},  permission = "op")
    public static void tournamentCancel(CommandSender sender) {
        if (instance.getTournament() == null) {
            sender.sendMessage(ChatColor.RED + "There is no running tournament to cancel.");
            return;
        }

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7The &c&ltournament&7 was &ccancelled."));
        Bukkit.broadcastMessage("");
        instance.setTournament(null);
    }

    @Command(names = { "tournament forcestart"}, permission = "op")
    public static void tournamentForceStart(CommandSender sender) {
        if (instance.getTournament() == null) {
            sender.sendMessage(ChatColor.RED + "There is no tournament to force start.");
            return;
        }

        if (instance.getTournament().getCurrentRound() != -1 || instance.getTournament().getBeginNextRoundIn() != 31) {
            sender.sendMessage(ChatColor.RED + "This tournament is already in progress.");
            return;
        }

        instance.getTournament().start();
        sender.sendMessage(ChatColor.GREEN + "Force started tournament.");
    }


    private static List<TournamentStatus> allStatuses = Lists.newArrayList();

    private void populateTournamentStatuses() {
        List<KitType> viewableKits = KitType.getAllTypes().stream().filter(kit -> !kit.isHidden()).collect(Collectors.toList());
        allStatuses.add(new TournamentStatus(0, ImmutableList.of(1), ImmutableList.of(16, 32), viewableKits));
        allStatuses.add(new TournamentStatus(250, ImmutableList.of(1), ImmutableList.of(32), viewableKits));
        allStatuses.add(new TournamentStatus(300, ImmutableList.of(1), ImmutableList.of(48, 64), ImmutableList.of(KitType.byId("NODEBUFF"))));
        allStatuses.add(new TournamentStatus(400, ImmutableList.of(1), ImmutableList.of(64), ImmutableList.of(KitType.byId("NODEBUFF"))));
        allStatuses.add(new TournamentStatus(500, ImmutableList.of(1), ImmutableList.of(128), ImmutableList.of(KitType.byId("NODEBUFF"))));
        allStatuses.add(new TournamentStatus(600, ImmutableList.of(1), ImmutableList.of(128), ImmutableList.of(KitType.byId("NODEBUFF"))));
        allStatuses.add(new TournamentStatus(700, ImmutableList.of(1), ImmutableList.of(128), ImmutableList.of(KitType.byId("NODEBUFF"))));
        allStatuses.add(new TournamentStatus(800, ImmutableList.of(1), ImmutableList.of(128), ImmutableList.of(KitType.byId("NODEBUFF"))));
    }

    @EventHandler
    public void onHalfHour(HalfHourEvent event) {
        if (instance.getTournament() != null) return; // already a tournament in progress

        TournamentStatus status = TournamentStatus.forPlayerCount(Bukkit.getOnlinePlayers().size());

        int teamSize = status.getTeamSizes().get(Proton.RANDOM.nextInt(status.getTeamSizes().size()));
        int teamCount = status.getTeamCounts().get(Proton.RANDOM.nextInt(status.getTeamCounts().size()));
        KitType kitType = status.getKitTypes().get(Proton.RANDOM.nextInt(status.getKitTypes().size()));

        Tournament tournament;
        instance.setTournament(tournament = new Tournament(kitType, teamSize, teamCount));

        Bukkit.getScheduler().runTaskLater(Mars.getInstance(), () -> {
            if (tournament == instance.getTournament() && instance.getTournament() != null && instance.getTournament().getCurrentRound() == -1) {
                instance.getTournament().start();
            }
        }, 3 * 20 * 60);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (instance.getTournament() == tournament) {
                    tournament.broadcastJoinMessage();
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(Mars.getInstance(), 60 * 20, 60 * 20);
    }

    @Getter
    private static class TournamentStatus {
        private int minimumPlayerCount;
        private List<Integer> teamSizes;
        private List<Integer> teamCounts;
        private List<KitType> kitTypes;

        public TournamentStatus(int minimumPlayerCount, List<Integer> teamSizes, List<Integer> teamCounts, List<KitType> kitTypes) {
            this.minimumPlayerCount = minimumPlayerCount;
            this.teamSizes = teamSizes;
            this.teamCounts = teamCounts;
            this.kitTypes = kitTypes;
        }

        public static TournamentStatus forPlayerCount(int playerCount) {
            for (int i = allStatuses.size() - 1; 0 <= i; i--) {
                if (allStatuses.get(i).minimumPlayerCount <= playerCount) return allStatuses.get(i);
            }


            throw new IllegalArgumentException("No suitable sizes found!");
        }
    }
}