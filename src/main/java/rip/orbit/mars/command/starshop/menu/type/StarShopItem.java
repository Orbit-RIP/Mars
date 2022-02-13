package rip.orbit.mars.command.starshop.menu.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import rip.orbit.nebula.util.CC;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 14/08/2021 / 3:18 PM
 * HCTeams / rip.orbit.mars.stars.menu.type
 */

@AllArgsConstructor
@Getter
public enum StarShopItem {

	KILLEFFECT_BLOOD(200, 1, Material.REDSTONE, 0, CC.translate("&4&lBlood Kill Effect"), true, "user addperm %player% potpvp.killeffect.blood"),
	KILLEFFECT_PEARL(150, 1, Material.ENDER_PEARL, 0, CC.translate("&a&lPearl Kill Effect"), true, "user addperm %player% potpvp.killeffect.pearl"),
	KILLEFFECT_FIREWORK(125, 1, Material.FIREWORK, 0, CC.translate("&f&lFireWork Kill Effect"), true, "user addperm %player% potpvp.killeffect.firework"),
	KILLEFFECT_WATER(100, 1, Material.WATER_BUCKET, 0, CC.translate("&b&lWater Kill Effect"), true, "user addperm %player% potpvp.killeffect.water"),
	KILLEFFECT_FLAME(100, 1, Material.FLINT_AND_STEEL, 0, CC.translate("&c&lFlame Kill Effect"), true, "user addperm %player% potpvp.killeffect.flame");

	private final int price, amount;
	private final Material material;
	private final int data;
	private final String displayName;
	private final boolean useCommand;
	private final String command;

	}
