package sk.tuke.game.pong.arena.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import kpi.openlab.arena.interfaces.Bot;
import kpi.openlab.arena.interfaces.BotResult;
import sk.tuke.game.pong.arena.GameInfo;
import sk.tuke.game.pong.arena.PongGame;
import sk.tuke.game.pong.interfaces.PlayerActions;

import java.util.List;

/**
 * Created by otara on 17.4.2017.
 */
public class GameOverScreen implements Screen {

	private final Texture backgroundImage;
	private List<BotResult> results;
	PongGame game;
	private OrthographicCamera gameCam;
	private FitViewport gamePort;
	private Stage gameStage;
	private long time;


	public GameOverScreen(PongGame game, List<BotResult> results, List<Bot<PlayerActions>> bots) {
		time = System.currentTimeMillis();
		this.results = results;
		this.game = game;
		game.setResults(results);
		gameCam = new OrthographicCamera();
		gamePort = new FitViewport(GameInfo.GAME_WIDTH / GameInfo.PPM, GameInfo.GAME_HEIGHT / GameInfo.PPM, gameCam);
		backgroundImage = new Texture(Gdx.files.internal("bg.png"));
		gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
		gameStage = new Stage();
		Table table = new Table();
		table.setFillParent(true);
		table.add(new Label("PongComplex", new Label.LabelStyle(new BitmapFont(), Color.YELLOW))).expandX().center();
		table.row();
		for (BotResult result : results) {
			for (Bot<PlayerActions> bot : bots) {
				if (result.getBotId() == bot.getId()) {
					table.add(new Label(bot.getName(), new Label.LabelStyle(new BitmapFont(), Color.WHITE))).expandX();
					table.add(new Label(String.format("%03d", result.getScore()), new Label.LabelStyle(new BitmapFont(), Color.WHITE))).expandX();
					break;
				}
			}
			table.row();
		}
		table.align(Align.center);
		gameStage.addActor(table);
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		gameStage.getBatch().begin();
		gameStage.getBatch().draw(backgroundImage, 0, 0, GameInfo.GAME_WIDTH, GameInfo.GAME_HEIGHT);
		gameStage.getBatch().end();
		gameStage.draw();
		if (System.currentTimeMillis() > time + 5000)
			Gdx.app.exit();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}
}
