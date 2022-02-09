package rip.orbit.mars.ability;

import cc.fyre.proton.Proton;
import lombok.Getter;
import rip.orbit.mars.Mars;
import rip.orbit.mars.ability.items.*;
import rip.orbit.mars.ability.items.pocketbards.Regeneration;
import rip.orbit.mars.ability.items.pocketbards.Resistance;
import rip.orbit.mars.ability.items.pocketbards.Speed;
import rip.orbit.mars.ability.items.pocketbards.Strength;
import rip.orbit.mars.ability.param.AbilityParameterType;
import rip.orbit.mars.ability.profile.ProfileListener;
import rip.orbit.mars.util.cooldown.Cooldowns;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 01/07/2021 / 12:53 AM
 * HCTeams / rip.orbit.mars.ability
 */
public class AbilityHandler {

	@Getter private final List<Ability> abilities;
	@Getter private final List<Ability> pocketbards;
	@Getter private final Cooldowns abilityCD;
	@Getter private final Cooldowns abilityEffect;

	public AbilityHandler() {
		abilities = new ArrayList<>();
		pocketbards = new ArrayList<>();
		abilityEffect = new Cooldowns();
		abilityCD = new Cooldowns();

		Proton.getInstance().getCommandHandler().registerParameterType(Ability.class, new AbilityParameterType());

		abilities.add(new Switcher());
//		abilities.add(new Turret());
		abilities.add(new Recon());
		abilities.add(new AntiBuildStick());
		abilities.add(new AbilityInspector());
		abilities.add(new Curse());
		abilities.add(new Warrior());
		abilities.add(new TimeWarp());
		abilities.add(new Thorns());
		abilities.add(new GuardianAngel());
		abilities.add(new GhostMode());
		abilities.add(new PocketBard());
		abilities.add(new NinjaStar());
//		abilities.add(new Voider());
		abilities.add(new Dome());

		pocketbards.add(new Strength());
		pocketbards.add(new Resistance());
		pocketbards.add(new Regeneration());
		pocketbards.add(new Speed());

		Bukkit.getPluginManager().registerEvents(new ProfileListener(), Mars.getInstance());

	}

	public Ability byName(String name) {
		for (Ability ability : abilities) {
			if (ability.name().equals(name)) {
				return ability;
			}
		}
		return null;
	}

}
