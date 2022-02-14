package rip.orbit.mars.lobby;

import lombok.experimental.UtilityClass;
import cc.fyre.proton.util.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static rip.orbit.mars.MarsLang.LEFT_ARROW;
import static rip.orbit.mars.MarsLang.RIGHT_ARROW;
import static org.bukkit.ChatColor.*;

@UtilityClass
public final class LobbyItems {

	public static final ItemStack SPECTATE_RANDOM_ITEM = new ItemStack(Material.COMPASS);
	public static final ItemStack SPECTATE_MENU_ITEM = new ItemStack(Material.PAPER);
	public static final ItemStack ENABLE_SPEC_MODE_ITEM = new ItemStack(Material.REDSTONE_TORCH_ON);
	public static final ItemStack DISABLE_SPEC_MODE_ITEM = new ItemStack(Material.LEVER);
	public static final ItemStack PARTY_ITEM = new ItemStack(Material.NAME_TAG);
	public static final ItemStack MANAGE_ITEM = new ItemStack(Material.ANVIL);
	public static final ItemStack UNFOLLOW_ITEM = new ItemStack(Material.INK_SACK, 1, DyeColor.RED.getDyeData());
	public static final ItemStack PLAYER_STATISTICS = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
	public static final ItemStack PARKOUR_ITEM = new ItemStack(Material.LEASH, 1);

	static {
//        ItemUtils.setDisplayName(SPECTATE_RANDOM_ITEM, LEFT_ARROW + YELLOW + BOLD + "Spectate Random Match" + RIGHT_ARROW);
		ItemUtils.setDisplayName(SPECTATE_RANDOM_ITEM, LEFT_ARROW + GOLD + "Spectate Random Match" + RIGHT_ARROW);
		ItemUtils.setDisplayName(SPECTATE_MENU_ITEM, LEFT_ARROW + GOLD + "Spectate Menu" + RIGHT_ARROW);
		ItemUtils.setDisplayName(ENABLE_SPEC_MODE_ITEM, LEFT_ARROW + GREEN + "Enable Spectator Mode" + RIGHT_ARROW);
//        ItemUtils.setDisplayName(DISABLE_SPEC_MODE_ITEM, LEFT_ARROW + AQUA + BOLD + "Disable Spectator Mode" + RIGHT_ARROW);
		ItemUtils.setDisplayName(DISABLE_SPEC_MODE_ITEM, LEFT_ARROW + RED + "Disable Spectator Mode" + RIGHT_ARROW);
		ItemUtils.setDisplayName(PARTY_ITEM, LEFT_ARROW + GOLD + "Create a Party" + RIGHT_ARROW);
		ItemUtils.setDisplayName(MANAGE_ITEM, LEFT_ARROW + GOLD + "Manage Orbit Practice " + RIGHT_ARROW);
		ItemUtils.setDisplayName(UNFOLLOW_ITEM, LEFT_ARROW + RED + "Stop Following " + RIGHT_ARROW);
		ItemUtils.setDisplayName(PLAYER_STATISTICS, LEFT_ARROW + GOLD + "Statistics " + RIGHT_ARROW);
		ItemUtils.setDisplayName(PARKOUR_ITEM, LEFT_ARROW + GOLD + "Right click to reset parkour challenge");
	}

}