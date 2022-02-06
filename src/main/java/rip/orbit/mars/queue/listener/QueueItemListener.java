package rip.orbit.mars.queue.listener;

import cc.fyre.proton.Proton;
import com.google.common.collect.ImmutableList;

import rip.orbit.mars.Mars;
import rip.orbit.mars.kittype.KitType;
import rip.orbit.mars.kittype.menu.select.CustomSelectKitTypeMenu;
import rip.orbit.mars.listener.RankedMatchQualificationListener;
import rip.orbit.mars.match.MatchHandler;
import rip.orbit.mars.party.Party;
import rip.orbit.mars.queue.QueueHandler;
import rip.orbit.mars.queue.QueueItems;
import rip.orbit.mars.util.ItemListener;
import rip.orbit.mars.validation.PotPvPValidation;
import cc.fyre.proton.autoreboot.AutoRebootHandler;
import cc.fyre.proton.util.UUIDUtils;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

// This class followes a different organizational style from other item listeners
// because we need seperate listeners for ranked/unranked, we have methods which
// we call which generate a Consumer<Player> designed for either ranked/unranked,
// based on the argument passed. Returning Consumers makes this code slightly
// harder to follow, but saves us from a lot of duplication
public final class QueueItemListener extends ItemListener {

    private final Function<KitType, CustomSelectKitTypeMenu.CustomKitTypeMeta> selectionAdditionRanked = selectionMenuAddition(true);
    private final Function<KitType, CustomSelectKitTypeMenu.CustomKitTypeMeta> selectionAdditionUnranked = selectionMenuAddition(false);
    private final QueueHandler queueHandler;

    public QueueItemListener(QueueHandler queueHandler) {
        this.queueHandler = queueHandler;

        addHandler(QueueItems.JOIN_SOLO_UNRANKED_QUEUE_ITEM, joinSoloConsumer(false));
        addHandler(QueueItems.JOIN_SOLO_RANKED_QUEUE_ITEM, joinSoloConsumer(true));

        addHandler(QueueItems.JOIN_PARTY_UNRANKED_QUEUE_ITEM, joinPartyConsumer(false));
        addHandler(QueueItems.JOIN_PARTY_RANKED_QUEUE_ITEM, joinPartyConsumer(true));

        addHandler(QueueItems.LEAVE_SOLO_UNRANKED_QUEUE_ITEM, p -> queueHandler.leaveQueue(p, false));
        addHandler(QueueItems.LEAVE_SOLO_RANKED_QUEUE_ITEM, p -> queueHandler.leaveQueue(p, false));

        Consumer<Player> leaveQueuePartyConsumer = player -> {
            Party party = Mars.getInstance().getPartyHandler().getParty(player);

            // don't message, players who aren't leader shouldn't even get this item
            if (party != null && party.isLeader(player.getUniqueId())) {
                queueHandler.leaveQueue(party, false);
            }
        };

        addHandler(QueueItems.LEAVE_PARTY_UNRANKED_QUEUE_ITEM, leaveQueuePartyConsumer);
        addHandler(QueueItems.LEAVE_PARTY_RANKED_QUEUE_ITEM, leaveQueuePartyConsumer);
    }

    private Consumer<Player> joinSoloConsumer(boolean ranked) {
        return player -> {
            if (ranked) {
                if (rebootSoon()) {
                    player.sendMessage(ChatColor.RED + "You can't join ranked queues with a reboot scheduled soon.");
                    return;
                }

                if (!RankedMatchQualificationListener.isQualified(player.getUniqueId())) {
                    int needed = RankedMatchQualificationListener.getWinsNeededToQualify(player.getUniqueId());
                    player.sendMessage(ChatColor.RED + "You can't join ranked queues with less than " + RankedMatchQualificationListener.MIN_MATCH_WINS + " unranked 1v1 wins. You need " + needed + " more wins!");
                    return;
                }
            }

            if (PotPvPValidation.canJoinQueue(player)) {
                new CustomSelectKitTypeMenu(kitType -> {
                    queueHandler.joinQueue(player, kitType, ranked);
                    player.closeInventory();
                }, ranked ? selectionAdditionRanked : selectionAdditionUnranked, "Join " + (ranked ? "Ranked" : "Unranked") + " Queue...", ranked).openMenu(player);
            }
        };
    }

    private Consumer<Player> joinPartyConsumer(boolean ranked) {
        return player -> {
            Party party = Mars.getInstance().getPartyHandler().getParty(player);

            // just fail silently, players who aren't a leader
            // of a party shouldn't even have this item
            if (party == null || !party.isLeader(player.getUniqueId())) {
                return;
            }

            if (ranked) {
                if (rebootSoon()) {
                    player.sendMessage(ChatColor.RED + "You can't join ranked queues with a reboot scheduled soon.");
                    return;
                }

                for (UUID member : party.getMembers()) {
                    if (!RankedMatchQualificationListener.isQualified(member)) {
                        int needed = RankedMatchQualificationListener.getWinsNeededToQualify(member);
                        player.sendMessage(ChatColor.RED + "Your party can't join ranked queues because " + UUIDUtils.name(member) + " has less than " + RankedMatchQualificationListener.MIN_MATCH_WINS + " unranked 1v1 wins. They need " + needed + " more wins!");
                        return;
                    }
                }
            }

            // try to check validation issues in advance
            // (will be called again in QueueHandler#joinQueue)
            if (PotPvPValidation.canJoinQueue(party)) {
                new CustomSelectKitTypeMenu(kitType -> {
                    queueHandler.joinQueue(party, kitType, ranked);
                    player.closeInventory();
                }, ranked ? selectionAdditionRanked : selectionAdditionUnranked, "Play " + (ranked ? "Ranked" : "Unranked"), ranked).openMenu(player);
            }
        };
    }

    private Function<KitType, CustomSelectKitTypeMenu.CustomKitTypeMeta> selectionMenuAddition(boolean ranked) {
        return kitType -> {
            MatchHandler matchHandler = Mars.getInstance().getMatchHandler();

            int inFightsRanked = matchHandler.countPlayersPlayingMatches(m -> m.getKitType() == kitType && m.isRanked());
            int inQueueRanked = queueHandler.countPlayersQueued(kitType, true);

            int inFightsUnranked = matchHandler.countPlayersPlayingMatches(m -> m.getKitType() == kitType && !m.isRanked());
            int inQueueUnranked = queueHandler.countPlayersQueued(kitType, false);

            return new CustomSelectKitTypeMenu.CustomKitTypeMeta(
                // clamp value to >= 1 && <= 64
                Math.max(1, Math.min(64, ranked ? inQueueRanked + inFightsRanked : inQueueUnranked + inFightsUnranked)),
                ranked ?  ImmutableList.of(
                        ChatColor.RED + "Fighting" + ChatColor.GRAY + ": " + ChatColor.WHITE + inFightsRanked,
                        ChatColor.RED + "Queueing" + ChatColor.GRAY + ": " + ChatColor.WHITE + inQueueRanked) :
                ImmutableList.of(
                    ChatColor.RED + "Fighting" + ChatColor.GRAY + ": " + ChatColor.WHITE + inFightsUnranked,
                    ChatColor.RED + "Queueing" + ChatColor.GRAY + ": " + ChatColor.WHITE + inQueueUnranked
                )
            );
        };
    }

    private boolean rebootSoon() {
        return Proton.getInstance().getAutoRebootHandler().isRebooting() && Proton.getInstance().getAutoRebootHandler().getRebootSecondsRemaining() <= TimeUnit.MINUTES.toSeconds(5);
    }

}