package rip.orbit.mars.ability.param;

import cc.fyre.proton.command.param.ParameterType;
import rip.orbit.mars.Mars;
import rip.orbit.mars.ability.Ability;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 01/07/2021 / 11:36 AM
 * HCTeams / rip.orbit.mars.ability.param
 */
public class AbilityParameterType implements ParameterType<Ability> {

	@Override
	public Ability transform(CommandSender sender, String source) {
		for (Ability abi : Mars.getInstance().getAbilityHandler().getOrbitAbilities()) {
			if (source.equalsIgnoreCase(abi.name())) {
				return abi;
			}
		}

		sender.sendMessage(ChatColor.RED + "No ability with the name " + source + " found.");
		return (null);
	}

	@Override
	public List<String> tabComplete(Player sender, Set<String> flags, String source) {
		List<String> completions = new ArrayList<>();

		for (Ability ability : Mars.getInstance().getAbilityHandler().getOrbitAbilities()) {
			if (StringUtils.startsWith(ability.name(), source)) {
				completions.add(ability.name());
			}
		}

		return (completions);
	}

}