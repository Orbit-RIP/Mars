
package rip.orbit.mars.scoreboard;

import cc.fyre.proton.Proton;
import rip.orbit.mars.Mars;
import rip.orbit.mars.elo.EloHandler;
import rip.orbit.mars.match.MatchHandler;
import rip.orbit.mars.party.Party;
import rip.orbit.mars.party.PartyHandler;
import rip.orbit.mars.queue.MatchQueue;
import rip.orbit.mars.queue.MatchQueueEntry;
import rip.orbit.mars.queue.QueueHandler;
import cc.fyre.proton.autoreboot.AutoRebootHandler;
import cc.fyre.proton.util.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.mars.tournament.Tournament;
import rip.orbit.nebula.Nebula;
import rip.orbit.nebula.timer.Timer;
import rip.orbit.nebula.timer.TimerHandler;

import java.util.LinkedList;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;

final class LobbyScoreGetter implements BiConsumer<Player, LinkedList<String>> {

    private int LAST_ONLINE_COUNT = 0;
    private int LAST_IN_FIGHTS_COUNT = 0;
    private int LAST_IN_QUEUES_COUNT = 0;

    private long lastUpdated = System.currentTimeMillis();

    @Override
    public void accept(Player player, LinkedList<String> scores) {
        Optional<UUID> followingOpt = Mars.getInstance().getFollowHandler().getFollowing(player);
        MatchHandler matchHandler = Mars.getInstance().getMatchHandler();
        PartyHandler partyHandler = Mars.getInstance().getPartyHandler();
        QueueHandler queueHandler = Mars.getInstance().getQueueHandler();
        EloHandler eloHandler = Mars.getInstance().getEloHandler();

        scores.add("&6&l┃ &fOnline: &6" + LAST_ONLINE_COUNT);
        scores.add("&6&l┃ &fIn Queues: &6" + LAST_IN_QUEUES_COUNT);
        scores.add("&6&l┃ &fIn Fights: &6" + LAST_IN_FIGHTS_COUNT);

        TimerHandler timerHandler = Nebula.getInstance().getTimerHandler();
        int i = 0;
        for (Timer timer : timerHandler.getTimers()) {
            if (timer.getTimeLeft() > 0) {
                if (i == 1) {
                    scores.add(" ");
                }
                scores.add("&6&l┃ " + timer.getDisplay() + ": &6" + TimeUtils.formatLongIntoHHMMSS(timer.getTimeLeft() / 1000));
                ++i;
            }
        }

        Party playerParty = partyHandler.getParty(player);
        if (playerParty != null) {
            int size = playerParty.getMembers().size();
            scores.add(" ");
            scores.add("&6&l┃ &fYour Team: &f" + size);
        }

        if (2500 <= System.currentTimeMillis() - lastUpdated) {
            lastUpdated = System.currentTimeMillis();
            LAST_ONLINE_COUNT = Bukkit.getOnlinePlayers().size();
            LAST_IN_FIGHTS_COUNT = matchHandler.countPlayersPlayingInProgressMatches();
            LAST_IN_QUEUES_COUNT = queueHandler.getQueuedCount();
        }

        if (followingOpt.isPresent()) {
            Player following = Bukkit.getPlayer(followingOpt.get());
            scores.add(" ");
            scores.add("&fFollowing: *&6" + following.getName());

            if (player.hasPermission("orbit.staff")) {
                MatchQueueEntry targetEntry = getQueueEntry(following);

                if (targetEntry != null) {
                    MatchQueue queue = targetEntry.getQueue();

                    scores.add("&6&l┃ &fQueued: &6" + (queue.isRanked() ? "(R)" : "(UR)") + " " + queue.getKitType().getDisplayName());
//                    scores.add("&7" + (queue.isRanked() ? "Ranked" : "Unranked") + " " + queue.getKitType().getDisplayName());
                }
            }
        }

        MatchQueueEntry entry = getQueueEntry(player);
        Tournament tournament = Mars.getInstance().getTournamentHandler().getTournament();
        if (entry != null) {
            String waitTimeFormatted = TimeUtils.formatIntoMMSS(entry.getWaitSeconds());
            MatchQueue queue = entry.getQueue();

            scores.add("&b&7&m--------------------");
            scores.add("&6" + (queue.isRanked() ? "Ranked" : "Unranked") + " " + queue.getKitType().getDisplayName());
            scores.add("&6&l┃ &fTime: *&6" + waitTimeFormatted);

            if (queue.isRanked()) {
                if (tournament != null) {
                    int elo = eloHandler.getElo(entry.getMembers(), queue.getKitType());
                    int window = entry.getWaitSeconds() * QueueHandler.RANKED_WINDOW_GROWTH_PER_SECOND;

                    scores.add("&6&l┃ &fSearch range: *&6" + Math.max(0, elo - window) + " - " + (elo + window));
                }
            }
        }

        if (Proton.getInstance().getAutoRebootHandler().isRebooting()) {
            String secondsStr = TimeUtils.formatIntoMMSS(Proton.getInstance().getAutoRebootHandler().getRebootSecondsRemaining());
            scores.add("&6&l┃ &fRebooting&7: &6" + secondsStr);
        }

        if (player.hasMetadata("ModMode")) {
            scores.add(ChatColor.GRAY.toString() + ChatColor.BOLD + "In Silent Mode");
        }

        if (tournament != null) {
            scores.add("&7&m--------------------");
            scores.add("&6&lTournament");
            if (tournament.getStage() == Tournament.TournamentStage.WAITING_FOR_TEAMS) {
                int teamSize = tournament.getRequiredPartySize();
                scores.add("&6&l┃ &fKit&7: &6" + tournament.getType().getDisplayName());
                scores.add("&6&l┃ &fTeam Size&7: &6" + teamSize + "v" + teamSize);
                int multiplier = teamSize < 3 ? teamSize : 1;
                scores.add("&6&l┃ &f" + (teamSize < 3 ? "Players"  : "Teams") + "&7: &6" + (tournament.getActiveParties().size() * multiplier + "/" + tournament.getRequiredPartiesToStart() * multiplier));
            } else if (tournament.getStage() == Tournament.TournamentStage.COUNTDOWN) {
                if (tournament.getCurrentRound() == 0) {
                    scores.add("&6&l┃ &fBegins in &6" + tournament.getBeginNextRoundIn() + "&f second" + (tournament.getBeginNextRoundIn() == 1 ? "." : "s."));
                } else {
                    scores.add("&6&lRound " + (tournament.getCurrentRound() + 1));
                    scores.add("&6&l┃ &fBegins in &6" + tournament.getBeginNextRoundIn() + "&f second" + (tournament.getBeginNextRoundIn() == 1 ? "." : "s."));
                }
            } else if (tournament.getStage() == Tournament.TournamentStage.IN_PROGRESS) {
                scores.add("&6&l┃ &fRound&7: &6" + tournament.getCurrentRound());

                int teamSize = tournament.getRequiredPartySize();
                int multiplier = teamSize < 3 ? teamSize : 1;

                scores.add("&6&l┃ &f" + (teamSize < 3 ? "Players" : "Teams") + "&7: &6" + tournament.getActiveParties().size() * multiplier + "/" + tournament.getRequiredPartiesToStart() * multiplier);
                scores.add("&6&l┃ &fDuration&7: &6" + TimeUtils.formatIntoMMSS((int) (System.currentTimeMillis() - tournament.getRoundStartedAt()) / 1000));
            }
        }


    }

    private MatchQueueEntry getQueueEntry(Player player) {
        PartyHandler partyHandler = Mars.getInstance().getPartyHandler();
        QueueHandler queueHandler = Mars.getInstance().getQueueHandler();

        Party playerParty = partyHandler.getParty(player);
        if (playerParty != null) {
            return queueHandler.getQueueEntry(playerParty);
        } else {
            return queueHandler.getQueueEntry(player.getUniqueId());
        }
    }

}