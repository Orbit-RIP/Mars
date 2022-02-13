package rip.orbit.mars.duel.command;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import cc.fyre.proton.util.UUIDUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.orbit.mars.Mars;
import rip.orbit.mars.MarsLang;
import rip.orbit.mars.arena.ArenaSchematic;
import rip.orbit.mars.duel.DuelHandler;
import rip.orbit.mars.duel.DuelInvite;
import rip.orbit.mars.duel.PartyDuelInvite;
import rip.orbit.mars.duel.PlayerDuelInvite;
import rip.orbit.mars.duel.command.AcceptCommand;
import rip.orbit.mars.kittype.KitType;
import rip.orbit.mars.kittype.menu.select.SelectKitTypeMenu;
import rip.orbit.mars.lobby.LobbyHandler;
import rip.orbit.mars.match.MatchHandler;
import rip.orbit.mars.party.Party;
import rip.orbit.mars.party.PartyHandler;
import rip.orbit.mars.validation.PotPvPValidation;
import rip.orbit.nebula.util.CC;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DuelCommand {

	@Command(names = {"duel", "1v1"}, permission = "")
	public static void duel(Player sender, @Parameter(name = "player") Player target) {
		if (sender == target) {
			sender.sendMessage(ChatColor.RED + "You can't duel yourself!");
			return;
		}

		PartyHandler partyHandler = Mars.getInstance().getPartyHandler();
		LobbyHandler lobbyHandler = Mars.getInstance().getLobbyHandler();

		Party senderParty = partyHandler.getParty(sender);
		Party targetParty = partyHandler.getParty(target);

		if (senderParty != null && targetParty != null) {
			// party dueling party (legal)
			if (!PotPvPValidation.canSendDuel(senderParty, targetParty, sender)) {
				return;
			}

			new SelectKitTypeMenu(kitType -> {
				sender.closeInventory();

				// reassign these fields so that any party changes
				// (kicks, etc) are reflectednow
				Party newSenderParty = partyHandler.getParty(sender);
				Party newTargetParty = partyHandler.getParty(target);

				if (newSenderParty != null && newTargetParty != null) {
					if (newSenderParty.isLeader(sender.getUniqueId())) {
						duel(sender, newSenderParty, newTargetParty, kitType);
					} else {
						sender.sendMessage(MarsLang.NOT_LEADER_OF_PARTY);
					}
				}
			}, "Select a kit type...").openMenu(sender);
		} else if (senderParty == null && targetParty == null) {
			// player dueling player (legal)
			if (!PotPvPValidation.canSendDuel(sender, target)) {
				return;
			}

			if (target.hasPermission("potpvp.famous") && System.currentTimeMillis() - lobbyHandler.getLastLobbyTime(target) < 3_000) {
				sender.sendMessage(ChatColor.RED + target.getName() + " just returned to the lobby, please wait a moment.");
				return;
			}

			new SelectKitTypeMenu(kitType -> {
				sender.closeInventory();
				duel(sender, target, kitType);
			}, "Select a kit type...").openMenu(sender);
		} else if (senderParty == null) {
			// player dueling party (illegal)
			sender.sendMessage(ChatColor.RED + "You must create a party to duel " + target.getName() + "'s party.");
		} else {
			// party dueling player (illegal)
			sender.sendMessage(ChatColor.RED + "You must leave your party to duel " + target.getName() + ".");
		}
	}

	public static void duel(Player sender, Player target, KitType kitType) {
		if (!PotPvPValidation.canSendDuel(sender, target)) {
			return;
		}
		Menu menu = new Menu() {

			@Override
			public String getTitle(Player player) {
				return CC.translate("&6Choose a map...");
			}

			@Override
			public Map<Integer, Button> getButtons(Player player) {
				Map<Integer, Button> buttons = new HashMap<>();

				int i = 0;
				for (ArenaSchematic schematic : Mars.getInstance().getArenaHandler().getSchematics()) {

					if (!schematic.isEnabled()) continue;
					if (!MatchHandler.canUseSchematic(kitType, schematic)) continue;

					buttons.put(i, new Button() {
						@Override
						public String getName(Player player) {
							return CC.translate("&6" + schematic.getName());
						}

						@Override
						public List<String> getDescription(Player player) {
							return CC.translate(Collections.singletonList("&fClick to choose the &6%name%&f map to play on.".replace("%name%", schematic.getName())));
						}

						@Override
						public Material getMaterial(Player player) {
							return schematic.getDisplayMaterial();
						}

						@Override
						public void clicked(Player player, int slot, ClickType clickType) {
							DuelHandler duelHandler = Mars.getInstance().getDuelHandler();
							DuelInvite autoAcceptInvite = duelHandler.findInvite(target, sender);

							// if two players duel each other for the same thing automatically
							// accept it to make their life a bit easier.
							if (autoAcceptInvite != null && autoAcceptInvite.getKitType() == kitType) {
								AcceptCommand.accept(sender, target);
								return;
							}

							DuelInvite alreadySentInvite = duelHandler.findInvite(sender, target);

							if (alreadySentInvite != null) {
								if (alreadySentInvite.getKitType() == kitType) {
									sender.sendMessage(ChatColor.YELLOW + "You have already invited " + ChatColor.AQUA + target.getName() + ChatColor.YELLOW + " to a " + kitType.getColoredDisplayName() + ChatColor.YELLOW + " duel.");
									return;
								} else {
									// if an invite was already sent (with a different kit type)
									// just delete it (so /accept will accept the 'latest' invite)
									duelHandler.removeInvite(alreadySentInvite);
								}
							}

							target.sendMessage(sender.getDisplayName() + ChatColor.WHITE + " has sent you a " + kitType.getColoredDisplayName() + ChatColor.WHITE + " duel on the " + CC.GOLD + schematic.getName() + CC.WHITE + " map.");
							target.spigot().sendMessage(createInviteNotification(sender.getName()));

							sender.sendMessage(ChatColor.WHITE + "Successfully sent a " + kitType.getColoredDisplayName() + ChatColor.WHITE + " duel invite to " + target.getDisplayName() + ChatColor.WHITE + ".");
							duelHandler.insertInvite(new PlayerDuelInvite(sender, target, kitType, schematic));
							player.closeInventory();
						}
					});
					++i;
				}

				return buttons;
			}
		};
		sender.closeInventory();
		menu.openMenu(sender);
	}

	public static void duel(Player sender, Party senderParty, Party targetParty, KitType kitType) {
		if (!PotPvPValidation.canSendDuel(senderParty, targetParty, sender)) {
			return;
		}

		DuelHandler duelHandler = Mars.getInstance().getDuelHandler();
		DuelInvite autoAcceptInvite = duelHandler.findInvite(targetParty, senderParty);
		String targetPartyLeader = UUIDUtils.name(targetParty.getLeader());

		// if two players duel each other for the same thing automatically
		// accept it to make their life a bit easier.
		if (autoAcceptInvite != null && autoAcceptInvite.getKitType() == kitType) {
			AcceptCommand.accept(sender, Bukkit.getPlayer(targetParty.getLeader()));
			return;
		}

		DuelInvite alreadySentInvite = duelHandler.findInvite(senderParty, targetParty);

		if (alreadySentInvite != null) {
			if (alreadySentInvite.getKitType() == kitType) {
				sender.sendMessage(ChatColor.WHITE + "You have already invited " + ChatColor.GOLD + targetPartyLeader + "'s party" + ChatColor.WHITE + " to a " + kitType.getColoredDisplayName() + ChatColor.WHITE + " duel.");
				return;
			} else {
				// if an invite was already sent (with a different kit type)
				// just delete it (so /accept will accept the 'latest' invite)
				duelHandler.removeInvite(alreadySentInvite);
			}
		}

		targetParty.message(ChatColor.AQUA + sender.getName() + "'s Party (" + senderParty.getMembers().size() + ")" + ChatColor.YELLOW + " has sent you a " + kitType.getColoredDisplayName() + ChatColor.YELLOW + " duel.");
		Bukkit.getPlayer(targetParty.getLeader()).spigot().sendMessage(createInviteNotification(sender.getName()));

		sender.sendMessage(ChatColor.WHITE + "Successfully sent a " + kitType.getColoredDisplayName() + ChatColor.WHITE + " duel invite to " + ChatColor.GOLD + targetPartyLeader + "'s party" + ChatColor.WHITE + ".");
		duelHandler.insertInvite(new PartyDuelInvite(senderParty, targetParty, kitType));
	}

	private static TextComponent[] createInviteNotification(String sender) {
		TextComponent firstPart = new TextComponent("Click here or type ");
		TextComponent commandPart = new TextComponent("/accept " + sender);
		TextComponent secondPart = new TextComponent(" to accept the invite");

		firstPart.setColor(net.md_5.bungee.api.ChatColor.GRAY);
		commandPart.setColor(net.md_5.bungee.api.ChatColor.GOLD);
		secondPart.setColor(net.md_5.bungee.api.ChatColor.GRAY);

		ClickEvent.Action runCommand = ClickEvent.Action.RUN_COMMAND;
		HoverEvent.Action showText = HoverEvent.Action.SHOW_TEXT;

		firstPart.setClickEvent(new ClickEvent(runCommand, "/accept " + sender));
		firstPart.setHoverEvent(new HoverEvent(showText, new BaseComponent[]{new TextComponent(ChatColor.GREEN + "Click here to accept")}));

		commandPart.setClickEvent(new ClickEvent(runCommand, "/accept " + sender));
		commandPart.setHoverEvent(new HoverEvent(showText, new BaseComponent[]{new TextComponent(ChatColor.GREEN + "Click here to accept")}));

		secondPart.setClickEvent(new ClickEvent(runCommand, "/accept " + sender));
		secondPart.setHoverEvent(new HoverEvent(showText, new BaseComponent[]{new TextComponent(ChatColor.GREEN + "Click here to accept")}));

		return new TextComponent[]{firstPart, commandPart, secondPart};
	}

}