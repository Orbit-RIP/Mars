package rip.orbit.mars.scoreboard;

import cc.fyre.proton.Proton;
import cc.fyre.proton.scoreboard.config.ScoreboardConfiguration;
import cc.fyre.proton.scoreboard.construct.TitleGetter;
import org.apache.commons.lang.StringEscapeUtils;
import rip.orbit.mars.Mars;

public final class PotPvPScoreboardConfiguration {

    public static ScoreboardConfiguration create() {
        ScoreboardConfiguration configuration = new ScoreboardConfiguration();

        configuration.setTitleGetter(new TitleGetter(Mars.getInstance().getAnimationHandler().getTitle() + " &7[Season 1]"));
        configuration.setScoreGetter(new MultiplexingScoreGetter(
            new MatchScoreGetter(),
            new LobbyScoreGetter(),
            new GameScoreGetter()
        ));

        return configuration;
    }

}
