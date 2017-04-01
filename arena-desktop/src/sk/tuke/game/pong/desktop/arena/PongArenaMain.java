package sk.tuke.game.pong.desktop.arena;

import kpi.openlab.arena.gdx.ArenaLwjglApplication;
import kpi.openlab.arena.interfaces.Bot;
import kpi.openlab.arena.interfaces.BotResult;
import sk.tuke.game.pong.arena.PongGame;
import sk.tuke.game.pong.interfaces.PlayerActions;


import java.util.List;

public class PongArenaMain extends ArenaLwjglApplication<PongGame,PlayerActions>{

    @Override
    public int getNumberOfRequiredBots() {
        return 1;
    }

    @Override
    public boolean isHigherScoreBetter() {
        return true;
    }

    @Override
    protected PongGame arenaStarting(List<Bot<PlayerActions>> bots) {
        return new PongGame(bots.get(0));
    }

    @Override
    protected List<BotResult> arenaFinishing(PongGame pong) {
        return pong.getResults();
    }

    @Override
    public Class<PlayerActions> getBotInterfaceClass() {
        return PlayerActions.class;
    }

//    @Override
//    protected LwjglApplicationConfiguration getLwjglConfiguration() {
//        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
//        config.title = "Pong";
//        config.width = Pong.WIDTH;
//        config.height = Pong.HEIGHT;
//        return config;
//    }
}