//package rip.bridge.practice.lobby.menu;
//
//import rip.bridge.practice.Practice;
//import rip.bridge.practice.match.Match;
//import rip.bridge.practice.match.MatchState;
//import rip.bridge.practice.match.MatchTeam;
//import rip.bridge.practice.setting.Setting;
//import rip.bridge.practice.setting.SettingHandler;
//import rip.bridge.qlib.menu.Button;
//import rip.bridge.qlib.menu.pagination.PaginatedMenu;
//import org.bukkit.Bukkit;
//import org.bukkit.ChatColor;
//import org.bukkit.entity.Player;
//
//import javax.validation.constraints.NotNull;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//public final class TournamentSpectateMenu extends PaginatedMenu {
//
//    public TournamentSpectateMenu() {
//        setAutoUpdate(true);
//    }
//
//    @Override
//    public String getPrePaginatedTitle(Player player) {
//        return "Tournament listings";
//    }
//
//    @Override
//    public Map<Integer, Button> getAllPagesButtons(Player player) {
//        SettingHandler settingHandler = Practice.getInstance().getSettingHandler();
//        Map<Integer, Button> buttons = new HashMap<>();
//        int i = 0;
//
//        for (Match match : Practice.getInstance().getTournamentHandler().getTournament().getMatches()) {
//            // players can view this menu while spectating
//            if (match.isSpectator(player.getUniqueId())) {
//                continue;
//            }
//
//            if (match.getState() == MatchState.ENDING) {
//                continue;
//            }
//
//            if (Practice.getInstance().getTournamentHandler().isInTournament(match)) {
//                int numTotalPlayers = 0;
//                int numSpecDisabled = 0;
//
//                for (MatchTeam team : match.getTeams()) {
//                    for (UUID member : team.getAliveMembers()) {
//                        numTotalPlayers++;
//
//                        if (!settingHandler.getSetting(Bukkit.getPlayer(member), Setting.ALLOW_SPECTATORS)) {
//                            numSpecDisabled++;
//                        }
//                    }
//                }
//
//                // if >= 50% of participants have spectators disabled
//                // we won't render this match in the menu
//                if ((float) numSpecDisabled / (float) numTotalPlayers >= 0.5) {
//                    continue;
//                }
//            }
//
//            buttons.put(i++, new TournamentSpectateButton(match));
//        }
//
//        return buttons;
//    }
//
//    // we lock the size of this inventory at full, otherwise we'll have
//    // issues if it 'grows' into the next line while it's open (say we open
//    // the menu with 8 entries, then it grows to 11 [and onto the second row]
//    // - this breaks things)
//    @Override
//    public int size(Map<Integer, Button> buttons) {
//        return 9 * 6;
//    }
//
//}