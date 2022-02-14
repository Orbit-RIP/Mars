package rip.orbit.mars.queue;

import cc.fyre.proton.util.ItemUtils;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lombok.experimental.UtilityClass;
import rip.orbit.nebula.util.CC;

import static org.bukkit.ChatColor.*;
import static rip.orbit.mars.MarsLang.LEFT_ARROW;
import static rip.orbit.mars.MarsLang.RIGHT_ARROW;

@UtilityClass
public final class QueueItems {

    public static final ItemStack JOIN_SOLO_UNRANKED_QUEUE_ITEM = new ItemStack(Material.IRON_SWORD);
    public static final ItemStack LEAVE_SOLO_UNRANKED_QUEUE_ITEM = new ItemStack(Material.INK_SACK, 1, (byte) DyeColor.RED.getDyeData());

    public static final ItemStack JOIN_SOLO_RANKED_QUEUE_ITEM = new ItemStack(Material.DIAMOND_SWORD);
    public static final ItemStack LEAVE_SOLO_RANKED_QUEUE_ITEM = new ItemStack(Material.INK_SACK, 1, (byte) DyeColor.RED.getDyeData());

    public static final ItemStack JOIN_PARTY_UNRANKED_QUEUE_ITEM = new ItemStack(Material.IRON_SWORD);
    public static final ItemStack LEAVE_PARTY_UNRANKED_QUEUE_ITEM = new ItemStack(Material.ARROW);

    public static final ItemStack JOIN_PARTY_RANKED_QUEUE_ITEM = new ItemStack(Material.DIAMOND_SWORD);
    public static final ItemStack LEAVE_PARTY_RANKED_QUEUE_ITEM = new ItemStack(Material.ARROW);

    static {
        ItemUtils.setDisplayName(JOIN_SOLO_UNRANKED_QUEUE_ITEM, LEFT_ARROW + GOLD + "Play Unranked" + RIGHT_ARROW);
        ItemUtils.setDisplayName(LEAVE_SOLO_UNRANKED_QUEUE_ITEM, LEFT_ARROW + RED + "Leave Unranked Queue" + RIGHT_ARROW);

        ItemUtils.setDisplayName(JOIN_SOLO_RANKED_QUEUE_ITEM, LEFT_ARROW + GOLD + "Play Ranked" + RIGHT_ARROW);
        ItemUtils.setDisplayName(LEAVE_SOLO_RANKED_QUEUE_ITEM, LEFT_ARROW + RED + "Leave Ranked Queue" + RIGHT_ARROW);

        ItemUtils.setDisplayName(JOIN_PARTY_UNRANKED_QUEUE_ITEM, LEFT_ARROW + GOLD + "Play 2v2 Unranked" + RIGHT_ARROW);
        ItemUtils.setDisplayName(LEAVE_PARTY_UNRANKED_QUEUE_ITEM, LEFT_ARROW + RED + "Leave 2v2 Unranked Queue" + RIGHT_ARROW);

        ItemUtils.setDisplayName(JOIN_PARTY_RANKED_QUEUE_ITEM, LEFT_ARROW + GOLD + "Join 2v2 Ranked" + RIGHT_ARROW);
        ItemUtils.setDisplayName(LEAVE_PARTY_RANKED_QUEUE_ITEM, LEFT_ARROW + RED + "Leave 2v2 Ranked Queue" + RIGHT_ARROW);
    }

}