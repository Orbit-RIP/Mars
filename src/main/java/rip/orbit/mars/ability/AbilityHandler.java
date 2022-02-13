package rip.orbit.mars.ability;

import cc.fyre.proton.Proton;
import lombok.Getter;
import rip.orbit.mars.Mars;
import rip.orbit.mars.ability.items.orbit.*;
import rip.orbit.mars.ability.items.orbit.GuardianAngel;
import rip.orbit.mars.ability.items.orbit.NinjaStar;
import rip.orbit.mars.ability.items.orbit.PocketBard;
import rip.orbit.mars.ability.items.orbit.Switcher;
import rip.orbit.mars.ability.items.orbit.TimeWarp;
import rip.orbit.mars.ability.items.pocketbard.Regeneration;
import rip.orbit.mars.ability.items.pocketbard.Resistance;
import rip.orbit.mars.ability.items.pocketbard.Speed;
import rip.orbit.mars.ability.items.pocketbard.Strength;
import rip.orbit.mars.ability.items.viper.*;
import rip.orbit.mars.ability.param.AbilityParameterType;
import rip.orbit.mars.ability.profile.ProfileListener;
import rip.orbit.mars.util.cooldown.Cooldowns;
import org.bukkit.Bukkit;

import java.util.*;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 01/07/2021 / 12:53 AM
 * HCTeams / rip.orbit.mars.ability
 */
public class AbilityHandler {

	@Getter private final List<Ability> orbitAbilities, viperAbilities, caveAbilities;
	@Getter private final List<Ability> orbitPocketBards, viperPocketBards, cavePocketBards;
	@Getter private final Cooldowns abilityCD;
	@Getter private final Cooldowns abilityEffect;

	public AbilityHandler() {
		orbitAbilities = new ArrayList<>();
		viperAbilities = new ArrayList<>();
		caveAbilities = new ArrayList<>();
		orbitPocketBards = new ArrayList<>();
		viperPocketBards = new ArrayList<>();
		cavePocketBards = new ArrayList<>();
		abilityEffect = new Cooldowns();
		abilityCD = new Cooldowns();

		Proton.getInstance().getCommandHandler().registerParameterType(Ability.class, new AbilityParameterType());

		orbitAbilities.add(new Switcher());
		orbitAbilities.add(new Recon());
		orbitAbilities.add(new AntiBuildStick());
		orbitAbilities.add(new AbilityInspector());
		orbitAbilities.add(new Curse());
		orbitAbilities.add(new Warrior());
		orbitAbilities.add(new TimeWarp());
		orbitAbilities.add(new Thorns());
		orbitAbilities.add(new GuardianAngel());
		orbitAbilities.add(new GhostMode());
		orbitAbilities.add(new PocketBard());
		orbitAbilities.add(new NinjaStar());
		orbitAbilities.add(new Dome());

		orbitPocketBards.add(new Strength());
		orbitPocketBards.add(new Resistance());
		orbitPocketBards.add(new Regeneration());
		orbitPocketBards.add(new Speed());

		orbitAbilities.add(new Strength());
		orbitAbilities.add(new Resistance());
		orbitAbilities.add(new Regeneration());
		orbitAbilities.add(new Speed());

		Bukkit.getPluginManager().registerEvents(new ProfileListener(), Mars.getInstance());

		for (Ability ability : orbitAbilities) {
			ability.loadFromRedis();
		}

		for (Ability ability : orbitPocketBards) {
			ability.loadFromRedis();
		}

		viperAbilities.add(new ExoticBone());
		viperAbilities.add(new rip.orbit.mars.ability.items.viper.GuardianAngel());
		viperAbilities.add(new Invisibility());
		viperAbilities.add(new FocusMode());
		viperAbilities.add(new TPBow());
		viperAbilities.add(new RageBall());
		viperAbilities.add(new Combo());
		viperAbilities.add(new rip.orbit.mars.ability.items.viper.NinjaStar());
		viperAbilities.add(new rip.orbit.mars.ability.items.viper.PocketBard());
		viperAbilities.add(new rip.orbit.mars.ability.items.viper.Switcher());
		viperAbilities.add(new rip.orbit.mars.ability.items.viper.TimeWarp());

		viperPocketBards.add(new rip.orbit.mars.ability.items.viper.pocketbard.Regeneration());
		viperPocketBards.add(new rip.orbit.mars.ability.items.viper.pocketbard.Resistance());
		viperPocketBards.add(new rip.orbit.mars.ability.items.viper.pocketbard.Speed());
		viperPocketBards.add(new rip.orbit.mars.ability.items.viper.pocketbard.Strength());

		viperAbilities.add(new rip.orbit.mars.ability.items.viper.pocketbard.Regeneration());
		viperAbilities.add(new rip.orbit.mars.ability.items.viper.pocketbard.Resistance());
		viperAbilities.add(new rip.orbit.mars.ability.items.viper.pocketbard.Speed());
		viperAbilities.add(new rip.orbit.mars.ability.items.viper.pocketbard.Strength());

		caveAbilities.add(new rip.orbit.mars.ability.items.cave.FocusMode());
		caveAbilities.add(new rip.orbit.mars.ability.items.cave.Strength());
		caveAbilities.add(new rip.orbit.mars.ability.items.cave.Switcher());
		caveAbilities.add(new rip.orbit.mars.ability.items.cave.TimeWarp());
		caveAbilities.add(new rip.orbit.mars.ability.items.cave.AntiBuildRod());
		caveAbilities.add(new rip.orbit.mars.ability.items.cave.NinjaStar());

	}

	public Ability byName(String name) {
		for (Ability ability : orbitAbilities) {
			if (ability.name().equals(name)) {
				return ability;
			}
		}
		return null;
	}

}
