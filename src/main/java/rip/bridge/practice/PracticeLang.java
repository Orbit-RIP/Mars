package rip.bridge.practice;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

import lombok.experimental.UtilityClass;
import rip.bridge.practice.commands.HelpCommand;
import rip.bridge.practice.lobby.LobbyItems;
import rip.bridge.practice.party.commands.PartyHelpCommand;
import rip.bridge.practice.queue.QueueItems;

/**
 * Constants for messages/strings commonly shown to players
 */
@UtilityClass
public final class PracticeLang {

    /**
     * `&7&l» ` - Arrow used on the left side of item display names
     * Named left arrow due to its usage on the left side of items, despite the fact
     * the arrow is actually pointing to the right.
     * @see LobbyItems usage
     * @see QueueItems usage
     */
    public static final String LEFT_ARROW = ChatColor.BLUE.toString() + ChatColor.BOLD + "» ";

    public static final String LEFT_ARROW_NAKED = "»";

    /**
     * ` &7&l«` - Arrow used on the right side of item display names
     * Named right arrow due to its usage on the right side of items, despite the fact
     * the arrow is actually pointing to the left.
     * @see LobbyItems usage
     * @see QueueItems usage
     */
    public static final String RIGHT_ARROW = " " + ChatColor.BLUE.toString() + ChatColor.BOLD + "«";

    /**
     * Example omitted - Solid line which almost entirely spans the
     * (default) Minecraft chat box. 53 is chosen for no reason other than its width
     * being almost equal to that of the chat box.
     * @see HelpCommand
     * @see PartyHelpCommand
     */
    public static final String LONG_LINE = ChatColor.STRIKETHROUGH + StringUtils.repeat("-", 53);

    /**
     * `&cYou are not in a party.` - Self explanatory
     * @see rip.bridge.practice.party.commands
     */
    public static final String NOT_IN_PARTY = ChatColor.RED + "You are not in a party.";

    /**
     * `&cYou are not the leader of your party.` - Self explanatory
     * @see rip.bridge.practice.party.commands
     */
    public static final String NOT_LEADER_OF_PARTY = ChatColor.RED + "You are not the leader of your party.";

    /**
     * `&cThere was an error starting the match, please contact an admin.` - Self explanatory
     */
    public static final String ERROR_WHILE_STARTING_MATCH = ChatColor.RED + "There was an error starting the match, please contact an admin.";

}