package sk.tuke.game.pongcomplex.desktop.arena;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import kpi.openlab.arena.gdx.ArenaLwjglApplication;
import kpi.openlab.arena.interfaces.Bot;
import kpi.openlab.arena.interfaces.BotResult;
import sk.tuke.game.pongcomplex.arena.GameInfo;
import sk.tuke.game.pongcomplex.arena.PongGame;
import sk.tuke.game.pongcomplex.interfaces.PlayerActions;

import java.util.List;

public class PongArenaMain extends ArenaLwjglApplication<PongGame,PlayerActions>{

    @Override
    public int getNumberOfRequiredBots() {
		return 2;
	}

    @Override
    public boolean isHigherScoreBetter() {
        return true;
    }

    @Override
    protected PongGame arenaStarting(List<Bot<PlayerActions>> bots) {
        return new PongGame(bots);
    }

    @Override
    protected List<BotResult> arenaFinishing(PongGame pong) {
        return pong.getResults();
    }

    @Override
    public Class<PlayerActions> getBotInterfaceClass() {
        return PlayerActions.class;
    }

    @Override
    protected LwjglApplicationConfiguration getLwjglConfiguration() {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = GameInfo.GAME_NAME;
        config.width = GameInfo.GAME_WIDTH;
        config.height = GameInfo.GAME_HEIGHT;
        config.forceExit = false;
        return config;
    }
}
