package sk.tuke.game.pong.arena;

import com.badlogic.gdx.Game;
import kpi.openlab.arena.interfaces.Bot;
import kpi.openlab.arena.interfaces.BotResult;
import sk.tuke.game.pong.arena.screens.StartScreen;
import sk.tuke.game.pong.interfaces.PlayerActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PongGame extends Game {


	protected List<Bot<PlayerActions>> playerBots;

	List<BotResult> results;

	public PongGame(List<Bot<PlayerActions>> bots) {
		super();
		Objects.requireNonNull(bots, "bots must not be null");
		playerBots = bots;
		results = new ArrayList<>();
	}

	@Override
	public void create() {
		setScreen(new StartScreen(this, playerBots));
	}

	@Override
	public void render() {
		super.render();
	}

	public List<BotResult> getResults() {
		return results;
	}

	public void setResults(List<BotResult> results) {
		this.results = results;
	}
}
