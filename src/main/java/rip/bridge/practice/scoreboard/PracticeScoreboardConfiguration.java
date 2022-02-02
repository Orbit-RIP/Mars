package rip.bridge.practice.scoreboard;

import lombok.experimental.UtilityClass;
import rip.bridge.practice.Practice;
import rip.bridge.qlib.scoreboard.ScoreboardConfiguration;
import rip.bridge.qlib.scoreboard.TitleGetter;

@UtilityClass
public final class PracticeScoreboardConfiguration {

    // ⏐ (not |, which is the standard pipe) is similar to a pipe but
    // doesn't have a split in the middle when rendered in Minecraft
//    public static final String SCOREBOARD_TITLE = Practice.getInstance().getDominantColor() + "&lBridge &7&l⏐ &fPractice";

    public static ScoreboardConfiguration create() {
        ScoreboardConfiguration configuration = new ScoreboardConfiguration();

        configuration.setTitleGetter(TitleGetter.forStaticString(Practice.getInstance().getDominantColor() + "&l" + Practice.getInstance().getServerName() + " &7&l⏐ &fPractice"));
        configuration.setScoreGetter(new MultiplexingScoreGetter(
                new MatchScoreGetter(),
                new LobbyScoreGetter()
        ));

        return configuration;
    }

}