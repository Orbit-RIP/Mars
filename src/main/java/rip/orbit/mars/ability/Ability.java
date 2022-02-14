package rip.orbit.mars.ability;

import cc.fyre.proton.util.ItemBuilder;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import rip.orbit.mars.Mars;
import rip.orbit.mars.ability.items.AbilityType;
import rip.orbit.mars.ability.items.orbit.Dome;
import rip.orbit.mars.match.Match;
import rip.orbit.mars.match.MatchTeam;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import rip.orbit.mars.persist.PersistMap;
import rip.orbit.nebula.util.CC;
import rip.orbit.mars.util.cooldown.Cooldowns;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 30/06/2021 / 12:51 PM
 * HCTeams / rip.orbit.mars.ability
 */

public abstract class Ability extends PersistMap<Integer> implements Listener {

	public Ability(String name) {

		Bukkit.getPluginManager().registerEvents(this, Mars.getInstance());

		setKeyPrefix(name + "-Used");
		setMongoName(name + "-Uses");
	}

	public abstract Cooldowns cooldown();
	public abstract String name();
	public abstract String displayName();
	public abstract int data();
	public abstract Material mat();
	public abstract boolean glow();
	public abstract List<String> lore();
	public abstract List<String> foundInfo();

	@Setter private ItemStack stack = ItemBuilder.of(mat()).setLore(lore()).data((short) data()).name(CC.translate(displayName())).build();

	public ItemStack getStack() {
		if (glow()) {
			stack.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		}
		return stack;
	}

	public ItemStack getStack(int amount) {
		if (glow()) {
			stack.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		}
		ItemStack stack = this.stack.clone();
		stack.setAmount(amount);
		return stack;
	}

	public static boolean canAttack(Player attacker, Player damaged) {
		Match match = Mars.getInstance().getMatchHandler().getMatchPlaying(damaged);
		if (match != null) {
			if (match.getSpectators().contains(attacker.getUniqueId()))
				return false;
			for (MatchTeam team : match.getTeams()) {
				if (team.getAliveMembers().contains(damaged.getUniqueId()) && team.getAllMembers().contains(attacker.getUniqueId())) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean isClick(PlayerInteractEvent event, String click) {

		return event.getAction().name().contains(click);
	}

	public boolean canUse(Player player) {
		if (Mars.getInstance().getAbilityHandler().getAbilityCD().onCooldown(player)) {
			player.sendMessage(CC.translate("&cYou are currently on &eAbility Item" + "&c cooldown for &l" + Mars.getInstance().getAbilityHandler().getAbilityCD().getRemaining(player)));
			return false;
		}

		if (Dome.antiAbility.onCooldown(player)) {
			player.sendMessage(CC.translate("&cYou cannot do this for &l" + Dome.antiAbility.getRemaining(player)));
			return false;
		}
		if (cooldown().onCooldown(player)) {
			player.sendMessage(CC.translate("&cYou are currently on " + displayName() + "&c cooldown for &l" + cooldown().getRemaining(player)));
			return false;
		}

		return true;
	}

	public boolean isSimilar(PlayerInteractEvent event) {
		ItemStack item = event.getItem();
		if (item == null) return false;
		if (item.getItemMeta() == null) return false;
		if (!item.getItemMeta().hasDisplayName()) return false;

		return ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(displayName()));

	}

	public boolean isSimilar(ItemStack item) {
		if (item == null) return false;
		if (item.getItemMeta() == null) return false;
		if (!item.getItemMeta().hasDisplayName()) return false;

		return ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(displayName()));

	}

	public boolean isSimilar(PlayerInteractEvent event, ItemStack stack) {
		ItemStack item = event.getItem();
		if (item == null) return false;
		if (item.getItemMeta() == null) return false;
		if (!item.getItemMeta().hasDisplayName()) return false;

		return ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(displayName()));

	}

	public void addCooldown(Player player, int seconds) {
		cooldown().applyCooldown(player, seconds);
		Mars.getInstance().getAbilityHandler().getAbilityCD().applyCooldown(player, 10);

		add(player.getUniqueId(), 1);
	}

	public void addHits(Player player, Map<UUID, Integer> hits) {

		Bukkit.getScheduler().runTaskAsynchronously(Mars.getInstance(), () -> {
			hits.putIfAbsent(player.getUniqueId(), 1);
			hits.put(player.getUniqueId(), hits.get(player.getUniqueId()) + 1);
		});
	}

	public boolean checkInstanceSnowball(Object instance) {
		return instance instanceof Snowball;
	}

	public boolean checkInstancePlayer(Object instance) {
		return instance instanceof Player;
	}

	public boolean checkInstanceEgg(Object instance) {
		return instance instanceof Egg;
	}

	public boolean isInDistance(Player player, Player target, int distance) {
		return player.getLocation().distance(target.getLocation()) < distance;
	}

	public boolean isInDistance(Location player, Location target, int distance) {
		return player.distance(target) < distance;
	}

	public void takeItem(Player player) {
		if (player.getItemInHand().getAmount() > 1) {
			player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
		} else {
			player.setItemInHand(null);
		}
	}

	@EventHandler
	public void onLeftClick(PlayerInteractEvent event) {
		if (event.getAction() != Action.LEFT_CLICK_BLOCK)
			return;

		if (event.getItem() == null)
			return;

		if (isSimilar(event.getItem())) {
			if (cooldown().onCooldown(event.getPlayer())) {
				event.getPlayer().sendMessage(CC.translate("&cYou are currently on " + displayName() + "&c cooldown for &l" + cooldown().getRemaining(event.getPlayer())));
			}
		}
	}

	public int get(UUID check) {
		return (contains(check) ? getValue(check) : 0);
	}
	public void set(UUID update, int amount) {
		updateValueSync(update, amount);
	}
	public void add(UUID update, int amount) {
		set(update, get(update) + amount);
	}

	@Override
	public String getRedisValue(Integer lives) {
		return (String.valueOf(lives));
	}

	@Override
	public Integer getJavaObject(String str) {
		return (Integer.parseInt(str));
	}

	@Override
	public Object getMongoValue(Integer lives) {
		return (lives);
	}

}
