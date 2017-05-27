package sk.tuke.game.pongcomplex.arena;

import com.badlogic.gdx.Game;
import kpi.openlab.arena.interfaces.Bot;
import kpi.openlab.arena.interfaces.BotResult;
import sk.tuke.game.pongcomplex.arena.screens.StartScreen;
import sk.tuke.game.pongcomplex.interfaces.PlayerActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Manage game execution, input and output.
 */
public class PongGame extends Game {


	private List<Bot<PlayerActions>> playerBots;

	List<BotResult> results;

	/**
	 * Control input and prepare results list.
	 *
	 * @param bots Input bots include student solution.
	 */
	public PongGame(List<Bot<PlayerActions>> bots) {
		super();
		Objects.requireNonNull(bots, "bots must not be null");
		playerBots = bots;
		results = new ArrayList<>();
	}

	/**
	 * Start game with StartScreen.
	 */
	@Override
	public void create() {
		setScreen(new StartScreen(this, playerBots));
	}

	@Override
	public void render() {
		super.render();
	}

	/**
	 * Get results from game.
	 * @return List of players results.
	 */
	public List<BotResult> getResults() {
		return results;
	}

	/**
	 * Set results before end game.
	 * @param results Set list of results.
	 */
	public void setResults(List<BotResult> results) {
		this.results = results;
	}
}
